package au.com.biztune.retail.domain;

/**
 * Created by arash on 22/06/2016.
 */
public class UserAccess {
    private long id;
    private long usrId;
    private long acptId;

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

    public long getAcptId() {
        return acptId;
    }

    public void setAcptId(long acptId) {
        this.acptId = acptId;
    }
}
