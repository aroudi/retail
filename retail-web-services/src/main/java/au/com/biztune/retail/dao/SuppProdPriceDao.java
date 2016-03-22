package au.com.biztune.retail.dao;

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
     * get supplier Produc Price per OrgUId, ProdId.
     * @param orguId orguId
     * @param prodId prodId
     * @return SuppProdPrice
     */
    List<SuppProdPrice> getAllSuppProdPricesByOrguIdAndProdId(long orguId, long prodId);

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
}
