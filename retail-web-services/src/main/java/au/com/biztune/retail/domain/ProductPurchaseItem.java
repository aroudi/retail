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
    private double costBeforeTax;
    private double bulkPrice;
    private double bulkPrice2;
    private double bulkPrice3;
    private double bulkPrice4;
    private double bulkPrice5;
    private double bulkPriceBeforeTax;
    private double bulkQty;
    private double bulkQty2;
    private double bulkQty3;
    private double bulkQty4;
    private double bulkQty5;
    private LegalTender legalTender;
    private double sprcMinOrdQty;
    private long sprcLeadTime;
    private double sprcMinOrdVal;
    private TaxLegVariance taxLegVariance;
    private String prodSku;
    private String prodDesc;
    private String prodName;


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

    public String getProdSku() {
        return prodSku;
    }

    public void setProdSku(String prodSku) {
        this.prodSku = prodSku;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public String getProdDesc() {
        return prodDesc;
    }

    public void setProdDesc(String prodDesc) {
        this.prodDesc = prodDesc;
    }

    public double getBulkPrice2() {
        return bulkPrice2;
    }

    public void setBulkPrice2(double bulkPrice2) {
        this.bulkPrice2 = bulkPrice2;
    }

    public double getBulkPrice3() {
        return bulkPrice3;
    }

    public void setBulkPrice3(double bulkPrice3) {
        this.bulkPrice3 = bulkPrice3;
    }

    public double getBulkPrice4() {
        return bulkPrice4;
    }

    public void setBulkPrice4(double bulkPrice4) {
        this.bulkPrice4 = bulkPrice4;
    }

    public double getBulkPrice5() {
        return bulkPrice5;
    }

    public void setBulkPrice5(double bulkPrice5) {
        this.bulkPrice5 = bulkPrice5;
    }

    public double getBulkQty2() {
        return bulkQty2;
    }

    public void setBulkQty2(double bulkQty2) {
        this.bulkQty2 = bulkQty2;
    }

    public double getBulkQty3() {
        return bulkQty3;
    }

    public void setBulkQty3(double bulkQty3) {
        this.bulkQty3 = bulkQty3;
    }

    public double getBulkQty4() {
        return bulkQty4;
    }

    public void setBulkQty4(double bulkQty4) {
        this.bulkQty4 = bulkQty4;
    }

    public double getBulkQty5() {
        return bulkQty5;
    }

    public void setBulkQty5(double bulkQty5) {
        this.bulkQty5 = bulkQty5;
    }
}
