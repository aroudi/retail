package au.com.biztune.retail.dao;

import au.com.biztune.retail.domain.ProdOrguLink;
import au.com.biztune.retail.domain.Product;
import au.com.biztune.retail.domain.ProductSaleItem;
import au.com.biztune.retail.domain.ProuTxrlLink;

import java.util.List;

/**
 * Created by arash on 18/03/2016.
 */
public interface ProductDao {
    /**
     * Add new Product.
     * @param product product
     */
    void insertProduct(Product product);

    /**
     * UPDATE Product.
     * @param product product
     */
    void updateProduct(Product product);


    /**
     * insert prouduct tax link.
     * @param prouTxrlLink prouTxrlLink
     */
    void insertProdTaxLink(ProuTxrlLink prouTxrlLink);

    /**
     * insert prouduct orgu link.
     * @param prodOrguLink prodOrguLink
     */
    void insertProdOrguLink(ProdOrguLink prodOrguLink);

    /**
     * get all products.
     * @param orguId orguId
     * @return List of Product
     */

    List<Product> getAllProductsPerOrgUnitId(long orguId);

    /**
     * get all product per orguId and prodId.
     * @param orguId orguId
     * @param prodId prodId
     * @return List of Product
     */
    Product getProductPerOrgUnitIdAndProdId(long orguId, long prodId);

    /**
     * get all product per orguId and prodId.
     * @param prodId prodId
     * @return List of Product
     */
    Product getProductPerProdId(long prodId);

    /**
     * get all product per orguId and reference.
     * @param reference reference
     * @return List of Product
     */
    Product getProductPerReference(String reference);


    /**
     * get all product per orguId and reference.
     * @param skuCode skuCode
     * @return List of Product
     */
    Product getProductPerSku(String skuCode);

    /**
     * delete product orgu link.
     * @param prouId prouId
     */
    void deleteProdOrguLink (long prouId);

    /**
     * delete product orgu link.
     * @param prouId prouId
     */
    void deleteProdTaxLink (long prouId);

    /**
     * delete product by id.
     * @param prodId prodId
     */
    void deleteProduct(long prodId);

    /**
     * get all product Sale Items per orgunitid. this is used for Transaction Sale.
     * @param orguId orguId
     * @return List of Product
     */
    List<ProductSaleItem> getAllProductSaleItemsPerOrgUnitId(long orguId);

    /**
     * get Product Sale Item per reference.
     * @param reference reference
     * @return List of Product
     */
    ProductSaleItem getProductSaleItemPerReference(String reference);

    /**
     * get Product Sale Item per sku.
     * @param sku sku
     * @return List of Product
     */
    ProductSaleItem getProductSaleItemPerSku(String sku);


    /**
     * get Product Sale Item per id.
     * @param prodId prodId
     * @return List of ProductSaleItem
     */
    ProductSaleItem getProductSaleItemPerProdId(long prodId);
}
