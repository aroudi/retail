package au.com.biztune.retail.form;

import au.com.biztune.retail.domain.GeneralSearchForm;

/**
 * Created by arash on 31/01/2017.
 */
public class BoqSearchForm extends GeneralSearchForm {
    private long projectId;
    private long statusId;
    private String referenceCode;
    private String boqName;
    private String orderNo;

    public long getProjectId() {
        return projectId;
    }

    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }

    public long getStatusId() {
        return statusId;
    }

    public void setStatusId(long statusId) {
        this.statusId = statusId;
    }

    public String getReferenceCode() {
        return referenceCode;
    }

    public void setReferenceCode(String referenceCode) {
        this.referenceCode = referenceCode;
    }

    public String getBoqName() {
        return boqName;
    }

    public void setBoqName(String boqName) {
        this.boqName = boqName;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
}
