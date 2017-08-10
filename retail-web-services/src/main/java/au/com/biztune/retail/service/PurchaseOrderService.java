package au.com.biztune.retail.service;

import au.com.biztune.retail.domain.*;
import au.com.biztune.retail.response.CommonResponse;

import javax.ws.rs.core.SecurityContext;
import java.util.List;

/**
 * Created by arash on 14/05/2016.
 */
public interface PurchaseOrderService {
    /**
     * create Purchase Order From Boq.
     * @param boqDetail boqDetail
     * @param appUser appUser
     * @return PurchaseOrderHeader
     */
    PurchaseOrderHeader createPoFromBoq(BoqDetail boqDetail, AppUser appUser);
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
     * @param  securityContext securityContext
     * @return response
     */
    CommonResponse savePurchaseOrder(PurchaseOrderHeader purchaseOrderHeader, SecurityContext securityContext);

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
     /**
     * get product purchase items for specific supplier.
     * @param suppId suppId
     * @param searchStr searchStr
     * @return List of PruductPurchaseItem
     */
    List<ProductPurchaseItem> getAllSupplierProductPurchaseItems(long suppId, String searchStr);
    /**
     * get all purchase Order Header.
     * @param supplierId supplierId
     * @return List of PurchaseOrderHeader
     */
    List<PurchaseOrderHeader> getAllPurchaseOrderHeaderPerOrguIdAndSupplierIdAndStatusCode(long supplierId);

    /**
     * get all purchase Order Header per orguid and status.
     * @return List of PurchaseOrderHeader
     */
    List<PurchaseOrderHeader> getAllPurchaseOrderHeaderNotFullyReceived();

    /**
     * get all purchase Order Header for specific supplier.
     * @param supplierId supplierId
     * @return List of PurchaseOrderHeader
     */
    List<PurchaseOrderHeader> getAllPurchaseOrderHeaderPerOrguIdAndSupplierId(long supplierId);
    /**
     * search purchase order header.
     * @param searchForm searchForm
     * @return List of PurchaseOrderHeader
     */
    List<PurchaseOrderHeader> searchPurchaseOrderHeaders(GeneralSearchForm searchForm);

    /**
     * get product purchase item for specific supplier and catalog no.
     * @param suppId suppId
     * @param catalogNo catalogNo
     * @return List of PruductPurchaseItem
     */
    ProductPurchaseItem getSupplierProductPurchaseItemPerCatalogNo(long suppId, String catalogNo);

    /**
     * get all purchase Order Header for specific product.
     * @param prodId prodId
     * @return List of PurchaseOrderHeader
     */
    List<PurchaseLine> getAllPurchaseOrderOfProduct(long prodId);

    /**
     * search purchase order header.
     * @param searchForm searchForm
     * @return List of PurchaseOrderHeader
     */
    GeneralSearchForm searchPurchaseOrderHeadersPaging(GeneralSearchForm searchForm);
    /**
     * create Purchase Order From Boq.
     * @param txnDetail sale order detail
     * @param appUser appUser
     * @return PurchaseOrderHeader
     */
    PurchaseOrderHeader createPoFromSaleOrder(TxnDetail txnDetail, AppUser appUser);

    /**
     * add lines to Purchase Order Header from txn detail.
     * @param purchaseOrderHeader purchaseOrderHeader
     * @param txnDetail txnDetail
     * @return true if successfull, otherwise return false;
     */
    boolean addLineToPoFromTxnDetail(PurchaseOrderHeader purchaseOrderHeader, TxnDetail txnDetail);

    /**
     * get all purchase order headers linked to specific sale order.
     * @param txhdId transaction header id.
     * @return List of purchase order linked to sale order
     */
    List<PurchaseOrderHeader> getAllPurchaseOrderOfSaleOrder(long txhdId);

    /**
     * update status of linked BOQ.
     * @param pohId purchaes order header id.
     */
    void updatePurchaseOrderLinkedBoqStatus(long pohId);

    /**
     * delete purchase order per poh id.
     * @param pohId pohId
     * @return CommonResponse.
     */
    CommonResponse deletePurchaseOrderPerPhoId(long pohId);

    /**
     * update order status of linked sales orders.
     * @param pohId purchaes order header id.
     */
    void updatePurchaseOrderLinkedSaleOrderStatus(long pohId);

    /**
     * get all IN-PROGRESS and CONFIRMED purchase Order Header.
     * @param supplierId supplierId
     * @return List of PurchaseOrderHeader
     */
    List<PurchaseOrderHeader> getAllOutstandingAndConfirmedPurchaseOrderHeaderPerOrguIdAndSupplierId(long supplierId);
}
