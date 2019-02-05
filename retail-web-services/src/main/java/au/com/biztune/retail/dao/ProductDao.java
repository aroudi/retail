package au.com.biztune.retail.dao;

import au.com.biztune.retail.domain.ProdOrguLink;
import au.com.biztune.retail.domain.Product;
import au.com.biztune.retail.domain.ProductSaleItem;
import au.com.biztune.retail.domain.ProuTxrlLink;
import au.com.biztune.retail.util.SearchClause;
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
    List<Product> getProductPerSku(String skuCode);

    /**
     * get all product per orguId and reference.
     * @param skuCode skuCode
     * @param refCode refCode
     * @return List of Product
     */
    List<Product> getProductPerSkuAndRef(String skuCode, String refCode);

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
     * @param searchStr searchStr
     * @return List of Product
     */
    List<ProductSaleItem> getAllProductSaleItemsPerOrgUnitId(String searchStr);

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
    List<ProductSaleItem> getProductSaleItemPerSku(String sku);


    /**
     * get Product Sale Item per id.
     * @param prodId prodId
     * @return List of ProductSaleItem
     */
    ProductSaleItem getProductSaleItemPerProdId(long prodId);

    /**
     * get product sale item per orguid and sku.
     * @param orguId orguId
     * @param sku sku
     * @return ProductSaleItem
     */
    List<ProductSaleItem> getProductSaleItemPerOrgUnitIdAndSku(long orguId, String sku);

    /**
     * get product list in page format.
     * @param orguId orguId
     * @param fromIndex fromIndex
     * @param toIndex toIndex
     * @return product list in paging mechanism.
     */
    List<Product> getAllProductsPerOrgUnitIdPaging(long orguId, long fromIndex, long toIndex);

    /**
     * get product list in page format.
     * @param orguId orguId
     * @param fromIndex fromIndex
     * @param toIndex toIndex
     * @return product list in paging mechanism.
     */
    List<Product> getAllProductExtendedPerOrgUnitIdPaging(long orguId, long fromIndex, long toIndex);
    /**
     * search product.
     * @param orguId orguId
     * @param fromIndex fromIndex
     * @param toIndex toIndex
     * @param searchClauses searchClauses
     * @return Product List
     */
    List<Product> searchProductsPaging(long orguId, long fromIndex, long toIndex, List<SearchClause> searchClauses);
    /**
     * search product.
     * @param orguId orguId
     * @param fromIndex fromIndex
     * @param toIndex toIndex
     * @param searchClauses searchClauses
     * @return Product List
     */
    List<Product> searchProductsExtendedPaging(long orguId, long fromIndex, long toIndex, List<SearchClause> searchClauses);
    /**
     * get all product class.
     * @return list of product class.
     */
    List<String> getAllProductClass();

    /**
     * get product search total records.
     * @param orguId orguId
     * @param searchClauses searchClauses
     * @return product search total records.
     */
    long searchProductsTotalRecords(long orguId, List<SearchClause> searchClauses);

    /**
     * check if product exists per sku and ref.
     * @param sku sku code.
     * @param ref reference
     * @return list of products match
     */
    List<Product> checkProductExistPerSkuAndRef(String sku, String ref);

    /**
     * update product status.
     * @param statusId new status id
     * @param productIdList product id list
     * @param orguId organisation unit id
     */
    void updateProductStatus(long statusId, long orguId, List productIdList);

    /**
     * update product description per prod id.
     * @param prodDesc product description
     * @param prodId product id.
     */
    void updateProductDescription(String prodDesc, long prodId);
}
