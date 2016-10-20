// Sydney Trains 2015
package au.com.biztune.retail.rest;

import au.com.biztune.retail.domain.DeliveryNoteHeader;
import au.com.biztune.retail.domain.GeneralSearchForm;
import au.com.biztune.retail.response.CommonResponse;
import au.com.biztune.retail.security.Secured;
import au.com.biztune.retail.service.DeliveryNoteService;
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
@Path("deliveryNote")
public class DeliveryNoteRest {
    private final Logger logger = LoggerFactory.getLogger(DeliveryNoteRest.class);
    @Context
    private UriInfo uriInfo;
    @Context
    private Request request;

    @Autowired
    private DeliveryNoteService deliveryNoteService;

    @Context
    private SecurityContext securityContext;
    /**
     * Returns list of Delivery Notes.
     * @return list of DeliveryNoteHeader
     */
    @Secured
    @Path("/header/all")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<DeliveryNoteHeader> getAllDeliveryNotes() {
        try {
            return deliveryNoteService.getAllDeliveryNoteHeaders();
        } catch (Exception e) {
            logger.error ("Error in retrieving delivery note header List :", e);
            return null;
        }
    }

    /**
     * get delivery note whole by id.
     * @param id id.
     * @return List of DeliveryNoteHeader
     */
    @Secured
    @GET
    @Path("/header/getWhole/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public DeliveryNoteHeader getDeliveryNoteWholeById (@PathParam("id") long id) {
        return deliveryNoteService.getDeliveryNoteWhole(id);
    }

    /**
     * add/edit a delivery note.
     * @param deliveryNoteHeader deliveryNoteHeader
     * @return CommonResponse
     */
    @Secured
    @Path("/add")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public CommonResponse addDeliveryNote (DeliveryNoteHeader deliveryNoteHeader) {
        return deliveryNoteService.saveDeliveryNote(deliveryNoteHeader, securityContext);
    }
    /**
     * Returns list of Delivery Notes.
     * @param supplierId supplierId
     * @return list of DeliveryNoteHeader
     */
    @Secured
    @Path("/getSupplierDeliveryNotes/{supplierId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<DeliveryNoteHeader> getAllSupplierDeliveryNotes(@PathParam("supplierId") long supplierId) {
        try {
            return deliveryNoteService.getAllSuppliersDeliveryNotes(supplierId);
        } catch (Exception e) {
            logger.error ("Error in retrieving delivery note header List :", e);
            return null;
        }
    }
    /**
     * search delivery note.
     * @param generalSearchForm generalSearchForm
     * @return list of purchase order header
     */
    @Secured
    @Path("/header/search")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<DeliveryNoteHeader> search(GeneralSearchForm generalSearchForm) {
        try {
            return deliveryNoteService.searchDeliveryNote(generalSearchForm);
        } catch (Exception e) {
            logger.error ("Error in searching delivery notes:", e);
            return null;
        }
    }

}
