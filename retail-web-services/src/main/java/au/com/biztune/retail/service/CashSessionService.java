package au.com.biztune.retail.service;

import au.com.biztune.retail.domain.*;
import au.com.biztune.retail.form.AddFloatForm;
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
     * @return SessionEvent.
     */
    SessionEvent createSessionEvent(long sessionId, String eventType, long userId);

    /**
     * create Session Media.
     * @param sessionEvent sessionEvent
     * @param paymentMedia paymentMedia
     * @param mediaCount mediaCount
     * @param mediaValue mediaValue
     * @return Session Media.
     */
    SessionMedia createSessionMedia(SessionEvent sessionEvent, PaymentMedia paymentMedia, double mediaCount, double mediaValue);

    /**
     * get All current cash sessions.
     * @return List of cash session
     */
    List<CashSession> getAllCurrentCashSessions();

    /**
     * add float/ pickup cash.
     * @param addFloatForm addFloatForm
     * @param securityContext securityContext
     * @return CommonResponse.
     */
    CommonResponse addFloat(AddFloatForm addFloatForm, SecurityContext securityContext);
}
