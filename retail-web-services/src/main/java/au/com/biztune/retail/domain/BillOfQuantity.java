package au.com.biztune.retail.domain;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by arash on 1/04/2016.
 */
public class BillOfQuantity {
    private long id;
    private long orguId;
    private Project project;
    private String referenceCode;
    private Timestamp dateCreated;
    private String boqName;
    private String orderNo;
    private String note;
    private ConfigCategory boqStatus;
    private Timestamp boqLastModifiedDate;
    private long boqLastModifiedBy;
    private double boqValueGross;
    private double boqValueNett;
    private double boqTotalQty;
    private int boqTotalLines;
    private List<BoqDetail> lines;
    private AppUser user;

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

    public long getOrguId() {
        return orguId;
    }

    public void setOrguId(long orguId) {
        this.orguId = orguId;
    }

    public ConfigCategory getBoqStatus() {
        return boqStatus;
    }

    public void setBoqStatus(ConfigCategory boqStatus) {
        this.boqStatus = boqStatus;
    }

    public Timestamp getBoqLastModifiedDate() {
        return boqLastModifiedDate;
    }

    public void setBoqLastModifiedDate(Timestamp boqLastModifiedDate) {
        this.boqLastModifiedDate = boqLastModifiedDate;
    }

    public long getBoqLastModifiedBy() {
        return boqLastModifiedBy;
    }

    public void setBoqLastModifiedBy(long boqLastModifiedBy) {
        this.boqLastModifiedBy = boqLastModifiedBy;
    }

    public double getBoqValueGross() {
        return boqValueGross;
    }

    public void setBoqValueGross(double boqValueGross) {
        this.boqValueGross = boqValueGross;
    }

    public double getBoqValueNett() {
        return boqValueNett;
    }

    public void setBoqValueNett(double boqValueNett) {
        this.boqValueNett = boqValueNett;
    }

    public double getBoqTotalQty() {
        return boqTotalQty;
    }

    public void setBoqTotalQty(double boqTotalQty) {
        this.boqTotalQty = boqTotalQty;
    }

    public int getBoqTotalLines() {
        return boqTotalLines;
    }

    public void setBoqTotalLines(int boqTotalLines) {
        this.boqTotalLines = boqTotalLines;
    }

    public List<BoqDetail> getLines() {
        return lines;
    }

    public void setLines(List<BoqDetail> lines) {
        this.lines = lines;
    }

    public AppUser getUser() {
        return user;
    }

    public void setUser(AppUser user) {
        this.user = user;
    }
}
