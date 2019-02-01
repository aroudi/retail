package au.com.biztune.retail.service;

import au.com.biztune.retail.dao.AccountingDao;
import au.com.biztune.retail.dao.CashSessionDao;
import au.com.biztune.retail.dao.ConfigCategoryDao;
import au.com.biztune.retail.domain.*;
import au.com.biztune.retail.util.IdBConstant;
import au.com.biztune.retail.util.SearchClauseBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.*;

/**
 * Created by arash on 27/09/2016.
 */
@Component
public class AccountingExtractServiceImpl implements AccountingExtractService {
    @Autowired
    private AccountingDao accountingDao;
    @Autowired
    private CashSessionDao cashSessionDao;
    @Autowired
    private CashSessionService cashSessionService;
    @Autowired
    private ConfigCategoryDao configCategoryDao;
    private final Logger logger = LoggerFactory.getLogger(AccountingExtractServiceImpl.class);
    private CashSession cashSession;

    /**
     * process transaction and extract accounting figures.
     * @param txnHeader txnHeader.
     */
    public void extractAccountingFiguresFromTxn(TxnHeader txnHeader) {
        try {
            cashSession = cashSessionService.getActiveCashSession(txnHeader.getTxhdOperator());
            //cashSessionDao.getCurrentCashSessionPerUser(txnHeader.getTxhdOperator());
            //if cashcession is null create it.
            if (cashSession == null) {
                cashSession = cashSessionService.createCashSession(txnHeader.getUser());
            }
            //if session is closed, then open it
            if (cashSession.getCssnStatus().getCategoryCode().equals(IdBConstant.SESSION_STATE_CLOSED)) {
                final ConfigCategory openState = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_SESSION_STATE, IdBConstant.SESSION_STATE_OPEN);
                cashSession.setCssnStatus(openState);
                cashSessionDao.updateCashSessionStatus(cashSession);
            }

            if (txnHeader == null) {
                logger.debug("AccountingExtractService : transaction is empty");
                return;
            }
            //process accounting figures only for Invoice, Refund, SaleOrder and Account Payment.
            if (!(txnHeader.getTxhdTxnType().getCategoryCode().equals(IdBConstant.TXN_TYPE_INVOICE)
                    || txnHeader.getTxhdTxnType().getCategoryCode().equals(IdBConstant.TXN_TYPE_REFUND)
                    || txnHeader.getTxhdTxnType().getCategoryCode().equals(IdBConstant.TXN_TYPE_SALE)
                    || txnHeader.getTxhdTxnType().getCategoryCode().equals(IdBConstant.TXN_TYPE_ACCOUNT_PAYMENT)
                    || txnHeader.getTxhdTxnType().getCategoryCode().equals(IdBConstant.TXN_TYPE_REVERSE_ACC_PAYMENT)))
            {
                logger.debug("AccountingExtractService : wrong type transaction");
                return;
            }
            switch (txnHeader.getTxhdTxnType().getCategoryCode()) {
                case IdBConstant.TXN_TYPE_INVOICE :
                    processTxnForExtractingJournalEntry(txnHeader);
                    break;
                case IdBConstant.TXN_TYPE_REFUND :
                    processTxnForExtractingJournalEntry(txnHeader);
                    break;
                case IdBConstant.TXN_TYPE_SALE :
                    //process sale order paymment
                    processSaleOrderForExtractingJournalEntry(txnHeader);
                    break;
                case IdBConstant.TXN_TYPE_ACCOUNT_PAYMENT :
                    processDebtorPaymentForExtractingJournalEntry(txnHeader);
                    break;
                //TODO: JournalEntry for Reversal Debtor Payment
                case IdBConstant.TXN_TYPE_REVERSE_ACC_PAYMENT :
                    processDebtorPaymentForExtractingJournalEntry(txnHeader);
                    break;
                default:
                    break;
            }

        } catch (Exception e) {
            logger.error("Exception in extracting accounting figures from txn", e);
        }
    }

    private void processTxnForExtractingJournalEntry(TxnHeader txnHeader) {
        // process entry for cost of goods
        List<JournalEntry> journalEntryList = null;
        final double costOfGoods = calculateCostOfGoods(txnHeader);
        insertJournalEntry(txnHeader, IdBConstant.JOURNAL_ACTION_COST_OF_GOODS, Math.abs(costOfGoods));
        //process entry for payments.
        final Map<String, Double> paymentMap = processTxnMedia(txnHeader);
        if (paymentMap == null || paymentMap.keySet().size() <= 0) {
            return;
        }
        double paidByAccount = 0.00;
        double paidByNonAccount = 0.00;

        if (paymentMap.containsKey(IdBConstant.PAID_FROM_ACCOUNT)) {
            paidByAccount = paymentMap.get(IdBConstant.PAID_FROM_ACCOUNT);
        }
        if (paymentMap.containsKey(IdBConstant.PAID_FROM_NON_ACCOUNT)) {
            paidByNonAccount = paymentMap.get(IdBConstant.PAID_FROM_NON_ACCOUNT);
        }

        if (paidByAccount == 0 && paidByNonAccount == 0) {
            return;
        }

        //if all paid by non-account tender.
        if (paidByAccount == 0) {
            journalEntryList = insertJournalEntry(txnHeader, IdBConstant.JOURNAL_ACTION_SALE_INCOME, Math.abs(paidByNonAccount));
            insertJournalEntrySupport(txnHeader, journalEntryList, IdBConstant.PAID_FROM_NON_ACCOUNT);
            return;
        }
        //if all paid by account
        if (paidByNonAccount == 0) {
            journalEntryList = insertJournalEntry(txnHeader, IdBConstant.JOURNAL_ACTION_ACCOUNT_SALE, Math.abs(paidByAccount));
            insertJournalEntrySupport(txnHeader, journalEntryList, IdBConstant.PAID_FROM_ACCOUNT);
            return;
        }
        // some by account and some by non-account
        journalEntryList = insertJournalEntry(txnHeader, IdBConstant.JOURNAL_ACTION_INVOICE_PAYMENT, Math.abs(paidByNonAccount));
        insertJournalEntrySupport(txnHeader, journalEntryList, IdBConstant.PAID_FROM_NON_ACCOUNT);
        final double amountPaid = paidByAccount + paidByNonAccount;
        journalEntryList = insertJournalEntry(txnHeader, IdBConstant.JOURNAL_ACTION_ACCOUNT_SALE, Math.abs(amountPaid));
        insertJournalEntrySupport(txnHeader, journalEntryList, IdBConstant.PAID_FROM_ACCOUNT_AND_NON_ACCOUNT);
    }

    private void processSaleOrderForExtractingJournalEntry(TxnHeader txnHeader) {

        List<JournalEntry> journalEntryList = null;
        final Map<String, Double> paymentMap = processTxnMedia(txnHeader);
        if (paymentMap == null || paymentMap.keySet().size() <= 0) {
            return;
        }
        double paidByAccount = 0.00;
        double paidByNonAccount = 0.00;

        if (paymentMap.containsKey(IdBConstant.PAID_FROM_ACCOUNT)) {
            paidByAccount = paymentMap.get(IdBConstant.PAID_FROM_ACCOUNT);
        }
        if (paymentMap.containsKey(IdBConstant.PAID_FROM_NON_ACCOUNT)) {
            paidByNonAccount = paymentMap.get(IdBConstant.PAID_FROM_NON_ACCOUNT);
        }

        if (paidByAccount == 0 && paidByNonAccount == 0) {
            return;
        }
        journalEntryList = insertJournalEntry(txnHeader, IdBConstant.JOURNAL_ACTION_SALE_ORDER_PAYMENT, paidByAccount + paidByNonAccount);
        insertJournalEntrySupport(txnHeader, journalEntryList, IdBConstant.PAID_FROM_ACCOUNT_AND_NON_ACCOUNT);
    }

    private void processDebtorPaymentForExtractingJournalEntry(TxnHeader txnHeader) {

        List<JournalEntry> journalEntryList = null;
        final Map<String, Double> paymentMap = processTxnMedia(txnHeader);
        if (paymentMap == null || paymentMap.keySet().size() <= 0) {
            return;
        }
        double paidByAccount = 0.00;
        double paidByNonAccount = 0.00;

        if (paymentMap.containsKey(IdBConstant.PAID_FROM_ACCOUNT)) {
            paidByAccount = paymentMap.get(IdBConstant.PAID_FROM_ACCOUNT);
        }
        if (paymentMap.containsKey(IdBConstant.PAID_FROM_NON_ACCOUNT)) {
            paidByNonAccount = paymentMap.get(IdBConstant.PAID_FROM_NON_ACCOUNT);
        }

        if (paidByAccount == 0 && paidByNonAccount == 0) {
            return;
        }
        journalEntryList = insertJournalEntry(txnHeader, IdBConstant.JOURNAL_ACTION_INVOICE_PAYMENT, paidByNonAccount);
        insertJournalEntrySupport(txnHeader, journalEntryList, IdBConstant.PAID_FROM_NON_ACCOUNT);
    }

    /**
     * calculate cost of goods sold or returned before tax.
     * it only calculated for Invoice or Refund transactions.
     * @param txnHeader txnHeader
     * @return total cost of goods.
     */
    private double calculateCostOfGoods(TxnHeader txnHeader) {
        double costOfGoods = 0.00;
        if (txnHeader == null || txnHeader.getTxnDetails() == null || txnHeader.getTxnDetails().size() < 1) {
            return 0.00;
        }
        for (TxnDetail txnDetail : txnHeader.getTxnDetails()) {
            //process only invoiced items.
            if (txnDetail == null || txnDetail.isTxdeItemVoid() || !txnDetail.isSelected()) {
                continue;
            }
            if (txnDetail.getTxdeDetailType().getCategoryCode().equals(IdBConstant.TXN_LINE_TYPE_SALE)
                    || txnDetail.getTxdeDetailType().getCategoryCode().equals(IdBConstant.TXN_LINE_TYPE_REFUND))
            {
                costOfGoods = costOfGoods + txnDetail.getQuantity() * txnDetail.getItemCost();
            }
        }
        return costOfGoods;
    }

    private Map processTxnMedia(TxnHeader txnHeader) {
        final Map<String, Double> paymentMap = new HashMap<String, Double>();
        double totalPaidByAccount = 0.00;
        double totalPaidByNonAccount = 0.00;
        for (TxnMedia txnMedia : txnHeader.getTxnMedias()) {
            //only new added media should be count.
            if (txnMedia == null || !txnMedia.isNewAdded() || txnMedia.isTxmdVoided()) {
                continue;
            }
            if (txnMedia.getPaymentMedia().getPaymName().equals(IdBConstant.PAYMENT_MEDIA_ACCOUNT)) {
                totalPaidByAccount = totalPaidByAccount + txnMedia.getValue();
            } else {
                totalPaidByNonAccount = totalPaidByNonAccount + txnMedia.getValue();
            }
        }
        paymentMap.put(IdBConstant.PAID_FROM_ACCOUNT, totalPaidByAccount);
        paymentMap.put(IdBConstant.PAID_FROM_NON_ACCOUNT, totalPaidByNonAccount);
        return paymentMap;
    }

    private List<JournalEntry> insertJournalEntry(TxnHeader txnHeader, String journalAction, double amount) {
        try {
            JournalEntry journalEntry = null;
            final List<JournalEntry> journalEntryList = new ArrayList<JournalEntry>();
            List<JournalRule> journalRuleList = null;
            journalRuleList = accountingDao.getJournalRuleByOrguIdAndTxnTypeAndAction(txnHeader.getOrgUnit().getId()
                    , txnHeader.getTxhdTxnType().getCategoryCode(), journalAction);
            if (journalRuleList != null && journalRuleList.size() > 1) {

                for (JournalRule journalRule : journalRuleList) {
                    //check if journal entry contains 'Sales Income' then break amount per each tax code
                    if (journalRule == null || journalRule.getAccount() == null) {
                        logger.error("jouranl rule is null or does not have an account");
                        continue;
                    }
                    if (journalRule.getAccount().getAccName().equals(IdBConstant.ACCOUNT_SALES_INCOME)) {
                        //break income per tax code
                        final Map<String, Double> itemTaxGroupMap = extractTaxFiguresFromItem(txnHeader);
                        if (itemTaxGroupMap != null && itemTaxGroupMap.keySet() != null && itemTaxGroupMap.keySet().size() > 0) {
                            for (String taxCode: itemTaxGroupMap.keySet()) {
                                journalEntry = new JournalEntry();
                                journalEntry.setSrcTxnType(txnHeader.getTxhdTxnType().getId());
                                journalEntry.setSrcTxnId(txnHeader.getId());
                                journalEntry.setAppUserId(txnHeader.getUser().getId());
                                journalEntry.setOrguId(txnHeader.getOrgUnit().getId());
                                journalEntry.setCssnSessionId(cashSession.getId());
                                journalEntry.setJrnActual(journalRule.isJrnrActual());
                                journalEntry.setJrnDate(new Timestamp(new Date().getTime()));
                                journalEntry.setAccId(journalRule.getAccount().getId());
                                journalEntry.setJrnAccCode(journalRule.getJrnrAccCode());
                                journalEntry.setJrnAccDesc(journalRule.getJrnrAccDesc());
                                journalEntry.setJrnTaxCode(taxCode);
                                if (journalRule.isJrnrDebit()) {
                                    journalEntry.setJrnDebit(Math.abs(itemTaxGroupMap.get(taxCode)));
                                } else if (journalRule.isJrnrCredit()) {
                                    journalEntry.setJrnCredit(Math.abs(itemTaxGroupMap.get(taxCode)));
                                }
                                accountingDao.insertJournalEntry(journalEntry);
                                journalEntryList.add(journalEntry);
                            }
                        }
                    } else {
                        journalEntry = new JournalEntry();
                        journalEntry.setSrcTxnType(txnHeader.getTxhdTxnType().getId());
                        journalEntry.setSrcTxnId(txnHeader.getId());
                        journalEntry.setAppUserId(txnHeader.getUser().getId());
                        journalEntry.setOrguId(txnHeader.getOrgUnit().getId());
                        journalEntry.setCssnSessionId(cashSession.getId());
                        journalEntry.setJrnActual(journalRule.isJrnrActual());
                        journalEntry.setJrnDate(new Timestamp(new Date().getTime()));
                        journalEntry.setAccId(journalRule.getAccount().getId());
                        journalEntry.setJrnAccCode(journalRule.getJrnrAccCode());
                        journalEntry.setJrnAccDesc(journalRule.getJrnrAccDesc());
                        if (journalRule.isJrnrDebit()) {
                            journalEntry.setJrnDebit(amount);
                        } else if (journalRule.isJrnrCredit()) {
                            journalEntry.setJrnCredit(amount);
                        }
                        accountingDao.insertJournalEntry(journalEntry);
                        journalEntryList.add(journalEntry);
                    }
                }
            }
            return journalEntryList;
        } catch (Exception e) {
            logger.error("Error in logging journal entry:", e);
            return null;
        }
    }

    private void insertJournalEntrySupport(TxnHeader txnHeader, List<JournalEntry> journalEntries, String paidFromMedia) {
        JournalSupport journalSupport = null;
        if (journalEntries == null || journalEntries.size() < 1) {
            return;
        }
        for (JournalEntry journalEntry : journalEntries) {
            for (TxnMedia txnMedia : txnHeader.getTxnMedias()) {
                if (txnMedia == null || !txnMedia.isNewAdded() || txnMedia.isTxmdVoided()) {
                    continue;
                }
                if (paidFromMedia.equals(IdBConstant.PAID_FROM_ACCOUNT)) {
                    if (!txnMedia.getPaymentMedia().getPaymName().equals(IdBConstant.PAYMENT_MEDIA_ACCOUNT)) {
                        continue;
                    }
                    journalSupport = new JournalSupport();
                    journalSupport.setJrnId(journalEntry.getId());
                    journalSupport.setTxmdId(txnMedia.getId());
                } else if (paidFromMedia.equals(IdBConstant.PAID_FROM_NON_ACCOUNT)) {
                    if (txnMedia.getPaymentMedia().getPaymName().equals(IdBConstant.PAYMENT_MEDIA_ACCOUNT)) {
                        continue;
                    }
                    journalSupport = new JournalSupport();
                    journalSupport.setJrnId(journalEntry.getId());
                    journalSupport.setTxmdId(txnMedia.getId());
                } else {
                    journalSupport = new JournalSupport();
                    journalSupport.setJrnId(journalEntry.getId());
                    journalSupport.setTxmdId(txnMedia.getId());
                }
                accountingDao.insertJournalSupport(journalSupport);
            }
        }
    }


    private Map<String, Double> extractTaxFiguresFromItem(TxnHeader txnHeader) {

        Double totalTaxGroup = null;
        final Map<String, Double> itemTaxGroupMap = new HashMap<String, Double>();
        if (txnHeader != null && txnHeader.getTxnDetails() != null) {
            for (TxnDetail txnDetail : txnHeader.getTxnDetails()) {
                if (txnDetail == null || txnDetail.isTxdeItemVoid() || !txnDetail.isSelected()) {
                    continue;
                }
                for (TaxRule taxRule : txnDetail.getProduct().getProdOrguLink().getTaxRules()) {
                    if (taxRule == null) {
                        continue;
                    }
                    if (itemTaxGroupMap.containsKey(taxRule.getTaxLegVariance().getTxlvCode())) {
                        totalTaxGroup = itemTaxGroupMap.get(taxRule.getTaxLegVariance().getTxlvCode());
                        if (totalTaxGroup != null) {
                            totalTaxGroup = totalTaxGroup + txnDetail.getTxdeValueNet() * txnDetail.getQuantity();
                        }
                    } else {
                        totalTaxGroup = txnDetail.getTxdeValueNet() * txnDetail.getQuantity();
                        itemTaxGroupMap.put(taxRule.getTaxLegVariance().getTxlvCode(), totalTaxGroup);
                    }
                }
            }
        }
        return itemTaxGroupMap;
    }

    /**
     * search Sale Order and Quote per parameters.
     * @param searchForm searchForm
     * @return List of Journal Entry
     */
    public List<JournalEntry> accountingExportReport(GeneralSearchForm searchForm) {
        try {
            if (searchForm == null) {
                logger.error("search form is null");
                return null;
            }
            return accountingDao.accountingExportRpt(SearchClauseBuilder.buildSearchWhereCluase(searchForm, "CASH_SESSION"));
        } catch (Exception e) {
            logger.error("Exception in searching transaction: ", e);
            return null;
        }
    }
}
