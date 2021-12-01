package au.com.biztune.retail.domain;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by akhoshraft on 12/03/2016.
 */
public class Product {
    private long id;
    private String prodSku;
    private String prodBarcode;
    private long orguIdOwning;
    private String prodName;
    private String prodDesc;
    private String reference;
    private boolean prodOwnBrand;
    private String prodUrl;
    private Timestamp prodCreated;
    private Timestamp prodModified;
    private String prodReceiptDesc;
    private String prodWarrantyTerm;
    private String prodBrand;
    private String prodImageDesc;
    private String prodUrlThumb;
    private String prodClass;
    private String prodWarrantyText;
    private ConfigCategory prodType;
    private ProdOrguLink prodOrguLink;
    private List<Price> priceList;
    private List<SuppProdPrice> suppProdPriceList;
    private Double stockQty;
    private Double reservedQty;
    private Price sellPrice;
    private String prodLocation;
    private boolean deleted;
    //set to 0 when importing by BOQ. after BOQ verified set it to 1
    private boolean verified;
    private List<ProdDeptCat> productGroups;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getProdSku() {
        return prodSku;
    }

    public void setProdSku(String prodSku) {
        this.prodSku = prodSku;
    }

    public long getOrguIdOwning() {
        return orguIdOwning;
    }

    public void setOrguIdOwning(long orguIdOwning) {
        this.orguIdOwning = orguIdOwning;
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

    public boolean isProdOwnBrand() {
        return prodOwnBrand;
    }

    public void setProdOwnBrand(boolean prodOwnBrand) {
        this.prodOwnBrand = prodOwnBrand;
    }

    public String getProdUrl() {
        return prodUrl;
    }

    public void setProdUrl(String prodUrl) {
        this.prodUrl = prodUrl;
    }

    public Timestamp getProdCreated() {
        return prodCreated;
    }

    public void setProdCreated(Timestamp prodCreated) {
        this.prodCreated = prodCreated;
    }

    public Timestamp getProdModified() {
        return prodModified;
    }

    public void setProdModified(Timestamp prodModified) {
        this.prodModified = prodModified;
    }

    public String getProdReceiptDesc() {
        return prodReceiptDesc;
    }

    public void setProdReceiptDesc(String prodReceiptDesc) {
        this.prodReceiptDesc = prodReceiptDesc;
    }

    public String getProdWarrantyTerm() {
        return prodWarrantyTerm;
    }

    public void setProdWarrantyTerm(String prodWarrantyTerm) {
        this.prodWarrantyTerm = prodWarrantyTerm;
    }

    public String getProdBrand() {
        return prodBrand;
    }

    public void setProdBrand(String prodBrand) {
        this.prodBrand = prodBrand;
    }

    public String getProdImageDesc() {
        return prodImageDesc;
    }

    public void setProdImageDesc(String prodImageDesc) {
        this.prodImageDesc = prodImageDesc;
    }

    public String getProdUrlThumb() {
        return prodUrlThumb;
    }

    public void setProdUrlThumb(String prodUrlThumb) {
        this.prodUrlThumb = prodUrlThumb;
    }

    public String getProdClass() {
        return prodClass;
    }

    public void setProdClass(String prodClass) {
        this.prodClass = prodClass;
    }

    public String getProdWarrantyText() {
        return prodWarrantyText;
    }

    public void setProdWarrantyText(String prodWarrantyText) {
        this.prodWarrantyText = prodWarrantyText;
    }

    public ConfigCategory getProdType() {
        return prodType;
    }

    public void setProdType(ConfigCategory prodType) {
        this.prodType = prodType;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public ProdOrguLink getProdOrguLink() {
        return prodOrguLink;
    }

    public void setProdOrguLink(ProdOrguLink prodOrguLink) {
        this.prodOrguLink = prodOrguLink;
    }

    public List<Price> getPriceList() {
        return priceList;
    }

    public void setPriceList(List<Price> priceList) {
        this.priceList = priceList;
    }

    public List<SuppProdPrice> getSuppProdPriceList() {
        return suppProdPriceList;
    }

    public void setSuppProdPriceList(List<SuppProdPrice> suppProdPriceList) {
        this.suppProdPriceList = suppProdPriceList;
    }

    public Double getReservedQty() {
        return reservedQty;
    }

    public void setReservedQty(Double reservedQty) {
        this.reservedQty = reservedQty;
    }

    public Price getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(Price sellPrice) {
        this.sellPrice = sellPrice;
    }

    public String getProdLocation() {
        return prodLocation;
    }

    public void setProdLocation(String prodLocation) {
        this.prodLocation = prodLocation;
    }

    public String getProdBarcode() {
        return prodBarcode;
    }

    public void setProdBarcode(String prodBarcode) {
        this.prodBarcode = prodBarcode;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public List<ProdDeptCat> getProductGroups() {
        return productGroups;
    }

    public void setProductGroups(List<ProdDeptCat> productGroups) {
        this.productGroups = productGroups;
    }

    public Double getStockQty() {
        return stockQty;
    }

    public void setStockQty(Double stockQty) {
        this.stockQty = stockQty;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }
}
