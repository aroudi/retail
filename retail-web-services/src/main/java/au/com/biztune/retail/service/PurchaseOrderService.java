package au.com.biztune.retail.service;

import au.com.biztune.retail.domain.BoqDetail;
import au.com.biztune.retail.domain.ProductPurchaseItem;
import au.com.biztune.retail.domain.PurchaseOrderHeader;
import au.com.biztune.retail.response.CommonResponse;

import java.util.List;

/**
 * Created by arash on 14/05/2016.
 */
public interface PurchaseOrderService {
    /**
     * create Purchase Order From Boq.
     * @param boqDetail boqDetail
     * @return PurchaseOrderHeader
     */
    PurchaseOrderHeader createPoFromBoq(BoqDetail boqDetail);
    /**
     * add lines to Purchase Order Header.
     * @param purchaseOrderHeader purchaseOrderHeader
     * @param boqDetail boqDetail
     * @return boolean
     */
    boolean addLineToPoFromBoqDetail(PurchaseOrderHeader purchaseOrderHeader, BoqDetail boqDetail);

    /**
     * save Purchase Order Header into database.
     * @param purchaseOrderHeader purchaseOrderHeader
     * @return response
     */
    CommonResponse savePurchaseOrder(PurchaseOrderHeader purchaseOrderHeader);

    /**
     * get all purchase Order Header.
     * @return List of PurchaseOrderHeader
     */
    List<PurchaseOrderHeader> getAllPurchaseOrderHeaders();

    /**
     * get PurchaseOrderHeader whole oblect per pohId.
     * @param pohId pohId.
     * @return PurchaseOrderHeader
     */
    PurchaseOrderHeader getPurchaseOrderHeaderWhole(long pohId);
    /**
     * get product purchase items for specific supplier.
     * @param suppId suppId
     * @return List of PruductPurchaseItem
     */
    List<ProductPurchaseItem> getAllSupplierProductPurchaseItems(long suppId);
    /**
     * get all purchase Order Header.
     * @param configStatusCode configStatusCode
     * @param supplierId supplierId
     * @return List of PurchaseOrderHeader
     */
    List<PurchaseOrderHeader> getAllPurchaseOrderHeaderPerOrguIdAndSupplierIdAndStatusCode(long supplierId, String configStatusCode);
}
