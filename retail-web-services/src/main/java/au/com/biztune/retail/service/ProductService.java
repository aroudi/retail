package au.com.biztune.retail.service;

import au.com.biztune.retail.domain.Product;
import au.com.biztune.retail.domain.ProductSaleItem;
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
}
