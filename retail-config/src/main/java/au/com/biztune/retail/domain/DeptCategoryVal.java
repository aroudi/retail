package au.com.biztune.retail.domain;

/**
 * Created by arash on 27/03/2018.
 */
public class DeptCategoryVal {
    private long id;
    private long deptCatId;
    private long deptId;
    private long cathId;
    private long catvId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDeptId() {
        return deptId;
    }

    public void setDeptId(long deptId) {
        this.deptId = deptId;
    }

    public long getCathId() {
        return cathId;
    }

    public void setCathId(long cathId) {
        this.cathId = cathId;
    }

    public long getCatvId() {
        return catvId;
    }

    public void setCatvId(long catvId) {
        this.catvId = catvId;
    }

    public long getDeptCatId() {
        return deptCatId;
    }

    public void setDeptCatId(long deptCatId) {
        this.deptCatId = deptCatId;
    }
}
