package au.com.biztune.retail.domain;

/**
 * Created by arash on 21/09/2016.
 */
public class SessionEventDetail {
    private long id;
    private long cssnSessionId;
    private long seevId;
    private long orguId;
    private long storeId;
    private MediaType mediaType;
    private PaymentMedia paymentMedia;
    private double mediaCountActual;
    private double mediaCountExpected;
    private double mediaCountDiff;
    private double mediaValueActual;
    private double mediaValueExpected;
    private double mediaValueDiff;
    private String sedeComment;

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

    public double getMediaCountActual() {
        return mediaCountActual;
    }

    public void setMediaCountActual(double mediaCountActual) {
        this.mediaCountActual = mediaCountActual;
    }

    public double getMediaCountExpected() {
        return mediaCountExpected;
    }

    public void setMediaCountExpected(double mediaCountExpected) {
        this.mediaCountExpected = mediaCountExpected;
    }

    public double getMediaCountDiff() {
        return mediaCountDiff;
    }

    public void setMediaCountDiff(double mediaCountDiff) {
        this.mediaCountDiff = mediaCountDiff;
    }

    public double getMediaValueActual() {
        return mediaValueActual;
    }

    public void setMediaValueActual(double mediaValueActual) {
        this.mediaValueActual = mediaValueActual;
    }

    public double getMediaValueExpected() {
        return mediaValueExpected;
    }

    public void setMediaValueExpected(double mediaValueExpected) {
        this.mediaValueExpected = mediaValueExpected;
    }

    public double getMediaValueDiff() {
        return mediaValueDiff;
    }

    public void setMediaValueDiff(double mediaValueDiff) {
        this.mediaValueDiff = mediaValueDiff;
    }

    public String getSedeComment() {
        return sedeComment;
    }

    public void setSedeComment(String sedeComment) {
        this.sedeComment = sedeComment;
    }
}
