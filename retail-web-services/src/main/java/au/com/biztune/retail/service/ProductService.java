package au.com.biztune.retail.service;

import au.com.biztune.retail.domain.Product;
import au.com.biztune.retail.domain.ProductSaleItem;
import au.com.biztune.retail.domain.Supplier;
import au.com.biztune.retail.domain.UnitOfMeasure;
import au.com.biztune.retail.form.ProductForm;
import au.com.biztune.retail.response.CommonResponse;

import java.util.List;

/**
 * Created by arash on 30/03/2016.
 */
public interface ProductService {

    /**
     * add product and its related objects.
     * @param productForm productForm
     * @return CommonResponse
     */
    CommonResponse addProductWhole(ProductForm productForm);


    /**
     * get all products.
     * @return List of products
     */
    List<Product> getAllProducts();
    /**
     * get Product Detail.
     * @param prodId prodId
     * @return ProductForm
     */
    ProductForm getProductDetail(long prodId);
    /**
     * get product object per sku.
     * @param skuCode skuCode
     * @return Product
     */
    Product getProductPerSku(String skuCode);

    /**
     * get all products as Sale Items.
     * @return List of ProductSaleItem
     */
    List<ProductSaleItem> getAllProductsAsSaleItem();
    /**
     * get product object per sku.
     * @param skuCode skuCode
     * @return Product
     */
    ProductSaleItem getProductSaleItemPerSku(String skuCode);
    /**
     * get product object per sku.
     * @param reference reference
     * @return Product
     */
    ProductSaleItem getProductSaleItemPerReference(String reference);

    /**
     * get product sale item per sku.
     * @param sku sku
     * @return ProductSaleItem
     */
    ProductSaleItem getProductSaleItemPerOrguIdAndSku(String sku);
    /**
     * import product.
     * @param prodSku prodSku
     * @param prodName prodName
     * @param prodDesc prodDesc
     * @param reference reference
     * @param taxName taxName
     * @param prodBrand prodBrand
     * @param prodClass prodClass
     * @param prodType prodType
     * @param supplier supplier
     * @param catalogueNo catalogueNo
     * @param unitOfMeasure unitOfMeasure
     * @param cost cost
     * @param price price
     * @param bulkPrice bulkPrice
     * @return Product
     */
    Product addProduct(String prodSku
            , String prodName
            , String prodDesc
            , String reference
            , String taxName
            , String prodBrand
            , String prodClass
            , String prodType
            , Supplier supplier
            , String catalogueNo
            , UnitOfMeasure unitOfMeasure
            , double cost
            , double price
            , double bulkPrice);
    /**
     * get product object per sku.
     * @param prodId prodId
     * @return Product
     */
    ProductSaleItem getProductSaleItemPerProdId(long prodId);

    /**
     * get all products in paging format.
     * @param pageNo pageNo
     * @param pageSize pageSize
     * @return paging.
     */
    List<Product> getAllProductsInPagingFormat(long pageNo, long pageSize);
}
