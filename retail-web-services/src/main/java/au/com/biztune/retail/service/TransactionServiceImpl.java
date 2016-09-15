package au.com.biztune.retail.service;

import au.com.biztune.retail.dao.*;
import au.com.biztune.retail.domain.*;
import au.com.biztune.retail.form.TxnDetailForm;
import au.com.biztune.retail.form.TxnHeaderForm;
import au.com.biztune.retail.form.TxnMediaForm;
import au.com.biztune.retail.response.CommonResponse;
import au.com.biztune.retail.session.SessionState;
import au.com.biztune.retail.util.DateUtil;
import au.com.biztune.retail.util.IdBConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.SecurityContext;
import java.security.Principal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by akhoshraft on 16/03/2016.
 */

@Component
public class TransactionServiceImpl implements TransactionService {
    private final Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);

    @Autowired
    private SessionState sessionState;

    @Autowired
    private TxnDao txnDao;

    @Autowired
    private ConfigCategoryDao configCategoryDao;

    @Autowired
    private StockService stockService;
    private SecurityContext securityContext;
    @Autowired
    private InvoiceDao invoiceDao;

    @Autowired
    private CustomerAccountDebtDao customerAccountDebtDao;
    @Autowired
    private CustomerDao customerDao;
    /**
     * submit a transaction and save it into database.
     * @param  txnHeaderForm txnHeaderForm
     * @param  securityContext securityContext
     * @return CommonResponse
     */
    public CommonResponse saveTransaction(TxnHeaderForm txnHeaderForm, SecurityContext securityContext) {
        this.securityContext = securityContext;
        final CommonResponse response = new CommonResponse();
        try {
            double txhdValueGross = 0.0;
            double txhdValueNett = 0.0;
            double txhdValueTax = 0.0;
            double txhdValuePaid = 0.0;
            response.setStatus(IdBConstant.RESULT_SUCCESS);

            if (txnHeaderForm == null || txnHeaderForm.getTxnDetailFormList() == null || txnHeaderForm.getTxnMediaFormList() == null) {
                response.setStatus(IdBConstant.RESULT_FAILURE);
                response.setMessage("transaction object or its related objects are null");
                return response;
            }
            final boolean isNew = txnHeaderForm.getId() > 0 ? false : true;
            final Timestamp currentDate = new Timestamp(new Date().getTime());
            final TxnHeader txnHeader = new TxnHeader();
            if (!isNew) {
                txnHeader.setId(txnHeaderForm.getId());
                txnHeader.setTxhdVoided(txnHeaderForm.isTxhdVoided());
                //check if we have converted txnQuote to Sale
                if (txnHeaderForm.isConvertedToTxnSale()) {
                    txnHeader.setTxhdOrigTxnNr(txnHeaderForm.getTxhdTxnNr());
                    txnHeader.setTxhdTxnNr(generateTxnNumber(txnHeader.getId(), IdBConstant.TXN_NUMBER_PREFIX));
                    txnHeaderForm.setTxhdTxnNr(txnHeader.getTxhdTxnNr());
                    final ConfigCategory txnType = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_TXN_TYPE, IdBConstant.TXN_TYPE_SALE);
                    txnHeader.setTxhdTxnType(txnType);
                }
            } else {
                txnHeader.setTxhdTradingDate(currentDate);
                txnHeader.setTxhdVoided(false);
            }
            txnHeader.setOrgUnit(sessionState.getStore().getOrgUnit());
            txnHeader.setStore(sessionState.getStore());
            txnHeader.setOrgUnitOriginal(sessionState.getStore().getOrgUnit());
            txnHeader.setTxhdTxnType(txnHeaderForm.getTxhdTxnType());
            txnHeader.setTxhdValueGross(txnHeaderForm.getTxhdValueGross());
            txnHeader.setTxhdValueNett(txnHeaderForm.getTxhdValueNett());
            txnHeader.setTxhdValueDue(txnHeaderForm.getTxhdValueDue());
            txnHeader.setTxhdValRounding(txnHeaderForm.getTxhdValRounding());
            txnHeader.setTxhdValueTax(txnHeaderForm.getTxhdValueTax());
            txnHeader.setCustomer(txnHeaderForm.getCustomer());
            txnHeader.setTxhdDlvAddress(txnHeaderForm.getTxhdDlvAddress());
            final Principal principal = securityContext.getUserPrincipal();
            AppUser appUser = null;
            if (principal instanceof AppUser) {
                appUser = (AppUser) principal;
                txnHeader.setTxhdOperator(appUser.getId());
            }
            if (isNew) {
                //save it to database.
                txnDao.insertTxnHeader(txnHeader);
                if (txnHeader.getTxhdTxnType().getCategoryCode().equals(IdBConstant.TXN_TYPE_SALE)) {
                    txnHeader.setTxhdTxnNr(generateTxnNumber(txnHeader.getId(), IdBConstant.TXN_NUMBER_PREFIX));
                } else if (txnHeader.getTxhdTxnType().getCategoryCode().equals(IdBConstant.TXN_TYPE_QUOTE)) {
                    txnHeader.setTxhdTxnNr(generateTxnNumber(txnHeader.getId(), IdBConstant.QUOTE_NUMBER_PREFIX));
                }
                txnDao.assigneTxnNumber(txnHeader);
                txnHeaderForm.setTxhdTxnNr(txnHeader.getTxhdTxnNr());
            } else {
                txnDao.updateTxnHeader(txnHeader);
            }
            txnHeaderForm.setId(txnHeader.getId());
            TxnDetail txnDetail = null;
            for (TxnDetailForm txnDetailForm: txnHeaderForm.getTxnDetailFormList()) {
                if (txnDetailForm == null) {
                    continue;
                }
                if (txnDetailForm.isDeleted()) {
                    continue;
                }
                txnDetail = new TxnDetail();
                if (isNew) {
                    txnDetail.setOrguId(sessionState.getStore().getOrgUnit().getId());
                    txnDetail.setStoreId(sessionState.getStore().getId());
                } else {
                    txnDetail.setOrguId(txnHeader.getOrgUnit().getId());
                    txnDetail.setStoreId(txnHeader.getStore().getId());
                }
                txnDetail.setTxhdId(txnHeader.getId());
                txnDetail.setProduct(txnDetailForm.getProduct());
                txnDetail.setUnitOfMeasure(txnDetailForm.getUnitOfMeasure());
                txnDetail.setTxdeValueLine(txnDetailForm.getTxdeValueLine());
                txnDetail.setTxdeProfitMargin(txnDetailForm.getTxdeProfitMargin());
                txnDetail.setTxdeValueProfit(txnDetailForm.getTxdeValueProfit());
                txnDetail.setTxdeValueGross(txnDetailForm.getTxdeValueGross());
                txnDetail.setTxdeTax(txnDetailForm.getTxdeTax());
                txnDetail.setTxdeValueNet(txnDetailForm.getTxdeValueNet());
                txnDetail.setTxdeQuantitySold(txnDetailForm.getTxdeQuantitySold());
                txnDetail.setTxdeQtyBalance(txnDetailForm.getTxdeQtyBalance());
                txnDetail.setTxdeQtyTotalInvoiced(txnDetailForm.getTxdeQtyTotalInvoiced() + txnDetailForm.getTxdeQtyInvoiced());
                txnDetail.setTxdePriceSold(txnDetailForm.getTxdePriceSold());
                txnDetail.setTxdeLineRefund(txnDetailForm.isTxdeLineRefund());
                txnDetail.setTxdeItemVoid(txnDetailForm.isTxdeItemVoid());
                if (txnDetailForm.getId() < 0) {
                    final ConfigCategory txntLineType = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_TXN_LINE_TYPE, IdBConstant.TXN_LINE_TYPE_SALE);
                    if (txntLineType != null) {
                        txnDetail.setTxdeDetailType(txntLineType);
                    }
                    txnDao.insertTxnDetail(txnDetail);
                } else {
                    txnDetail.setId(txnDetailForm.getId());
                    txnDao.updateTxnDetail(txnDetail);
                }
                txnDetailForm.setId(txnDetail.getId());
                txhdValueGross = txhdValueGross + txnDetail.getTxdeValueGross() * txnDetail.getTxdeQuantitySold();
                txhdValueNett = txhdValueNett + txnDetail.getTxdeValueNet() * txnDetail.getTxdeQuantitySold();
                txhdValueTax = txhdValueTax + txnDetail.getTxdeTax() * txnDetail.getTxdeValueGross() * txnDetail.getTxdeQuantitySold();;
                //TODO: update stock for sale order
                /*
                if (txnHeader.getTxhdTxnType().getCategoryCode().equals(IdBConstant.TXN_TYPE_SALE)
                        && txnHeader.getTxhdState().getCategoryCode().equals(IdBConstant.TXN_STATE_FINAL))
                {
                    updateStockQuantity(txnHeader, txnDetail);
                }
                */
            }
            //save txn media
            for (TxnMediaForm txnMediaForm : txnHeaderForm.getTxnMediaFormList()) {
                txnMediaForm.setTxhdId(txnHeader.getId());
                doSaleOrderPayment(txnMediaForm);
            }
            //get amount paid for transaction
            final Double amountPaid = txnDao.getTxnHeaderAmountPaid(txnHeader.getId());
            if (amountPaid == null) {
                txhdValuePaid = 0.00;
            } else {
                txhdValuePaid = amountPaid;
            }
            //update txn header total values
            txnHeader.setTxhdValueGross(txhdValueGross);
            txnHeader.setTxhdValueNett(txhdValueNett);
            txnHeader.setTxhdValueTax(txhdValueTax);
            txnHeader.setTxhdValueDue(txhdValueNett - txhdValuePaid);
            if (Math.abs(txhdValueNett - txhdValuePaid) <= IdBConstant.ROUNDING_VALUE_BASE) {
                txnHeader.setTxhdValueDue(0.00);
                txnHeader.setTxhdValRounding(txhdValueNett - txhdValuePaid);
            }
            ConfigCategory txnState = null;

            if (txnHeader.getTxhdValueDue() > 0) {
                txnState = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_TXN_STATE, IdBConstant.TXN_STATE_DRAFT);
            } else {
                txnState = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_TXN_STATE, IdBConstant.TXN_STATE_FINAL);
            }
            if (txnState != null) {
                txnHeader.setTxhdState(txnState);
            }
            txnDao.updateTxnHeaderTotalValues(txnHeader);
            response.setInfo(txnHeader.getTxhdTxnNr());
        return response;
        } catch (Exception e) {
            logger.error("Exception in saving transaction: ", e);
            response.setStatus(IdBConstant.RESULT_FAILURE);
            response.setMessage("Exception in saving Transaction");
            return response;
        }
    }

    /**
     * add payment.
     * @param txnHeaderForm txnHeaderForm
     * @param securityContext securityContext
     * @return Response.
     */
    public CommonResponse addPayment(TxnHeaderForm txnHeaderForm, SecurityContext securityContext) {
        this.securityContext = securityContext;
        final CommonResponse response = new CommonResponse();
        try {
            response.setStatus(IdBConstant.RESULT_SUCCESS);

            if (txnHeaderForm == null || txnHeaderForm.getTxnDetailFormList() == null || txnHeaderForm.getTxnMediaFormList() == null) {
                response.setStatus(IdBConstant.RESULT_FAILURE);
                response.setMessage("transaction object or its related objects are null");
                return response;
            }

            final Timestamp currentDate = new Timestamp(new Date().getTime());
            final TxnHeader txnHeader = new TxnHeader();
            txnHeader.setId(txnHeaderForm.getId());
            txnHeader.setOrgUnit(txnHeaderForm.getStore().getOrgUnit());
            txnHeader.setStore(txnHeaderForm.getStore());
            /*
            final ConfigCategory txnState = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_TXN_STATE, IdBConstant.TXN_STATE_DRAFT);
            if (txnState != null) {
                txnHeader.setTxhdState(txnState);
            }
            */
            txnHeader.setTxhdValueGross(txnHeaderForm.getTxhdValueGross());
            txnHeader.setTxhdValueNett(txnHeaderForm.getTxhdValueNett());
            txnHeader.setTxhdValueDue(txnHeaderForm.getTxhdValueDue());
            txnHeader.setTxhdValRounding(txnHeaderForm.getTxhdValRounding());
            txnHeader.setTxhdValueTax(txnHeaderForm.getTxhdValueTax());

            //save it to database.
            final Principal principal = securityContext.getUserPrincipal();
            AppUser appUser = null;
            if (principal instanceof AppUser) {
                appUser = (AppUser) principal;
                txnHeader.setTxhdOperator(appUser.getId());
            }
            txnDao.updateTxnHeader(txnHeader);


            //save txn media
            for (TxnMediaForm txnMediaForm : txnHeaderForm.getTxnMediaFormList()) {
                txnMediaForm.setTxhdId(txnHeader.getId());
                doSaleOrderPayment(txnMediaForm);
            }
            response.setInfo(txnHeaderForm.getTxhdTxnNr());
            return response;
        } catch (Exception e) {
            logger.error("Exception in saving transaction: ", e);
            response.setStatus(IdBConstant.RESULT_FAILURE);
            response.setMessage("Exception in saving Transaction");
            return response;
        }
    }


    /**
     * get all transaction header of type sale and quote of store.
     * @return List of TxnHeader
     */
    public List<TxnHeader> getAllTxnHeadersForStore() {
        try {
            final List<Long> txnType = new ArrayList<Long>();
            ConfigCategory configCategory = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_TXN_TYPE, IdBConstant.TXN_TYPE_SALE);
            txnType.add(configCategory.getId());
            configCategory = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_TXN_TYPE, IdBConstant.TXN_TYPE_QUOTE);
            txnType.add(configCategory.getId());
            return txnDao.getTxnHeaderPerStoreId(sessionState.getStore().getId(), txnType);
        } catch (Exception e) {
            logger.error("Exception in getting transaction sale: ", e);
            return null;
        }
    }

    /**
     * get Transaction Sale per Id.
     * @param  txhdId Transaction Header Id
     * @return TxnHeaderForm
     */
    public TxnHeaderForm getTxnHeaderPerId(long txhdId) {
        try {
            final TxnHeader txnHeader = txnDao.getTxnHeaderPerTxhdId(txhdId);
            if (txnHeader == null) {
                logger.error("Not able to fetch transaction id " + txhdId);
                return null;
            }
            return createTxnHeaderToForm(txnHeader);
        } catch (Exception e) {
            logger.error("Exception in getting transaction per id: ", e);
            return null;
        }
    }

    /**
     * get Invoice per Id.
     * @param  invoiceId invoiceId
     * @return TxnHeaderForm
     */
    public TxnHeaderForm getInvoicePerId(long invoiceId) {
        try {
            final TxnHeader txnHeader = invoiceDao.getInvoiceHeaderPerInvoiceId(invoiceId);
            if (txnHeader == null) {
                logger.error("Not able to fetch invoice id " + invoiceId);
                return null;
            }
            //set txn_type to invoice
            final ConfigCategory txnType = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_TXN_TYPE, IdBConstant.TXN_TYPE_INVOICE);
            if (txnType != null) {
                txnHeader.setTxhdTxnType(txnType);
            }
            //set txn_state to invoice
            final ConfigCategory txnState = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_TXN_STATE, IdBConstant.TXN_STATE_FINAL);
            if (txnState != null) {
                txnHeader.setTxhdState(txnType);
            }
            return createTxnHeaderToForm(txnHeader);
        } catch (Exception e) {
            logger.error("Exception in getting invoice per id: ", e);
            return null;
        }
    }

    /**
     * create Form from TxnHeader.
     * @param txnHeader txnHeader
     * @return TxnHeaderForm
     */
    public TxnHeaderForm createTxnHeaderToForm(TxnHeader txnHeader) {
        final TxnHeaderForm txnHeaderForm = new TxnHeaderForm();
        txnHeaderForm.setStore(txnHeader.getStore());
        txnHeaderForm.setTxhdTxnType(txnHeader.getTxhdTxnType());
        txnHeaderForm.setTxhdState(txnHeader.getTxhdState());
        txnHeaderForm.setTxhdTxnNr(txnHeader.getTxhdTxnNr());
        txnHeaderForm.setId(txnHeader.getId());
        txnHeaderForm.setTxhdOrigTxnNr(txnHeader.getTxhdOrigTxnNr());
        txnHeaderForm.setTxhdValueGross(txnHeader.getTxhdValueGross());
        txnHeaderForm.setTxhdValueNett(txnHeader.getTxhdValueNett());
        txnHeaderForm.setTxhdValueDue(txnHeader.getTxhdValueDue());
        txnHeaderForm.setTxhdValueChange(txnHeader.getTxhdValueChange());
        txnHeaderForm.setTxhdValRounding(txnHeader.getTxhdValRounding());
        txnHeaderForm.setTxhdValueTax(txnHeader.getTxhdValueTax());
        txnHeaderForm.setCustomer(txnHeader.getCustomer());
        txnHeaderForm.setTxhdDlvAddress(txnHeader.getTxhdDlvAddress());

        if (txnHeader.getTxnDetails() != null) {
            TxnDetailForm txnDetailForm;
            final List<TxnDetailForm> txnDetailFormList = new ArrayList<TxnDetailForm>();
            for (TxnDetail txnDetail : txnHeader.getTxnDetails()) {
                if (txnDetail == null) {
                    continue;
                }
                txnDetailForm = new TxnDetailForm();
                txnDetailForm.setId(txnDetail.getId());
                txnDetailForm.setTxdePriceOveriden(txnDetail.isTxdePriceOveriden());
                txnDetailForm.setProduct(txnDetail.getProduct());
                txnDetailForm.setUnitOfMeasure(txnDetail.getUnitOfMeasure());
                txnDetailForm.setTxdeValueLine(txnDetail.getTxdeValueLine());
                txnDetailForm.setTxdeProfitMargin(txnDetail.getTxdeProfitMargin());
                txnDetailForm.setTxdeValueGross(txnDetail.getTxdeValueGross());
                txnDetailForm.setTxdeValueProfit(txnDetail.getTxdeValueProfit());
                txnDetailForm.setTxdeTax(txnDetail.getTxdeTax());
                txnDetailForm.setTxdeValueNet(txnDetail.getTxdeValueNet());
                txnDetailForm.setTxdeQuantitySold(txnDetail.getTxdeQuantitySold());
                txnDetailForm.setTxdeQtyBalance(txnDetail.getTxdeQtyBalance());
                txnDetailForm.setTxdeQtyTotalInvoiced(txnDetail.getTxdeQtyTotalInvoiced());
                txnDetailForm.setTxdePriceSold(txnDetail.getTxdePriceSold());
                txnDetailForm.setTxdeLineRefund(txnDetail.isTxdeLineRefund());
                txnDetailForm.setTxdeItemVoid(txnDetail.isTxdeItemVoid());
                txnDetailForm.setTxdeDetailType(txnDetail.getTxdeDetailType());
                txnDetailForm.setCalculatedLineValue(txnDetailForm.getTxdeQuantitySold() * txnDetailForm.getTxdeValueGross());
                txnDetailForm.setCalculatedLineTax(txnDetailForm.getTxdeTax() * txnDetailForm.getCalculatedLineValue());
                txnDetailFormList.add(txnDetailForm);
            }
            txnHeaderForm.setTxnDetailFormList(txnDetailFormList);
        }
        if (txnHeader.getTxnMedias() != null) {
            TxnMediaForm txnMediaForm;
            final List<TxnMediaForm> txnMediaFormList = new ArrayList<TxnMediaForm>();
            for (TxnMedia txnMedia: txnHeader.getTxnMedias()) {
                if (txnMedia == null) {
                    continue;
                }
                txnMediaForm = new TxnMediaForm();
                txnMediaForm.setTxhdId(txnHeaderForm.getId());
                txnMediaForm.setId(txnMedia.getId());
                txnMediaForm.setPaymentMedia(txnMedia.getPaymentMedia());
                txnMediaForm.setTxmdType(txnMedia.getTxmdType());
                txnMediaForm.setTxmdAmountLocal(txnMedia.getTxmdAmountLocal());
                txnMediaForm.setTxmdVoided(txnMedia.isTxmdVoided());
                txnMediaFormList.add(txnMediaForm);
            }
            txnHeaderForm.setTxnMediaFormList(txnMediaFormList);
        }
        return txnHeaderForm;
    }

    /**
     * generate Transaction Number.
     * @param txnId txnId
     * @param preFix preFix
     * @return TxnNumber
     */
    public String generateTxnNumber(long txnId, String preFix) {
        final Timestamp currentDate = new Timestamp(new Date().getTime());
        return preFix + DateUtil.dateToString(currentDate, "yyyy-MM-dd") + "-" + txnId;
    }

    /**
     * update stock quantity.
     * @param txnHeader txnHeader
     * @param txnDetail txnDetail
     */
    public void updateStockQuantity(TxnHeader txnHeader, TxnDetail txnDetail) {
        try {
            //get current user from security context.
            final Principal principal = securityContext.getUserPrincipal();
            AppUser appUser = null;
            if (principal instanceof AppUser) {
                appUser = (AppUser) principal;
            }

            String txnType = null;
            if (txnHeader.getTxhdTxnType().getCategoryCode().equals(IdBConstant.TXN_TYPE_QUOTE)) {
                txnType = IdBConstant.TXN_TYPE_QUOTE;
            } else if (txnHeader.getTxhdTxnType().getCategoryCode().equals(IdBConstant.TXN_TYPE_SALE)) {
                txnType = IdBConstant.TXN_TYPE_SALE;
            } else if (txnHeader.getTxhdTxnType().getCategoryCode().equals(IdBConstant.TXN_TYPE_INVOICE)) {
                txnType = IdBConstant.TXN_TYPE_INVOICE;
            }
            final Timestamp currentTime = new Timestamp(new Date().getTime());
            final StockEvent stockEvent = new StockEvent();
            stockEvent.setTxnTypeConst(txnType);
            stockEvent.setStckQty(txnDetail.getTxdeQuantitySold());
            stockEvent.setUnomId(txnDetail.getUnitOfMeasure().getId());
            //stockEvent.setSupplierId();
            stockEvent.setCostPrice(txnDetail.getTxdeValueLine());
            stockEvent.setProdId(txnDetail.getProduct().getId());
            stockEvent.setSellPrice(txnDetail.getTxdeValueNet());
            stockEvent.setStckEvntDate(currentTime);
            stockEvent.setTxnDate(txnHeader.getTxhdTradingDate());
            stockEvent.setTxnHeader(txnHeader.getId());
            stockEvent.setTxnLine(txnDetail.getId());
            stockEvent.setUserId(appUser.getId());
            stockEvent.setTxnNumber(txnHeader.getTxhdTxnNr());
            stockEvent.setOrguId(sessionState.getOrgUnit().getId());
            stockService.pushStockEvent(stockEvent);
        } catch (Exception e) {
            logger.error("Exception in creating stock event:", e);
        }
    }

    /**
     * create invoice from transaction.
     * @param  txnHeaderForm txnHeaderForm
     * @param  securityContext securityContext
     * @return CommonResponse
     */
    public CommonResponse createInvoice(TxnHeaderForm txnHeaderForm, SecurityContext securityContext) {
        this.securityContext = securityContext;
        CommonResponse response = new CommonResponse();
        final long invoiceId;
        try {
            response.setStatus(IdBConstant.RESULT_SUCCESS);

            if (txnHeaderForm == null || txnHeaderForm.getTxnDetailFormList() == null || txnHeaderForm.getTxnMediaFormList() == null) {
                response.setStatus(IdBConstant.RESULT_FAILURE);
                response.setMessage("transaction object or its related objects are null");
                return response;
            }
            final boolean isNew = txnHeaderForm.getId() > 0 ? false : true;
            response = saveTransaction(txnHeaderForm, securityContext);
            if (response.getStatus() == IdBConstant.RESULT_FAILURE) {
                return  response;
            }
            final Timestamp currentDate = new Timestamp(new Date().getTime());
            final TxnHeader txnHeader = new TxnHeader();
            txnHeader.setOrgUnit(sessionState.getStore().getOrgUnit());
            txnHeader.setStore(sessionState.getStore());
            txnHeader.setOrgUnitOriginal(sessionState.getStore().getOrgUnit());
            txnHeader.setTxhdTradingDate(currentDate);
            txnHeader.setTxhdValueGross(txnHeaderForm.getTxhdValueGross());
            txnHeader.setTxhdValueNett(txnHeaderForm.getTxhdValueNett());
            txnHeader.setTxhdValueDue(txnHeaderForm.getTxhdValueDue());
            txnHeader.setTxhdValRounding(txnHeaderForm.getTxhdValRounding());
            txnHeader.setTxhdValueTax(txnHeaderForm.getTxhdValueTax());
            txnHeader.setCustomer(txnHeaderForm.getCustomer());
            txnHeader.setTxhdOrigTxnNr(txnHeaderForm.getTxhdTxnNr());
            txnHeader.setParentId(txnHeaderForm.getId());
            final Principal principal = securityContext.getUserPrincipal();
            AppUser appUser = null;
            if (principal instanceof AppUser) {
                appUser = (AppUser) principal;
                txnHeader.setTxhdOperator(appUser.getId());
            }
            //save invoice
            invoiceDao.insertInvoice(txnHeader);
            invoiceId = txnHeader.getId();
            txnHeader.setTxhdTxnNr(generateTxnNumber(txnHeader.getId(), IdBConstant.INVOICE_PREFIX));
            invoiceDao.assigneInvoiceNumber(txnHeader);
            TxnDetail txnDetail = null;
            TxnDetail txnDetailMain = null;
            for (TxnDetailForm txnDetailForm: txnHeaderForm.getTxnDetailFormList()) {
                if (txnDetailForm == null) {
                    continue;
                }
                if (txnDetailForm.isDeleted()) {
                    continue;
                }
                //see if item has been selected for being invoiced.
                if (!txnDetailForm.isInvoiced()) {
                    continue;
                }
                txnDetail = new TxnDetail();
                txnDetail.setOrguId(sessionState.getStore().getOrgUnit().getId());
                txnDetail.setStoreId(sessionState.getStore().getId());
                txnDetail.setTxhdId(txnHeader.getId());
                txnDetail.setProduct(txnDetailForm.getProduct());
                txnDetail.setUnitOfMeasure(txnDetailForm.getUnitOfMeasure());
                txnDetail.setTxdeValueLine(txnDetailForm.getTxdeValueLine());
                txnDetail.setTxdeProfitMargin(txnDetailForm.getTxdeProfitMargin());
                txnDetail.setTxdeValueProfit(txnDetailForm.getTxdeValueProfit());
                txnDetail.setTxdeValueGross(txnDetailForm.getTxdeValueGross());
                txnDetail.setTxdeTax(txnDetailForm.getTxdeTax());
                txnDetail.setTxdeValueNet(txnDetailForm.getTxdeValueNet());
                //set quantity to number invoiced
                txnDetail.setTxdeQuantitySold(txnDetailForm.getTxdeQtyInvoiced());
                txnDetail.setTxdePriceSold(txnDetailForm.getTxdePriceSold());
                txnDetail.setTxdeLineRefund(txnDetailForm.isTxdeLineRefund());
                txnDetail.setTxdeItemVoid(txnDetailForm.isTxdeItemVoid());
                txnDetail.setTxdeParentDetail(txnDetailForm.getId());
                invoiceDao.insertInvoiceDetail(txnDetail);
                updateStockQuantity(txnHeader, txnDetail);
                //update balance on Sale Order
                txnDetailMain = new TxnDetail();
                txnDetailMain.setId(txnDetailForm.getId());
                txnDetailMain.setTxdeQtyBalance(txnDetailForm.getTxdeQtyBalance());
                txnDetailMain.setTxdeQtyTotalInvoiced(txnDetailForm.getTxdeQtyTotalInvoiced() + txnDetailForm.getTxdeQtyInvoiced());
                txnDao.updateTxnDetailQtyBalance(txnDetailMain);
            }
            //check due value.
            double totalUnpaid = txnHeaderForm.getTxhdValueNett();
            //if customer owing us
            if (txnHeaderForm.getTxhdValueDue() >= 0) {
                for (TxnMediaForm txnMediaForm : txnHeaderForm.getTxnMediaFormList()) {
                    //the media need to be saved under Sale Transaction(not invoice) and then link to invoice
                    txnMediaForm.setTxhdId(txnHeaderForm.getId());
                    if (txnMediaForm == null) {
                        continue;
                    }
                    if (txnMediaForm.isDeleted()) {
                        continue;
                    }
                    if (txnMediaForm.isTxmdVoided()) {
                        continue;
                    }
                    if (txnMediaForm.getTxmdType().getCategoryCode().equals(IdBConstant.TXN_MEDIA_TYPE_DEPOSIT)) {
                        //PAY FROM DEPOSEIT
                        doInvoicePaymentFromDeposit(txnMediaForm, txnMediaForm.getTxmdAmountLocal(), invoiceId);
                    } else {
                        //link to invoice
                        doInvoicePayment(txnMediaForm, invoiceId);
                    }
                }
            } else {
                for (TxnMediaForm txnMediaForm : txnHeaderForm.getTxnMediaFormList()) {
                    txnMediaForm.setTxhdId(txnHeaderForm.getId());
                    if (txnMediaForm == null) {
                        continue;
                    }
                    if (txnMediaForm.isDeleted()) {
                        continue;
                    }
                    if (txnMediaForm.isTxmdVoided()) {
                        continue;
                    }
                    if (txnMediaForm.getTxmdType().getCategoryCode().equals(IdBConstant.TXN_MEDIA_TYPE_DEPOSIT)) {
                        if (totalUnpaid >= txnMediaForm.getTxmdAmountLocal()) {
                            doInvoicePaymentFromDeposit(txnMediaForm, txnMediaForm.getTxmdAmountLocal(), invoiceId);
                            totalUnpaid = totalUnpaid - txnMediaForm.getTxmdAmountLocal();
                            if (totalUnpaid == 0) {
                                //payment done!
                                break;
                            }
                            continue;
                        } else {
                            doInvoicePaymentFromDeposit(txnMediaForm, totalUnpaid, invoiceId);
                            //payment done!
                            break;
                        }
                    }
                }
            }
            //check if customer has used from his credit account.
            if (txnHeaderForm.getCustomer().getCustomerType().getCategoryCode().equals(IdBConstant.CUSTOMER_TYPE_ACCOUNT)
                    && (txnHeaderForm.getTxhdValueCredit() > 0))
            {
                final CustomerAccountDebt customerAccountDebt = new CustomerAccountDebt();
                customerAccountDebt.setCustomer(txnHeaderForm.getCustomer());
                customerAccountDebt.setTxhdId(invoiceId);
                customerAccountDebt.setCadAmountDebt(txnHeaderForm.getTxhdValueCredit());
                customerAccountDebt.setBalance(txnHeaderForm.getTxhdValueCredit());
                customerAccountDebt.setCadPaid(false);
                customerAccountDebt.setTxhdTxnNr(txnHeader.getTxhdTxnNr());
                Date startDate = null;
                Calendar cal = null;
                //calculate due date for payment
                if (txnHeaderForm.getCustomer().isCreditStartEom()) {
                    cal = Calendar.getInstance();
                    cal.setTime(currentDate);
                    cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
                    startDate = cal.getTime();
                } else {
                    startDate = new Date();
                }
                customerAccountDebt.setCadStartDate(new Timestamp(startDate.getTime()));
                //calculate due date
                cal = Calendar.getInstance();
                cal.setTime(startDate);
                cal.add(Calendar.DATE, txnHeaderForm.getCustomer().getCreditDuration());
                customerAccountDebt.setCadDueDate(new Timestamp(cal.getTime().getTime()));
                customerAccountDebtDao.insert(customerAccountDebt);
                //update customer debt
                txnHeaderForm.getCustomer().setRemainCredit(txnHeaderForm.getCustomer().getRemainCredit() - txnHeaderForm.getTxhdValueCredit());
                txnHeaderForm.getCustomer().setOwing(txnHeaderForm.getCustomer().getOwing() + txnHeaderForm.getTxhdValueCredit());
                customerDao.updateCustomerDebt(txnHeaderForm.getCustomer());
            }
            response.setInfo(String.valueOf(invoiceId));
            return response;
        } catch (Exception e) {
            logger.error("Exception in saving transaction: ", e);
            response.setStatus(IdBConstant.RESULT_FAILURE);
            response.setMessage("Exception in saving Transaction");
            return response;
        }
    }

    /**
     * do paymebnt for Sale Order.
     * @param txnMediaForm txnMediaForm
     */
    public void doSaleOrderPayment(TxnMediaForm txnMediaForm) {
        if (txnMediaForm == null) {
            return;
        }
        if (txnMediaForm.isDeleted()) {
            return;
        }
        final TxnMedia txnMedia = new TxnMedia();
        txnMedia.setOrguId(sessionState.getStore().getOrgUnit().getId());
        txnMedia.setStoreId(sessionState.getStore().getId());
        txnMedia.setTxhdId(txnMediaForm.getTxhdId());
        txnMedia.setTxmdVoided(txnMediaForm.isTxmdVoided());
        txnMedia.setMedtId(txnMediaForm.getPaymentMedia().getMediaType().getId());
        txnMedia.setPaymentMedia(txnMediaForm.getPaymentMedia());
        txnMedia.setTxmdType(txnMediaForm.getTxmdType());
        txnMedia.setTxmdAmountLocal(txnMediaForm.getTxmdAmountLocal());
        //insert new txn_media
        if (txnMediaForm.getId() <= 0) {
            txnDao.insertTxnMedia(txnMedia);
            txnMediaForm.setId(txnMedia.getId());
        } else if (txnMediaForm.isTxmdVoided()) {
            txnMedia.setId(txnMediaForm.getId());
            txnDao.voidTxnMedia(txnMedia);
        }
    }


    /**
     * do paymebnt for invoice.
     * @param txnMediaForm txnMediaForm
     * @param invoiceId invoiceId
     */
    public void doInvoicePayment(TxnMediaForm txnMediaForm, long invoiceId) {
        final TxnMedia txnMedia = new TxnMedia();
        txnMedia.setOrguId(sessionState.getStore().getOrgUnit().getId());
        txnMedia.setStoreId(sessionState.getStore().getId());
        txnMedia.setTxhdId(invoiceId);
        txnMedia.setParentId(txnMediaForm.getId());
        txnMedia.setTxmdAmountLocal(txnMediaForm.getTxmdAmountLocal());
        invoiceDao.insertIncoiceMedia(txnMedia);
    }

    /**
     * do invoice payment from deposit.
     * @param txnMediaForm txnMediaForm
     * @param invoiceId invoiceId
     * @param amount amount need to pay for this invoice
     */
    public void doInvoicePaymentFromDeposit(TxnMediaForm txnMediaForm, double amount, long invoiceId) {

        //check amount. it should be smaller than deposit amount.
        if (amount > txnMediaForm.getTxmdAmountLocal()) {
            return;
        }
        //first we need to void the original deposit.
        //check the amount. if less than deposit, we need pay amount from deposit and save rest of deposit as a new PaymentMedea
        final TxnMedia txnMedia = new TxnMedia();
        txnMedia.setTxmdVoided(true);
        txnMedia.setId(txnMediaForm.getId());
        txnDao.voidTxnMedia(txnMedia);
        //pay amount for this invoice
        final TxnMediaForm invoicePaymentMedia = new TxnMediaForm();
        invoicePaymentMedia.setId(-1);
        invoicePaymentMedia.setTxmdAmountLocal(amount);
        invoicePaymentMedia.setParentId(txnMediaForm.getId());
        invoicePaymentMedia.setTxhdId(txnMediaForm.getTxhdId());
        invoicePaymentMedia.setTxmdVoided(false);
        invoicePaymentMedia.setPaymentMedia(txnMediaForm.getPaymentMedia());
        final ConfigCategory txnMediaType = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_TXN_MEDIA_TYPE, IdBConstant.TXN_MEDIA_TYPE_SALE);
        if (txnMediaType != null) {
            invoicePaymentMedia.setTxmdType(txnMediaType);
        }
        //do payment
        doSaleOrderPayment(invoicePaymentMedia);
        //link it to this invoice
        doInvoicePayment(invoicePaymentMedia, invoiceId);

        //check if we have any deposit amount left
        final double depositRemain = txnMediaForm.getTxmdAmountLocal() - amount;
        if (depositRemain > 0) {
            final TxnMediaForm depositPaymentMedia = new TxnMediaForm();
            depositPaymentMedia.setTxmdAmountLocal(depositRemain);
            depositPaymentMedia.setParentId(txnMediaForm.getId());
            depositPaymentMedia.setTxhdId(txnMediaForm.getTxhdId());
            depositPaymentMedia.setTxmdVoided(false);
            depositPaymentMedia.setPaymentMedia(txnMediaForm.getPaymentMedia());
            final ConfigCategory txnMediaDepositType = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_TXN_MEDIA_TYPE, IdBConstant.TXN_MEDIA_TYPE_DEPOSIT);
            if (txnMediaDepositType != null) {
                depositPaymentMedia.setTxmdType(txnMediaDepositType);
            }
            doSaleOrderPayment(depositPaymentMedia);
            //link it to this invoice
            doInvoicePayment(depositPaymentMedia, invoiceId);
        }
    }
    /**
     * get all transaction of store.
     * @return List of TxnHeader
     */
    public List<TxnHeader> getAllInvoiceHeadersForStore() {
        try {
            return invoiceDao.getAllInvoiceHeaderPerStoreId(sessionState.getStore().getId());
        } catch (Exception e) {
            logger.error("Exception in getting transaction sale: ", e);
            return null;
        }
    }

    /**
     * create Txn Account Payment.
     * @param debtorPaymentForm debtorPaymentForm
     * @param securityContext securityContext
     * @return CommonResponse
     */
    public CommonResponse createTxnAccPayment(DebtorPaymentForm debtorPaymentForm, SecurityContext securityContext) {
        this.securityContext = securityContext;
        final CommonResponse response = new CommonResponse();
        try {
            response.setStatus(IdBConstant.RESULT_SUCCESS);

            if (debtorPaymentForm == null || debtorPaymentForm.getDebtList() == null || debtorPaymentForm.getTxnMediaList() == null) {
                response.setStatus(IdBConstant.RESULT_FAILURE);
                response.setMessage("transaction object or its related objects are null");
                return response;
            }
                final Timestamp currentDate = new Timestamp(new Date().getTime());
                final TxnHeader txnHeader = new TxnHeader();
                txnHeader.setOrgUnit(sessionState.getStore().getOrgUnit());
                txnHeader.setStore(sessionState.getStore());
                final ConfigCategory txnType = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_TXN_TYPE, IdBConstant.TXN_TYPE_ACCOUNT_PAYMENT);
                if (txnType != null) {
                    txnHeader.setTxhdTxnType(txnType);
                }
                txnHeader.setOrgUnitOriginal(sessionState.getStore().getOrgUnit());
                txnHeader.setTxhdTradingDate(currentDate);
                txnHeader.setTxhdVoided(false);
                txnHeader.setTxhdValueNett(debtorPaymentForm.getTotal());
                txnHeader.setTxhdValueDue(debtorPaymentForm.getAmountDue());
                txnHeader.setTxhdValRounding(debtorPaymentForm.getValueRounding());
                txnHeader.setCustomer(debtorPaymentForm.getCustomer());
                final Principal principal = securityContext.getUserPrincipal();
                AppUser appUser = null;
                if (principal instanceof AppUser) {
                    appUser = (AppUser) principal;
                    txnHeader.setTxhdOperator(appUser.getId());
                }
                //save it to database.
                txnDao.insertTxnHeader(txnHeader);
                txnHeader.setTxhdTxnNr(generateTxnNumber(txnHeader.getId(), IdBConstant.TXN_NUMBER_PREFIX));
                txnDao.assigneTxnNumber(txnHeader);
                //save txn_acc_payment items
                for (CustomerAccountDebt customerAccountDebt : debtorPaymentForm.getDebtList()) {
                    if (customerAccountDebt == null) {
                        continue;
                    }
                    customerAccountDebt.setOrguId(sessionState.getStore().getOrgUnit().getId());
                    customerAccountDebt.setStoreId(sessionState.getStore().getId());
                    customerAccountDebt.setCadPaymentDate(currentDate);
                    customerAccountDebt.setTxnAccPayId(txnHeader.getId());
                    customerAccountDebtDao.insertTxnAccPayment(customerAccountDebt);

                    if (customerAccountDebt.getBalance() - customerAccountDebt.getPaying() == 0.00) {
                        customerAccountDebt.setCadPaid(true);
                    }
                    //update the customer account debt amount
                    customerAccountDebt.setBalance(customerAccountDebt.getBalance() - customerAccountDebt.getPaying());
                    customerAccountDebtDao.update(customerAccountDebt);
                }

                //save txn media
                final ConfigCategory mediaType = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_TXN_MEDIA_TYPE, IdBConstant.TXN_MEDIA_TYPE_SALE);
                for (TxnMediaForm txnMediaForm : debtorPaymentForm.getTxnMediaList()) {
                    txnMediaForm.setTxmdType(mediaType);
                    txnMediaForm.setTxhdId(txnHeader.getId());
                    doSaleOrderPayment(txnMediaForm);
                }
                response.setInfo(txnHeader.getTxhdTxnNr());
            return response;
        } catch (Exception e) {
            logger.error("Exception in saving transaction: ", e);
            response.setStatus(IdBConstant.RESULT_FAILURE);
            response.setMessage("Exception in saving Transaction");
            return response;
        }
    }



}
