/*
 * Package: au.com.biztune.retail.domain
 * Class: CashupDetailTxnSummaryRow
 * Copyright: (c) 2018 Sydney Trains
 */
package au.com.biztune.retail.domain;

import java.sql.Timestamp;

/**
 * CashupDetailTxnSummaryRow.
 *
 * @author arash
 */
public class CashupDetailTxnSummaryRow {
    private String docketNo;
    private Timestamp tradingDate;
    private String txnType;
    private String operator;
    private double saleTotal;
    private String paymentMedia;
    private double paymentMediaAmount;
    private double txnTotalCost;
    private double txnTotalPriceLessTax;
    private double txnTotalPricePlusTax;
    private double txnTotalProfit;

    public String getDocketNo() {
        return docketNo;
    }

    public void setDocketNo(String docketNo) {
        this.docketNo = docketNo;
    }

    public Timestamp getTradingDate() {
        return tradingDate;
    }

    public void setTradingDate(Timestamp tradingDate) {
        this.tradingDate = tradingDate;
    }

    public String getTxnType() {
        return txnType;
    }

    public void setTxnType(String txnType) {
        this.txnType = txnType;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public double getSaleTotal() {
        return saleTotal;
    }

    public void setSaleTotal(double saleTotal) {
        this.saleTotal = saleTotal;
    }

    public String getPaymentMedia() {
        return paymentMedia;
    }

    public void setPaymentMedia(String paymentMedia) {
        this.paymentMedia = paymentMedia;
    }

    public double getPaymentMediaAmount() {
        return paymentMediaAmount;
    }

    public void setPaymentMediaAmount(double paymentMediaAmount) {
        this.paymentMediaAmount = paymentMediaAmount;
    }

    public double getTxnTotalCost() {
        return txnTotalCost;
    }

    public void setTxnTotalCost(double txnTotalCost) {
        this.txnTotalCost = txnTotalCost;
    }

    public double getTxnTotalPriceLessTax() {
        return txnTotalPriceLessTax;
    }

    public void setTxnTotalPriceLessTax(double txnTotalPriceLessTax) {
        this.txnTotalPriceLessTax = txnTotalPriceLessTax;
    }

    public double getTxnTotalPricePlusTax() {
        return txnTotalPricePlusTax;
    }

    public void setTxnTotalPricePlusTax(double txnTotalPricePlusTax) {
        this.txnTotalPricePlusTax = txnTotalPricePlusTax;
    }

    public double getTxnTotalProfit() {
        return txnTotalProfit;
    }

    public void setTxnTotalProfit(double txnTotalProfit) {
        this.txnTotalProfit = txnTotalProfit;
    }
}
