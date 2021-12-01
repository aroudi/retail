package au.com.biztune.retail.domain;

import java.sql.Timestamp;

/**
 * created by arash on 29 /08/2018.
 */
public class ReportGoodReceivedRow {
    private long delnId;
    private String delnNoteNo;
    private String supplierName;
    private Timestamp delnCreatedDate;
    private Timestamp delnDeliveryDate;
    private double delnValueGross;
    private double delnValueNett;
    private String freightTaxCode;
    private double freightTax;
    private double freightAmount;
    private double delnSurcharge;
    private String catalogueNo;
    private String partNo;
    private double dlnlUnitCost;
    private double dlnlQty;
    private String taxCode;
    private double dlnlValueTax;
    private double dlnlValueGross;
    private double totalCost;

    public long getDelnId() {
        return delnId;
    }

    public void setDelnId(long delnId) {
        this.delnId = delnId;
    }

    public String getDelnNoteNo() {
        return delnNoteNo;
    }

    public void setDelnNoteNo(String delnNoteNo) {
        this.delnNoteNo = delnNoteNo;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public Timestamp getDelnCreatedDate() {
        return delnCreatedDate;
    }

    public void setDelnCreatedDate(Timestamp delnCreatedDate) {
        this.delnCreatedDate = delnCreatedDate;
    }

    public Timestamp getDelnDeliveryDate() {
        return delnDeliveryDate;
    }

    public void setDelnDeliveryDate(Timestamp delnDeliveryDate) {
        this.delnDeliveryDate = delnDeliveryDate;
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

    public String getFreightTaxCode() {
        return freightTaxCode;
    }

    public void setFreightTaxCode(String freightTaxCode) {
        this.freightTaxCode = freightTaxCode;
    }

    public double getFreightTax() {
        return freightTax;
    }

    public void setFreightTax(double freightTax) {
        this.freightTax = freightTax;
    }

    public double getFreightAmount() {
        return freightAmount;
    }

    public void setFreightAmount(double freightAmount) {
        this.freightAmount = freightAmount;
    }

    public double getDelnSurcharge() {
        return delnSurcharge;
    }

    public void setDelnSurcharge(double delnSurcharge) {
        this.delnSurcharge = delnSurcharge;
    }

    public String getCatalogueNo() {
        return catalogueNo;
    }

    public void setCatalogueNo(String catalogueNo) {
        this.catalogueNo = catalogueNo;
    }

    public String getPartNo() {
        return partNo;
    }

    public void setPartNo(String partNo) {
        this.partNo = partNo;
    }

    public double getDlnlUnitCost() {
        return dlnlUnitCost;
    }

    public void setDlnlUnitCost(double dlnlUnitCost) {
        this.dlnlUnitCost = dlnlUnitCost;
    }

    public double getDlnlQty() {
        return dlnlQty;
    }

    public void setDlnlQty(double dlnlQty) {
        this.dlnlQty = dlnlQty;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public double getDlnlValueTax() {
        return dlnlValueTax;
    }

    public void setDlnlValueTax(double dlnlValueTax) {
        this.dlnlValueTax = dlnlValueTax;
    }

    public double getDlnlValueGross() {
        return dlnlValueGross;
    }

    public void setDlnlValueGross(double dlnlValueGross) {
        this.dlnlValueGross = dlnlValueGross;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }
}
