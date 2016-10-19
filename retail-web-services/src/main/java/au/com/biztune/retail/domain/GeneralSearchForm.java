package au.com.biztune.retail.domain;

import java.util.List;

/**
 * Created by arash on 18/10/2016.
 */
public class GeneralSearchForm {
    private String dateFrom;
    private String dateTo;
    private String noFrom;
    private String noTo;
    private List<Long> txnTypeList;
    private long clientId;

    public String getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    public String getDateTo() {
        return dateTo;
    }

    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }

    public String getNoFrom() {
        return noFrom;
    }

    public void setNoFrom(String noFrom) {
        this.noFrom = noFrom;
    }

    public String getNoTo() {
        return noTo;
    }

    public void setNoTo(String noTo) {
        this.noTo = noTo;
    }

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public List<Long> getTxnTypeList() {
        return txnTypeList;
    }

    public void setTxnTypeList(List<Long> txnTypeList) {
        this.txnTypeList = txnTypeList;
    }
}
