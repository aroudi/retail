// Sydney Trains 2015

package au.com.biztune.retail.rest;

import au.com.biztune.retail.form.AccountingExportForm;
import au.com.biztune.retail.security.Secured;
import au.com.biztune.retail.service.AccountingExportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

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

}
