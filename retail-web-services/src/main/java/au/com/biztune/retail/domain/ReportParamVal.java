package au.com.biztune.retail.domain;

/**
 * created by arash on 10/05/2018.
 */
public class ReportParamVal {
    private long repParamValId;
    private long repParamId;
    private String repParamKey;
    private String repParamVal;
    private int displayOrder;
    private String tableAlias;

    public long getRepParamValId() {
        return repParamValId;
    }

    public void setRepParamValId(long repParamValId) {
        this.repParamValId = repParamValId;
    }

    public long getRepParamId() {
        return repParamId;
    }

    public void setRepParamId(long repParamId) {
        this.repParamId = repParamId;
    }

    public String getRepParamVal() {
        return repParamVal;
    }

    public void setRepParamVal(String repParamVal) {
        this.repParamVal = repParamVal;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    public String getTableAlias() {
        return tableAlias;
    }

    public void setTableAlias(String tableAlias) {
        this.tableAlias = tableAlias;
    }

    public String getRepParamKey() {
        return repParamKey;
    }

    public void setRepParamKey(String repParamKey) {
        this.repParamKey = repParamKey;
    }
    @Override
    public String toString() {
        return this.repParamVal;
    }
}
