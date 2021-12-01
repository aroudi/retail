package au.com.biztune.retail.domain;

/**
 * Created by arash on 30/11/2016.
 */
public class AccExportSessionLink {
    private long accExpId;
    private long cssnSessionId;

    public long getAccExpId() {
        return accExpId;
    }

    public void setAccExpId(long accExpId) {
        this.accExpId = accExpId;
    }

    public long getCssnSessionId() {
        return cssnSessionId;
    }

    public void setCssnSessionId(long cssnSessionId) {
        this.cssnSessionId = cssnSessionId;
    }
}
