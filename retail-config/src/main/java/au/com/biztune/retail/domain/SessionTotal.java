package au.com.biztune.retail.domain;

/**
 * Created by arash on 21/09/2016.
 */
public class SessionTotal {
    private long id;
    private long cssnSessionId;
    private MediaType mediaType;
    private PaymentMedia paymentMedia;
    private double mediaTotalCount;
    private double mediaTotalValue;

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

    public double getMediaTotalCount() {
        return mediaTotalCount;
    }

    public void setMediaTotalCount(double mediaTotalCount) {
        this.mediaTotalCount = mediaTotalCount;
    }

    public double getMediaTotalValue() {
        return mediaTotalValue;
    }

    public void setMediaTotalValue(double mediaTotalValue) {
        this.mediaTotalValue = mediaTotalValue;
    }

}
