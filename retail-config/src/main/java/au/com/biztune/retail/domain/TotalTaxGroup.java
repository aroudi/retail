package au.com.biztune.retail.domain;

import java.sql.Timestamp;

/**
 * Created by arash on 26/09/2016.
 */
public class TotalTaxGroup {
    private long id;
    private long orguId;
    private long storeId;
    private TaxLegVariance taxLegVariance;
    private Timestamp totgTradingDate;
    private String totgTxgpCode;
    private double totgTaxedValue;
    private double totgTax;
    private double totgTotSalesQty;

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

    public long getStoreId() {
        return storeId;
    }

    public void setStoreId(long storeId) {
        this.storeId = storeId;
    }

    public TaxLegVariance getTaxLegVariance() {
        return taxLegVariance;
    }

    public void setTaxLegVariance(TaxLegVariance taxLegVariance) {
        this.taxLegVariance = taxLegVariance;
    }

    public Timestamp getTotgTradingDate() {
        return totgTradingDate;
    }

    public void setTotgTradingDate(Timestamp totgTradingDate) {
        this.totgTradingDate = totgTradingDate;
    }

    public String getTotgTxgpCode() {
        return totgTxgpCode;
    }

    public void setTotgTxgpCode(String totgTxgpCode) {
        this.totgTxgpCode = totgTxgpCode;
    }

    public double getTotgTaxedValue() {
        return totgTaxedValue;
    }

    public void setTotgTaxedValue(double totgTaxedValue) {
        this.totgTaxedValue = totgTaxedValue;
    }

    public double getTotgTax() {
        return totgTax;
    }

    public void setTotgTax(double totgTax) {
        this.totgTax = totgTax;
    }

    public double getTotgTotSalesQty() {
        return totgTotSalesQty;
    }

    public void setTotgTotSalesQty(double totgTotSalesQty) {
        this.totgTotSalesQty = totgTotSalesQty;
    }
}
