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
    private long statusId;
    //use for purchase order
    private long creationTypeId;
    private String searchRange;
    private String generatedBy;
    private String projectCode;
    private long pageNo;
    private long pageSize;
    private long totalRecords;
    private List result;
    private long supplierId;
    private boolean imported;
    private String expDateTo;



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

    public long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(long supplierId) {
        this.supplierId = supplierId;
    }

    public String getSearchRange() {
        return searchRange;
    }

    public void setSearchRange(String searchRange) {
        this.searchRange = searchRange;
    }

    public String getGeneratedBy() {
        return generatedBy;
    }

    public void setGeneratedBy(String generatedBy) {
        this.generatedBy = generatedBy;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public long getPageNo() {
        return pageNo;
    }

    public void setPageNo(long pageNo) {
        this.pageNo = pageNo;
    }

    public long getPageSize() {
        return pageSize;
    }

    public void setPageSize(long pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(long totalRecords) {
        this.totalRecords = totalRecords;
    }

    public List getResult() {
        return result;
    }

    public void setResult(List result) {
        this.result = result;
    }

    public long getStatusId() {
        return statusId;
    }

    public void setStatusId(long statusId) {
        this.statusId = statusId;
    }

    public long getCreationTypeId() {
        return creationTypeId;
    }

    public void setCreationTypeId(long creationTypeId) {
        this.creationTypeId = creationTypeId;
    }

    public boolean isImported() {
        return imported;
    }

    public void setImported(boolean imported) {
        this.imported = imported;
    }

    public String getExpDateTo() {
        return expDateTo;
    }

    public void setExpDateTo(String expDateTo) {
        this.expDateTo = expDateTo;
    }
}
