package au.com.biztune.retail.domain;

import java.sql.Timestamp;

/**
 * Created by arash on 26/09/2016.
 */
public class TotalSaleOperator {
    private long id;
    private AppUser toopOperator;
    private ConfigCategory toopTxnType;
    private long orguId;
    private long storeId;
    private Timestamp toopTradingDate;
    private double toopSaleQty;
    private double toopItemsValue;
    private double toopProfitValue;
    private double toopTaxedValue;
    private double toopTaxPaid;
    private double toopSaleValue;
    private double toopRefundQty;
    private double toopRefundValue;
    private double toopDiscountValue;
    private double toopVoidQty;
    private double toopVoidValue;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public AppUser getToopOperator() {
        return toopOperator;
    }

    public void setToopOperator(AppUser toopOperator) {
        this.toopOperator = toopOperator;
    }

    public ConfigCategory getToopTxnType() {
        return toopTxnType;
    }

    public void setToopTxnType(ConfigCategory toopTxnType) {
        this.toopTxnType = toopTxnType;
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

    public Timestamp getToopTradingDate() {
        return toopTradingDate;
    }

    public void setToopTradingDate(Timestamp toopTradingDate) {
        this.toopTradingDate = toopTradingDate;
    }

    public double getToopSaleQty() {
        return toopSaleQty;
    }

    public void setToopSaleQty(double toopSaleQty) {
        this.toopSaleQty = toopSaleQty;
    }

    public double getToopItemsValue() {
        return toopItemsValue;
    }

    public void setToopItemsValue(double toopItemsValue) {
        this.toopItemsValue = toopItemsValue;
    }

    public double getToopProfitValue() {
        return toopProfitValue;
    }

    public void setToopProfitValue(double toopProfitValue) {
        this.toopProfitValue = toopProfitValue;
    }

    public double getToopTaxedValue() {
        return toopTaxedValue;
    }

    public void setToopTaxedValue(double toopTaxedValue) {
        this.toopTaxedValue = toopTaxedValue;
    }

    public double getToopTaxPaid() {
        return toopTaxPaid;
    }

    public void setToopTaxPaid(double toopTaxPaid) {
        this.toopTaxPaid = toopTaxPaid;
    }

    public double getToopSaleValue() {
        return toopSaleValue;
    }

    public void setToopSaleValue(double toopSaleValue) {
        this.toopSaleValue = toopSaleValue;
    }

    public double getToopRefundQty() {
        return toopRefundQty;
    }

    public void setToopRefundQty(double toopRefundQty) {
        this.toopRefundQty = toopRefundQty;
    }

    public double getToopRefundValue() {
        return toopRefundValue;
    }

    public void setToopRefundValue(double toopRefundValue) {
        this.toopRefundValue = toopRefundValue;
    }

    public double getToopDiscountValue() {
        return toopDiscountValue;
    }

    public void setToopDiscountValue(double toopDiscountValue) {
        this.toopDiscountValue = toopDiscountValue;
    }

    public double getToopVoidQty() {
        return toopVoidQty;
    }

    public void setToopVoidQty(double toopVoidQty) {
        this.toopVoidQty = toopVoidQty;
    }

    public double getToopVoidValue() {
        return toopVoidValue;
    }

    public void setToopVoidValue(double toopVoidValue) {
        this.toopVoidValue = toopVoidValue;
    }
}
