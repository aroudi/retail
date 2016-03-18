package au.com.biztune.retail.domain;

/**
 * Created by arash on 18/03/2016.
 */
public class TaxRuleName {
    private long id;
    private String txrnName;
    private String txrnCode;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTxrnName() {
        return txrnName;
    }

    public void setTxrnName(String txrnName) {
        this.txrnName = txrnName;
    }

    public String getTxrnCode() {
        return txrnCode;
    }

    public void setTxrnCode(String txrnCode) {
        this.txrnCode = txrnCode;
    }
}
