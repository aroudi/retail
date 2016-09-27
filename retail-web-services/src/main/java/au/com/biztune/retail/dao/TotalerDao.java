package au.com.biztune.retail.dao;

import au.com.biztune.retail.domain.TotalMediaOperator;
import au.com.biztune.retail.domain.TotalSaleOperator;
import au.com.biztune.retail.domain.TotalTaxGroup;

/**
 * Created by arash on 26/09/2016.
 */
public interface TotalerDao {

    /**
     * insert totalTaxGroup.
     * @param totalTaxGroup totalTaxGroup
     */
    void insertTotalTaxGroup(TotalTaxGroup totalTaxGroup);

    /**
     * insert totalSaleOperator.
     * @param totalSaleOperator totalSaleOperator
     */
    void insertTotalSaleOperator(TotalSaleOperator totalSaleOperator);

    /**
     * insert totalMediaOperator.
     * @param totalMediaOperator totalMediaOperator
     */
    void insertTotalMediaOperator(TotalMediaOperator totalMediaOperator);
}
