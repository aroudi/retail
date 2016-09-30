package au.com.biztune.retail.form;

import au.com.biztune.retail.domain.CashSession;
import au.com.biztune.retail.domain.SessionEventDetail;

import java.util.List;

/**
 * Created by arash on 28/09/2016.
 */
public class ReconciliationForm {
    private CashSession cashSession;
    private String comment;
    private List<SessionEventDetail> sessionEventDetailList;

    public CashSession getCashSession() {
        return cashSession;
    }

    public void setCashSession(CashSession cashSession) {
        this.cashSession = cashSession;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<SessionEventDetail> getSessionEventDetailList() {
        return sessionEventDetailList;
    }

    public void setSessionEventDetailList(List<SessionEventDetail> sessionEventDetailList) {
        this.sessionEventDetailList = sessionEventDetailList;
    }
}
