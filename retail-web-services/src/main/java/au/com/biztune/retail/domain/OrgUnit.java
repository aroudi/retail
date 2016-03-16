package au.com.biztune.retail.domain;

/**
 * Created by akhoshraft on 16/03/2016.
 */
public class OrgUnit {
    private long id;
    private String orguName;
    private String orguDesc;
    private String orguCode;
    private long pribId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOrguName() {
        return orguName;
    }

    public void setOrguName(String orguName) {
        this.orguName = orguName;
    }

    public String getOrguDesc() {
        return orguDesc;
    }

    public void setOrguDesc(String orguDesc) {
        this.orguDesc = orguDesc;
    }

    public String getOrguCode() {
        return orguCode;
    }

    public void setOrguCode(String orguCode) {
        this.orguCode = orguCode;
    }

    public long getPribId() {
        return pribId;
    }

    public void setPribId(long pribId) {
        this.pribId = pribId;
    }
}
