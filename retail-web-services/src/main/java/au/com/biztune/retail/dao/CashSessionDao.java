package au.com.biztune.retail.dao;

import au.com.biztune.retail.domain.*;

import java.util.List;

/**
 * Created by arash on 21/09/2016.
 */
public interface CashSessionDao {

    /**
     * create Cash Session.
     * @param cashSession cashSession
     */
    void createCashSession(CashSession cashSession);

    /**
     * insert SessionEvent.
     * @param sessionEvent sessionEvent
     */
    void insertSessionEvent(SessionEvent sessionEvent);

    /**
     * insert Session Media.
     * @param sessionMedia sessionMedia
     */
    void insertSessionMedia(SessionMedia sessionMedia);

    /**
     * insert sessionTotal.
     * @param sessionTotal sessionTotal
     */
    void insertSessionTotal(SessionTotal sessionTotal);

    /**
     * insert SessionEventDetail.
     * @param sessionEventDetail sessionEventDetail
     */
    void insertSessionEventDetail(SessionEventDetail sessionEventDetail);

    /**
     * insert TxnSessionLink.
     * @param txnSessionLink txnSessionLink
     */
    void insertTxnSessionLink(TxnSessionLink txnSessionLink);

    /**
     * update cash session.
     * @param cashSession cashSession
     */
    void updateCashSession(CashSession cashSession);

    /**
     * update SessionTotal for specific media and session id.
     * @param sessionTotal sessionTotal
     */
    void updateSessionTotalPerSessionIdAndPaymentMediaId(SessionTotal sessionTotal);

    /**
     * get SessionTotal Per SessionId and PaymentMediaId.
     * @param sessionId sessionId
     * @param paymentMediaId paymentMediaId
     * @return SessionTotal
     */
    SessionTotal getSessionTotalPerSessionIdAndPaymentMediaId(long sessionId, long paymentMediaId);

    /**
     * get SessionTotal Media Per SessionId.
     * @param sessionId sessionId
     * @return List of SessionTotal
     */
    List<SessionTotal> getSessionTotalMediasPerSessionId(long sessionId);

    /**
     * get Current Cash Session Per UserId.
     * @param userId userId
     * @return CashSession
     */
    CashSession getCurrentCashSessionPerUser(long userId);

    /**
     * get All Current(Open or Closed) sessions.
     * @return List of Current Sessions.
     */
    List<CashSession> getAllCurrentCashSessions();

    /**
     * get All Session Ended.
     * @return List of Ended sessions.
     */
    List<CashSession> getAllEndedCashSessions();

    /**
     * update Cash Session Total Float.
     * @param cashSession cashSession
     */
    void updateCashSessionTotalFloat (CashSession cashSession);

    /**
     * update cash session total pickup.
     * @param cashSession cashSession
     */
    void updateCashSessionTotalPickup (CashSession cashSession);
}
