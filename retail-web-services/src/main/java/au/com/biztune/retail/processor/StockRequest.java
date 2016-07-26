package au.com.biztune.retail.processor;

import au.com.biztune.retail.domain.StockEvent;
import au.com.biztune.retail.util.queuemanager.Request;

/**
 * Created by arash on 26/07/2016.
 */
public class StockRequest extends Request {
    private StockEvent stockEvent;

    public StockEvent getStockEvent() {
        return stockEvent;
    }

    public void setStockEvent(StockEvent stockEvent) {
        this.stockEvent = stockEvent;
    }
}
