package au.com.biztune.retail.service;

import au.com.biztune.retail.form.AccountingExportForm;

import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.StreamingOutput;

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

    }
