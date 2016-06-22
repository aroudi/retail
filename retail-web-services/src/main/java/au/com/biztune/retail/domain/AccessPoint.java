package au.com.biztune.retail.domain;

/**
 * Created by arash on 22/06/2016.
 */
public class AccessPoint {
    private long id;
    private String acptName;
    private String acptToken;
    private String acptHelpUrl;
    private String acptDesc;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAcptName() {
        return acptName;
    }

    public void setAcptName(String acptName) {
        this.acptName = acptName;
    }

    public String getAcptToken() {
        return acptToken;
    }

    public void setAcptToken(String acptToken) {
        this.acptToken = acptToken;
    }

    public String getAcptHelpUrl() {
        return acptHelpUrl;
    }

    public void setAcptHelpUrl(String acptHelpUrl) {
        this.acptHelpUrl = acptHelpUrl;
    }

    public String getAcptDesc() {
        return acptDesc;
    }

    public void setAcptDesc(String acptDesc) {
        this.acptDesc = acptDesc;
    }
}
