package au.com.biztune.retail.domain;

import java.sql.Timestamp;

/**
 * Created by arash on 1/04/2016.
 */
public class BillOfQuantity {
    private long id;
    private Project project;
    private String referenceCode;
    private Timestamp dateCreated;
    private String boqName;
    private String orderNo;
    private String note;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public String getReferenceCode() {
        return referenceCode;
    }

    public void setReferenceCode(String referenceCode) {
        this.referenceCode = referenceCode;
    }

    public Timestamp getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Timestamp dateCreated) {
        this.dateCreated = dateCreated;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
