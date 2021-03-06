package au.com.biztune.retail.service;

import au.com.biztune.retail.dao.AccountingDao;
import au.com.biztune.retail.dao.CashSessionDao;
import au.com.biztune.retail.dao.ConfigCategoryDao;
import au.com.biztune.retail.dao.PaymentMediaDao;
import au.com.biztune.retail.domain.*;
import au.com.biztune.retail.form.AddFloatForm;
import au.com.biztune.retail.form.ReconciliationForm;
import au.com.biztune.retail.processor.SessionProcessor;
import au.com.biztune.retail.processor.SessionRequest;
import au.com.biztune.retail.response.CommonResponse;
import au.com.biztune.retail.session.SessionState;
import au.com.biztune.retail.util.IdBConstant;
import au.com.biztune.retail.util.queuemanager.QueueManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.SecurityContext;
import java.security.Principal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by arash on 22/09/2016.
 */
@Component
public class CashSessionServiceImpl implements CashSessionService {

    private SecurityContext securityContext;

    @Autowired
    private SessionState sessionState;
    private final Logger logger = LoggerFactory.getLogger(CashSession.class);

    @Autowired
    private CashSessionDao cashSessionDao;

    @Autowired
    private ConfigCategoryDao configCategoryDao;

    @Autowired
    private SessionProcessor sessionProcessor;

    @Autowired
    private QueueManager sessionProcessorQueue;

    @Autowired
    private PaymentMediaDao paymentMediaDao;

    @Autowired
    private AccountingDao accountingDao;

    /**
     * get Active cash session.
     * @param userId logined user id
     * @return active cash session.
     */
    public CashSession getActiveCashSession(long userId) {
        //check the cash session mode is per user or company
        if (sessionState.getStore().getCashSessionType().getCategoryCode().equals(IdBConstant.CASH_SESSION_TYPE_PER_USER)) {
            return cashSessionDao.getCurrentCashSessionPerUser(userId);
        } else {
            return cashSessionDao.getStoreActiveCashSession(sessionState.getOrgUnit().getId(), sessionState.getStore().getId());
        }
    }

    /**
     * get last ended/reconciled cash session.
     * @param userId logined user id
     * @return active cash session.
     */
    private CashSession getLastEndedOrReconciledCashSession(long userId) {
        //check the cash session mode is per user or company
        if (sessionState.getStore().getCashSessionType().getCategoryCode().equals(IdBConstant.CASH_SESSION_TYPE_PER_USER)) {
            return cashSessionDao.getUserLastEndedOrReconciledCashSession(sessionState.getOrgUnit().getId(),
                    sessionState.getStore().getId(), userId);
        } else {
            return cashSessionDao.getStoreLastEndedOrReconciledCashSession(sessionState.getOrgUnit().getId(), sessionState.getStore().getId());
        }
    }

    /**
     * create cash session for user.
     * @param user user.
     * @return CashSession
     */
    public CashSession createCashSession(AppUser user) {
        try {
            //first get last ended or reconciled cash session to transfer float to new cash session.
            final CashSession lastProcessedCashSession = getLastEndedOrReconciledCashSession(user.getId());
            final CashSession cashSession = new CashSession();
            cashSession.setCssnOperator(user);
            cashSession.setOrguId(sessionState.getOrgUnit().getId());
            cashSession.setStoreId(sessionState.getStore().getId());
            final Timestamp currentDate = new Timestamp(new Date().getTime());
            cashSession.setCssnTradingDate(currentDate);
            final ConfigCategory status = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_SESSION_STATE, IdBConstant.SESSION_STATE_OPEN);
            cashSession.setCssnStatus(status);
            cashSession.setCssnStartDate(currentDate);
            //transfer the float amount from ended/reconciled cashsession to new cash sessioni
            if (lastProcessedCashSession != null) {
                cashSession.setCssnCurrentCash(lastProcessedCashSession.getCssnCurrentCash()
                        + (lastProcessedCashSession.getCssnTotalFloat() - lastProcessedCashSession.getCssnTotalPickup()));

            } else {
                cashSession.setCssnCurrentCash(0);
            }
            cashSession.setCssnTotalFloat(0);
            cashSession.setCssnTotalPickup(0);
            cashSessionDao.createCashSession(cashSession);
            return cashSession;
        } catch (Exception e) {
            logger.error("Exception in creating cash session.", e);
            return null;
        }
    }

    /**
     * create Session Event.
     * @param sessionId sessionId
     * @param eventType eventType
     * @param userId userId
     * @param comment comment
     * @return SessionEvent.
     */
    public SessionEvent createSessionEvent(long sessionId, String eventType, long userId, String comment) {
        try {
            //create session event
            final SessionEvent sessionEvent = new SessionEvent();
            sessionEvent.setCssnSessionId(sessionId);
            sessionEvent.setOrguId(sessionState.getOrgUnit().getId());
            sessionEvent.setStoreId(sessionState.getStore().getId());
            final Timestamp currentDate = new Timestamp(new Date().getTime());
            sessionEvent.setSeevEventDate(currentDate);
            final ConfigCategory configEventType = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_SESSION_EVENT, eventType);
            if (eventType != null) {
                sessionEvent.setSeevEventType(configEventType);
            }
            sessionEvent.setSeevOperator(userId);
            sessionEvent.setSeevComment(comment);
            cashSessionDao.insertSessionEvent(sessionEvent);
            return sessionEvent;
        } catch (Exception e) {
            logger.error("Exception in creating cash session event", e);
            return null;
        }
    }


    /**
     * create Session Media.
     * @param sessionEvent sessionEvent
     * @param txnMedia txnMedia
     * @param paymentMedia paymentMedia
     * @param mediaCount mediaCount
     * @param mediaValue mediaValue
     * @return Session Media.
     */
    public SessionMedia createSessionMedia(SessionEvent sessionEvent, TxnMedia txnMedia, PaymentMedia paymentMedia, double mediaCount, double mediaValue) {
        try {
            final SessionMedia sessionMedia = new SessionMedia();
            sessionMedia.setOrguId(sessionEvent.getOrguId());
            sessionMedia.setStoreId(sessionEvent.getStoreId());
            sessionMedia.setCssnSessionId(sessionEvent.getCssnSessionId());
            sessionMedia.setSeevId(sessionEvent.getId());
            sessionMedia.setPaymentMedia(paymentMedia);
            sessionMedia.setMediaType(paymentMedia.getMediaType());
            sessionMedia.setSemeMediaCount(mediaCount);
            sessionMedia.setSemeMediaValue(mediaValue);
            if (txnMedia != null) {
                sessionMedia.setTxmdId(txnMedia.getId());
            }
            cashSessionDao.insertSessionMedia(sessionMedia);
            return sessionMedia;
        } catch (Exception e) {
            logger.error("Exception in creating session media", e);
            return null;
        }
    }

    /**
     * assign a cash session to user logged in.
     * @param appUser appUser
     */
    public void assignCashSessionToLoggedinUser(AppUser appUser) {
        try {
            //first search for active(not ended or reconciled) cash session assigned to user.
            CashSession cashSession = getActiveCashSession(appUser.getId());
            //cashSessionDao.getCurrentCashSessionPerUser(appUser.getId());
            //if no active cash session then create one.
            if (cashSession == null) {
                cashSession = createCashSession(appUser);
            }
            //if assigned session is closed, then open it.
            if (!cashSession.getCssnStatus().getCategoryCode().equals(IdBConstant.SESSION_STATE_OPEN)) {
                final ConfigCategory status = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_SESSION_STATE, IdBConstant.SESSION_STATE_OPEN);
                cashSession.setCssnStatus(status);
                cashSessionDao.updateCashSessionStatus(cashSession);
            }
        } catch (Exception e) {
            logger.error("Exception in assigning cash session to user", e);
        }
    }


    /**
     * in session per user mode, close cash session after logged out.
     * @param appUser appUser.
     */
    public void closeCashSessionForLoggedOutUser(AppUser appUser) {
        try {
            //check if session is opened per user?
            if (sessionState.getStore().getCashSessionType().getCategoryCode().equals(IdBConstant.CASH_SESSION_TYPE_PER_USER)) {
                //first search for active(not ended or reconciled) cash session assigned to user.
                final CashSession cashSession = cashSessionDao.getCurrentCashSessionPerUser(appUser.getId());
                if (cashSession == null) {
                    return;
                }
                //if assigned session is open, then close it.
                if (cashSession.getCssnStatus().getCategoryCode().equals(IdBConstant.SESSION_STATE_OPEN)) {
                    final ConfigCategory status = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_SESSION_STATE, IdBConstant.SESSION_STATE_CLOSED);
                    cashSession.setCssnStatus(status);
                    cashSessionDao.updateCashSessionStatus(cashSession);
                }
            }
        } catch (Exception e) {
            logger.error("Exception in assigning cash session to user", e);
        }
    }

    /**
     * process session event.
     * @param txnHeader txnHeader
     * @param seevType seevType - session event type
     */
    public void processSessionEvent (TxnHeader txnHeader, String seevType) {
        try {
            final SessionRequest sessionRequest = new SessionRequest();
            sessionRequest.setProcessor(sessionProcessor);
            sessionRequest.setEventType(seevType);
            sessionRequest.setTxnHeader(txnHeader);
            sessionProcessorQueue.push(sessionRequest);
        } catch (Exception e) {
            logger.error("Exception in processing stock event:", e);
        }
    }

    /**
     * get current cash session(open or closed) of user.
     * @param appUser appUser
     * @return CashSession
     */
    public CashSession getUserCurrentCashSession(AppUser appUser) {
        try {
            if (appUser == null) {
                logger.error("can not return cash session for null user");
                return null;
            }
            //search for active cash session. if not found create and assign to user.
            CashSession cashSession = getActiveCashSession(appUser.getId());
            //cashSessionDao.getCurrentCashSessionPerUser(appUser.getId());
            if (cashSession == null) {
                cashSession = createCashSession(appUser);
            }
            return cashSession;

        } catch (Exception e) {
            logger.error("Exception in retreiving user's cash session ", e);
            return null;
        }
    }

    /**
     * get All current cash sessions.
     * @return List of cash session
     */
    public List<CashSession> getAllUnReconciledCashSessions() {
        try {
            return cashSessionDao.getAllUnReconciledCashSessions();
        } catch (Exception e) {
            logger.error("Exception in retrieving list of cash sessions ", e);
            return null;
        }
    }

    /**
     * add float/ pickup cash.
     * @param addFloatForm addFloatForm
     * @param securityContext securityContext
     * @return CommonResponse.
     */
    public CommonResponse addFloat(AddFloatForm addFloatForm, SecurityContext securityContext) {
        this.securityContext = securityContext;
        //save it to database.
        final Principal principal = securityContext.getUserPrincipal();
        AppUser appUser = null;
        if (principal instanceof AppUser) {
            appUser = (AppUser) principal;
        }

        final CommonResponse commonResponse = new CommonResponse();
        commonResponse.setStatus(IdBConstant.RESULT_SUCCESS);
        try {
            if (addFloatForm == null || addFloatForm.getCashSession() == null || addFloatForm.getEventType() == null) {
                commonResponse.setStatus(IdBConstant.RESULT_FAILURE);
                commonResponse.setMessage("input form is corrupted.");
                return commonResponse;
            }
            final SessionEvent sessionEvent = createSessionEvent(addFloatForm.getCashSession().getId(), addFloatForm.getEventType().getCategoryCode(), appUser.getId(), addFloatForm.getComment());
            if (sessionEvent == null) {
                commonResponse.setStatus(IdBConstant.RESULT_FAILURE);
                commonResponse.setMessage("error in saving session event.");
                return commonResponse;
            }
            //add session media
            final PaymentMedia paymentMediaCash = paymentMediaDao.getPaymentMediaPerCode(IdBConstant.PAY_MEDIA_CODE_CASH);
            final SessionMedia sessionMedia = createSessionMedia(sessionEvent, null, paymentMediaCash, 1.0, addFloatForm.getAmount());
            if (sessionMedia == null) {
                commonResponse.setStatus(IdBConstant.RESULT_FAILURE);
                commonResponse.setMessage("error in saving session media.");
                return commonResponse;
            }

            //set total amount on cash session
            if (addFloatForm.getEventType().getCategoryCode().equals(IdBConstant.SESSION_EVENT_TYPE_FLOAT)) {
                addFloatForm.getCashSession().setCssnTotalFloat(addFloatForm.getCashSession().getCssnTotalFloat() + addFloatForm.getAmount());
                cashSessionDao.updateCashSessionTotalFloat(addFloatForm.getCashSession());
            }

            if (addFloatForm.getEventType().getCategoryCode().equals(IdBConstant.SESSION_EVENT_TYPE_PICKUP)) {
                addFloatForm.getCashSession().setCssnTotalPickup(addFloatForm.getCashSession().getCssnTotalPickup() + addFloatForm.getAmount());
                cashSessionDao.updateCashSessionTotalPickup(addFloatForm.getCashSession());
            }
            return commonResponse;
        } catch (Exception e) {
            logger.error("Exception in adding float", e);
            commonResponse.setStatus(IdBConstant.RESULT_FAILURE);
            commonResponse.setMessage("Erro in adding float" + e.getMessage());
            return commonResponse;
        }
    }


    /**
     * Reconcile Session.
     * @param reconciliationForm reconciliationForm
     * @param securityContext securityContext
     * @return CommonResponse
     */
    public CommonResponse reconcileSession(ReconciliationForm reconciliationForm, SecurityContext securityContext) {
        this.securityContext = securityContext;
        final Principal principal = securityContext.getUserPrincipal();
        AppUser appUser = null;
        if (principal instanceof AppUser) {
            appUser = (AppUser) principal;
        }

        final CommonResponse commonResponse = new CommonResponse();
        commonResponse.setStatus(IdBConstant.RESULT_SUCCESS);
        try {
            if (reconciliationForm == null) {
                commonResponse.setStatus(IdBConstant.RESULT_FAILURE);
                commonResponse.setMessage("input form is corrupted.");
                return commonResponse;
            }

            //create session event for reconciliation.
            final SessionEvent sessionEvent = createSessionEvent(reconciliationForm.getCashSession().getId(),
                    IdBConstant.SESSION_EVENT_TYPE_RECONCILE, appUser.getId(), reconciliationForm.getComment());

            //save session event detail for reconciliation.
            for (SessionEventDetail sessionEventDetail : reconciliationForm.getSessionEventDetailList()) {
                if (sessionEventDetail == null) {
                    continue;
                }
                sessionEventDetail.setSeevId(sessionEvent.getId());
                sessionEventDetail.setCssnSessionId(reconciliationForm.getCashSession().getId());
                cashSessionDao.insertSessionEventDetail(sessionEventDetail);
            }

            //change session status to reconciled.
            final ConfigCategory sessionStatus = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_SESSION_STATE, IdBConstant.SESSION_STATE_RECONCILED);
            reconciliationForm.getCashSession().setCssnStatus(sessionStatus);
            final Timestamp currentDate = new Timestamp(new Date().getTime());
            reconciliationForm.getCashSession().setCssnReconcileDate(currentDate);
            cashSessionDao.updateCashSessionStatus(reconciliationForm.getCashSession());
            //enter journal entry
            processReconciliationDataForJournalEntry(appUser.getId(), reconciliationForm.getSessionEventDetailList());
            //return response
            return commonResponse;
        } catch (Exception e) {
            logger.error("Exception in Reconciliation session", e);
            commonResponse.setStatus(IdBConstant.RESULT_FAILURE);
            commonResponse.setMessage("Erro in adding float" + e.getMessage());
            return commonResponse;
        }
    }

    /**
     * fetch session total data for reconciliation.
     * @param cashSessionId cashSessionId
     * @return SessionEventDetail which contains session data.
     */
    public List<SessionEventDetail> fetchSessionDataForReconciliation(long cashSessionId) {
        try {
            //first fetch all payment medias.
            final List<PaymentMedia> paymentMedias = paymentMediaDao.getPaymentMediasForReconciliation();
            if (paymentMedias == null) {
                logger.error("not able to find any payment media. something goes wrong");
                return null;
            }
            SessionEventDetail sessionEventDetail = null;
            SessionTotal sessionTotal = null;
            final List<SessionEventDetail> sessionTotals = new ArrayList<SessionEventDetail>();
            for (PaymentMedia paymentMedia : paymentMedias) {
                if (paymentMedia == null) {
                    continue;
                }
                sessionEventDetail = new SessionEventDetail();
                sessionEventDetail.setStoreId(sessionState.getStore().getId());
                sessionEventDetail.setOrguId(sessionState.getOrgUnit().getId());
                sessionEventDetail.setPaymentMedia(paymentMedia);
                sessionEventDetail.setCssnSessionId(cashSessionId);
                //fetch payment media data for session.
                sessionTotal = cashSessionDao.getSessionTotalPerSessionIdAndPaymentMediaId(cashSessionId, paymentMedia.getId());
                if (sessionTotal == null) {
                    sessionEventDetail.setMediaCountExpected(0.00);
                    sessionEventDetail.setMediaValueExpected(0.00);
                } else {
                    sessionEventDetail.setMediaCountExpected(sessionTotal.getMediaTotalCount());
                    sessionEventDetail.setMediaValueExpected(sessionTotal.getMediaTotalValue());
                }
                sessionEventDetail.setMediaValueActual(0.00);
                sessionEventDetail.setMediaCountActual(0.00);
                sessionEventDetail.setMediaType(paymentMedia.getMediaType());
                sessionEventDetail.setMediaCountDiff(sessionEventDetail.getMediaCountExpected() - sessionEventDetail.getMediaCountActual());
                sessionEventDetail.setMediaValueDiff(sessionEventDetail.getMediaValueExpected() - sessionEventDetail.getMediaValueActual());

                sessionTotals.add(sessionEventDetail);
            }
            return  sessionTotals;
        } catch (Exception e) {
            logger.error("Exception in fetching session data for reconciliation", e);
            return null;
        }
    }

    /**
     * End cash session.
     * @param cashSession cashSession
     * @param securityContext securityContext
     * @return common response.
     */
    public CommonResponse endCashSession(CashSession cashSession, SecurityContext securityContext) {
        this.securityContext = securityContext;
        final Principal principal = securityContext.getUserPrincipal();
        AppUser appUser = null;
        if (principal instanceof AppUser) {
            appUser = (AppUser) principal;
        }

        final CommonResponse commonResponse = new CommonResponse();
        commonResponse.setStatus(IdBConstant.RESULT_SUCCESS);
        try {
            if (cashSession == null) {
                commonResponse.setStatus(IdBConstant.RESULT_FAILURE);
                commonResponse.setMessage("cash session is null.");
                return commonResponse;
            }
            //first create session event.
            final SessionEvent sessionEvent = createSessionEvent(cashSession.getId(), IdBConstant.SESSION_EVENT_TYPE_END, appUser.getId(), "");

            //change session status to closed.
            final ConfigCategory sessionStatus = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_SESSION_STATE, IdBConstant.SESSION_STATE_ENDED);
            cashSession.setCssnStatus(sessionStatus);
            final Timestamp currentDate = new Timestamp(new Date().getTime());
            cashSession.setCssnActEndDate(currentDate);
            cashSessionDao.updateCashSessionStatus(cashSession);

            //return response
            return commonResponse;

        } catch (Exception e) {
            logger.error("Exception in ending session", e);
            commonResponse.setStatus(IdBConstant.RESULT_FAILURE);
            commonResponse.setMessage("Erro in ending session" + e.getMessage());
            return commonResponse;
        }
    }

    /**
     * retreive reconciled sessions.
     * @return List of reconciled sessions
     */
    public List<SessionEvent> getReconciledSessions() {
        try {
            return cashSessionDao.getReconciledSessionEvents();
        } catch (Exception e) {
            logger.error("Exception in retreiving reconciled sessions");
            return null;
        }
    }

    private void processReconciliationDataForJournalEntry(long userid, List<SessionEventDetail> sessionEventDetailList) {
        try {
            double actualTotal = 0.00;
            double expectedTotal = 0.00;
            boolean credit = true;
            long cashSessionId = 0;
            long sessionEventId = 0;
            long orguId = 0;
            final ConfigCategory txnType = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_SESSION_EVENT, IdBConstant.SESSION_EVENT_TYPE_RECONCILE);
            //process each session event.
            for (SessionEventDetail sessionEventDetail : sessionEventDetailList) {
                if (sessionEventDetail == null) {
                    continue;
                }
                cashSessionId = sessionEventDetail.getCssnSessionId();
                sessionEventId = sessionEventDetail.getSeevId();
                orguId = sessionEventDetail.getOrguId();
                if (sessionEventDetail.getMediaValueDiff() != 0) {
                    if (sessionEventDetail.getMediaValueDiff() > 0) {
                        credit = false;
                    } else {
                        credit = true;
                    }
                    insertJournalEntry(orguId, userid, cashSessionId,
                            txnType, sessionEventId, IdBConstant.JOURNAL_ACTION_ACTUAL_BANK_ACCOUNT,
                            sessionEventDetail.getPaymentMedia().getPaymName(), Math.abs(sessionEventDetail.getMediaValueDiff()), credit);
                }
                actualTotal = actualTotal + sessionEventDetail.getMediaValueActual();
                expectedTotal = expectedTotal + sessionEventDetail.getMediaValueExpected();
            }
            if (actualTotal - expectedTotal != 0) {
                if (actualTotal - expectedTotal > 0) {
                    credit = true;
                } else {
                    credit = false;
                }
                insertJournalEntry(orguId, userid, cashSessionId,
                        txnType, sessionEventId, IdBConstant.JOURNAL_ACTION_TILL_ADJUSTMENT, "", Math.abs(actualTotal - expectedTotal), credit);
            }

        } catch (Exception e) {
            logger.error("Error in extracting journal entry from reconciliation", e);
        }
    }

    /**
     * insert journal entry.
     * @param orguId orguId
     * @param userId userId
     * @param cashSessionId cashSessionId
     * @param txnType txnType
     * @param seevId seevId
     * @param journalAction journalAction
     * @param amount amount
     * @param isCredit isCredit
     */
    private void insertJournalEntry(long orguId, long userId, long cashSessionId, ConfigCategory txnType,
                                                  long seevId, String journalAction, String accountName, double amount, boolean isCredit)
    {
        JournalEntry journalEntry = null;
        final JournalRule journalRule;
        final List<JournalRule> journalRuleList = accountingDao.getJournalRuleByOrguIdAndTxnTypeAndAction(orguId
                , txnType.getCategoryCode(), journalAction);
        if (journalRuleList != null && journalRuleList.size() > 0) {
            journalRule = journalRuleList.get(0);
            journalEntry = new JournalEntry();
            journalEntry.setSrcTxnType(txnType.getId());
            journalEntry.setSrcTxnId(seevId);
            journalEntry.setAppUserId(userId);
            journalEntry.setOrguId(orguId);
            journalEntry.setCssnSessionId(cashSessionId);
            journalEntry.setJrnActual(journalRule.isJrnrActual());
            journalEntry.setAccId(journalRule.getAccount().getId());
            journalEntry.setJrnAccCode(journalRule.getJrnrAccCode());
            journalEntry.setJrnDate(new Timestamp(new Date().getTime()));
            if (!accountName.isEmpty()) {
                journalEntry.setJrnAccDesc(accountName);
            } else {
                journalEntry.setJrnAccDesc(journalRule.getJrnrAccDesc());
            }
            if (isCredit) {
                journalEntry.setJrnCredit(amount);
            } else {
                journalEntry.setJrnDebit(amount);
            }
            accountingDao.insertJournalEntry(journalEntry);
        }
    }
}
