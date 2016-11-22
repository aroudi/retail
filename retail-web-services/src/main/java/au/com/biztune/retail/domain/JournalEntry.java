package au.com.biztune.retail.domain;

/**
 * Created by arash on 22/11/2016.
 */
public class JournalEntry {
    private long id;
    private long orguId;
    private long cssnSessionId;
    private long srcTxnType;
    private long srcTxnId;
    private long appUserId;
    private long accId;
    private String jrnAccCode;
    private String jrnAccDesc;
    private double jrnDebit;
    private double jrnCredit;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getOrguId() {
        return orguId;
    }

    public void setOrguId(long orguId) {
        this.orguId = orguId;
    }

    public long getCssnSessionId() {
        return cssnSessionId;
    }

    public void setCssnSessionId(long cssnSessionId) {
        this.cssnSessionId = cssnSessionId;
    }

    public long getSrcTxnType() {
        return srcTxnType;
    }

    public void setSrcTxnType(long srcTxnType) {
        this.srcTxnType = srcTxnType;
    }

    public long getSrcTxnId() {
        return srcTxnId;
    }

    public void setSrcTxnId(long srcTxnId) {
        this.srcTxnId = srcTxnId;
    }

    public long getAppUserId() {
        return appUserId;
    }

    public void setAppUserId(long appUserId) {
        this.appUserId = appUserId;
    }

    public long getAccId() {
        return accId;
    }

    public void setAccId(long accId) {
        this.accId = accId;
    }

    public String getJrnAccCode() {
        return jrnAccCode;
    }

    public void setJrnAccCode(String jrnAccCode) {
        this.jrnAccCode = jrnAccCode;
    }

    public String getJrnAccDesc() {
        return jrnAccDesc;
    }

    public void setJrnAccDesc(String jrnAccDesc) {
        this.jrnAccDesc = jrnAccDesc;
    }

    public double getJrnDebit() {
        return jrnDebit;
    }

    public void setJrnDebit(double jrnDebit) {
        this.jrnDebit = jrnDebit;
    }

    public double getJrnCredit() {
        return jrnCredit;
    }

    public void setJrnCredit(double jrnCredit) {
        this.jrnCredit = jrnCredit;
    }
}
