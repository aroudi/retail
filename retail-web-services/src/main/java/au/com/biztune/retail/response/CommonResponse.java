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
}

