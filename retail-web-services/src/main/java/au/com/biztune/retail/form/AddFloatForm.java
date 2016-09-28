package au.com.biztune.retail.form;

import au.com.biztune.retail.domain.CashSession;
import au.com.biztune.retail.domain.ConfigCategory;

/**
 * Created by arash on 28/09/2016.
 */
public class AddFloatForm {
    private CashSession cashSession;
    private ConfigCategory eventType;
    private double amount;
    private String comment;

    public CashSession getCashSession() {
        return cashSession;
    }

    public void setCashSession(CashSession cashSession) {
        this.cashSession = cashSession;
    }

    public ConfigCategory getEventType() {
        return eventType;
    }

    public void setEventType(ConfigCategory eventType) {
        this.eventType = eventType;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
