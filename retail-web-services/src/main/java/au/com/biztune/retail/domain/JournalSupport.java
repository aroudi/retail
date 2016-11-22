package au.com.biztune.retail.domain;

/**
 * Created by arash on 22/11/2016.
 */
public class JournalSupport {
    private long jrnId;
    private long txmdId;

    public long getJrnId() {
        return jrnId;
    }

    public void setJrnId(long jrnId) {
        this.jrnId = jrnId;
    }

    public long getTxmdId() {
        return txmdId;
    }

    public void setTxmdId(long txmdId) {
        this.txmdId = txmdId;
    }
}
