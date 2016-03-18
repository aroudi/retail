package au.com.biztune.retail.domain;

/**
 * Created by arash on 18/03/2016.
 */
public class PriceBand {
    private long id;
    private boolean pribActive;
    private String pribCode;
    private String pribName;
    private LegalTender legalTender;
    private boolean pribTaxIncluded;
    private long orguId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isPribActive() {
        return pribActive;
    }

    public void setPribActive(boolean pribActive) {
        this.pribActive = pribActive;
    }

    public String getPribCode() {
        return pribCode;
    }

    public void setPribCode(String pribCode) {
        this.pribCode = pribCode;
    }

    public String getPribName() {
        return pribName;
    }

    public void setPribName(String pribName) {
        this.pribName = pribName;
    }

    public LegalTender getLegalTender() {
        return legalTender;
    }

    public void setLegalTender(LegalTender legalTender) {
        this.legalTender = legalTender;
    }

    public boolean isPribTaxIncluded() {
        return pribTaxIncluded;
    }

    public void setPribTaxIncluded(boolean pribTaxIncluded) {
        this.pribTaxIncluded = pribTaxIncluded;
    }

    public long getOrguId() {
        return orguId;
    }

    public void setOrguId(long orguId) {
        this.orguId = orguId;
    }
}
