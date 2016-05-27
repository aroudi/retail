package au.com.biztune.retail.domain;

import java.sql.Timestamp;

/**
 * Created by arash on 27/05/2016.
 */
public class DeliveryNoteHeader {
    private long id;
    private long orguId;
    private String delnNoteNumber;
    private String delnGrn;
    private long pohId;
    private String pohOrderNumber;
    private Supplier supplier;
    private Timestamp delnDeliveryDate;
    private Timestamp delnCreatedDate;
    private long delnLastModifiedBy;
    private Timestamp delnLastModifiedDate;
    private double delnValueGross;
    private double delnValueNett;
    private double pohQtyOrdered;
    private double delnTotalQty;
    private int delnTotalLines;
    private ConfigCategory delnType;
    private ConfigCategory delnStatus;
    private LegalTender legalTender;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getOrguId() {
        return orguId;
    }

    public void setOrguId(long orguId) {
        this.orguId = orguId;
    }

    public String getDelnNoteNumber() {
        return delnNoteNumber;
    }

    public void setDelnNoteNumber(String delnNoteNumber) {
        this.delnNoteNumber = delnNoteNumber;
    }

    public String getDelnGrn() {
        return delnGrn;
    }

    public void setDelnGrn(String delnGrn) {
        this.delnGrn = delnGrn;
    }

    public long getPohId() {
        return pohId;
    }

    public void setPohId(long pohId) {
        this.pohId = pohId;
    }

    public String getPohOrderNumber() {
        return pohOrderNumber;
    }

    public void setPohOrderNumber(String pohOrderNumber) {
        this.pohOrderNumber = pohOrderNumber;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public Timestamp getDelnDeliveryDate() {
        return delnDeliveryDate;
    }

    public void setDelnDeliveryDate(Timestamp delnDeliveryDate) {
        this.delnDeliveryDate = delnDeliveryDate;
    }

    public Timestamp getDelnCreatedDate() {
        return delnCreatedDate;
    }

    public void setDelnCreatedDate(Timestamp delnCreatedDate) {
        this.delnCreatedDate = delnCreatedDate;
    }

    public long getDelnLastModifiedBy() {
        return delnLastModifiedBy;
    }

    public void setDelnLastModifiedBy(long delnLastModifiedBy) {
        this.delnLastModifiedBy = delnLastModifiedBy;
    }

    public Timestamp getDelnLastModifiedDate() {
        return delnLastModifiedDate;
    }

    public void setDelnLastModifiedDate(Timestamp delnLastModifiedDate) {
        this.delnLastModifiedDate = delnLastModifiedDate;
    }

    public double getDelnValueGross() {
        return delnValueGross;
    }

    public void setDelnValueGross(double delnValueGross) {
        this.delnValueGross = delnValueGross;
    }

    public double getDelnValueNett() {
        return delnValueNett;
    }

    public void setDelnValueNett(double delnValueNett) {
        this.delnValueNett = delnValueNett;
    }

    public double getPohQtyOrdered() {
        return pohQtyOrdered;
    }

    public void setPohQtyOrdered(double pohQtyOrdered) {
        this.pohQtyOrdered = pohQtyOrdered;
    }

    public double getDelnTotalQty() {
        return delnTotalQty;
    }

    public void setDelnTotalQty(double delnTotalQty) {
        this.delnTotalQty = delnTotalQty;
    }

    public int getDelnTotalLines() {
        return delnTotalLines;
    }

    public void setDelnTotalLines(int delnTotalLines) {
        this.delnTotalLines = delnTotalLines;
    }

    public ConfigCategory getDelnType() {
        return delnType;
    }

    public void setDelnType(ConfigCategory delnType) {
        this.delnType = delnType;
    }

    public ConfigCategory getDelnStatus() {
        return delnStatus;
    }

    public void setDelnStatus(ConfigCategory delnStatus) {
        this.delnStatus = delnStatus;
    }

    public LegalTender getLegalTender() {
        return legalTender;
    }

    public void setLegalTender(LegalTender legalTender) {
        this.legalTender = legalTender;
    }
}
