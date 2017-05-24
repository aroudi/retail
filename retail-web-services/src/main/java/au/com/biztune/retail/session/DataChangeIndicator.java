package au.com.biztune.retail.session;

/**
 * Created by arash on 22/05/2017.
 */
public class DataChangeIndicator {
    private boolean productDataUpdated;

    public boolean isProductDataUpdated() {
        return productDataUpdated;
    }

    public void setProductDataUpdated(boolean productDataUpdated) {
        this.productDataUpdated = productDataUpdated;
    }
}
