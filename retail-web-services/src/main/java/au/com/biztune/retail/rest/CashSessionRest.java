package au.com.biztune.retail.rest;

import au.com.biztune.retail.domain.*;
import au.com.biztune.retail.form.AddFloatForm;
import au.com.biztune.retail.form.ReconciliationForm;
import au.com.biztune.retail.report.AccountingRptMgr;
import au.com.biztune.retail.report.ReconciliationRptMgr;
import au.com.biztune.retail.response.CommonResponse;
import au.com.biztune.retail.security.Secured;
import au.com.biztune.retail.service.AccountingExtractService;
import au.com.biztune.retail.service.CashSessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by arash on 22/02/2016.
 */
@Component
@Path("cashSession")
public class CashSessionRest {
    private final Logger logger = LoggerFactory.getLogger(CashSessionRest.class);
    @Context
    private UriInfo uriInfo;
    @Context
    private Request request;

    @Autowired
    private CashSessionService cashSessionService;

    @Autowired
    private ReconciliationRptMgr reconciliationRptMgr;

    @Autowired
    private AccountingExtractService accountingExtractService;

    @Context
    private SecurityContext securityContext;

    @Autowired
    private AccountingRptMgr accountingRptMgr;

    /**
     * Returns list of active Cash Sessions.
     * @return list of cash sessions
     */
    @Secured
    @Path("/getAllCurrentSession")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<CashSession> getAllCurrentCashSessions() {
        try {
            return cashSessionService.getAllUnReconciledCashSessions();
        } catch (Exception e) {
            logger.error ("Error in retrieving cash session List :", e);
            return null;
        }
    }

    /**
     * add float.
     * @param addFloatForm addFloatForm
     * @return CommonResponse
     */
    @Secured
    @Path("/addFloat")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public CommonResponse addFloat (AddFloatForm addFloatForm) {
        return cashSessionService.addFloat(addFloatForm, securityContext);
    }


    /**
     * Returns list of active Cash Sessions.
     * @param sessionId sessionId
     * @return list of cash sessions
     */
    @Secured
    @GET
    @Path("/fetchSessionTotalForReconciliation/{sessionId}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<SessionEventDetail> fetchSessionDataForReconciliation(@PathParam("sessionId") long sessionId) {
        try {
            return cashSessionService.fetchSessionDataForReconciliation(sessionId);
        } catch (Exception e) {
            logger.error ("Error in fetching cash session data for reconciliation :", e);
            return null;
        }
    }

    /**
     * End Session.
     * @param cashSession cashSession
     * @return Common respones
     */
    @Secured
    @Path("/endSession")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public CommonResponse endSession(CashSession cashSession) {
        try {
            return cashSessionService.endCashSession(cashSession, securityContext);
        } catch (Exception e) {
            logger.error ("Error in ending session :", e);
            return null;
        }
    }

    /**
     * reconcile Session.
     * @param reconciliationForm reconciliationForm
     * @return CommonResponse
     */
    @Secured
    @Path("/reconcileSession")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public CommonResponse reconcileSession (ReconciliationForm reconciliationForm) {
        return cashSessionService.reconcileSession(reconciliationForm, securityContext);
    }

    /**
     * export reconciliation report as PDF.
     * @param seevId seevId
     * @return Stream output.
     */
    @Secured
    @Path("/reconciliation/exportPdf/{seevId}")
    @GET
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response exportReconciliRptToPdf(@PathParam("seevId") long seevId) {
        final StreamingOutput streamingOutput = reconciliationRptMgr.createReconciliationRptPdfStream(seevId);
        //purchaseOrderRptMgr.createPurchaseOrderPdfFile(pohId);
        return Response.ok(streamingOutput).header("Content-Disposition", "attachment; filename = PurchaseOrder" + seevId + ".pdf").build();
    }
    /**
     * Returns list of reconciled sessions.
     * @return list of reconciled sessions
     */
    @Secured
    @Path("/getAllReconciledSessions")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<SessionEvent> getAllReconciledSessions() {
        try {
            return cashSessionService.getReconciledSessions();
        } catch (Exception e) {
            logger.error ("Error in retrieving reconciled sessions :", e);
            return null;
        }
    }
    /**
     * Accounting Report.
     * @param searchForm searchForm
     * @return CommonResponse
     */

    @Secured
    @Path("/accountingReport")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<JournalEntry> searchInvoice (GeneralSearchForm searchForm) {
        return accountingExtractService.accountingExportReport(searchForm);
    }

    /**
     * export reconciliation report as PDF.
     * @param generalSearchForm generalSearchForm
     * @return Stream output.
     */
    @Secured
    @Path("/accountingSummaryReport")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response exportAccountingSummaryRptToPdf(GeneralSearchForm generalSearchForm) {
        final StreamingOutput streamingOutput = accountingRptMgr.createAccountingRptPdfStream(generalSearchForm, securityContext);
        //purchaseOrderRptMgr.createPurchaseOrderPdfFile(pohId);
        return Response.ok(streamingOutput).header("Content-Disposition", "attachment; filename = AccountingSummaryReport" + ".pdf").build();
    }


}
