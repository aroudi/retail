package au.com.biztune.retail.domain;

import java.util.List;

/**
 * Created by arash on 26/03/2018.
 */
public class Department {
    private long id;
    private String deptName;
    private String deptCode;
    private String deptDesc;
    private boolean active;
    private List<CategoryHeading> categoryHeadingList;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public String getDeptDesc() {
        return deptDesc;
    }

    public void setDeptDesc(String deptDesc) {
        this.deptDesc = deptDesc;
    }

    public List<CategoryHeading> getCategoryHeadingList() {
        return categoryHeadingList;
    }

    public void setCategoryHeadingList(List<CategoryHeading> categoryHeadingList) {
        this.categoryHeadingList = categoryHeadingList;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
