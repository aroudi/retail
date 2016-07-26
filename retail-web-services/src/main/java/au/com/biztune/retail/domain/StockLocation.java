package au.com.biztune.retail.domain;

/**
 * Created by arash on 25/07/2016.
 */
public class StockLocation {
    private long id;
    private String stckLocnCode;
    private String stckLocnName;
    private String stckLocnAddress;
    private long orguId;
    private long storeId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStckLocnCode() {
        return stckLocnCode;
    }

    public void setStckLocnCode(String stckLocnCode) {
        this.stckLocnCode = stckLocnCode;
    }

    public String getStckLocnName() {
        return stckLocnName;
    }

    public void setStckLocnName(String stckLocnName) {
        this.stckLocnName = stckLocnName;
    }

    public String getStckLocnAddress() {
        return stckLocnAddress;
    }

    public void setStckLocnAddress(String stckLocnAddress) {
        this.stckLocnAddress = stckLocnAddress;
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
}
