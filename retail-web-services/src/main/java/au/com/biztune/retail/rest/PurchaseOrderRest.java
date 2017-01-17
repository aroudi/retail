// Sydney Trains 2015
package au.com.biztune.retail.rest;

import au.com.biztune.retail.domain.GeneralSearchForm;
import au.com.biztune.retail.domain.PurchaseLine;
import au.com.biztune.retail.report.PurchaseOrderRptMgr;
import au.com.biztune.retail.domain.ProductPurchaseItem;
import au.com.biztune.retail.domain.PurchaseOrderHeader;
import au.com.biztune.retail.response.CommonResponse;
import au.com.biztune.retail.security.Secured;
import au.com.biztune.retail.service.PurchaseOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
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
    @Context
    private SecurityContext securityContext;

    @Autowired
    private PurchaseOrderService purchaseOrderService;

    @Autowired
    private PurchaseOrderRptMgr purchaseOrderRptMgr;
    /**
     * Returns list of customers.
     * @return list of Customer
     */
    @Secured
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
    @Secured
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
    @Secured
    @GET
    @Path("/detail/getPurchaseItems/{suppId}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ProductPurchaseItem> getProductPurchaseItemsPerSuppId (@PathParam("suppId") long suppId) {
        return purchaseOrderService.getAllSupplierProductPurchaseItems(suppId);
    }


    /**
     * get Product Purchase Item per supplier id and catalog no.
     * @param suppId suppId.
     * @param catalogNo catalogNo.
     * @return ProductPurchaseItem
     */
    @Secured
    @GET
    @Path("/detail/getPurchaseItem/{suppId}/{catalogNo}")
    @Produces(MediaType.APPLICATION_JSON)
    public ProductPurchaseItem getProductPurchaseItemPerSuppIdAndCatalogNo (@PathParam("suppId") long suppId, @PathParam("catalogNo") String catalogNo) {
        return purchaseOrderService.getSupplierProductPurchaseItemPerCatalogNo(suppId, catalogNo);
    }

    /**
     * add/edit a Purchase order.
     * @param purchaseOrderHeader purchaseOrderHeader
     * @return CommonResponse
     */
    @Secured
    @Path("/add")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public CommonResponse addPurchaseOrder (PurchaseOrderHeader purchaseOrderHeader) {
        return purchaseOrderService.savePurchaseOrder(purchaseOrderHeader, securityContext);
    }


    /**
     * update linked boqs.
     * @param purchaseOrderHeader purchaseOrderHeader
     * @return CommonResponse
     */
    @Secured
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

    @Secured
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

    /**
     * export purchase order as PDF.
     * @param pohId pohId
     * @return Stream output.
     */
    @Secured
    @Path("/exportPdf/{pohId}")
    @GET
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response exportPurchaseOrderToPdf(@PathParam("pohId") long pohId) {
        final StreamingOutput streamingOutput = purchaseOrderRptMgr.createPurchaseOrderPdfStream(pohId);
        //purchaseOrderRptMgr.createPurchaseOrderPdfFile(pohId);
        return Response.ok(streamingOutput).header("Content-Disposition", "attachment; filename = PurchaseOrder" + pohId + ".pdf").build();
    }

    /**
     * Returns list of purchase order header not fully received or cancelled. we need this for replacing a BOQ line with line from purchase order
     * @return list of purchase order headers
     */
    @Secured
    @Path("/header/getAllNotFullyReceived")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<PurchaseOrderHeader> getAllPurchaseOrderNotFullyReceived() {
        try {
            return purchaseOrderService.getAllPurchaseOrderHeaderNotFullyReceived();
        } catch (Exception e) {
            logger.error ("Error in retrieving purchase order header List :", e);
            return null;
        }
    }
    /**
     * get all supplier's purchase orders.
     * @param suppId suppId.
     * @return List of purchase orders
     */
    @Secured
    @GET
    @Path("/getSupplierPurchaseOrders/{suppId}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<PurchaseOrderHeader> getSupplierPurchaseOrders (@PathParam("suppId") long suppId) {
        return purchaseOrderService.getAllPurchaseOrderHeaderPerOrguIdAndSupplierId(suppId);
    }

    /**
     * get all product's purchase orders.
     * @param prodId prodId.
     * @return List of purchase orders
     */
    @Secured
    @GET
    @Path("/getProductPurchaseOrders/{prodId}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<PurchaseLine> getProductPurchaseOrders (@PathParam("prodId") long prodId) {
        return purchaseOrderService.getAllPurchaseOrderOfProduct(prodId);
    }


    /**
     * search purchase order header.
     * @param generalSearchForm generalSearchForm
     * @return list of purchase order header
     */
    @Secured
    @Path("/header/search")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<PurchaseOrderHeader> search(GeneralSearchForm generalSearchForm) {
        try {
            return purchaseOrderService.searchPurchaseOrderHeaders(generalSearchForm);
        } catch (Exception e) {
            logger.error ("Error in searching purchase order header List :", e);
            return null;
        }
    }
}
