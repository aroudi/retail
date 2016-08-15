package au.com.biztune.retail.rest;

import au.com.biztune.retail.domain.TxnHeader;
import au.com.biztune.retail.form.TxnHeaderForm;
import au.com.biztune.retail.report.TransactionRptMgr;
import au.com.biztune.retail.response.CommonResponse;
import au.com.biztune.retail.security.Secured;
import au.com.biztune.retail.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;

/**
 * Created by arash on 22/02/2016.
 */
@Component
@Path("transaction")
public class TransactionRest {
    private final Logger logger = LoggerFactory.getLogger(TransactionRest.class);
    @Context
    private UriInfo uriInfo;
    @Context
    private Request request;

    @Autowired
    private TransactionService transactionService;
    @Autowired
    private TransactionRptMgr transactionRptMgr;
    @Context
    private SecurityContext securityContext;

    /**
     * Returns list of sale transactions.
     * @return list of sale transactions
     */
    @Secured
    @Path("/all")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<TxnHeader> getAllTransactions() {
        try {
            return transactionService.getAllTxnHeadersForStore();
        } catch (Exception e) {
            logger.error ("Error in retrieving transaction List :", e);
            return null;
        }
    }

    /**
     * crate a Sale Transaction.
     * @param txnHeaderForm transaction Header
     * @return CommonResponse
     */
    @Secured
    @Path("/add")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public CommonResponse addTransaction (TxnHeaderForm txnHeaderForm) {
        if (txnHeaderForm.getId() < 0) {
            return transactionService.addTransaction(txnHeaderForm, securityContext);
        } else {
            return transactionService.updateTransaction(txnHeaderForm, securityContext);
        }
    }

    /**
     * add payment to transaction.
     * @param txnHeaderForm transaction Header
     * @return CommonResponse
     */
    @Secured
    @Path("/addPayment")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public CommonResponse addPayment (TxnHeaderForm txnHeaderForm) {
        return transactionService.addPayment(txnHeaderForm, securityContext);
    }

    /**
     * get transaction by Id.
     * @param id id.
     * @return TxnHeaderForm
     */
    @Secured
    @GET
    @Path("/get/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public TxnHeaderForm getTransactionById (@PathParam("id") long id) {
        return transactionService.getTxnHeaderPerId(id);
    }

    /**
     * export Transaction as PDF.
     * @param txhdId txhdId
     * @return Stream output.
     */
    @Secured
    @Path("/exportPdf/{txhdId}")
    @GET
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response exportTransactionToPdf(@PathParam("txhdId") long txhdId) {
        final StreamingOutput streamingOutput = transactionRptMgr.createTransactionPdfStream(txhdId);
        return Response.ok(streamingOutput).header("Content-Disposition", "attachment; filename = transaction" + txhdId + ".pdf").build();
    }
}
