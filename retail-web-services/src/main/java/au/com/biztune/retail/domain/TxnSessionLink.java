package au.com.biztune.retail.domain;

import java.sql.Timestamp;

/**
 * Created by arash on 21/09/2016.
 */
public class TxnSessionLink {
    private long id;
    private long cssnSessionId;
    private long txnId;
    private ConfigCategory txnType;
    private long orguId;
    private long storeId;
    private String tillShortDesc;
    private String txhdTxnNr;
    private Timestamp tslDateCreated;

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

    public long getTxnId() {
        return txnId;
    }

    public void setTxnId(long txnId) {
        this.txnId = txnId;
    }

    public ConfigCategory getTxnType() {
        return txnType;
    }

    public void setTxnType(ConfigCategory txnType) {
        this.txnType = txnType;
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

    public String getTillShortDesc() {
        return tillShortDesc;
    }

    public void setTillShortDesc(String tillShortDesc) {
        this.tillShortDesc = tillShortDesc;
    }

    public String getTxhdTxnNr() {
        return txhdTxnNr;
    }

    public void setTxhdTxnNr(String txhdTxnNr) {
        this.txhdTxnNr = txhdTxnNr;
    }

    public Timestamp getTslDateCreated() {
        return tslDateCreated;
    }

    public void setTslDateCreated(Timestamp tslDateCreated) {
        this.tslDateCreated = tslDateCreated;
    }
}
