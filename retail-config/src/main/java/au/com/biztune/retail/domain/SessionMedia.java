package au.com.biztune.retail.domain;

/**
 * Created by arash on 21/09/2016.
 */
public class SessionMedia {
    private long id;
    private long cssnSessionId;
    private long seevId;
    private long orguId;
    private long storeId;
    private MediaType mediaType;
    private PaymentMedia paymentMedia;
    private double semeMediaCount;
    private double semeMediaValue;
    private long txmdId;

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

    public long getSeevId() {
        return seevId;
    }

    public void setSeevId(long seevId) {
        this.seevId = seevId;
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

    public MediaType getMediaType() {
        return mediaType;
    }

    public void setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    public PaymentMedia getPaymentMedia() {
        return paymentMedia;
    }

    public void setPaymentMedia(PaymentMedia paymentMedia) {
        this.paymentMedia = paymentMedia;
    }

    public double getSemeMediaCount() {
        return semeMediaCount;
    }

    public void setSemeMediaCount(double semeMediaCount) {
        this.semeMediaCount = semeMediaCount;
    }

    public double getSemeMediaValue() {
        return semeMediaValue;
    }

    public void setSemeMediaValue(double semeMediaValue) {
        this.semeMediaValue = semeMediaValue;
    }

    public long getTxmdId() {
        return txmdId;
    }

    public void setTxmdId(long txmdId) {
        this.txmdId = txmdId;
    }
}
