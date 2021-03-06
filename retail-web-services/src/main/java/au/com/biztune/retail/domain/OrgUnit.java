package au.com.biztune.retail.domain;

/**
 * Created by akhoshraft on 16/03/2016.
 */
public class OrgUnit {
    private long id;
    private String orguName;
    private String orguDesc;
    private String orguCode;
    private PriceBand priceBand;
    private StockLocation stockLocation;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOrguName() {
        return orguName;
    }

    public void setOrguName(String orguName) {
        this.orguName = orguName;
    }

    public String getOrguDesc() {
        return orguDesc;
    }

    public void setOrguDesc(String orguDesc) {
        this.orguDesc = orguDesc;
    }

    public String getOrguCode() {
        return orguCode;
    }

    public void setOrguCode(String orguCode) {
        this.orguCode = orguCode;
    }

    public PriceBand getPriceBand() {
        return priceBand;
    }

    public void setPriceBand(PriceBand priceBand) {
        this.priceBand = priceBand;
    }

    public StockLocation getStockLocation() {
        return stockLocation;
    }

    public void setStockLocation(StockLocation stockLocation) {
        this.stockLocation = stockLocation;
    }
}
