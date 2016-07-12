package au.com.biztune.retail.domain;

import java.security.Principal;
import java.util.List;

/**
 * Created by arash on 22/06/2016.
 */
public class AppUser implements Principal {
    private long id;
    private String usrName;
    private String usrPass;
    private String usrFirstName;
    private String usrSurName;
    private String usrActive;
    private boolean usrDeleted;
    private String usrLogedOn;
    private List<AccessPoint> accessPoints;
    private List<AppRole> appRoles;
    private String token;

    /**
     * get User Name.
     * @return String.
     */
    public String getName() {
        return usrName;
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsrName() {
        return usrName;
    }

    public void setUsrName(String usrName) {
        this.usrName = usrName;
    }

    public String getUsrPass() {
        return usrPass;
    }

    public void setUsrPass(String usrPass) {
        this.usrPass = usrPass;
    }

    public String getUsrFirstName() {
        return usrFirstName;
    }

    public void setUsrFirstName(String usrFirstName) {
        this.usrFirstName = usrFirstName;
    }

    public String getUsrSurName() {
        return usrSurName;
    }

    public void setUsrSurName(String usrSurName) {
        this.usrSurName = usrSurName;
    }

    public String getUsrActive() {
        return usrActive;
    }

    public void setUsrActive(String usrActive) {
        this.usrActive = usrActive;
    }

    public String getUsrLogedOn() {
        return usrLogedOn;
    }

    public void setUsrLogedOn(String usrLogedOn) {
        this.usrLogedOn = usrLogedOn;
    }

    public List<AccessPoint> getAccessPoints() {
        return accessPoints;
    }

    public void setAccessPoints(List<AccessPoint> accessPoints) {
        this.accessPoints = accessPoints;
    }

    public List<AppRole> getAppRoles() {
        return appRoles;
    }

    public void setAppRoles(List<AppRole> appRoles) {
        this.appRoles = appRoles;
    }

    public boolean isUsrDeleted() {
        return usrDeleted;
    }

    public void setUsrDeleted(boolean usrDeleted) {
        this.usrDeleted = usrDeleted;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
