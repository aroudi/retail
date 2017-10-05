package au.com.biztune.retail.rest;

import au.com.biztune.retail.domain.DebtorPaymentForm;
import au.com.biztune.retail.domain.GeneralSearchForm;
import au.com.biztune.retail.domain.TxnHeader;
import au.com.biztune.retail.form.TxnHeaderForm;
import au.com.biztune.retail.report.TransactionRptMgr;
import au.com.biztune.retail.response.CommonResponse;
import au.com.biztune.retail.security.Secured;
import au.com.biztune.retail.service.InvoiceImportService;
import au.com.biztune.retail.service.TransactionService;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.InputStream;
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
    @Autowired
    private InvoiceImportService invoiceImportService;

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
       return transactionService.saveTransaction(txnHeaderForm, securityContext);
    }

    /**
     * crate an Invoice.
     * @param txnHeaderForm transaction Header
     * @return CommonResponse
     */
    @Secured
    @Path("/invoice")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public CommonResponse invoice (TxnHeaderForm txnHeaderForm) {
            return transactionService.createInvoice(txnHeaderForm, securityContext);
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
    public Response exportSaleOrderToPdf(@PathParam("txhdId") long txhdId) {
        final StreamingOutput streamingOutput = transactionRptMgr.convertSaleOrderToPdf(txhdId);
        return Response.ok(streamingOutput).header("Content-Disposition", "attachment; filename = transaction" + txhdId + ".pdf").build();
    }

    /**
     * export Invoice as PDF.
     * @param invoiceId invoiceId
     * @return Stream output.
     */
    @Secured
    @Path("/invoice/exportPdf/{invoiceId}")
    @GET
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response exportInvoiceToPdf(@PathParam("invoiceId") long invoiceId) {
        final StreamingOutput streamingOutput = transactionRptMgr.convertInvoiceToPdf(invoiceId);
        return Response.ok(streamingOutput).header("Content-Disposition", "attachment; filename = transaction" + invoiceId + ".pdf").build();
    }


    /**
     * reprint delivery docket.
     * @param invoiceId invoiceId
     * @return Stream output.
     */
    @Secured
    @Path("/reprintDeliveryDocket/{invoiceId}")
    @GET
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response reprintDeliveryDocket(@PathParam("invoiceId") long invoiceId) {
        final StreamingOutput streamingOutput = transactionRptMgr.reprintDeliveryDocket(invoiceId);
        return Response.ok(streamingOutput).header("Content-Disposition", "attachment; filename = transaction" + invoiceId + ".pdf").build();
    }

    /**
     * Returns list of Invoice.
     * @return list of Invoice
     */
    @Secured
    @Path("/invoice/all")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<TxnHeader> getAllInvoices() {
        try {
            return transactionService.getAllInvoiceHeadersForStore();
        } catch (Exception e) {
            logger.error ("Error in retrieving invoice List :", e);
            return null;
        }
    }

    /**
     * get invoice by Id.
     * @param id id.
     * @return TxnHeaderForm
     */
    @Secured
    @GET
    @Path("/getInvoice/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public TxnHeaderForm getInvoiceById (@PathParam("id") long id) {
        return transactionService.getInvoicePerId(id);
    }

    /**
     * crate a txn account payment Transaction.
     * @param debtorPaymentForm debtorPaymentForm
     * @return CommonResponse
     */
    @Secured
    @Path("/txnAccPayment/add")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public CommonResponse addTxnAccPayment (DebtorPaymentForm debtorPaymentForm) {
        return transactionService.createTxnAccPayment(debtorPaymentForm, securityContext);
    }

    /**
     * Returns list of Invoice for specific customer.
     * @param customerId customerId
     * @return list of Invoice
     */
    @Secured
    @Path("/invoice/customer/{customerId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<TxnHeader> getAllInvoicesOfCustomer(@PathParam("customerId") long customerId) {
        try {
            return transactionService.getAllInvoiceOfCustomer(customerId);
        } catch (Exception e) {
            logger.error ("Error in retrieving invoice List :", e);
            return null;
        }
    }

    /**
     * Returns list of SaleOrders and Quotes for specific customer.
     * @param customerId customerId
     * @return list of Sale Orders and Quotes
     */
    @Secured
    @Path("/saleOrder/customer/{customerId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<TxnHeader> getAllSaleOrderAndQuoteOfCustomer(@PathParam("customerId") long customerId) {
        try {
            return transactionService.getAllSaleOrdersAndQuotesOfCustomer(customerId);
        } catch (Exception e) {
            logger.error ("Error in retrieving invoice List :", e);
            return null;
        }
    }

    /**
     * refund transaction.
     * @param txnHeaderForm transaction Header
     * @return CommonResponse
     */
    @Secured
    @Path("/refund")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public CommonResponse refundTxn (TxnHeaderForm txnHeaderForm) {
        return transactionService.refundTransaction(txnHeaderForm, securityContext);
    }
    /**
     * search invoice.
     * @param searchForm searchForm
     * @return CommonResponse
     */

    @Secured
    @Path("/searchTxnHeader")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<TxnHeader> searchTxnHeader (GeneralSearchForm searchForm) {
        return transactionService.searchTxnHeader(searchForm);
    }

    /**
     * search txn header paging.
     * @param searchForm searchForm
     * @return txn header list
     */
    @Secured
    @Path("/searchTxnHeaderPaging")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public GeneralSearchForm searchTxnHeaderPaging (GeneralSearchForm searchForm) {
        return transactionService.searchTxnHeaderPaging(searchForm);
    }

    /**
     * search invoice.
     * @param searchForm searchForm
     * @return CommonResponse
     */

    @Secured
    @Path("/searchInvoice")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<TxnHeader> searchInvoice (GeneralSearchForm searchForm) {
        return transactionService.searchInvoice(searchForm);
    }

    /**
     * search invoice paging.
     * @param searchForm searchForm
     * @return invoice list,
     */
    @Secured
    @Path("/searchInvoicePaging")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public GeneralSearchForm searchInvoicePaging (GeneralSearchForm searchForm) {
        return transactionService.searchInvoicePaging(searchForm);
    }

    /**
     * delete quote by Id.
     * @param id id.
     * @return CommonResponse
     */
    @Secured
    @GET
    @Path("/deleteQuote/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public CommonResponse deleteQuoteById (@PathParam("id") long id) {
        return transactionService.deleteQuote(id);
    }

    /**
     * upload bill of quantity.
     * @param uploadedInputStream uploadedInputStream
     * @return CommonResponse
     */
    @Secured
    @POST
    @Path("/importInvoice")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public CommonResponse uploadBillOfQuantity(@FormDataParam("file")InputStream uploadedInputStream) {
        return invoiceImportService.importInvoice(uploadedInputStream, securityContext);
    }
}
