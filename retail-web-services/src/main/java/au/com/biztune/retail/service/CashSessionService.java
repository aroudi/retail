package au.com.biztune.retail.service;

import au.com.biztune.retail.domain.*;
import au.com.biztune.retail.form.AddFloatForm;
import au.com.biztune.retail.form.ReconciliationForm;
import au.com.biztune.retail.response.CommonResponse;

import javax.ws.rs.core.SecurityContext;
import java.util.List;

/**
 * Created by arash on 23/09/2016.
 */
public interface CashSessionService {

    /**
     * create cash session for user.
     * @param user user.
     * @return CashSession
     */
    CashSession createCashSession(AppUser user);

    /**
     * assign a cash session to user logged in.
     * @param appUser appUser
     */
    void assignCashSessionToLoggedinUser(AppUser appUser);

    /**
     * close cash session after logged out.
     * @param appUser appUser.
     */
    void closeCashSessionForLoggedOutUser(AppUser appUser);

    /**
     * process session event.
     * @param txnHeader txnHeader
     * @param seevType seevType - session event type
     */
    void processSessionEvent (TxnHeader txnHeader, String seevType);

    /**
     * create Session Event.
     * @param sessionId sessionId
     * @param eventType eventType
     * @param userId userId
     * @param comment comment
     * @return SessionEvent.
     */
    SessionEvent createSessionEvent(long sessionId, String eventType, long userId, String comment);

    /**
     * create Session Media.
     * @param sessionEvent sessionEvent
     * @param txnMedia txnMedia
     * @param paymentMedia paymentMedia
     * @param mediaCount mediaCount
     * @param mediaValue mediaValue
     * @return Session Media.
     */
    SessionMedia createSessionMedia(SessionEvent sessionEvent, TxnMedia txnMedia, PaymentMedia paymentMedia, double mediaCount, double mediaValue);

    /**
     * get All current cash sessions.
     * @return List of cash session
     */
    List<CashSession> getAllUnReconciledCashSessions();

    /**
     * add float/ pickup cash.
     * @param addFloatForm addFloatForm
     * @param securityContext securityContext
     * @return CommonResponse.
     */
    CommonResponse addFloat(AddFloatForm addFloatForm, SecurityContext securityContext);

    /**
     * fetch session total data for reconciliation.
     * @param cashSessionId cashSessionId
     * @return SessionEventDetail which contains session data.
     */
    List<SessionEventDetail> fetchSessionDataForReconciliation(long cashSessionId);

    /**
     * End cash session.
     * @param cashSession cashSession
     * @param securityContext securityContext
     * @return common response.
     */
    CommonResponse endCashSession(CashSession cashSession, SecurityContext securityContext);

    /**
     * Reconcile Session.
     * @param reconciliationForm reconciliationForm
     * @param securityContext securityContext
     * @return CommonResponse
     */
    CommonResponse reconcileSession(ReconciliationForm reconciliationForm, SecurityContext securityContext);

    /**
     * retreive reconciled sessions.
     * @return List of reconciled sessions
     */
    List<SessionEvent> getReconciledSessions();

    /**
     * get Active cash session.
     * @param userId logined user id
     * @return active cash session.
     */
    CashSession getActiveCashSession(long userId);

}
