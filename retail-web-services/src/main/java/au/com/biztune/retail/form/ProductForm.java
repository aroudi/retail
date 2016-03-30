package au.com.biztune.retail.form;

import au.com.biztune.retail.domain.*;

import java.util.List;

/**
 * Created by arash on 29/03/2016.
 */
public class ProductForm {
    private long prodId;
    private long prouId;
    private String prodSku;
    private String reference;
    private String prodName;
    private ConfigCategory status;
    private String prodDesc;
    private String prodBrand;
    private String prodClass;
    private ConfigCategory prodType;
    private List<TaxRule> taxRules;
    private List<SuppProdPrice> suppProdPrices;
    private UnitOfMeasure prceUnitOfMeasure;
    private double prceUnomQty;
    private double prcePrice;
    private double prceMargin;
    private boolean prceTaxIncluded;

    public long getProdId() {
        return prodId;
    }

    public void setProdId(long prodId) {
        this.prodId = prodId;
    }

    public long getProuId() {
        return prouId;
    }

    public void setProuId(long prouId) {
        this.prouId = prouId;
    }

    public String getProdSku() {
        return prodSku;
    }

    public void setProdSku(String prodSku) {
        this.prodSku = prodSku;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public ConfigCategory getStatus() {
        return status;
    }

    public void setStatus(ConfigCategory status) {
        this.status = status;
    }

    public String getProdDesc() {
        return prodDesc;
    }

    public void setProdDesc(String prodDesc) {
        this.prodDesc = prodDesc;
    }

    public String getProdBrand() {
        return prodBrand;
    }

    public void setProdBrand(String prodBrand) {
        this.prodBrand = prodBrand;
    }

    public String getProdClass() {
        return prodClass;
    }

    public void setProdClass(String prodClass) {
        this.prodClass = prodClass;
    }

    public ConfigCategory getProdType() {
        return prodType;
    }

    public void setProdType(ConfigCategory prodType) {
        this.prodType = prodType;
    }

    public List<TaxRule> getTaxRules() {
        return taxRules;
    }

    public void setTaxRules(List<TaxRule> taxRules) {
        this.taxRules = taxRules;
    }

    public List<SuppProdPrice> getSuppProdPrices() {
        return suppProdPrices;
    }

    public void setSuppProdPrices(List<SuppProdPrice> suppProdPrices) {
        this.suppProdPrices = suppProdPrices;
    }

    public UnitOfMeasure getPrceUnitOfMeasure() {
        return prceUnitOfMeasure;
    }

    public void setPrceUnitOfMeasure(UnitOfMeasure prceUnitOfMeasure) {
        this.prceUnitOfMeasure = prceUnitOfMeasure;
    }

    public double getPrceUnomQty() {
        return prceUnomQty;
    }

    public void setPrceUnomQty(double prceUnomQty) {
        this.prceUnomQty = prceUnomQty;
    }

    public double getPrcePrice() {
        return prcePrice;
    }

    public void setPrcePrice(double prcePrice) {
        this.prcePrice = prcePrice;
    }

    public double getPrceMargin() {
        return prceMargin;
    }

    public void setPrceMargin(double prceMargin) {
        this.prceMargin = prceMargin;
    }

    public boolean isPrceTaxIncluded() {
        return prceTaxIncluded;
    }

    public void setPrceTaxIncluded(boolean prceTaxIncluded) {
        this.prceTaxIncluded = prceTaxIncluded;
    }
}

