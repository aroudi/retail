package au.com.biztune.retail.domain;

/**
 * Created by arash on 14/04/2016.
 */
public class MediaType {
    private long id;
    private String medtName;
    private String medtDesc;
    private String medtCode;
    private boolean medtRefundable;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMedtName() {
        return medtName;
    }

    public void setMedtName(String medtName) {
        this.medtName = medtName;
    }

    public String getMedtDesc() {
        return medtDesc;
    }

    public void setMedtDesc(String medtDesc) {
        this.medtDesc = medtDesc;
    }

    public String getMedtCode() {
        return medtCode;
    }

    public void setMedtCode(String medtCode) {
        this.medtCode = medtCode;
    }

    public boolean isMedtRefundable() {
        return medtRefundable;
    }

    public void setMedtRefundable(boolean medtRefundable) {
        this.medtRefundable = medtRefundable;
    }
}
