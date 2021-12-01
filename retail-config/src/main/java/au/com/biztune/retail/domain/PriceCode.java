package au.com.biztune.retail.domain;

import java.sql.Timestamp;

/**
 * Created by arash on 18/03/2016.
 */
public class PriceCode {
    private long id;
    private String prccName;
    private String prccCode;
    private Timestamp prccCreated;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPrccName() {
        return prccName;
    }

    public void setPrccName(String prccName) {
        this.prccName = prccName;
    }

    public String getPrccCode() {
        return prccCode;
    }

    public void setPrccCode(String prccCode) {
        this.prccCode = prccCode;
    }

    public Timestamp getPrccCreated() {
        return prccCreated;
    }

    public void setPrccCreated(Timestamp prccCreated) {
        this.prccCreated = prccCreated;
    }
}
