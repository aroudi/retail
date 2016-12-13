package au.com.biztune.retail.service;

import au.com.biztune.retail.dao.*;
import au.com.biztune.retail.domain.*;
import au.com.biztune.retail.form.AccountingExportForm;
import au.com.biztune.retail.response.CommonResponse;
import au.com.biztune.retail.session.SessionState;
import au.com.biztune.retail.util.DateUtil;
import au.com.biztune.retail.util.IdBConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.StreamingOutput;
import java.io.IOException;
import java.io.OutputStream;
import java.security.Principal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by arash on 5/12/2016.
 */
@Component
public class AccountingExportServiceImpl implements AccountingExportService {
    private final Logger logger = LoggerFactory.getLogger(AccountingExportServiceImpl.class);

    @Autowired
    private SupplierDao supplierDao;

    @Autowired
    private DeliveryNoteDao deliveryNoteDao;

    @Autowired
    private TaxRuleDao taxRuleDao;

    @Autowired
    private CashSessionDao cashSessionDao;
    @Autowired
    private SessionState sessionState;
    private SecurityContext securityContext;

    @Autowired
    private AccountingDao accountingDao;

    private String buildHeader() {
        final Date currentDate = new Date();
        final String dateStr = DateUtil.dateToString(new Timestamp(currentDate.getTime()), "dd/MM/yyyy");
        return "[BitPos Retail Manager " + dateStr + "]" + " " + "[Exported]";
    }

    private String buildTaxCodeList() {
        final List<TaxRule> taxRuleList = taxRuleDao.getAllTaxRules();
        if (taxRuleList == null) {
            return "";
        }
        final StringBuilder taxCodeList = new StringBuilder();
        for (TaxRule taxRule : taxRuleList) {
            taxCodeList.append("0");
            taxCodeList.append("\t");
            taxCodeList.append(taxRule.getTxrlCode());
            taxCodeList.append(System.getProperty("line.separator"));
        }
        return taxCodeList.toString();
    }

    private String buildGap(String gapStr, int noOfChar) {
        final StringBuilder result = new StringBuilder();
        for (int i = 0; i < noOfChar; i++) {
            result.append(gapStr);
        }
        return result.toString();
    }

    private String buildSupplierRecord(Supplier supplier) {
        if (supplier == null) {
            return "";
        }
        final StringBuilder result = new StringBuilder();
        result.append("1");
        result.append("\t");
        result.append(supplier.getSupplierName());
        result.append(buildGap("\t", 2));
        if (supplier.getContact() != null && supplier.getContact().getAddress1() != null) {
            result.append(supplier.getContact().getAddress1());
            result.append(" ");
        }
        if (supplier.getContact() != null && supplier.getContact().getState() != null) {
            result.append(supplier.getContact().getState());
            result.append(" ");
        }
        if (supplier.getContact() != null && supplier.getContact().getPostCode() != null) {
            result.append(supplier.getContact().getPostCode());
            result.append(" ");
        }
        result.append(buildGap("\t", 8));
        if (supplier.getContact() != null && supplier.getContact().getPhone() != null) {
            result.append(supplier.getContact().getPhone());
        }
        result.append(buildGap("\t", 2));
        if (supplier.getContact() != null && supplier.getContact().getFax() != null) {
            result.append(supplier.getContact().getFax());
        }
        result.append(buildGap("\t", 3));
        if (supplier.getContactFirstName() != null) {
            result.append(supplier.getContactFirstName());
        }
        result.append("\t");
        if (supplier.getContactSurName() != null) {
            result.append(supplier.getContactSurName());
        }
        result.append(buildGap("\t", 11));
        if (supplier.getSupplierCode() != null) {
            result.append(supplier.getSupplierCode());
        }
        result.append("\t");
        result.append(System.getProperty("line.separator"));
        return result.toString();
    }

    private String buildDeliveryNoteRecord(DeliveryNoteHeader deliveryNoteHeader) {
        if (deliveryNoteHeader == null) {
            return "";
        }
        final StringBuilder result = new StringBuilder();
        result.append("2");
        result.append("\t");
        if (deliveryNoteHeader.getSupplier() != null) {
            result.append(deliveryNoteHeader.getSupplier().getSupplierName());
        }
        result.append(buildGap("\t", 6));
        result.append("X");
        result.append(buildGap("\t", 2));
        if (deliveryNoteHeader.getDelnDeliveryDate() != null) {
            result.append(DateUtil.dateToString(deliveryNoteHeader.getDelnDeliveryDate(), "dd/MM/yyyy"));
        }
        result.append("\t");
        if (deliveryNoteHeader.getDelnNoteNumber() != null) {
            result.append(deliveryNoteHeader.getDelnNoteNumber());
        }
        result.append(buildGap("\t", 4));
        final Account inventoryAccount = accountingDao.getAccountByName(IdBConstant.ACCOUNT_INVENTORY);
        if (inventoryAccount != null) {
            result.append(inventoryAccount.getAccCode());
        } else {
            result.append("11400");
        }
        result.append(buildGap("\t", 2));
        result.append(deliveryNoteHeader.getDelnValueNett());
        result.append("\t");
        result.append("55");
        result.append("\t");
        if (deliveryNoteHeader.getDelnComment() != null) {
            result.append(deliveryNoteHeader.getDelnComment());
        }
        result.append("\t");
        result.append("Purchase RetailManager - #");
        result.append(deliveryNoteHeader.getId());
        result.append(buildGap("\t", 2));
        if (deliveryNoteHeader.getFreightAmount() > 0) {
            if (deliveryNoteHeader.getFreightTaxCode() != null) {
                result.append(deliveryNoteHeader.getFreightTaxCode());
            } else {
                result.append("GST");
            }
            result.append(buildGap("\t", 4));
            result.append(deliveryNoteHeader.getFreightAmount());
            result.append("\t");
            result.append(deliveryNoteHeader.getFreightAmount() + deliveryNoteHeader.getFreightTax());
            result.append("\t");
            if (deliveryNoteHeader.getFreightTaxCode() != null) {
                result.append(deliveryNoteHeader.getFreightTaxCode());
            } else {
                result.append("GST");
            }
            result.append(buildGap("\t", 2));
            result.append(deliveryNoteHeader.getFreightTax());
            result.append(System.getProperty("line.separator"));
        }
        return result.toString();
    }


    private String buildJournalEntryRecord(JournalEntry journalEntry, boolean isSecondRecord) {
        if (journalEntry == null) {
            return "";
        }
        String dateStr = "";
        if (journalEntry.getJrnDate() != null) {
            dateStr = DateUtil.dateToString(journalEntry.getJrnDate(), "dd/MM/yyyy");
        }
        final StringBuilder result = new StringBuilder();
        if (isSecondRecord) {
            result.append("\t");
        } else {
            result.append("3");
            result.append(buildGap("\t", 2));
        }
        if (dateStr != null && !dateStr.isEmpty()) {
            result.append(dateStr);
        }
        result.append("\t");

        if (dateStr != null && !dateStr.isEmpty()) {
            result.append(dateStr);
        }
        result.append(" ");
        result.append(journalEntry.getJrnAccDesc());
        if (journalEntry.getAccount().getAccName().equals(IdBConstant.ACCOUNT_SALES_INCOME) && journalEntry.getJrnTaxCode() != null) {
            result.append(" ( " + journalEntry.getJrnTaxCode() + " ) ");
        }
        result.append(" - User: " + journalEntry.getAppUserId() + " - #" + journalEntry.getCssnSessionId());
        result.append("\t");
        result.append(journalEntry.getJrnAccCode());
        result.append("\t");
        if (journalEntry.getJrnDebit() > 0) {
            result.append(journalEntry.getJrnDebit());
        }
        result.append("\t");
        if (journalEntry.getJrnCredit() > 0) {
            result.append(journalEntry.getJrnCredit());
        }
        result.append("\t");
        result.append("55");
        result.append("\t");

        if (!journalEntry.getAccount().getAccName().equals(IdBConstant.ACCOUNT_INVENTORY)
                && !journalEntry.getAccount().getAccName().equals(IdBConstant.ACCOUNT_COST_OF_GOODS)
                && !journalEntry.getAccount().getAccName().equals(IdBConstant.ACCOUNT_CLEARING_ACCOUNT))
        {
            final double creditAmount = journalEntry.getJrnCredit();
            final double debitAmount = journalEntry.getJrnDebit();
            if (journalEntry.getJrnTaxCode() != null) {
                result.append(journalEntry.getJrnTaxCode());
            } else {
                result.append("N-T");
            }
            //for sale income add extra entry
            final Account clearingAccount = accountingDao.getAccountByName(IdBConstant.ACCOUNT_CLEARING_ACCOUNT);
            journalEntry.setAccount(clearingAccount);
            journalEntry.setJrnAccDesc(clearingAccount.getAccDesc());
            journalEntry.setJrnAccCode(clearingAccount.getAccCode());
            if (debitAmount > 0) {
                journalEntry.setJrnCredit(debitAmount);
                journalEntry.setJrnDebit(0.00);
            } else if (creditAmount > 0) {
                journalEntry.setJrnDebit(creditAmount);
                journalEntry.setJrnCredit(0.00);
            }
            result.append(System.getProperty("line.separator"));
            result.append(buildJournalEntryRecord(journalEntry, true));
        } else {
            result.append("N-T");
            if (isSecondRecord) {
                result.append(System.getProperty("line.separator"));
            }

        }
        return result.toString();
    }
    /**
     * Export Locations list into csv format and return as StreamingOutput.
     * @param accountingExportForm accountingExportForm
     * @param securityContext securityContext
     * @return Streamingoutput
     */
    public StreamingOutput exportAccountingDataToTxt(final AccountingExportForm accountingExportForm, SecurityContext securityContext) {
        StreamingOutput streamingOutput = null;
        this.securityContext = securityContext;

        try {
            streamingOutput = new StreamingOutput() {
                @Override
                public void write(OutputStream output) throws IOException, WebApplicationException {
                    prepareAccountingData(output, accountingExportForm);
                }
            };
            return streamingOutput;
        } catch (Exception e) {
            logger.error("Exception in exporting accounting data: ", e);
            return null;
        }
    }


    /**
     * prepare accounting data for export.
     * @param output output
     */
    private void prepareAccountingData (OutputStream output, AccountingExportForm accountingExportForm) {
        try {
            //set user
            final Principal principal = securityContext.getUserPrincipal();
            AppUser appUser = null;
            if (principal instanceof AppUser) {
                appUser = (AppUser) principal;
            }
            final StringBuilder fileContent = new StringBuilder();
            List<Supplier> supplierList = null;
            List<DeliveryNoteHeader> deliveryNoteHeaderList = null;
            List<JournalEntry> journalEntryList = null;
            String content;
            if (output == null) {
                return;
            }
            content = buildHeader();
            output.write(content.getBytes());
            fileContent.append(content);

            output.write(System.getProperty("line.separator").getBytes());
            fileContent.append(System.getProperty("line.separator"));
            output.write(System.getProperty("line.separator").getBytes());
            fileContent.append(System.getProperty("line.separator"));

            content = buildTaxCodeList();
            output.write(content.getBytes());
            fileContent.append(content);


            output.write(System.getProperty("line.separator").getBytes());
            fileContent.append(System.getProperty("line.separator"));
            output.write(System.getProperty("line.separator").getBytes());
            fileContent.append(System.getProperty("line.separator"));

            if (accountingExportForm.isExportSuppliers()) {
                //fetch supplier list
                supplierList = supplierDao.getSupplierListForAccExport();

                if (supplierList != null) {
                    for (Supplier supplier : supplierList) {
                        content = buildSupplierRecord(supplier);
                        output.write(content.getBytes());
                        fileContent.append(content);
                        output.write(System.getProperty("line.separator").getBytes());
                        fileContent.append(System.getProperty("line.separator"));
                    }
                }
                output.write(System.getProperty("line.separator").getBytes());
                fileContent.append(System.getProperty("line.separator"));
                output.write(System.getProperty("line.separator").getBytes());
                fileContent.append(System.getProperty("line.separator"));
            }

            if (accountingExportForm.isExportDeliveryNotes()) {
                //fetch delivery note header list
                deliveryNoteHeaderList = deliveryNoteDao.getDelNoteHeadersForAccExport();
                if (deliveryNoteHeaderList != null) {
                    for (DeliveryNoteHeader deliveryNoteHeader : deliveryNoteHeaderList) {
                        content = buildDeliveryNoteRecord(deliveryNoteHeader);
                        output.write(content.getBytes());
                        fileContent.append(content);
                        output.write(System.getProperty("line.separator").getBytes());
                        fileContent.append(System.getProperty("line.separator"));
                    }
                }
                output.write(System.getProperty("line.separator").getBytes());
                fileContent.append(System.getProperty("line.separator"));
                output.write(System.getProperty("line.separator").getBytes());
                fileContent.append(System.getProperty("line.separator"));
            }

            if (accountingExportForm.isExportAccJournal()) {
                //fetch journal entries.
                final List<Long> sessionList = new ArrayList<Long>();
                //to see if we passing first entry for inventory or second entry
                int inventoryJournalEntryNo = 0;
                if (accountingExportForm.getCashSessionList() != null) {
                    for (CashSession cashSession : accountingExportForm.getCashSessionList()) {
                        sessionList.add(cashSession.getId());
                    }
                    journalEntryList = accountingDao.getJournalEntryPerSessions(sessionList);
                    if (journalEntryList != null) {
                        for (JournalEntry journalEntry : journalEntryList) {
                            if (journalEntry.getAccount().getAccName().equals(IdBConstant.ACCOUNT_INVENTORY)
                                    || journalEntry.getAccount().getAccName().equals(IdBConstant.ACCOUNT_COST_OF_GOODS))
                            {
                                inventoryJournalEntryNo++;
                                //if even
                                if (inventoryJournalEntryNo == 2) {
                                    content = buildJournalEntryRecord(journalEntry, true);
                                    inventoryJournalEntryNo = 0;
                                } else {
                                    content = buildJournalEntryRecord(journalEntry, false);
                                }
                            } else {
                                content = buildJournalEntryRecord(journalEntry, false);
                            }
                            output.write(content.getBytes());
                            fileContent.append(content);
                            output.write(System.getProperty("line.separator").getBytes());
                            fileContent.append(System.getProperty("line.separator"));
                        }
                    }
                }
            }
            //recored the log
            final AccountingExport accountingExport = new AccountingExport();
            accountingExport.setStoreId(sessionState.getStore().getId());
            accountingExport.setOrguId(sessionState.getOrgUnit().getId());
            accountingExport.setAppUserId(appUser.getId());
            accountingExport.setExportTime(new Timestamp((new Date()).getTime()));
            accountingExport.setExportedContents(fileContent.toString());
            accountingExport.setExportSuppliers(accountingExportForm.isExportSuppliers());
            accountingExport.setExportDeliveryNotes(accountingExportForm.isExportDeliveryNotes());
            accountingExport.setExportJournalEntries(accountingExportForm.isExportAccJournal());
            accountingDao.insertAccountingExport(accountingExport);
            if (accountingExportForm.isExportSuppliers() && supplierList != null) {
                AccExportSuppLink accExportSuppLink;
                for (Supplier supplier: supplierList) {
                    accExportSuppLink = new AccExportSuppLink();
                    accExportSuppLink.setAccExpId(accountingExport.getId());
                    accExportSuppLink.setSupplierId(supplier.getId());
                    accountingDao.insertAccExportSuppLink(accExportSuppLink);
                    supplierDao.setSupplierExportFlagPerSuppId(supplier.getId());
                }
            }
            if (accountingExportForm.isExportDeliveryNotes() && deliveryNoteHeaderList != null) {
                AccExportDelnLink accExportDelnLink;
                for (DeliveryNoteHeader deliveryNoteHeader : deliveryNoteHeaderList) {
                    accExportDelnLink = new AccExportDelnLink();
                    accExportDelnLink.setAccExpId(accountingExport.getId());
                    accExportDelnLink.setDelnId(deliveryNoteHeader.getId());
                    accountingDao.insertAccExportDelnLink(accExportDelnLink);
                    deliveryNoteDao.updateDeliveryNoteAccExportStatusById(deliveryNoteHeader.getId());
                }
            }

            if (accountingExport.isExportJournalEntries() && accountingExportForm.getCashSessionList() != null) {
                AccExportSessionLink accExportSessionLink;
                for (CashSession cashSession : accountingExportForm.getCashSessionList()) {
                    accExportSessionLink = new AccExportSessionLink();
                    accExportSessionLink.setAccExpId(accountingExport.getId());
                    accExportSessionLink.setCssnSessionId(cashSession.getId());
                    accountingDao.insertAccExportSessionLink(accExportSessionLink);
                    cashSessionDao.updateCashSessionImportStatus(cashSession.getId());
                }
            }
            //return output
            output.flush();
            output.close();
        } catch (Exception e) {
            logger.error("Exception in preparing accounting data: ", e);
            return;
        }
    }

    /**
     * initiate accounting export form.
     * @return AccountingExportForm.
     */
    public AccountingExportForm initiateExportForm() {
        try {
            final int noOfSuppliers = supplierDao.getNoOfSupplierToBeExported();
            final int noOfDeliveryNotes = deliveryNoteDao.getNoOfDeliveryNoteToBeExported();
            final List<CashSession> cashSessions = cashSessionDao.getAllReconciledCashSession();
            final AccountingExportForm accountingExportForm = new AccountingExportForm();
            accountingExportForm.setNoOfSuppliers(noOfSuppliers);
            accountingExportForm.setNoOfDeliveryNotes(noOfDeliveryNotes);
            accountingExportForm.setExportSuppliers(false);
            accountingExportForm.setExportDeliveryNotes(false);
            accountingExportForm.setExportAccJournal(false);
            if (cashSessions != null) {
                accountingExportForm.setCashSessionList(cashSessions);
            }
            return accountingExportForm;
        } catch (Exception e) {
            logger.error("Exception in initiating export form: ", e);
            return null;
        }
    }

    /**
     * get all accounts.
     * @return List of accounts.
     */
    public List<Account> getAllAccountByOrguId() {
        try {
            return accountingDao.getAllAccountsByOrguId(sessionState.getOrgUnit().getId());
        } catch (Exception e) {
            logger.error("Exception in retreiving accounts", e);
            return null;
        }
    }

    /**
     * update account codes.
     * @param accountList accountList
     * @return Common Response.
     */
    public CommonResponse updateAccountCodes(List<Account> accountList) {
        final CommonResponse response = new CommonResponse();
        try {
            response.setStatus(IdBConstant.RESULT_SUCCESS);
            if (accountList != null && accountList.size() > 0) {
                for (Account account : accountList) {
                    accountingDao.updateAccountCode(account);
                }
            }
            return response;
        } catch (Exception e) {
            logger.error("Exception in updating account codes.", e);
            response.setStatus(IdBConstant.RESULT_FAILURE);
            response.setMessage("Error in saving account code: " + e.getMessage());
            return response;
        }
    }




    /**
     * get all accounting export by orguId.
     * @return List of accounting exports.
     */
    public List<AccountingExport> getAllAccountingExportByOrguId() {
        try {
            return accountingDao.getAllAccountingExportPerOrguId(sessionState.getOrgUnit().getId());
        } catch (Exception e) {
            logger.error("Exception in retreiving accounting export", e);
            return null;
        }
    }

    /**
     * Retreive journal contents from db.
     * @param id id
     * @return Streamingoutput
     */
    public StreamingOutput getExportedAccountsContentById(long id) {
        StreamingOutput streamingOutput = null;

        try {
            final AccountingExport accountingExport = accountingDao.getAccountingExportById(id);
            if (accountingExport == null) {
                return null;
            }
            streamingOutput = new StreamingOutput() {
                @Override
                public void write(OutputStream output) throws IOException, WebApplicationException {
                    output.write(accountingExport.getExportedContents().getBytes());
                    output.flush();
                    output.close();
                }
            };
            return streamingOutput;
        } catch (Exception e) {
            logger.error("Exception in retreiving exported contents: ", e);
            return null;
        }
    }
}
