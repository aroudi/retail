package au.com.biztune.retail.util.queuemanager;

/**
 * Created by arash on 6/01/2016.
 */
public class Response {
    private boolean succeeded;
    private String message;

    public boolean isSucceeded() {
        return succeeded;
    }

    public void setSucceeded(boolean succeeded) {
        this.succeeded = succeeded;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
