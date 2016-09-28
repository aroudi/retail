package au.com.biztune.retail.processor;

import au.com.biztune.retail.dao.CashSessionDao;
import au.com.biztune.retail.dao.ConfigCategoryDao;
import au.com.biztune.retail.domain.*;
import au.com.biztune.retail.service.CashSessionService;
import au.com.biztune.retail.service.StockService;
import au.com.biztune.retail.service.TotalerService;
import au.com.biztune.retail.util.IdBConstant;
import au.com.biztune.retail.util.queuemanager.Processor;
import au.com.biztune.retail.util.queuemanager.Request;
import au.com.biztune.retail.util.queuemanager.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by arash on 26/07/2016.
 */
@Component
public class SessionProcessor implements Processor {
    private final Logger logger = LoggerFactory.getLogger(SessionProcessor.class);

    @Autowired
    private CashSessionDao cashSessionDao;

    @Autowired
    private ConfigCategoryDao configCategoryDao;

    @Autowired
    private CashSessionService cashSessionService;

    @Autowired
    private TotalerService totalerService;

    @Autowired
    private StockService stockService;
    /**
     * process the incoming session requests.
     * @param request request
     * @return Response
     */
    public Response process(Request request) {
        final Response response = new Response();
        try {
            SessionRequest sessionRequest;
            if (request instanceof SessionRequest) {
                sessionRequest = (SessionRequest) request;
            } else {
                response.setSucceeded(false);
                response.setMessage("BAD ONSET REQUEST");
                return response;
            }
            //process session figures
            doSessionProcess(sessionRequest);

            //process totaller
            totalerService.extractTotalFiguresFromTxn(sessionRequest.getTxnHeader());

            //update stock
            stockService.processTxnForStockUpdate(sessionRequest.getTxnHeader());

            response.setSucceeded(true);
            return response;
        } catch (Exception e) {
            logger.error("Exception processing session request: ", e);
            response.setSucceeded(false);
            response.setMessage("Exception in processing stock request :" + e.getMessage());
            return response;
        }
    }

    private void processSessionMedia(SessionRequest sessionRequest, SessionEvent sessionEvent) {
        try {
            //extract media values used for this event
            final Map<PaymentMedia, Double[]> totalMediaVals = new HashMap<PaymentMedia, Double[]>();
            Double[] values = null;
            for (TxnMedia txnMedia : sessionRequest.getTxnHeader().getTxnMedias()) {
                //only new added media should be count.
                if (txnMedia == null || !txnMedia.isNewAdded() || txnMedia.isTxmdVoided()) {
                    continue;
                }
                if (totalMediaVals.containsKey(txnMedia.getPaymentMedia())) {
                    values = totalMediaVals.get(txnMedia.getPaymentMedia());
                    values[0] = values[0] + txnMedia.getCount();
                    values[1] = values[1] + txnMedia.getValue();
                } else {
                    values = new Double[2];
                    values[0] = (double) txnMedia.getCount();
                    values[1] = txnMedia.getValue();
                    totalMediaVals.put(txnMedia.getPaymentMedia(), values);
                }
            }
            //create session media
            SessionMedia sessionMedia = null;
            SessionTotal sessionTotal = null;
            for (PaymentMedia paymentMedia : totalMediaVals.keySet()) {
                values = totalMediaVals.get(paymentMedia);
                sessionMedia = cashSessionService.createSessionMedia(sessionEvent, paymentMedia, values[0], values[1]);

                //update session total
                sessionTotal = cashSessionDao.getSessionTotalPerSessionIdAndPaymentMediaId(sessionEvent.getCssnSessionId(), paymentMedia.getId());
                if (sessionTotal == null) {
                    sessionTotal = new SessionTotal();
                    sessionTotal.setMediaType(paymentMedia.getMediaType());
                    sessionTotal.setPaymentMedia(paymentMedia);
                    sessionTotal.setCssnSessionId(sessionEvent.getCssnSessionId());
                    sessionTotal.setMediaTotalCount(values[0]);
                    sessionTotal.setMediaTotalValue(values[1]);
                    cashSessionDao.insertSessionTotal(sessionTotal);
                } else {
                    sessionTotal.setMediaTotalCount(sessionTotal.getMediaTotalCount() + values[0]);
                    sessionTotal.setMediaTotalValue(sessionTotal.getMediaTotalValue() + values[1]);
                    cashSessionDao.updateSessionTotalPerSessionIdAndPaymentMediaId(sessionTotal);
                }
            }

        } catch (Exception e) {
            logger.error("Exception in processing session medias.", e);
        }
    }

    private void assignTxnToSession(SessionRequest sessionRequest, CashSession cashSession) {
        //assign txn to session
        final TxnSessionLink txnSessionLink = new TxnSessionLink();
        txnSessionLink.setTxhdTxnNr(sessionRequest.getTxnHeader().getTxhdTxnNr());
        txnSessionLink.setCssnSessionId(cashSession.getId());
        txnSessionLink.setOrguId(sessionRequest.getTxnHeader().getOrgUnit().getId());
        txnSessionLink.setStoreId(sessionRequest.getTxnHeader().getStore().getId());
        txnSessionLink.setTslDateCreated(new Timestamp(new Date().getTime()));
        txnSessionLink.setTxnId(sessionRequest.getTxnHeader().getId());
        txnSessionLink.setTxnType(sessionRequest.getTxnHeader().getTxhdTxnType());
        try {
            cashSessionDao.insertTxnSessionLink(txnSessionLink);
        } catch (Exception e) {
            logger.error("Exception in saving Transaction-Session-Link", e);
        }
    }

    private void doSessionProcess(SessionRequest sessionRequest) {
        //find the active session for current user;
        CashSession cashSession = cashSessionDao.getCurrentCashSessionPerUser(sessionRequest.getTxnHeader().getTxhdOperator());
        //if cashcession is null create it.
        if (cashSession == null) {
            cashSession = cashSessionService.createCashSession(sessionRequest.getTxnHeader().getUser());
        }
        //if session is closed, then open it
        if (cashSession.getCssnStatus().getCategoryCode().equals(IdBConstant.SESSION_STATE_CLOSED)) {
            final ConfigCategory openState = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_SESSION_STATE, IdBConstant.SESSION_STATE_OPEN);
            cashSession.setCssnStatus(openState);
            cashSessionDao.updateCashSession(cashSession);
        }
        //create session event
        final SessionEvent sessionEvent = cashSessionService.createSessionEvent(cashSession.getId(), sessionRequest.getEventType(), sessionRequest.getTxnHeader().getUser().getId());

        //process session media
        if (sessionRequest.getTxnHeader().getTxnMedias() != null) {
            processSessionMedia(sessionRequest, sessionEvent);
        }

        //assign txn to session
        assignTxnToSession(sessionRequest, cashSession);
    }
}
