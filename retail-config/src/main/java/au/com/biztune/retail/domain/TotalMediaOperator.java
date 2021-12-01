package au.com.biztune.retail.domain;

import java.sql.Timestamp;

/**
 * Created by arash on 26/09/2016.
 */
public class TotalMediaOperator {
    private long id;
    private AppUser tomoOperator;
    private long orguId;
    private long storeId;
    private Timestamp tomoTradingDate;
    private long medtId;
    private PaymentMedia paymentMedia;
    private double tomoSaleQty;
    private double tomoSaleValue;
    private double tomoRefundQty;
    private double tomoRefundValue;
    private double tomoCashbackQty;
    private double tomoCashbackValue;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public AppUser getTomoOperator() {
        return tomoOperator;
    }

    public void setTomoOperator(AppUser tomoOperator) {
        this.tomoOperator = tomoOperator;
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

    public Timestamp getTomoTradingDate() {
        return tomoTradingDate;
    }

    public void setTomoTradingDate(Timestamp tomoTradingDate) {
        this.tomoTradingDate = tomoTradingDate;
    }

    public long getMedtId() {
        return medtId;
    }

    public void setMedtId(long medtId) {
        this.medtId = medtId;
    }

    public PaymentMedia getPaymentMedia() {
        return paymentMedia;
    }

    public void setPaymentMedia(PaymentMedia paymentMedia) {
        this.paymentMedia = paymentMedia;
    }

    public double getTomoSaleQty() {
        return tomoSaleQty;
    }

    public void setTomoSaleQty(double tomoSaleQty) {
        this.tomoSaleQty = tomoSaleQty;
    }

    public double getTomoSaleValue() {
        return tomoSaleValue;
    }

    public void setTomoSaleValue(double tomoSaleValue) {
        this.tomoSaleValue = tomoSaleValue;
    }

    public double getTomoRefundQty() {
        return tomoRefundQty;
    }

    public void setTomoRefundQty(double tomoRefundQty) {
        this.tomoRefundQty = tomoRefundQty;
    }

    public double getTomoRefundValue() {
        return tomoRefundValue;
    }

    public void setTomoRefundValue(double tomoRefundValue) {
        this.tomoRefundValue = tomoRefundValue;
    }

    public double getTomoCashbackQty() {
        return tomoCashbackQty;
    }

    public void setTomoCashbackQty(double tomoCashbackQty) {
        this.tomoCashbackQty = tomoCashbackQty;
    }

    public double getTomoCashbackValue() {
        return tomoCashbackValue;
    }

    public void setTomoCashbackValue(double tomoCashbackValue) {
        this.tomoCashbackValue = tomoCashbackValue;
    }
}
