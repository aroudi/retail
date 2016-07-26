package au.com.biztune.retail.domain;

import java.sql.Timestamp;

/**
 * Created by arash on 25/07/2016.
 */
public class StockEvent {
    private long id;
    private long stckId;
    private long orguId;
    private long storeId;
    private long userId;
    private long supplierId;
    private long unomId;
    private long prodId;
    private long prgpId;
    private long selcId;
    private Timestamp stckEvntDate;
    private Timestamp txnDate;
    private long txnType;
    private long txnHeader;
    private long txnLine;
    private String txnNumber;
    private long stckLocId;
    private double stckQty;
    private double sellPrice;
    private double costPrice;
    private long stckCond;
    private long stckCat;
    private String txnTypeConst;
    private String reasonConst;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getStckId() {
        return stckId;
    }

    public void setStckId(long stckId) {
        this.stckId = stckId;
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

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(long supplierId) {
        this.supplierId = supplierId;
    }

    public long getUnomId() {
        return unomId;
    }

    public void setUnomId(long unomId) {
        this.unomId = unomId;
    }

    public long getProdId() {
        return prodId;
    }

    public void setProdId(long prodId) {
        this.prodId = prodId;
    }

    public long getPrgpId() {
        return prgpId;
    }

    public void setPrgpId(long prgpId) {
        this.prgpId = prgpId;
    }

    public long getSelcId() {
        return selcId;
    }

    public void setSelcId(long selcId) {
        this.selcId = selcId;
    }

    public Timestamp getStckEvntDate() {
        return stckEvntDate;
    }

    public void setStckEvntDate(Timestamp stckEvntDate) {
        this.stckEvntDate = stckEvntDate;
    }

    public Timestamp getTxnDate() {
        return txnDate;
    }

    public void setTxnDate(Timestamp txnDate) {
        this.txnDate = txnDate;
    }

    public long getTxnType() {
        return txnType;
    }

    public void setTxnType(long txnType) {
        this.txnType = txnType;
    }

    public long getTxnHeader() {
        return txnHeader;
    }

    public void setTxnHeader(long txnHeader) {
        this.txnHeader = txnHeader;
    }

    public long getTxnLine() {
        return txnLine;
    }

    public void setTxnLine(long txnLine) {
        this.txnLine = txnLine;
    }

    public String getTxnNumber() {
        return txnNumber;
    }

    public void setTxnNumber(String txnNumber) {
        this.txnNumber = txnNumber;
    }

    public long getStckLocId() {
        return stckLocId;
    }

    public void setStckLocId(long stckLocId) {
        this.stckLocId = stckLocId;
    }

    public double getStckQty() {
        return stckQty;
    }

    public void setStckQty(double stckQty) {
        this.stckQty = stckQty;
    }

    public double getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(double sellPrice) {
        this.sellPrice = sellPrice;
    }

    public double getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(double costPrice) {
        this.costPrice = costPrice;
    }

    public long getStckCond() {
        return stckCond;
    }

    public void setStckCond(long stckCond) {
        this.stckCond = stckCond;
    }

    public long getStckCat() {
        return stckCat;
    }

    public void setStckCat(long stckCat) {
        this.stckCat = stckCat;
    }

    public String getTxnTypeConst() {
        return txnTypeConst;
    }

    public void setTxnTypeConst(String txnTypeConst) {
        this.txnTypeConst = txnTypeConst;
    }

    public String getReasonConst() {
        return reasonConst;
    }

    public void setReasonConst(String reasonConst) {
        this.reasonConst = reasonConst;
    }
}
