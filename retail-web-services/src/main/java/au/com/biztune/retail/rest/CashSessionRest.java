package au.com.biztune.retail.rest;

import au.com.biztune.retail.domain.CashSession;
import au.com.biztune.retail.domain.SessionEventDetail;
import au.com.biztune.retail.form.AddFloatForm;
import au.com.biztune.retail.form.ReconciliationForm;
import au.com.biztune.retail.response.CommonResponse;
import au.com.biztune.retail.security.Secured;
import au.com.biztune.retail.service.CashSessionService;
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
@Path("cashSession")
public class CashSessionRest {
    private final Logger logger = LoggerFactory.getLogger(CashSessionRest.class);
    @Context
    private UriInfo uriInfo;
    @Context
    private Request request;

    @Autowired
    private CashSessionService cashSessionService;

    @Context
    private SecurityContext securityContext;

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

}
