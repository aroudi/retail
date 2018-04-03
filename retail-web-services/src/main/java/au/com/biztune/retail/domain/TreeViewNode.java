package au.com.biztune.retail.domain;


import java.util.List;

/**
 * Created by arash on 28/03/2018.
 */
public class TreeViewNode {
    private long id;
    private long primaryKey;
    private String name;
    //Department, CategoryHeading or CategoryValue
    private String nodeType;
    private long parentNodeId;
    private int order;
    private List<TreeViewNode> children;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public List<TreeViewNode> getChildren() {
        return children;
    }

    public void setChildren(List<TreeViewNode> children) {
        this.children = children;
    }

    public long getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(long primaryKey) {
        this.primaryKey = primaryKey;
    }

    public long getParentNodeId() {
        return parentNodeId;
    }

    public void setParentNodeId(long parentNodeId) {
        this.parentNodeId = parentNodeId;
    }
}
