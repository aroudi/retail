package au.com.biztune.retail.dao;

import au.com.biztune.retail.domain.ProductPurchaseItem;
import au.com.biztune.retail.domain.SuppProdPrice;

import java.util.List;

/**
 * Created by arash on 22/03/2016.
 */
public interface SuppProdPriceDao {
    /**
     * get supplier Produc Price per OrgUId, ProdId and SuppId.
     * @param orguId orguId
     * @param prodId prodId
     * @param suppId suppId
     * @return SuppProdPrice
     */
    SuppProdPrice getSuppProdPriceByOrguIdAndProdIdAndSuppId(long orguId, long prodId, long suppId);

    /**
     * check if supplier specific product exists..
     * @param orguId orguId
     * @param prodId prodId
     * @param suppId suppId
     * @return SuppProdPrice
     */
    SuppProdPrice checkIfSupplierProductExistsPerOrguIdAndProdIdAndSuppId(long orguId, long prodId, long suppId);

    /**
     * get default suppliers for a product.
     * @param orguId orguId
     * @param prodId prodId
     * @return SuppProdPrice for default suppliers.
     */
    List<SuppProdPrice> getDefaultSuppliersPerOrguIdAndProdId(long orguId, long prodId);
    /**
     * get supplier Produc Price per OrgUId, ProdId.
     * @param orguId orguId
     * @param prodId prodId
     * @return SuppProdPrice
     */
    List<SuppProdPrice> getAllSuppProdPricesByOrguIdAndProdId(long orguId, long prodId);

    /**
     * get all products (and their RRP price) of specific supplier.
     * @param orguId organisation unit id.
     * @param suppId supplier id.
     * @return List of all products for specific supplier
     */
    List<SuppProdPrice> getAllProductPurchaseItemsWithRrpPerOrgUnitIdAndSuppId(long orguId, long suppId);
    /**
     * get all supplier prices for specific product.
     * @param orgUnitId orgUnitId
     * @param suppId suppId
     * @param searchStr searchStr
     * @return List of SuppProdPrice
     */
    List<ProductPurchaseItem> getAllProductPurchaseItemsPerOrgUnitIdAndSuppId(long orgUnitId, long suppId, String searchStr);



    /**
     * get Product Purchase Item Per SprcId.
     * @param sprcId sprcId
     * @return List of SuppProdPrice
     */
    ProductPurchaseItem getProductPurchaseItemPerSprcId(long sprcId);


    /**
     * get Product Purchase Item Per Orgunit Id, ProdId and SuppId.
     * @param orguId orguId
     * @param prodId prodId
     * @param suppId suppId
     * @return List of SuppProdPrice
     */
    ProductPurchaseItem getProductPurchaseItemByOrguIdAndProdIdAndSuppId(long orguId, long prodId, long suppId);

    /**
     * get all supplier prices for specific product.
     * @param prodId prodId
     * @return List of SuppProdPrice
     */
    List<SuppProdPrice> getAllSuppProdPricesByProdId(long prodId);

    /**
     * insert Supplier Product Price.
     * @param suppProdPrice suppProdPrice
     */
    void insert(SuppProdPrice suppProdPrice);

    /**
     * update values on suppProdPrice.
     * @param suppProdPrice suppProdPrice.
     */
    void updateValues(SuppProdPrice suppProdPrice);
    /**
     * delete supplier product price per id.
     * @param id id
     */
    void deleteSuppProdPricePerId(long id);

    /**
     * delete supplier prod price per solid and prod id.
     * @param solId solId
     * @param prodId prodId
     */
    void deleteSuppProdPricePerSolIdAndProdId(long solId, long prodId);

    /**
     * delete supplier prod price per solid and prod id.
     * @param prodId prodId
     * @param orguId orguId
     */
    void deleteSuppProdPricePerProdIdAndOrguId(long prodId, long orguId);

    /**
     * get all products supply by specific supplier.
     * @param orguId orguId
     * @param suppId suppId
     * @return List of SuppProdPrice.
     */
    List<SuppProdPrice> getAllSupplierProducts(long orguId, long suppId);

    /**
     * get product from specic supplier by catalog no.
     * @param orguId orguId
     * @param suppId suppId
     * @param catalogueNo catalogueNo
     * @return ProductPurchaseItem
     */
    ProductPurchaseItem getProductPurchaseItemPerOrgUnitIdAndSuppIdAndCatalogId(long orguId, long suppId, String catalogueNo);
    /**
     * get product from specic supplier by catalog no.
     * @param orguId orguId
     * @param supplierName supplierName
     * @param catalogueNo catalogueNo
     * @return ProductPurchaseItem
     */

    List<SuppProdPrice> getSuppProdPricePerOrgUnitIdAndSupplierNameAndCatalogNo(long orguId, String supplierName, String catalogueNo);
    /**
     * update product costs per solId and ProdId.
     * @param solId Supplier-OrgU-Id
     * @param prodId product id
     * @param cost product cost
     * @param price product RRP
     * @param bulkPrice product bulk price
     */
    void updateProductCostsPerSolIdAndProdId(long solId, long prodId, double cost, double price, double bulkPrice);

    /**
     * update product price and bulk price per sprc id.
     * @param sprcId sprcId
     * @param price product cost
     * @param bulkPrice bulk price.
     */
    void updateProductCostsPerSprcId(long sprcId, double price, double bulkPrice);

    /**
     * mark product as deleted.
     * @param prodId product id
     */
    void markProductAsDeletedPerProdId(long prodId);

    /**
     * mark supplier as deleted.
     * @param solId supplier-orgu-id
     */
    void markSupplierAsDeletedPerSolId(long solId);

    /**
     * delete supplier product price per Supplier Orgu Link Id.
     * @param solId solId
     */
    void deleteSuppProdPricePerSolId(long solId);
}
