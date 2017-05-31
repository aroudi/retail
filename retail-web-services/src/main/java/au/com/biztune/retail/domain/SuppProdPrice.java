package au.com.biztune.retail.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;

import java.sql.Timestamp;

/**
 * Created by arash on 22/03/2016.
 */
public class SuppProdPrice {
    private long id;
    private long solId;
    private long prodId;
    private String catalogueNo;
    private String partNo;
    private UnitOfMeasure unitOfMeasure;
    private UnitOfMeasure unitOfMeasureContent;
    private double unomQty;
    private double price;
    private double costBeforeTax;
    private double bulkPrice;
    private double bulkPriceBeforeTax;
    private double bulkQty;
    @JsonIgnore
    private LegalTender legalTender;
    @JsonIgnore
    private double sprcMinOrdQty;
    @JsonIgnore
    private long sprcLeadTime;
    @JsonIgnore
    private Timestamp sprcCreated;
    @JsonIgnore
    private Timestamp sprcModified;
    private boolean sprcPrefferBuy;
    @JsonIgnore
    private double sprcMinOrdVal;
    private Supplier supplier;
    private boolean deleted;
    private TaxLegVariance taxLegVariance;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getSolId() {
        return solId;
    }

    public void setSolId(long solId) {
        this.solId = solId;
    }

    public long getProdId() {
        return prodId;
    }

    public void setProdId(long prodId) {
        this.prodId = prodId;
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

    public UnitOfMeasure getUnitOfMeasure() {
        return unitOfMeasure;
    }

    public void setUnitOfMeasure(UnitOfMeasure unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }

    public double getUnomQty() {
        return unomQty;
    }

    public void setUnomQty(double unomQty) {
        this.unomQty = unomQty;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getBulkPrice() {
        return bulkPrice;
    }

    public void setBulkPrice(double bulkPrice) {
        this.bulkPrice = bulkPrice;
    }

    public double getBulkQty() {
        return bulkQty;
    }

    public void setBulkQty(double bulkQty) {
        this.bulkQty = bulkQty;
    }

    public LegalTender getLegalTender() {
        return legalTender;
    }

    public void setLegalTender(LegalTender legalTender) {
        this.legalTender = legalTender;
    }

    public double getSprcMinOrdQty() {
        return sprcMinOrdQty;
    }

    public void setSprcMinOrdQty(double sprcMinOrdQty) {
        this.sprcMinOrdQty = sprcMinOrdQty;
    }

    public long getSprcLeadTime() {
        return sprcLeadTime;
    }

    public void setSprcLeadTime(long sprcLeadTime) {
        this.sprcLeadTime = sprcLeadTime;
    }

    public Timestamp getSprcCreated() {
        return sprcCreated;
    }

    public void setSprcCreated(Timestamp sprcCreated) {
        this.sprcCreated = sprcCreated;
    }

    public Timestamp getSprcModified() {
        return sprcModified;
    }

    public void setSprcModified(Timestamp sprcModified) {
        this.sprcModified = sprcModified;
    }

    public boolean isSprcPrefferBuy() {
        return sprcPrefferBuy;
    }

    public void setSprcPrefferBuy(boolean sprcPrefferBuy) {
        this.sprcPrefferBuy = sprcPrefferBuy;
    }

    public double getSprcMinOrdVal() {
        return sprcMinOrdVal;
    }

    public void setSprcMinOrdVal(double sprcMinOrdVal) {
        this.sprcMinOrdVal = sprcMinOrdVal;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public UnitOfMeasure getUnitOfMeasureContent() {
        return unitOfMeasureContent;
    }

    public void setUnitOfMeasureContent(UnitOfMeasure unitOfMeasureContent) {
        this.unitOfMeasureContent = unitOfMeasureContent;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public double getCostBeforeTax() {
        return costBeforeTax;
    }

    public void setCostBeforeTax(double costBeforeTax) {
        this.costBeforeTax = costBeforeTax;
    }

    public double getBulkPriceBeforeTax() {
        return bulkPriceBeforeTax;
    }

    public void setBulkPriceBeforeTax(double bulkPriceBeforeTax) {
        this.bulkPriceBeforeTax = bulkPriceBeforeTax;
    }

    public TaxLegVariance getTaxLegVariance() {
        return taxLegVariance;
    }

    public void setTaxLegVariance(TaxLegVariance taxLegVariance) {
        this.taxLegVariance = taxLegVariance;
    }
}
