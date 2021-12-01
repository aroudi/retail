package au.com.biztune.retail.domain;

/**
 * Created by arash on 22/06/2016.
 */
public class AppUserRole {
    private long id;
    private long usrId;
    private long roleId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUsrId() {
        return usrId;
    }

    public void setUsrId(long usrId) {
        this.usrId = usrId;
    }

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }
}
