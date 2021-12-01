package au.com.biztune.retail.domain;

/**
 * Created by arash on 30/11/2016.
 */
public class AccExportSuppLink {
    private long accExpId;
    private long supplierId;

    public long getAccExpId() {
        return accExpId;
    }

    public void setAccExpId(long accExpId) {
        this.accExpId = accExpId;
    }

    public long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(long supplierId) {
        this.supplierId = supplierId;
    }
}
