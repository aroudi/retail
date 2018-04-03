package au.com.biztune.retail.domain;

/**
 * Created by arash on 26/03/2018.
 */
public class DeptCategory {
    private long id;
    private long deptId;
    private boolean active;
    private long cathId;
    private int level;

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

    public boolean isActive() {
        return active;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
