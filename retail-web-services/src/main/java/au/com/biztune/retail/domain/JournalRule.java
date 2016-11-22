package au.com.biztune.retail.domain;

/**
 * Created by arash on 22/11/2016.
 */
public class JournalRule {
    private long id;
    private long orguId;
    private long txnType;
    private long jrnrAction;
    private Account account;
    private String jrnrAccCode;
    private String jrnrAccDesc;
    private boolean jrnrDebit;
    private boolean jrnrCredit;

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

    public long getTxnType() {
        return txnType;
    }

    public void setTxnType(long txnType) {
        this.txnType = txnType;
    }

    public long getJrnrAction() {
        return jrnrAction;
    }

    public void setJrnrAction(long jrnrAction) {
        this.jrnrAction = jrnrAction;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getJrnrAccCode() {
        return jrnrAccCode;
    }

    public void setJrnrAccCode(String jrnrAccCode) {
        this.jrnrAccCode = jrnrAccCode;
    }

    public String getJrnrAccDesc() {
        return jrnrAccDesc;
    }

    public void setJrnrAccDesc(String jrnrAccDesc) {
        this.jrnrAccDesc = jrnrAccDesc;
    }

    public boolean isJrnrDebit() {
        return jrnrDebit;
    }

    public void setJrnrDebit(boolean jrnrDebit) {
        this.jrnrDebit = jrnrDebit;
    }

    public boolean isJrnrCredit() {
        return jrnrCredit;
    }

    public void setJrnrCredit(boolean jrnrCredit) {
        this.jrnrCredit = jrnrCredit;
    }
}
