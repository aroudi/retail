package au.com.biztune.retail.dao;

import au.com.biztune.retail.domain.Stock;
import au.com.biztune.retail.domain.StockEvent;
import au.com.biztune.retail.domain.StockLocation;
import au.com.biztune.retail.domain.StockReserve;

import java.util.List;

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

    /**
     * insert stock reserve.
     * @param stockReserve stockReserve
     */
    void insertStockReserve(StockReserve stockReserve);

    /**
     * get stock reserve per orguId and ProdId and TxnType and TxnId.
     * @param orguId orguId
     * @param prodId prodId
     * @param txnType txnType
     * @param txnHeaderId txnHeaderId
     * @return StockReserve
     */
    StockReserve getStockReservePerOrguIdAndProdIdAndTxnTypeAndTxnId(long orguId, long prodId, String txnType, long txnHeaderId);

    /**
     * get stock reserve per orguId and ProdId and TxnType and TxnId.
     * @param orguId orguId
     * @param prodId prodId
     * @return List of StockReserve
     */
    List<StockReserve> getStockReservePerOrguIdAndProdId(long orguId, long prodId);

    /**
     * update stock reserve per id.
     * @param qty qty
     * @param id id
     */
    void updateStockReservePerId(long id, double qty);

    /**
     * get product audit trail.
     * @param prodId prodId
     * @param orguId orguId
     * @return List of StockEvent
     */
    List<StockEvent> getProductAuditTrail(long prodId, long orguId);
}
