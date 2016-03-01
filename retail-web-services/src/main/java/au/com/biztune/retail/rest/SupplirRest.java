package au.com.biztune.retail.rest;

import au.com.biztune.retail.domain.Supplier;
import au.com.biztune.retail.response.CommonResponse;
import au.com.biztune.retail.service.SupplierService;
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
@Path("supplier")
public class SupplirRest {
    private final Logger logger = LoggerFactory.getLogger(SupplirRest.class);
    @Context
    private UriInfo uriInfo;
    @Context
    private Request request;

    @Autowired
    private SupplierService supplierService;

    /**
     * Returns list of suppliers.
     * @return list of Supplier
     */

    @Path("/all")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Supplier> getAllSuppliers() {
        try {
            return supplierService.getAllSuppliers();
        } catch (Exception e) {
            logger.error ("Error in retrieving supplier List :", e);
            return null;
        }
    }

    /**
     * crate a supplier.
     * @param supplier supplier
     * @return CommonResponse
     */
    @Path("/add")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public CommonResponse addSupplier (Supplier supplier) {
        return supplierService.addSupplier(supplier);
    }

    /**
     * get supplier by Id.
     * @param id id.
     * @return Supplier
     */
    @GET
    @Path("/get/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Supplier getSupplierById (@PathParam("id") long id) {
        return supplierService.getSupplierById(id);
    }

    /**
     * get supplier by code.
     * @param code code.
     * @return Supplier
     */
    /*
    @GET
    @Path("/get/{code}")
    @Produces(MediaType.APPLICATION_JSON)
    public Supplier getSupplierByCode (@PathParam("code") String code) {
        return supplierService.getSupplierByCode(code);
    }
    */
}
