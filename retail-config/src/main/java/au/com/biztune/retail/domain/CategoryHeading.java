package au.com.biztune.retail.domain;

import java.util.List;

/**
 * Created by arash on 26/03/2018.
 */
public class CategoryHeading {
    private long id;
    private String cathName;
    private String cathCode;
    private String cathDesc;
    private boolean active;

    private List<CategoryValue> valueList;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCathName() {
        return cathName;
    }

    public void setCathName(String cathName) {
        this.cathName = cathName;
    }

    public String getCathCode() {
        return cathCode;
    }

    public void setCathCode(String cathCode) {
        this.cathCode = cathCode;
    }

    public String getCathDesc() {
        return cathDesc;
    }

    public void setCathDesc(String cathDesc) {
        this.cathDesc = cathDesc;
    }

    public List<CategoryValue> getValueList() {
        return valueList;
    }

    public void setValueList(List<CategoryValue> valueList) {
        this.valueList = valueList;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
