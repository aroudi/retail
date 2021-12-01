package au.com.biztune.retail.domain;

/**
 * Created by arash on 18/03/2016.
 */
public class LegalTender {
    private long id;
    private String legtCode;
    private String legtName;
    private String legtSymbol;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLegtCode() {
        return legtCode;
    }

    public void setLegtCode(String legtCode) {
        this.legtCode = legtCode;
    }

    public String getLegtName() {
        return legtName;
    }

    public void setLegtName(String legtName) {
        this.legtName = legtName;
    }

    public String getLegtSymbol() {
        return legtSymbol;
    }

    public void setLegtSymbol(String legtSymbol) {
        this.legtSymbol = legtSymbol;
    }
}
