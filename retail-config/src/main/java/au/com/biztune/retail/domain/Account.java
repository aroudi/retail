package au.com.biztune.retail.domain;

/**
 * Created by arash on 22/11/2016.
 */
public class Account {
    private long id;
    private long orguId;
    private ConfigCategory accType;
    private String accCode;
    private String accName;
    private String accDesc;

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

    public ConfigCategory getAccType() {
        return accType;
    }

    public void setAccType(ConfigCategory accType) {
        this.accType = accType;
    }

    public String getAccCode() {
        return accCode;
    }

    public void setAccCode(String accCode) {
        this.accCode = accCode;
    }

    public String getAccName() {
        return accName;
    }

    public void setAccName(String accName) {
        this.accName = accName;
    }

    public String getAccDesc() {
        return accDesc;
    }

    public void setAccDesc(String accDesc) {
        this.accDesc = accDesc;
    }
}
