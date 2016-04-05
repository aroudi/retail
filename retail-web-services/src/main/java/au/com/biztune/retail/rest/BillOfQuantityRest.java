// Sydney Trains 2015

package au.com.biztune.retail.rest;

import au.com.biztune.retail.domain.BoqDetail;
import au.com.biztune.retail.response.CommonResponse;
import au.com.biztune.retail.service.BillOfQuantityService;
import com.sun.jersey.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;
import java.io.InputStream;
import java.util.List;

/**
 * Created by arash on 10/08/2015.
 */

@Component
@Path("billOfQuantity")
public class BillOfQuantityRest {
    private final Logger logger = LoggerFactory.getLogger(BillOfQuantityRest.class);
    @Context
    private UriInfo uriInfo;

    @Context
    private Request request;

    @Autowired
    private BillOfQuantityService billOfQuantityService;

    /**
     * upload bill of quantity.
     * @param uploadedInputStream uploadedInputStream
     * @return CommonResponse
     */
    @POST
    @Path("/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public CommonResponse uploadBillOfQuantity(@FormDataParam("file")InputStream uploadedInputStream) {
        return billOfQuantityService.uploadBillOfQuantity(uploadedInputStream);
    }

    /**
     * get BillOfQuantity Detail by BOQId.
     * @param id id.
     * @return List of BoqDetail
     */
    @GET
    @Path("/getPerBoqId/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<BoqDetail> getBoqDetailByBoqId (@PathParam("id") long id) {
        return billOfQuantityService.getBoqDetailByBoqId(id);
    }
}
