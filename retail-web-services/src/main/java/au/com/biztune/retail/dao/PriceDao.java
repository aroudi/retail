package au.com.biztune.retail.dao;

import au.com.biztune.retail.domain.Price;
import au.com.biztune.retail.domain.PriceCode;

import java.util.List;

/**
 * Created by arash on 18/03/2016.
 */
public interface PriceDao {
    /**
     * get All product prices for specific product.
     * @param prodId prodId
     * @return List of Price
     */
    List<Price> getAllProductPrice(long prodId);

    /**
     * get Product Price per code.
     * @param priceCode priceCode
     * @return Price
     */
    Price getProductPricePerCode(String priceCode);

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
}
