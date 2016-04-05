package au.com.biztune.retail.response;

/**
 * Created by arash on 22/02/2016.
 */
public class CommonResponse {
    /**
     * status code.
     */
    protected int status;
    /**
     * message.
     */
    protected String message;
    /**
     * extra information.
     */
    protected String info;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}

