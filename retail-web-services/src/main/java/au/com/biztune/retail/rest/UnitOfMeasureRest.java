// Sydney Trains 2015

package au.com.biztune.retail.rest;

import au.com.biztune.retail.security.Secured;
import au.com.biztune.retail.service.UnitOfMeasureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
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
@Path("unitOfMeasure")
public class UnitOfMeasureRest {
    private final Logger logger = LoggerFactory.getLogger(UnitOfMeasureRest.class);
    @Context
    private UriInfo uriInfo;

    @Context
    private Request request;

    @Autowired
    private UnitOfMeasureService unitOfMeasureService;

    /**
     * Get All Unit Of Measures as JSON.
     * @return List of categories
     */
    @Secured
    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public List getAllUnitOfMeasures() {
        return unitOfMeasureService.getAllUnom();
    }
}
