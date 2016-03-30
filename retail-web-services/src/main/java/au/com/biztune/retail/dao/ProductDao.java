package au.com.biztune.retail.dao;

import au.com.biztune.retail.domain.ProdOrguLink;
import au.com.biztune.retail.domain.Product;
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
}
