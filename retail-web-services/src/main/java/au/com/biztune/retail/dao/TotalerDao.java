package au.com.biztune.retail.dao;

import au.com.biztune.retail.domain.TotalMediaOperator;
import au.com.biztune.retail.domain.TotalSaleOperator;
import au.com.biztune.retail.domain.TotalTaxGroup;

import java.sql.Timestamp;
import java.util.List;

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

    /**
     * get total sale report for specific time period.
     * @param fromDate fromDate
     * @param toDate toDate
     * @return TotalSaleOperator
     */
    TotalSaleOperator getTotalSaleReportPerDate (Timestamp fromDate, Timestamp toDate);

    /**
     * get total sale per operator in specific time period.
     * @param fromDate fromDate
     * @param toDate toDate
     * @return List of TotalSaleOperator
     */
    List<TotalSaleOperator> getTotalOperatorSaleReportPerDate(Timestamp fromDate, Timestamp toDate);
}
