package au.com.biztune.retail.domain;

import java.sql.Timestamp;

/**
 * Created by arash on 25/07/2016.
 */
public class Stock {
    private long id;
    private long supplierId;
    private long prodId;
    private long prgpId;
    private long selcId;
    private long orguIdLocation;
    private double stckQty;
    private long unomId;
    private long stckCond;
    private long stckCat;
    private long stckLocnId;
    private long orguIdRespbility;
    private double stckValue;
    private Timestamp stckLastVerified;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public long getSelcId() {
        return selcId;
    }

    public void setSelcId(long selcId) {
        this.selcId = selcId;
    }

    public long getOrguIdLocation() {
        return orguIdLocation;
    }

    public void setOrguIdLocation(long orguIdLocation) {
        this.orguIdLocation = orguIdLocation;
    }

    public double getStckQty() {
        return stckQty;
    }

    public void setStckQty(double stckQty) {
        this.stckQty = stckQty;
    }

    public long getUnomId() {
        return unomId;
    }

    public void setUnomId(long unomId) {
        this.unomId = unomId;
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

    public long getStckLocnId() {
        return stckLocnId;
    }

    public void setStckLocnId(long stckLocnId) {
        this.stckLocnId = stckLocnId;
    }

    public long getOrguIdRespbility() {
        return orguIdRespbility;
    }

    public void setOrguIdRespbility(long orguIdRespbility) {
        this.orguIdRespbility = orguIdRespbility;
    }

    public double getStckValue() {
        return stckValue;
    }

    public void setStckValue(double stckValue) {
        this.stckValue = stckValue;
    }

    public Timestamp getStckLastVerified() {
        return stckLastVerified;
    }

    public void setStckLastVerified(Timestamp stckLastVerified) {
        this.stckLastVerified = stckLastVerified;
    }
}
