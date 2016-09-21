package au.com.biztune.retail.domain;

import java.sql.Timestamp;

/**
 * Created by arash on 21/09/2016.
 */
public class SessionEvent {
    private long id;
    private long cssnSessionId;
    private long orguId;
    private long storeId;
    private ConfigCategory seevEventType;
    private Timestamp seevEventDate;
    private long seevOperator;
    private String seevReference;
    private String seevComment;
    private String seevReason;
    private Timestamp seevCollectDate;
    private Timestamp seevCollectRef;
    private String seevPickupNr;
    private String seevPayInSlip;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCssnSessionId() {
        return cssnSessionId;
    }

    public void setCssnSessionId(long cssnSessionId) {
        this.cssnSessionId = cssnSessionId;
    }

    public long getOrguId() {
        return orguId;
    }

    public void setOrguId(long orguId) {
        this.orguId = orguId;
    }

    public long getStoreId() {
        return storeId;
    }

    public void setStoreId(long storeId) {
        this.storeId = storeId;
    }

    public ConfigCategory getSeevEventType() {
        return seevEventType;
    }

    public void setSeevEventType(ConfigCategory seevEventType) {
        this.seevEventType = seevEventType;
    }

    public Timestamp getSeevEventDate() {
        return seevEventDate;
    }

    public void setSeevEventDate(Timestamp seevEventDate) {
        this.seevEventDate = seevEventDate;
    }

    public long getSeevOperator() {
        return seevOperator;
    }

    public void setSeevOperator(long seevOperator) {
        this.seevOperator = seevOperator;
    }

    public String getSeevReason() {
        return seevReason;
    }

    public void setSeevReason(String seevReason) {
        this.seevReason = seevReason;
    }

    public Timestamp getSeevCollectDate() {
        return seevCollectDate;
    }

    public void setSeevCollectDate(Timestamp seevCollectDate) {
        this.seevCollectDate = seevCollectDate;
    }

    public Timestamp getSeevCollectRef() {
        return seevCollectRef;
    }

    public void setSeevCollectRef(Timestamp seevCollectRef) {
        this.seevCollectRef = seevCollectRef;
    }

    public String getSeevPickupNr() {
        return seevPickupNr;
    }

    public void setSeevPickupNr(String seevPickupNr) {
        this.seevPickupNr = seevPickupNr;
    }

    public String getSeevPayInSlip() {
        return seevPayInSlip;
    }

    public void setSeevPayInSlip(String seevPayInSlip) {
        this.seevPayInSlip = seevPayInSlip;
    }

    public String getSeevReference() {
        return seevReference;
    }

    public void setSeevReference(String seevReference) {
        this.seevReference = seevReference;
    }

    public String getSeevComment() {
        return seevComment;
    }

    public void setSeevComment(String seevComment) {
        this.seevComment = seevComment;
    }
}
