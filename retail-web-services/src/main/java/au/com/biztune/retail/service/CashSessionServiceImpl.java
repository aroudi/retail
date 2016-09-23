package au.com.biztune.retail.service;

import au.com.biztune.retail.dao.CashSessionDao;
import au.com.biztune.retail.dao.ConfigCategoryDao;
import au.com.biztune.retail.domain.*;
import au.com.biztune.retail.processor.SessionProcessor;
import au.com.biztune.retail.processor.SessionRequest;
import au.com.biztune.retail.session.SessionState;
import au.com.biztune.retail.util.IdBConstant;
import au.com.biztune.retail.util.queuemanager.QueueManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by arash on 22/09/2016.
 */
@Component
public class CashSessionServiceImpl implements CashSessionService {

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
                cashSessionDao.updateCashSession(cashSession);
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
                cashSessionDao.updateCashSession(cashSession);
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
}
