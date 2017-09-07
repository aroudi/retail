package au.com.biztune.retail.service;

import au.com.biztune.retail.domain.Stock;
import au.com.biztune.retail.domain.StockEvent;
import au.com.biztune.retail.domain.TxnHeader;

import java.util.List;

/**
 * Created by arash on 26/07/2016.
 */
public interface StockService {
    /**
     * process stock event.
     * @param stockEvent stockEvent
     */
    void processStockEvent(StockEvent stockEvent);

    /**
     * check the saleable pristine stock quantity for specific product.
     * @param productId productId
     * @return Stock
     */
    Stock checkStockForSaleableProduct(long productId);

    /**
     * process stock event.
     * @param stockEvent stockEvent
     */
    void pushStockEvent (StockEvent stockEvent);

    /**
     * update stock quantity.
     * @param txnHeader txnHeader
     */
    void processTxnForStockUpdate(TxnHeader txnHeader);

    /**
     * view product audit trail.
     * @param prodId prodId
     * @return List of StockEvent
     */
    List<StockEvent> viewProductAuditTrail(long prodId);
}
