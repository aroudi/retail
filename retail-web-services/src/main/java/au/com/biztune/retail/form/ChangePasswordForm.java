package au.com.biztune.retail.form;

/**
 * Created by arash on 4/08/2016.
 */
public class ChangePasswordForm {
    private String oldPassword;
    private String password;
    private int userId;

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
