package au.com.biztune.retail.response;

import au.com.biztune.retail.domain.PurchaseOrderHeader;

import java.util.List;

/**
 * created by arash roudi 27/06/2018.
 */
public class PoFromSoResponse extends CommonResponse {
    private List<PurchaseOrderHeader> purchaseOrderHeaderList;

    public List<PurchaseOrderHeader> getPurchaseOrderHeaderList() {
        return purchaseOrderHeaderList;
    }

    public void setPurchaseOrderHeaderList(List<PurchaseOrderHeader> purchaseOrderHeaderList) {
        this.purchaseOrderHeaderList = purchaseOrderHeaderList;
    }
}
