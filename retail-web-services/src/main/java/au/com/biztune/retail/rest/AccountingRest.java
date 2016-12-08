// Sydney Trains 2015

package au.com.biztune.retail.rest;

import au.com.biztune.retail.domain.Account;
import au.com.biztune.retail.domain.AccountingExport;
import au.com.biztune.retail.form.AccountingExportForm;
import au.com.biztune.retail.response.CommonResponse;
import au.com.biztune.retail.security.Secured;
import au.com.biztune.retail.service.AccountingExportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;

/**
 * Created by arash on 10/08/2015.
 */

@Component
@Path("accounting")
public class AccountingRest {
    private final Logger logger = LoggerFactory.getLogger(AccountingRest.class);
    @Context
    private UriInfo uriInfo;

    @Context
    private Request request;

    @Context
    private SecurityContext securityContext;

    @Autowired
    private AccountingExportService accountingExportService;

    /**
     * export accounting data as txt format.
     * @param accountingExportForm accountingExportForm
     * @return txt file
     */
    @Secured
    @Path("/exportAsTxt")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response exportAccountingDataAsTxt(AccountingExportForm accountingExportForm) {
        final StreamingOutput streamingOutput = accountingExportService.exportAccountingDataToTxt(accountingExportForm, securityContext);
        return Response.ok(streamingOutput).header("content-disposition", "attachment; filename = Retail.txt").build();
    }

    /**
     * initiate accounting export form.
     * @return accounting export form.
     */
    @Secured
    @GET
    @Path("/initiateForm")
    @Produces(MediaType.APPLICATION_JSON)
    public AccountingExportForm initiateAccountingExportForm() {
        return accountingExportService.initiateExportForm();
    }

    /**
     * get all accounts.
     * @return List of accounts.
     */
    @Secured
    @GET
    @Path("/getAllAccounts")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Account> getAllAccounts() {
        return accountingExportService.getAllAccountByOrguId();
    }

    /**
     * get all accounts.
     * @param accountList accountList
     * @return List of accounts.
     */
    @Secured
    @POST
    @Path("/updateAccounts")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public CommonResponse updateAccountCodes(List<Account> accountList) {
        return accountingExportService.updateAccountCodes(accountList);
    }

    /**
     * get all accounting exports.
     * @return List of accounting exports.
     */
    @Secured
    @GET
    @Path("/getAllAccountingExport")
    @Produces(MediaType.APPLICATION_JSON)
    public List<AccountingExport> getAllAccountingExport() {
        return accountingExportService.getAllAccountingExportByOrguId();
    }


    /**
     * export accounting export as txt.
     * @param id id
     * @return txt file
     */
    @Secured
    @Path("/getAccountingExport/{id}")
    @GET
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getAccountingExportAsTxt(@PathParam("id") long id) {
        final StreamingOutput streamingOutput = accountingExportService.getExportedAccountsContentById(id);
        return Response.ok(streamingOutput).header("content-disposition", "attachment; filename = Retail.txt").build();
    }
}
