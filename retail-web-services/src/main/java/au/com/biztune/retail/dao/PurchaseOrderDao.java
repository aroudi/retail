package au.com.biztune.retail.dao;

import au.com.biztune.retail.domain.PurchaseLine;
import au.com.biztune.retail.domain.PurchaseOrderHeader;

import java.util.List;

/**
 * Created by arash on 5/05/2016.
 */
public interface PurchaseOrderDao {
    /**
     * insert Purchase Order Header.
     * @param purchaseOrderHeader purchaseOrderHeader
     */
    void insertPurchaseOrderHeader(PurchaseOrderHeader purchaseOrderHeader);

    /**
     * update purchase order header.
     * @param purchaseOrderHeader purchaseOrderHeader
     */
    void updatePurchaseOrderHeader(PurchaseOrderHeader purchaseOrderHeader);

    /**
     * Insert purchase line.
     * @param purchaseLine purchaseLine
     */
    void insertPurchaseLine(PurchaseLine purchaseLine);

    /**
     * update PurchaseLIne.
     * @param purchaseLine purchaseLine
     */
    void updatePurchaseLine(PurchaseLine purchaseLine);

    /**
     * get all purchase lines belong to specific purechase order header.
     * @param pohId pohId
     * @return List of Purchase Order Line.
     */
    List<PurchaseLine> getPurchaseLinesPerPohId(long pohId);

    /**
     * get All Purchase Order Headers per orguId.
     * @param orguId orguId
     * @return List of Purchase Order Header
     */
    List<PurchaseOrderHeader> getAllPurchaseOrderHeaderPerOrguId (long orguId);


    /**
     * get all purchase Order Header.
     * @param orguId orguId
     * @param supplierId supplierId
     * @param statusIds statusIds
     * @return List of PurchaseOrderHeader
     */
    List<PurchaseOrderHeader> getAllPurchaseOrderHeaderPerOrguIdAndSupplierIdAndStatusIds(long orguId, long supplierId, List statusIds);

    /**
     * get purchase order header per order no.
     * @param  orderNo orderNo
     * @return Purchase Order Header
     */
    PurchaseOrderHeader getPurchaseOrderHeaderPerOrderNo (String orderNo);

    /**
     * get purchase order header per pohId.
     * @param pohId pohId
     * @return Purchase Order Header.
     */
    PurchaseOrderHeader getPurchaseOrderWholePerPohId (long pohId);

    /**
     * get Max PohId.
     * @return pohId as long.
     */
    long getMaxPohId();

    /**
     * delete purchase line where their ids not in the proposed list.
     * @param pohId pohId
     * @param polIdList polIdList
     */
    void deletePurchaseLineWhereIdNotIn(long pohId, List polIdList);

    /**
     * get Purchase Order Line per id.
     * @param polId polId
     * @return Purchase Order Line
     */
    PurchaseLine getPurchaseLinePerId(long polId);

    /**
     * delete purchase order line per id.
     * @param polId polId
     */
    void deletePurchaseLinePerId(long polId);

    /**
     * get all purchase order headers per orguId and status.
     * @param orguId orguId
     * @param statusIds statusIds
     * @return List of purchase order header
     */
    List<PurchaseOrderHeader> getAllPurchaseOrderHeaderNotFullyReceived(long orguId, List statusIds);

    /**
     * update purchase line reserved qty.
     * @param purchaseLine purchaseLine
     */
    void updatePurchaseLineReserveQty(PurchaseLine purchaseLine);

    /**
     * get all purchaes order header for supplier.
     * @param orgUid orgUid
     * @param supplierId supplierId
     * @return List of purchase order header for specific supplier.
     */
    List<PurchaseOrderHeader> getAllPurchaseOrderHeaderPerOrguIdAndSupplierId(long orgUid, long supplierId);

    /**
     * search purchase order header.
     * @param orguId orguId
     * @param clauseList clauseList
     * @return List of purchase order headers
     */
    List<PurchaseOrderHeader> searchPurchaseOrderHeader(long orguId, List clauseList);

    /**
     * update flag to printed.
     * @param pohId pohId
     */
    void updatePohPrintStatus(long pohId);

    /**
     * retreive all purchaes orders related to the specific product.
     * @param prodId prodId
     * @param orguId orguId
     * @return List of PurchaseLine
     */
    List<PurchaseLine> getPurchaseOrdersOfProductPerProdId(long prodId, long orguId);

    /**
     * update project code for po.
     * @param pohId pohId
     * @param projectCode projectCode
     */
    void updatePohProjectCode(long pohId, String projectCode);

    /**
     * get purchase order header paging.
     * @param orguId orguId
     * @param clauseList clauseList
     * @param pageFrom pageFrom
     * @param pageTo pageTo
     * @return List of purchase orders paging.
     */
    List<PurchaseOrderHeader> searchPurchaseOrderHeaderPaging(long orguId, List clauseList, long pageFrom, long pageTo);

    /**
     * get query total rows.
     * @param orguId orguId
     * @param clauseList clauseList
     * @return query total rows.
     */
    long getPurchaseOrderQueryTotalRows(long orguId, List clauseList);

    /**
     * get all purchase order header generated for specific sale order.
     * @param orguId orguId
     * @param txhdId txhdId
     * @return List of purchase order header linked to sale order
     */
    List<PurchaseOrderHeader> getAllPurchaseOrderOfSaleOrder(long orguId, long txhdId);

    /**
     * delete purchase order per id.
     * @param pohId pohId
     */
    void deletePurchaseOrderPerId(long pohId);

    /**
     * delete purchase line per pohId.
     * @param pohId pohId
     */
    void deletePurchaseLinePerPohId(long pohId);
}
