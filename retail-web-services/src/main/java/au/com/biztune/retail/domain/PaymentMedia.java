package au.com.biztune.retail.domain;

/**
 * Created by arash on 14/04/2016.
 */
public class PaymentMedia {
    private long id;
    private String paymName;
    private String paymCode;
    private LegalTender legalTender;
    private MediaType mediaType;
    private String paymDisplayText;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPaymName() {
        return paymName;
    }

    public void setPaymName(String paymName) {
        this.paymName = paymName;
    }

    public String getPaymCode() {
        return paymCode;
    }

    public void setPaymCode(String paymCode) {
        this.paymCode = paymCode;
    }

    public LegalTender getLegalTender() {
        return legalTender;
    }

    public void setLegalTender(LegalTender legalTender) {
        this.legalTender = legalTender;
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public void setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    public String getPaymDisplayText() {
        return paymDisplayText;
    }

    public void setPaymDisplayText(String paymDisplayText) {
        this.paymDisplayText = paymDisplayText;
    }
}
