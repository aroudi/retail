// Sydney Trains 2015

package au.com.biztune.retail.rest;

import au.com.biztune.retail.domain.Product;
import au.com.biztune.retail.form.ProductForm;
import au.com.biztune.retail.response.CommonResponse;
import au.com.biztune.retail.security.Secured;
import au.com.biztune.retail.service.ProductService;
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
 * Created by arash on 10/08/2015.
 */

@Component
@Path("product")
public class ProductRest {
    private final Logger logger = LoggerFactory.getLogger(ProductRest.class);
    @Context
    private UriInfo uriInfo;

    @Context
    private Request request;

    @Autowired
    private ProductService productService;

    /**
     * Get All Unit Of Measures as JSON.
     * @return List of categories
     */
    @Secured
    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public List getAllProducts() {
        return productService.getAllProducts();
    }

    /**
     * crate a supplier.
     * @param productForm productForm
     * @return CommonResponse
     */
    @Secured
    @Path("/add")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public CommonResponse addProduct (ProductForm productForm) {
        return productService.addProductWhole(productForm);
    }
    /**
     * get supplier by Id.
     * @param id id.
     * @return CommonResponse
     */
    @Secured
    @GET
    @Path("/get/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public ProductForm getProductById (@PathParam("id") long id) {
        return productService.getProductDetail(id);
    }

    /**
     * get supplier by Id.
     * @param skuCode skuCode.
     * @return Product
     */
    @Secured
    @GET
    @Path("/getBySku/{skuCode}")
    @Produces(MediaType.APPLICATION_JSON)
    public Product getProductBySkuCode (@PathParam("skuCode") String skuCode) {
        return productService.getProductPerSku(skuCode);
    }

    /**
     * Get All Product Item Sales.
     * @return List ProductSaleItem
     */
    @Secured
    @GET
    @Path("/allProductSaleItem")
    @Produces(MediaType.APPLICATION_JSON)
    public List getAllProductSaleItems() {
        return productService.getAllProductsAsSaleItem();
    }

}
