package au.com.biztune.retail.processor;

import au.com.biztune.retail.util.queuemanager.Request;

import java.io.ByteArrayOutputStream;

/**
 * Created by arash on 3/03/2017.
 */
public class EmailRequest extends Request {
    private String toAddress;
    private String subject;
    private String message;
    private ByteArrayOutputStream attachFileAsStream;
    private String attachFileName;
    private boolean sendAsStream;

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ByteArrayOutputStream getAttachFileAsStream() {
        return attachFileAsStream;
    }

    public void setAttachFileAsStream(ByteArrayOutputStream attachFileAsStream) {
        this.attachFileAsStream = attachFileAsStream;
    }

    public String getAttachFileName() {
        return attachFileName;
    }

    public void setAttachFileName(String attachFileName) {
        this.attachFileName = attachFileName;
    }

    public boolean isSendAsStream() {
        return sendAsStream;
    }

    public void setSendAsStream(boolean sendAsStream) {
        this.sendAsStream = sendAsStream;
    }
}
