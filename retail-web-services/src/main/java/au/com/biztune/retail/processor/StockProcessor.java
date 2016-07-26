package au.com.biztune.retail.processor;

import au.com.biztune.retail.service.StockService;
import au.com.biztune.retail.util.queuemanager.Processor;
import au.com.biztune.retail.util.queuemanager.Request;
import au.com.biztune.retail.util.queuemanager.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * Created by arash on 26/07/2016.
 */
@Component
public class StockProcessor implements Processor {
    private final Logger logger = LoggerFactory.getLogger(StockProcessor.class);

    @Autowired
    private StockService stockService;
    /**
     * process the incomming stock requests.
     * @param request request
     * @return Response
     */
    public Response process(Request request) {
        final Response response = new Response();
        try {
            StockRequest stockRequest;
            if (request instanceof StockRequest) {
                stockRequest = (StockRequest) request;
            } else {
                response.setSucceeded(false);
                response.setMessage("BAD ONSET REQUEST");
                return response;
            }
            stockService.processStockEvent(stockRequest.getStockEvent());
            return response;
        } catch (Exception e) {
            logger.error("Exception processing stock request: ", e);
            response.setSucceeded(false);
            response.setMessage("Exception in processing stock request :" + e.getMessage());
            return response;
        }
    }
}
