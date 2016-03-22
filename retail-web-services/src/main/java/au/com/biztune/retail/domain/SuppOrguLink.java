package au.com.biztune.retail.domain;

/**
 * Created by arash on 22/03/2016.
 */
public class SuppOrguLink {
    private long id;
    private long suppId;
    private long orguId;
    private ConfigCategory status;
    private double solCredLimit;
    private double solMinOrdVal;
    private long solLeadTime;
    private String solOrderingCode;
    private double solMinPoQty;
    private long solPaymentDays;
    private String solAccCode;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getSuppId() {
        return suppId;
    }

    public void setSuppId(long suppId) {
        this.suppId = suppId;
    }

    public long getOrguId() {
        return orguId;
    }

    public void setOrguId(long orguId) {
        this.orguId = orguId;
    }

    public ConfigCategory getStatus() {
        return status;
    }

    public void setStatus(ConfigCategory status) {
        this.status = status;
    }

    public double getSolCredLimit() {
        return solCredLimit;
    }

    public void setSolCredLimit(double solCredLimit) {
        this.solCredLimit = solCredLimit;
    }

    public double getSolMinOrdVal() {
        return solMinOrdVal;
    }

    public void setSolMinOrdVal(double solMinOrdVal) {
        this.solMinOrdVal = solMinOrdVal;
    }

    public long getSolLeadTime() {
        return solLeadTime;
    }

    public void setSolLeadTime(long solLeadTime) {
        this.solLeadTime = solLeadTime;
    }

    public String getSolOrderingCode() {
        return solOrderingCode;
    }

    public void setSolOrderingCode(String solOrderingCode) {
        this.solOrderingCode = solOrderingCode;
    }

    public double getSolMinPoQty() {
        return solMinPoQty;
    }

    public void setSolMinPoQty(double solMinPoQty) {
        this.solMinPoQty = solMinPoQty;
    }

    public long getSolPaymentDays() {
        return solPaymentDays;
    }

    public void setSolPaymentDays(long solPaymentDays) {
        this.solPaymentDays = solPaymentDays;
    }

    public String getSolAccCode() {
        return solAccCode;
    }

    public void setSolAccCode(String solAccCode) {
        this.solAccCode = solAccCode;
    }
}
