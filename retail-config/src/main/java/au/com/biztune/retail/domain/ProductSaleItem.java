package au.com.biztune.retail.domain;

import java.sql.Timestamp;

/**
 * Created by arash on 19/04/2016.
 */
public class ProductSaleItem {
    private long id;
    private String prodSku;
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
    private Price sellPrice;
    private Price costPrice;
    private Double stockQty;
    private ProdOrguLink prodOrguLink;
    private String prodBarcode;

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

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
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

    public Price getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(Price sellPrice) {
        this.sellPrice = sellPrice;
    }

    public ProdOrguLink getProdOrguLink() {
        return prodOrguLink;
    }

    public void setProdOrguLink(ProdOrguLink prodOrguLink) {
        this.prodOrguLink = prodOrguLink;
    }

    public Price getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(Price costPrice) {
        this.costPrice = costPrice;
    }

    public String getProdBarcode() {
        return prodBarcode;
    }

    public void setProdBarcode(String prodBarcode) {
        this.prodBarcode = prodBarcode;
    }

    public Double getStockQty() {
        return stockQty;
    }

    public void setStockQty(Double stockQty) {
        this.stockQty = stockQty;
    }
}
