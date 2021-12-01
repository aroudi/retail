package au.com.biztune.retail.dao;

import au.com.biztune.retail.domain.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by arash on 21/09/2016.
 */
@Mapper
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
     * get Active Cash Session for store.
     * @param orguId orguId
     * @param storeId storeId
     * @return CashSession
     */
    CashSession getStoreActiveCashSession(long orguId, long storeId);

    /**
     * get All Current(Open or Closed) sessions.
     * @return List of Current Sessions.
     */
    List<CashSession> getAllCurrentCashSessions();

    /**
     * get All UNRECONCILED(Open or Closed or ended) sessions.
     * @return List of Current Sessions.
     */
    List<CashSession> getAllUnReconciledCashSessions();
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

    /**
     * update cash session status.
     * @param cashSession cashSession
     */
    void updateCashSessionStatus(CashSession cashSession);

    /**
     * get session total per session id and media id.
     * @param sessionId sessionId
     * @param paymId paymId
     * @return SessionTotal
     */
    SessionTotal getSessionTotalMediasPerSessionIdAndPaymId(long sessionId, long paymId);

    /**
     * get session event details for session id.
     * @param eventId eventId
     * @return SessionEventDetail
     */
    SessionEventDetail getSessionEventDetailsPerEventId(long eventId);

    /**
     * get cash session per id.
     * @param id id
     * @return Cash Session
     */
    CashSession getCashSessionPerId(long id);

    /**
     * get session event per id.
     * @param seevId seevId
     * @return session event.
     */
    SessionEvent getSessionEventPerId(long seevId);

    /**
     * get reconciled sessions.
     * @return Session Event
     */
    List<SessionEvent> getReconciledSessionEvents();

    /**
     * get all reconciled cash session.
     * @return reconciled cash session
     */
    List<CashSession> getAllReconciledCashSession();

    /**
     * update cash session import status.
     * @param cssnId cssnId
     */
    void updateCashSessionImportStatus(long cssnId);

    /**
     * Cashup Detail Txn Summary Report.
     * @param sessionId session id.
     * @return List of txn detail.
     */
    List<CashupDetailTxnSummaryRow> runCashupDetailTxnSummary(long sessionId);

    /**
     * get store Last Ended or Reconciled Cash Session for store.
     * @param orguId orguId
     * @param storeId storeId
     * @return CashSession
     */
    CashSession getStoreLastEndedOrReconciledCashSession(long orguId, long storeId);

    /**
     * get user Last Ended or Reconciled Cash Session for store.
     * @param orguId orguId
     * @param storeId storeId
     * @param userId userId
     * @return CashSession
     */
    CashSession getUserLastEndedOrReconciledCashSession(long orguId, long storeId, long userId);
}
