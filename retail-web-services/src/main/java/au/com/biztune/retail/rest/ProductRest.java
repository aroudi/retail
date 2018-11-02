// Sydney Trains 2015

package au.com.biztune.retail.rest;

import au.com.biztune.retail.domain.*;
import au.com.biztune.retail.form.ProductForm;
import au.com.biztune.retail.form.ProductSearchForm;
import au.com.biztune.retail.response.CommonResponse;
import au.com.biztune.retail.security.Secured;
import au.com.biztune.retail.service.ProductImportServiceImpl;
import au.com.biztune.retail.service.ProductService;
import au.com.biztune.retail.service.StockService;
import au.com.biztune.retail.service.TransactionService;
import au.com.biztune.retail.util.IdBConstant;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.ws.rs.core.MediaType;
import java.io.InputStream;
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

    @Autowired
    private ProductImportServiceImpl productImportService;

    @Autowired
    private TransactionService transactionService;
    @Autowired
    private StockService stockService;
    @Context
    private SecurityContext securityContext;

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
     * @return ProductSaleItem
     */
    @Secured
    @GET
    @Path("/saleItem/getBySku/{skuCode}")
    @Produces(MediaType.APPLICATION_JSON)
    public ProductSaleItem getProductBySkuCode (@PathParam("skuCode") String skuCode) {
        return productService.getProductSaleItemPerOrguIdAndSku(skuCode);
    }

    /**
     * get product sale item by Id.
     * @param prodId prodId.
     * @return ProductSaleItem
     */
    @Secured
    @GET
    @Path("/saleItem/getByProdId/{prodId}")
    @Produces(MediaType.APPLICATION_JSON)
    public ProductSaleItem getProductByProdId (@PathParam("prodId") long prodId) {
        return productService.getProductSaleItemPerProdId(prodId);
    }

    /**
     * search products sale items.
     * @param searchStr searchStr
     * @return List of product sale items.
     */
    @Secured
    @GET
    @Path("/allProductSaleItem/{searchStr}")
    @Produces(MediaType.APPLICATION_JSON)
    public List getAllProductSaleItems(@PathParam("searchStr") String searchStr) {
        return productService.getAllProductsAsSaleItem(searchStr);
    }

    /**
     * upload product from csv.
     * @param uploadedInputStream uploadedInputStream
     * @return Response
     */
    @Secured
    @POST
    @Path("/uploadCsv")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public CommonResponse uploadPorductsFromCsv(@FormDataParam("file")InputStream uploadedInputStream) {
        try {
            return productImportService.importProductsFromCsvInputStream(uploadedInputStream, securityContext);
        } catch (Exception e) {
            logger.error("Exception in importing products from csv.", e);
            return null;
        }
    }

    /**
     * get all products in paging format.
     * @param pageNo pageNo
     * @param pageSize pageSize
     * @return products in pages.
     */
    @Secured
    @GET
    @Path("/all/paging/{pageNo}/{pageSize}")
    @Produces(MediaType.APPLICATION_JSON)
    public ProductSearchForm getAllProducts(@PathParam("pageNo") long pageNo, @PathParam("pageSize") long pageSize) {
        return productService.getAllProductsInPagingFormat(pageNo, pageSize);
    }

    /**
     * search product.
     * @param productSearchForm productSearchForm
     * @return ProductSearchForm
     */
    @Secured
    @Path("/searchProductPaging")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ProductSearchForm searchProductPaging (ProductSearchForm productSearchForm) {
        return productService.searchProductPaging(productSearchForm);
    }

    @Secured
    @GET
    @Path("/allProductClass")
    @Produces(MediaType.APPLICATION_JSON)
    public List getAllProductClass() {
        return productService.getAllProductClass();
    }

    /**
     * get all transactions of product.
     * @param prodId prodId
     * @return List of transactions including product
     */
    @Secured
    @Path("/getTxnOfProduct/{prodId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<ProductSale> getTransactionsOfProduct(@PathParam("prodId") long prodId) {
        try {
            return transactionService.getTransactionsOfProduct(prodId);
        } catch (Exception e) {
            logger.error ("Error in retrieving transactions of product :", e);
            return null;
        }
    }

    /**
     * get all invoices of product.
     * @param prodId prodId
     * @return List of invoices including product
     */
    @Secured
    @Path("/getInvoiceOfProduct/{prodId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<ProductSale> getInvoicesOfProduct(@PathParam("prodId") long prodId) {
        try {
            return transactionService.getInvoicesOfProduct(prodId);
        } catch (Exception e) {
            logger.error ("Error in retrieving invoice of product :", e);
            return null;
        }
    }

    /**
     * update product price list in bulk.
     * @param updatedPriceList list of updated price.
     * @return Response.
     */
    @Secured
    @Path("/updateProductPriceInBulk")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public CommonResponse updateProductPricesInBulk (List<SuppProdPrice> updatedPriceList) {
        return productService.updateSupplierProductPricesInBulk(updatedPriceList, securityContext);
    }

    /**
     * upload product from csv.
     * @param uploadedInputStream uploadedInputStream
     * @return Response
     */
    @Secured
    @POST
    @Path("/updateProductPriceFromCsv")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public CommonResponse updatePorductPriceFromCsv(@FormDataParam("file")InputStream uploadedInputStream) {
        try {
            return productImportService.updateProductPriceFromCsvInputStream(uploadedInputStream, securityContext);
        } catch (Exception e) {
            logger.error("Exception in updating product price from csv.", e);
            return null;
        }
    }

    /**
     * get product reservation info.
     * @param prodId prodId
     * @return product reservation info
     */
    @Secured
    @Path("/getProductReservationInfo/{prodId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<StockReserve> getProductReservationInfo(@PathParam("prodId") long prodId) {
        try {
            return productService.getProductReservationInfo(prodId);
        } catch (Exception e) {
            logger.error ("Error in retrieving product reservation info :", e);
            return null;
        }
    }

    /**
     * view product audit trail.
     * @param prodId prodId
     * @return product audit trail info.
     */
    @Secured
    @Path("/getProductAuditTrail/{prodId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<StockEvent> getProductAuditTrailInfo(@PathParam("prodId") long prodId) {
        try {
            return stockService.viewProductAuditTrail(prodId);
        } catch (Exception e) {
            logger.error ("Error in retrieving product audit trail info :", e);
            return null;
        }
    }

    /**
     * check if product exists per sku and ref.
     * @param sku sku
     * @param ref ref
     * @return success if exists otherwise return failure
     */
    @Secured
    @Path("/checkIfProductExists/{sku}/{ref}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public CommonResponse checkIfProductExists (@PathParam("sku") String sku, @PathParam("ref") String ref) {
        final CommonResponse response = new CommonResponse();
        if (productService.checkIfProductExistsPerSkuAndReference(sku, ref)) {
            response.setStatus(IdBConstant.RESULT_FAILURE);
            response.setMessage("Product exists");
        } else {
            response.setStatus(IdBConstant.RESULT_SUCCESS);
            response.setMessage("Product does not exists");
        }
        return response;
    }

    /**
     * delete product logically.
     * @param productIdList product id list to be deleted.
     * @return response.
     */
    @Secured
    @Path("/logicalDelete")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public CommonResponse logicalDelete (List<Long> productIdList) {
        return productService.logicalDeleteProduct(productIdList);
    }
}
