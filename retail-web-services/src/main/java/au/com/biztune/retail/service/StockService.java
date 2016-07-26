package au.com.biztune.retail.service;

import au.com.biztune.retail.domain.Stock;
import au.com.biztune.retail.domain.StockEvent;

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
}
