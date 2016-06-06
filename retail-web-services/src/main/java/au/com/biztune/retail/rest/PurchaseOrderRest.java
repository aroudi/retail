// Sydney Trains 2015
package au.com.biztune.retail.rest;

import au.com.biztune.retail.domain.ProductPurchaseItem;
import au.com.biztune.retail.domain.PurchaseOrderHeader;
import au.com.biztune.retail.response.CommonResponse;
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

    /**
     * get Product Purchase Items per supplier id.
     * @param suppId suppId.
     * @return List of ProductPurchaseItem
     */
    @GET
    @Path("/detail/getPurchaseItems/{suppId}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ProductPurchaseItem> getProductPurchaseItemsPerSuppId (@PathParam("suppId") long suppId) {
        return purchaseOrderService.getAllSupplierProductPurchaseItems(suppId);
    }

    /**
     * add/edit a Purchase order.
     * @param purchaseOrderHeader purchaseOrderHeader
     * @return CommonResponse
     */
    @Path("/add")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public CommonResponse addPurchaseOrder (PurchaseOrderHeader purchaseOrderHeader) {
        return purchaseOrderService.savePurchaseOrder(purchaseOrderHeader);
    }


    /**
     * update linked boqs.
     * @param purchaseOrderHeader purchaseOrderHeader
     * @return CommonResponse
     */
    @Path("/updateLinkedBoqs")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public CommonResponse updateLinkedBqos (PurchaseOrderHeader purchaseOrderHeader) {
        return purchaseOrderService.updateLinkedBqos(purchaseOrderHeader);
    }

    /**
     * Returns list of Purchase Order Header confirmed.
     * @param suppId suppId.
     * @return list of confirmed purchase order header
     */

    @Path("/header/search/orguIdSupIdStatusId/{suppId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<PurchaseOrderHeader> getAllConfirmedPurchaseOrderPerSupplierId(@PathParam("suppId") long suppId) {
        try {
            return purchaseOrderService.getAllPurchaseOrderHeaderPerOrguIdAndSupplierIdAndStatusCode(suppId);
        } catch (Exception e) {
            logger.error ("Error in retrieving purchase order header List :", e);
            return null;
        }
    }

}
