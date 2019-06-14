package au.com.biztune.retail.domain;

import java.sql.Timestamp;

/**
 * Created by arash on 14/04/2016.
 */
public class TxnMedia {
    private long id;
    private long orguId;
    private long storeId;
    private long txhdId;
    private long medtId;
    private PaymentMedia paymentMedia;
    private ConfigCategory txmdType;
    private double txmdAmountLocal;
    private double txmdAmntForeign;
    private String txmdPan;
    private Timestamp txmdStartDate;
    private Timestamp txmdExpiryDate;
    private boolean txmdVoided;
    private boolean txmdRefunded;
    //use only for invoice. populated with media id from txn_media
    private long parentId;
    private boolean newAdded;
    private ConfigCategory invoiceMediaType;
    private String txmdComment;

    /**
     * get value based on txmdType.
     * @return media valu
     */
    public double getValue() {
        if (txmdType == null) {
            return getTxmdAmountLocal();
        }
        if ("TXN_MEDIA_SALE".equals(txmdType.getCategoryCode())) {
            return getTxmdAmountLocal();
        }
        if ("TXN_MEDIA_DEPOSIT".equals(txmdType.getCategoryCode())) {
            return getTxmdAmountLocal();
        }
        if ("TXN_MEDIA_REFUND".equals(txmdType.getCategoryCode())) {
            return getTxmdAmountLocal();
        }
        return getTxmdAmountLocal();
    }

    /**
     * get count based on txmdType.
     * @return media count -- always 1 or -1
     */
    public int getCount() {
        if (txmdType == null) {
            return 1;
        }
        if ("TXN_MEDIA_SALE".equals(txmdType.getCategoryCode())) {
            return 1;
        }
        if ("TXN_MEDIA_DEPOSIT".equals(txmdType.getCategoryCode())) {
            return 1;
        }
        if ("TXN_MEDIA_REFUND".equals(txmdType.getCategoryCode())) {
            return -1;
        }
        return 1;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public long getTxhdId() {
        return txhdId;
    }

    public void setTxhdId(long txhdId) {
        this.txhdId = txhdId;
    }

    public long getMedtId() {
        return medtId;
    }

    public void setMedtId(long medtId) {
        this.medtId = medtId;
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

    public double getTxmdAmntForeign() {
        return txmdAmntForeign;
    }

    public void setTxmdAmntForeign(double txmdAmntForeign) {
        this.txmdAmntForeign = txmdAmntForeign;
    }

    public String getTxmdPan() {
        return txmdPan;
    }

    public void setTxmdPan(String txmdPan) {
        this.txmdPan = txmdPan;
    }

    public Timestamp getTxmdStartDate() {
        return txmdStartDate;
    }

    public void setTxmdStartDate(Timestamp txmdStartDate) {
        this.txmdStartDate = txmdStartDate;
    }

    public Timestamp getTxmdExpiryDate() {
        return txmdExpiryDate;
    }

    public void setTxmdExpiryDate(Timestamp txmdExpiryDate) {
        this.txmdExpiryDate = txmdExpiryDate;
    }

    public boolean isTxmdVoided() {
        return txmdVoided;
    }

    public void setTxmdVoided(boolean txmdVoided) {
        this.txmdVoided = txmdVoided;
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public boolean isNewAdded() {
        return newAdded;
    }

    public void setNewAdded(boolean newAdded) {
        this.newAdded = newAdded;
    }

    public ConfigCategory getInvoiceMediaType() {
        return invoiceMediaType;
    }

    public void setInvoiceMediaType(ConfigCategory invoiceMediaType) {
        this.invoiceMediaType = invoiceMediaType;
    }

    public String getTxmdComment() {
        return txmdComment;
    }

    public void setTxmdComment(String txmdComment) {
        this.txmdComment = txmdComment;
    }

    public boolean isTxmdRefunded() {
        return txmdRefunded;
    }

    public void setTxmdRefunded(boolean txmdRefunded) {
        this.txmdRefunded = txmdRefunded;
    }
}
