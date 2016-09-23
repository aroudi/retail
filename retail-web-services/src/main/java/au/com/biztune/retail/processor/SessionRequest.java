package au.com.biztune.retail.processor;

import au.com.biztune.retail.domain.TxnHeader;
import au.com.biztune.retail.util.queuemanager.Request;

/**
 * Created by arash on 26/07/2016.
 */
public class SessionRequest extends Request {
    private String eventType;
    private TxnHeader txnHeader;
    public TxnHeader getTxnHeader() {
        return txnHeader;
    }
    public void setTxnHeader(TxnHeader txnHeader) {
        this.txnHeader = txnHeader;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }
}
