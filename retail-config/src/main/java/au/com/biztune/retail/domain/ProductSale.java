package au.com.biztune.retail.domain;

import java.sql.Timestamp;

/**
 * Created by arash on 13/02/2017.
 */
public class ProductSale {
    private long prodId;
    private String prodName;
    private long txdeId;
    private long txhdId;
    private String txhdTxnNr;
    private Timestamp txhdTradingDate;
    private String client;
    private String txnType;
    private double txdeValueGross;
    private double txdeValueNet;
    private double txdeQuantitySold;
    private double txdePriceSold;

    public long getProdId() {
        return prodId;
    }

    public void setProdId(long prodId) {
        this.prodId = prodId;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public long getTxdeId() {
        return txdeId;
    }

    public void setTxdeId(long txdeId) {
        this.txdeId = txdeId;
    }

    public long getTxhdId() {
        return txhdId;
    }

    public void setTxhdId(long txhdId) {
        this.txhdId = txhdId;
    }

    public String getTxhdTxnNr() {
        return txhdTxnNr;
    }

    public void setTxhdTxnNr(String txhdTxnNr) {
        this.txhdTxnNr = txhdTxnNr;
    }

    public Timestamp getTxhdTradingDate() {
        return txhdTradingDate;
    }

    public void setTxhdTradingDate(Timestamp txhdTradingDate) {
        this.txhdTradingDate = txhdTradingDate;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getTxnType() {
        return txnType;
    }

    public void setTxnType(String txnType) {
        this.txnType = txnType;
    }

    public double getTxdeValueGross() {
        return txdeValueGross;
    }

    public void setTxdeValueGross(double txdeValueGross) {
        this.txdeValueGross = txdeValueGross;
    }

    public double getTxdeValueNet() {
        return txdeValueNet;
    }

    public void setTxdeValueNet(double txdeValueNet) {
        this.txdeValueNet = txdeValueNet;
    }

    public double getTxdeQuantitySold() {
        return txdeQuantitySold;
    }

    public void setTxdeQuantitySold(double txdeQuantitySold) {
        this.txdeQuantitySold = txdeQuantitySold;
    }

    public double getTxdePriceSold() {
        return txdePriceSold;
    }

    public void setTxdePriceSold(double txdePriceSold) {
        this.txdePriceSold = txdePriceSold;
    }
}
