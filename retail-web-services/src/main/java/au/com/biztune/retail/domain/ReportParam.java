package au.com.biztune.retail.domain;

import java.util.List;

/**
 * created by arash on 10/05/2018.
 */
public class ReportParam {
    private long repParamId;
    private long repHeadId;
    private String repParamName;
    private String tableAlias;
    private List<ReportParamVal> reportParamValList;

    public long getRepParamId() {
        return repParamId;
    }

    public void setRepParamId(long repParamId) {
        this.repParamId = repParamId;
    }

    public long getRepHeadId() {
        return repHeadId;
    }

    public void setRepHeadId(long repHeadId) {
        this.repHeadId = repHeadId;
    }

    public String getRepParamName() {
        return repParamName;
    }

    public void setRepParamName(String repParamName) {
        this.repParamName = repParamName;
    }

    public String getTableAlias() {
        return tableAlias;
    }

    public void setTableAlias(String tableAlias) {
        this.tableAlias = tableAlias;
    }

    public List<ReportParamVal> getReportParamValList() {
        return reportParamValList;
    }

    public void setReportParamValList(List<ReportParamVal> reportParamValList) {
        this.reportParamValList = reportParamValList;
    }
}
