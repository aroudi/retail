package au.com.biztune.retail.domain;

/**
 * Created by arash on 22/06/2016.
 */
public class RoleAccess {
    private long id;
    private long roleId;
    private long acptId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public long getAcptId() {
        return acptId;
    }

    public void setAcptId(long acptId) {
        this.acptId = acptId;
    }
}
