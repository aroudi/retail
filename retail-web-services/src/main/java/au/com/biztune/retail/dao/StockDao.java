package au.com.biztune.retail.dao;

import au.com.biztune.retail.domain.Stock;
import au.com.biztune.retail.domain.StockEvent;
import au.com.biztune.retail.domain.StockLocation;

/**
 * Created by arash on 25/07/2016.
 */
public interface StockDao {
    /**
     * insert into stock.
     * @param stock stock
     */
    void insertStock(Stock stock);

    /**
     * update stock qty.
     * @param stock stock
     */
    void updateStockQty(Stock stock);
    /**
     * insert into stockEvent.
     * @param stockEvent stockEvent
     */
    void insertStockEvent(StockEvent stockEvent);

    /**
     * check stock for specific product.
     * @param prodId prodId
     * @param orguId orguId
     * @param stockConditionTypeId stockConditionTypeId
     * @param stockCategoryId stockCategoryId
     * @param stockLocationId stockLocationId
     * @return Stock
     */
    Stock checkStockForProduct(long prodId, long orguId, long stockConditionTypeId, long stockCategoryId, long stockLocationId);

    /**
     * get stock location per orguId.
     * @param orguId orguId
     * @return Stock Location
     */
    StockLocation getStockLocationPerOrguId(long orguId);

    /**
     * get saleable and pristine qty for product.
     * @param prodId prodId
     * @param orguId orguId
     * @return stock qty.
     */
    double getProductSaleablePristineStockQty(long prodId, long orguId);
}
