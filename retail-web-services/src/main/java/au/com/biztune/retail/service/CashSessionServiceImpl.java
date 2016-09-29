package au.com.biztune.retail.service;

import au.com.biztune.retail.dao.CashSessionDao;
import au.com.biztune.retail.dao.ConfigCategoryDao;
import au.com.biztune.retail.dao.PaymentMediaDao;
import au.com.biztune.retail.domain.*;
import au.com.biztune.retail.form.AddFloatForm;
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
    /**
     * create cash session for user.
     * @param user user.
     * @return CashSession
     */
    public CashSession createCashSession(AppUser user) {
        try {
            final CashSession cashSession = new CashSession();
            cashSession.setCssnOperator(user);
            cashSession.setOrguId(sessionState.getOrgUnit().getId());
            cashSession.setStoreId(sessionState.getStore().getId());
            final Timestamp currentDate = new Timestamp(new Date().getTime());
            cashSession.setCssnTradingDate(currentDate);
            final ConfigCategory status = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_SESSION_STATE, IdBConstant.SESSION_STATE_OPEN);
            cashSession.setCssnStatus(status);
            cashSession.setCssnStartDate(currentDate);
            cashSession.setCssnCurrentCash(0);
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
     * @param paymentMedia paymentMedia
     * @param mediaCount mediaCount
     * @param mediaValue mediaValue
     * @return Session Media.
     */
    public SessionMedia createSessionMedia(SessionEvent sessionEvent, PaymentMedia paymentMedia, double mediaCount, double mediaValue) {
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
            CashSession cashSession = cashSessionDao.getCurrentCashSessionPerUser(appUser.getId());
            //if no session assigned to user then create one.
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
     * close cash session after logged out.
     * @param appUser appUser.
     */
    public void closeCashSessionForLoggedOutUser(AppUser appUser) {
        try {
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
            CashSession cashSession = cashSessionDao.getCurrentCashSessionPerUser(appUser.getId());
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
    public List<CashSession> getAllCurrentCashSessions() {
        try {
            return cashSessionDao.getAllCurrentCashSessions();
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
            final SessionMedia sessionMedia = createSessionMedia(sessionEvent, paymentMediaCash, 1.0, addFloatForm.getAmount());
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
}
