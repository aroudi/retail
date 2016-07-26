package au.com.biztune.retail.util.queuemanager;

/**
 * Created by arash on 6/01/2016.
 */
public class Request {
    private int attempt = 0;
    private Processor processor;

    public int getAttempt() {
        return attempt;
    }

    public void addAttempt() {
        if (attempt < 0) {
            attempt = 0;
        }
        attempt++;
    }

    public Processor getProcessor() {
        return processor;
    }

    public void setProcessor(Processor processor) {
        this.processor = processor;
    }
}
