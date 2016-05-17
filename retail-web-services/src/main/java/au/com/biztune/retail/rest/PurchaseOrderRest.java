// Sydney Trains 2015
package au.com.biztune.retail.rest;

import au.com.biztune.retail.domain.PurchaseOrderHeader;
import au.com.biztune.retail.service.PurchaseOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;
import java.util.List;

/**
 * Created by arash on 22/02/2016.
 */
@Component
@Path("purchaseOrder")
public class PurchaseOrderRest {
    private final Logger logger = LoggerFactory.getLogger(PurchaseOrderRest.class);
    @Context
    private UriInfo uriInfo;
    @Context
    private Request request;

    @Autowired
    private PurchaseOrderService purchaseOrderService;

    /**
     * Returns list of customers.
     * @return list of Customer
     */

    @Path("/header/all")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<PurchaseOrderHeader> getAllCustomers() {
        try {
            return purchaseOrderService.getAllPurchaseOrderHeaders();
        } catch (Exception e) {
            logger.error ("Error in retrieving purchase order header List :", e);
            return null;
        }
    }

    /**
     * get purchase order whole by id.
     * @param id id.
     * @return PurchaseOrderHeader
     */
    @GET
    @Path("/header/getWhole/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public PurchaseOrderHeader getPurchaseOrderWholeById (@PathParam("id") long id) {
        return purchaseOrderService.getPurchaseOrderHeaderWhole(id);
    }

}
