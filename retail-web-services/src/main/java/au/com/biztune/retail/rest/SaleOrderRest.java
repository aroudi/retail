package au.com.biztune.retail.rest;

import au.com.biztune.retail.domain.PurchaseOrderHeader;
import au.com.biztune.retail.security.Secured;
import au.com.biztune.retail.service.SaleOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.*;
import java.util.List;

/**
 * Created by arash on 10/07/2017.
 */
@Component
@Path("saleOrder")
public class SaleOrderRest {
    private final Logger logger = LoggerFactory.getLogger(SaleOrderRest.class);
    @Context
    private UriInfo uriInfo;
    @Context
    private Request request;
    @Autowired
    private SaleOrderService saleOrderService;
    @Context
    private SecurityContext securityContext;

    /**
     * crate a Sale Transaction.
     * @param txhdIdList txhdIdList
     * @return List of generated purchase orders.
     */
    @Secured
    @Path("/convertSoToPo")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<PurchaseOrderHeader> createPurchaesOrderFromSaleOrder (List<Long> txhdIdList) {
        return saleOrderService.createPurchaseOrderFromSaleOrder(txhdIdList, securityContext);
    }

}
