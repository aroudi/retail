package au.com.biztune.retail.domain;

import java.sql.Timestamp;

/**
 * Created by arash on 18/03/2016.
 */
public class Price {
    private long id;
    private long prodId;
    private PriceCode priceCode;
    private PriceBand priceBand;
    private double margin;
    private double prcePrice;
    private Timestamp prceCreated;
    private Timestamp prceFromDate;
    private Timestamp prceToDate;
    private boolean prceSetCentral;
    private Timestamp prceModified;
    private boolean prceTaxIncluded;
    private UnitOfMeasure unitOfMeasure;
    private double unomQty;
    private boolean prceReduced;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getProdId() {
        return prodId;
    }

    public void setProdId(long prodId) {
        this.prodId = prodId;
    }

    public PriceCode getPriceCode() {
        return priceCode;
    }

    public void setPriceCode(PriceCode priceCode) {
        this.priceCode = priceCode;
    }

    public PriceBand getPriceBand() {
        return priceBand;
    }

    public void setPriceBand(PriceBand priceBand) {
        this.priceBand = priceBand;
    }

    public double getMargin() {
        return margin;
    }

    public void setMargin(double margin) {
        this.margin = margin;
    }

    public double getPrcePrice() {
        return prcePrice;
    }

    public void setPrcePrice(double prcePrice) {
        this.prcePrice = prcePrice;
    }

    public Timestamp getPrceCreated() {
        return prceCreated;
    }

    public void setPrceCreated(Timestamp prceCreated) {
        this.prceCreated = prceCreated;
    }

    public Timestamp getPrceModified() {
        return prceModified;
    }

    public void setPrceModified(Timestamp prceModified) {
        this.prceModified = prceModified;
    }

    public boolean isPrceTaxIncluded() {
        return prceTaxIncluded;
    }

    public void setPrceTaxIncluded(boolean prceTaxIncluded) {
        this.prceTaxIncluded = prceTaxIncluded;
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

    public boolean isPrceReduced() {
        return prceReduced;
    }

    public void setPrceReduced(boolean prceReduced) {
        this.prceReduced = prceReduced;
    }

    public Timestamp getPrceFromDate() {
        return prceFromDate;
    }

    public void setPrceFromDate(Timestamp prceFromDate) {
        this.prceFromDate = prceFromDate;
    }

    public Timestamp getPrceToDate() {
        return prceToDate;
    }

    public void setPrceToDate(Timestamp prceToDate) {
        this.prceToDate = prceToDate;
    }

    public boolean isPrceSetCentral() {
        return prceSetCentral;
    }

    public void setPrceSetCentral(boolean prceSetCentral) {
        this.prceSetCentral = prceSetCentral;
    }
}
