package au.com.biztune.retail.domain;

import java.util.List;

/**
 * Created by arash on 22/06/2016.
 */
public class AppRole {
    private long id;
    private String roleCode;
    private String roleName;
    private String roleDesc;
    private boolean roleDeleted;
    private List<AccessPoint> accessPoints;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleDesc() {
        return roleDesc;
    }

    public void setRoleDesc(String roleDesc) {
        this.roleDesc = roleDesc;
    }

    public List<AccessPoint> getAccessPoints() {
        return accessPoints;
    }

    public void setAccessPoints(List<AccessPoint> accessPoints) {
        this.accessPoints = accessPoints;
    }

    public boolean isRoleDeleted() {
        return roleDeleted;
    }

    public void setRoleDeleted(boolean roleDeleted) {
        this.roleDeleted = roleDeleted;
    }
}
