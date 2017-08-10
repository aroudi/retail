package au.com.biztune.retail.domain;

/**
 * Created by arash on 10/08/2017.
 */
public class StockReserve {
    private long id;
    private long stckId;
    private long orguId;
    private long storeId;
    private long supplierId;
    private long prodId;
    private long prgpId;
    private String txnType;
    private long txnHeaderId;
    private long txnLineId;
    private String txnNumber;
    private double qtyReserved;

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

    public long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(long supplierId) {
        this.supplierId = supplierId;
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

    public String getTxnType() {
        return txnType;
    }

    public void setTxnType(String txnType) {
        this.txnType = txnType;
    }

    public long getTxnHeaderId() {
        return txnHeaderId;
    }

    public void setTxnHeaderId(long txnHeaderId) {
        this.txnHeaderId = txnHeaderId;
    }

    public long getTxnLineId() {
        return txnLineId;
    }

    public void setTxnLineId(long txnLineId) {
        this.txnLineId = txnLineId;
    }

    public String getTxnNumber() {
        return txnNumber;
    }

    public void setTxnNumber(String txnNumber) {
        this.txnNumber = txnNumber;
    }

    public double getQtyReserved() {
        return qtyReserved;
    }

    public void setQtyReserved(double qtyReserved) {
        this.qtyReserved = qtyReserved;
    }
}
