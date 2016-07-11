// Sydney Trains 2015

package au.com.biztune.retail.rest;

import au.com.biztune.retail.security.Secured;
import au.com.biztune.retail.service.PaymentMediaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;
import java.util.List;

/**
 * Created by arash on 10/08/2015.
 */

@Component
@Path("paymentMedia")
public class PaymentMediaRest {
    private final Logger logger = LoggerFactory.getLogger(PaymentMediaRest.class);
    @Context
    private UriInfo uriInfo;

    @Context
    private Request request;

    @Autowired
    private PaymentMediaService paymentMediaService;

    /**
     * Get All Unit Of Measures as JSON.
     * @return List of categories
     */
    @Secured
    @GET
    @Path("/getAllMediaTypes")
    @Produces(MediaType.APPLICATION_JSON)
    public List getAllMediaTypes() {
        return paymentMediaService.getAllMediaTypes();
    }

    /**
     * get all media types of specific media type.
     * @param typeId typeId
     * @return List of PaymentMedia
     */
    @Secured
    @GET
    @Path("/getOfMediatype/{typeId}")
    @Produces(MediaType.APPLICATION_JSON)
    public List getPaymentMediasOfMediaType(@PathParam("typeId") long typeId) {
        return paymentMediaService.getAllPaymentMediaOfType(typeId);
    }
}
