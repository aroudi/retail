package au.com.biztune.retail.domain;

public class ReportParamVal {
    private long repParamValId;
    private long repParamId;
    private String repParamVal;
    private int displayOrder;

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
}
