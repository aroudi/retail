package au.com.biztune.retail.util.queuemanager;

/**
 * Created by arash on 6/01/2016.
 */
public interface Processor {
    public Response process(Request item);
}
