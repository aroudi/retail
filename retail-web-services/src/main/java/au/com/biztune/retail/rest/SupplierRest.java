package au.com.biztune.retail.rest;

import au.com.biztune.retail.domain.SuppProdPrice;
import au.com.biztune.retail.domain.Supplier;
import au.com.biztune.retail.response.CommonResponse;
import au.com.biztune.retail.security.Secured;
import au.com.biztune.retail.service.SupplierImportService;
import au.com.biztune.retail.service.SupplierService;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.InputStream;
import java.util.List;

/**
 * Created by arash on 22/02/2016.
 */
@Component
@Path("supplier")
public class SupplierRest {
    private final Logger logger = LoggerFactory.getLogger(SupplierRest.class);
    @Context
    private UriInfo uriInfo;
    @Context
    private Request request;

    @Autowired
    private SupplierService supplierService;
    @Autowired
    private SupplierImportService supplierImportService;
    @Context
    private SecurityContext securityContext;

    /**
     * Returns list of suppliers.
     * @return list of Supplier
     */
    @Secured
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
    @Secured
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
    @Secured
    @GET
    @Path("/get/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Supplier getSupplierById (@PathParam("id") long id) {
        return supplierService.getSupplierById(id);
    }

    /**
     * get all products suppliy by supplier.
     * @param id id.
     * @return list of supplier's product
     */
    @Secured
    @GET
    @Path("/productList/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<SuppProdPrice> getSupplierProducts (@PathParam("id") long id) {
        return supplierService.getSupplierProducts(id);
    }
    /**
     * get all products supplier with Price by supplier.
     * @param id id.
     * @return list of supplier's product
     */
    @Secured
    @GET
    @Path("/productListWithPrice/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<SuppProdPrice> getSupplierProductsWithPrice (@PathParam("id") long id) {
        return supplierService.getSupplierProductsWithPrice(id);
    }

    /**
     * upload bill of quantity.
     * @param uploadedInputStream uploadedInputStream
     * @return CommonResponse
     */
    @Secured
    @POST
    @Path("/importCsv")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public CommonResponse importSupplier(@FormDataParam("file")InputStream uploadedInputStream) {
        try {
            return supplierImportService.importSupplierFromCsvInputStream(uploadedInputStream);
        } catch (Exception e) {
            logger.error("Exception in importing supplier from csv.", e);
            return null;
        }
    }
}
