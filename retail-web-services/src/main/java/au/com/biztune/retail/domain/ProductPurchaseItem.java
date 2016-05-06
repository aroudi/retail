package au.com.biztune.retail.domain;

/**
 * Created by arash on 22/03/2016.
 */
public class ProductPurchaseItem {
    private long id;
    private long solId;
    private long prodId;
    private String catalogueNo;
    private String partNo;
    private UnitOfMeasure unitOfMeasure;
    private UnitOfMeasure unitOfMeasureContent;
    private double unomQty;
    private double price;
    private double bulkPrice;
    private double bulkQty;
    private LegalTender legalTender;
    private double sprcMinOrdQty;
    private long sprcLeadTime;
    private double sprcMinOrdVal;

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

    public UnitOfMeasure getUnitOfMeasureContent() {
        return unitOfMeasureContent;
    }

    public void setUnitOfMeasureContent(UnitOfMeasure unitOfMeasureContent) {
        this.unitOfMeasureContent = unitOfMeasureContent;
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

    public double getSprcMinOrdVal() {
        return sprcMinOrdVal;
    }

    public void setSprcMinOrdVal(double sprcMinOrdVal) {
        this.sprcMinOrdVal = sprcMinOrdVal;
    }
}
