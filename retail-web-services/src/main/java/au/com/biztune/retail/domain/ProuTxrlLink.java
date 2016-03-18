package au.com.biztune.retail.domain;

/**
 * Created by arash on 18/03/2016.
 */
public class ProuTxrlLink {
    private long prouId;
    private long txrlId;

    public long getProuId() {
        return prouId;
    }

    public void setProuId(long prouId) {
        this.prouId = prouId;
    }

    public long getTxrlId() {
        return txrlId;
    }

    public void setTxrlId(long txrlId) {
        this.txrlId = txrlId;
    }
}
