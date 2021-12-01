package au.com.biztune.retail.domain;

import java.sql.Timestamp;

/**
 * Created by arash on 18/03/2016.
 */
public class TaxLegVariance {
    private long id;
    private double txlvRate;
    private Timestamp txlvDatetimeFrom;
    private Timestamp txlvDatetimeTo;
    private String txlvCode;
    private String txlvDesc;
    private double txlvAmount;
    private boolean txlvIsActive;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getTxlvRate() {
        return txlvRate;
    }

    public void setTxlvRate(double txlvRate) {
        this.txlvRate = txlvRate;
    }

    public Timestamp getTxlvDatetimeFrom() {
        return txlvDatetimeFrom;
    }

    public void setTxlvDatetimeFrom(Timestamp txlvDatetimeFrom) {
        this.txlvDatetimeFrom = txlvDatetimeFrom;
    }

    public Timestamp getTxlvDatetimeTo() {
        return txlvDatetimeTo;
    }

    public void setTxlvDatetimeTo(Timestamp txlvDatetimeTo) {
        this.txlvDatetimeTo = txlvDatetimeTo;
    }

    public String getTxlvCode() {
        return txlvCode;
    }

    public void setTxlvCode(String txlvCode) {
        this.txlvCode = txlvCode;
    }

    public String getTxlvDesc() {
        return txlvDesc;
    }

    public void setTxlvDesc(String txlvDesc) {
        this.txlvDesc = txlvDesc;
    }

    public double getTxlvAmount() {
        return txlvAmount;
    }

    public void setTxlvAmount(double txlvAmount) {
        this.txlvAmount = txlvAmount;
    }

    public boolean isTxlvIsActive() {
        return txlvIsActive;
    }

    public void setTxlvIsActive(boolean txlvIsActive) {
        this.txlvIsActive = txlvIsActive;
    }
}
