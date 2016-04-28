package au.com.biztune.retail.form;

import au.com.biztune.retail.domain.ConfigCategory;
import au.com.biztune.retail.domain.PaymentMedia;

/**
 * Created by arash on 15/04/2016.
 */
public class TxnMediaForm {
    private long id;
    private PaymentMedia paymentMedia;
    private ConfigCategory txmdType;
    private double txmdAmountLocal;
    private boolean txmdVoided;
    private long orguId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public PaymentMedia getPaymentMedia() {
        return paymentMedia;
    }

    public void setPaymentMedia(PaymentMedia paymentMedia) {
        this.paymentMedia = paymentMedia;
    }

    public ConfigCategory getTxmdType() {
        return txmdType;
    }

    public void setTxmdType(ConfigCategory txmdType) {
        this.txmdType = txmdType;
    }

    public double getTxmdAmountLocal() {
        return txmdAmountLocal;
    }

    public void setTxmdAmountLocal(double txmdAmountLocal) {
        this.txmdAmountLocal = txmdAmountLocal;
    }

    public boolean isTxmdVoided() {
        return txmdVoided;
    }

    public void setTxmdVoided(boolean txmdVoided) {
        this.txmdVoided = txmdVoided;
    }

    public long getOrguId() {
        return orguId;
    }

    public void setOrguId(long orguId) {
        this.orguId = orguId;
    }
}
