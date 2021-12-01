package au.com.biztune.retail.domain;

import java.util.List;

/**
 * Created by akhoshraft on 12/03/2016.
 */
public class ProdOrguLink {
    private long id;
    private long prodId;
    private long orguId;
    private boolean prouRefundable;
    private boolean prouDiscountable;
    private boolean prouCanQtySale;
    private boolean prouForceQty;
    private ConfigCategory status;
    private int prouRefundDays;
    private List<TaxRule> taxRules;


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

    public long getOrguId() {
        return orguId;
    }

    public void setOrguId(long orguId) {
        this.orguId = orguId;
    }

    public boolean isProuRefundable() {
        return prouRefundable;
    }

    public void setProuRefundable(boolean prouRefundable) {
        this.prouRefundable = prouRefundable;
    }

    public boolean isProuDiscountable() {
        return prouDiscountable;
    }

    public void setProuDiscountable(boolean prouDiscountable) {
        this.prouDiscountable = prouDiscountable;
    }

    public boolean isProuCanQtySale() {
        return prouCanQtySale;
    }

    public void setProuCanQtySale(boolean prouCanQtySale) {
        this.prouCanQtySale = prouCanQtySale;
    }

    public boolean isProuForceQty() {
        return prouForceQty;
    }

    public void setProuForceQty(boolean prouForceQty) {
        this.prouForceQty = prouForceQty;
    }

    public ConfigCategory getStatus() {
        return status;
    }

    public void setStatus(ConfigCategory status) {
        this.status = status;
    }

    public int getProuRefundDays() {
        return prouRefundDays;
    }

    public void setProuRefundDays(int prouRefundDays) {
        this.prouRefundDays = prouRefundDays;
    }
    public List<TaxRule> getTaxRules() {
        return taxRules;
    }

    public void setTaxRules(List<TaxRule> taxRules) {
        this.taxRules = taxRules;
    }

}
