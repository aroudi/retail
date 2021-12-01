package au.com.biztune.retail.dao;

import au.com.biztune.retail.domain.Price;
import au.com.biztune.retail.domain.PriceCode;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by arash on 18/03/2016.
 */
@Mapper
public interface PriceDao {
    /**
     * get All product prices for specific product.
     * @param prodId prodId
     * @return List of Price
     */
    List<Price> getAllProductPrice(long prodId);

    /**
     * get Product Price per code.
     * @param prodId prodId
     * @param priceCode priceCode
     * @return Price
     */
    Price getProductPricePerProdIdAndCode(long prodId, String priceCode);

    /**
     * get Product Selling Price per Product Id.
     * @param  prodId prodId
     * @return Price.
     */
    Price getProductSellPricePerProdId(long prodId);

    /**
     * get Product Cost per Product Id. the product cost is the cost of default supplier
     * @param  prodId prodId
     * @return Price.
     */
    Price getProductCostPricePerProdId(long prodId);

    /**
     * get Product Price Per Id.
     * @param priceId priceId
     * @return Price
     */
    Price getProductPricePerPriceId(long priceId);


    /**
     * insert product price.
     * @param price price
     */
    void insert (Price price);

    /**
     * get product price code per code.
     * @param code code
     * @return PriceCode
     */
    PriceCode getProductPriceCodePerCode(String code);

    /**
     * dlete product price per id.
     * @param id id
     */
    void deleteProdPrice(long id);

    /**
     * dlete product price per prodId.
     * @param prodId prodId
     */
    void deleteProdPricePerProdId(long prodId);

    /**
     * update price.
     * @param prodId productId
     * @param priceCodeId Price Code Id
     * @param newPrice new price
     */
    void updatePricePerProdIdAndPrccId(long prodId, long priceCodeId, double newPrice);
}
