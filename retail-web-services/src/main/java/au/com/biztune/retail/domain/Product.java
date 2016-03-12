package au.com.biztune.retail.domain;

import java.security.Timestamp;

/**
 * Created by akhoshraft on 12/03/2016.
 */
public class Product {
    private long id;
    private String prodSku;
    private long orguIdOwning;
    private String prodName;
    private String prodDesc;
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
}
