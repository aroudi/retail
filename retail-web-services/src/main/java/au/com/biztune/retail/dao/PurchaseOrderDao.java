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
}