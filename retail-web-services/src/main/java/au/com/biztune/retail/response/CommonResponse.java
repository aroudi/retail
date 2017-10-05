package au.com.biztune.retail.response;

import java.util.ArrayList;
import java.util.List;

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

    /**
     * list of messages.
     */
    protected List<String> messageList;

    /**
     * add message to message list.
     * @param message message to be displayed.
     */
    public void addMessage(String message) {
        if (messageList == null) {
            messageList = new ArrayList<String>();
        }
        messageList.add(message);
    }

    /**
     * print message list.
     * @return message list.
     */
    public String toString() {
        final StringBuilder result = new StringBuilder("");
        for (String msg: messageList) {
            result.append(msg + "\n");
        }
        return result.toString();
    }
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

    public List<String> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<String> messageList) {
        this.messageList = messageList;
    }
}

