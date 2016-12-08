package au.com.biztune.retail.service;

import au.com.biztune.retail.domain.Account;
import au.com.biztune.retail.domain.AccountingExport;
import au.com.biztune.retail.form.AccountingExportForm;
import au.com.biztune.retail.response.CommonResponse;

import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.StreamingOutput;
import java.util.List;

/**
 * Created by arash on 5/12/2016.
 */
public interface AccountingExportService {
    /**
     * Export Locations list into csv format and return as StreamingOutput.
     * @param accountingExportForm accountingExportForm
     * @param securityContext securityContext
     * @return Streamingoutput
     */
    StreamingOutput exportAccountingDataToTxt(final AccountingExportForm accountingExportForm, SecurityContext securityContext);

    /**
     * initiate accounting export form.
     * @return AccountingExportForm.
     */
    AccountingExportForm initiateExportForm();

    /**
     * get all accounts.
     * @return List of accounts.
     */
    List<Account> getAllAccountByOrguId();

    /**
     * update account codes.
     * @param accountList accountList
     * @return Common Response.
     */
    CommonResponse updateAccountCodes(List<Account> accountList);

    /**
     * get all accounting export by orguId.
     * @return List of accounting exports.
     */
    List<AccountingExport> getAllAccountingExportByOrguId();

    /**
     * Retreive journal contents from db.
     * @param id id
     * @return Streamingoutput
     */
    StreamingOutput getExportedAccountsContentById(long id);
    }


