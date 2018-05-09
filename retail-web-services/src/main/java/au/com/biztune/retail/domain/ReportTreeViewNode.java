package au.com.biztune.retail.domain;


import java.util.List;

/**
 * Created by arash on 28/03/2018.
 */
public class ReportTreeViewNode {
    private long id;
    private long parentNodeId;
    private String name;
    private String nodeType;
    private List<ReportTreeViewNode> children;
    private List<ReportParam> reportParamList;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getParentNodeId() {
        return parentNodeId;
    }

    public void setParentNodeId(long parentNodeId) {
        this.parentNodeId = parentNodeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ReportTreeViewNode> getChildren() {
        return children;
    }

    public void setChildren(List<ReportTreeViewNode> children) {
        this.children = children;
    }

    public List<ReportParam> getReportParamList() {
        return reportParamList;
    }

    public void setReportParamList(List<ReportParam> reportParamList) {
        this.reportParamList = reportParamList;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }
}
