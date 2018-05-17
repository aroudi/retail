package au.com.biztune.retail.dao;

import au.com.biztune.retail.domain.RptSaleByMonthRow;

import java.util.List;

/**
 * CREATED BY ARASH ROUDI ON 16/05/2018.
 */
public interface ReportsDao {
    /**
     * get SaleByMonth reports data.
     * @param orguId organisationId
     * @param clauseList ClauseList
     * @return report data.
     */
    List<RptSaleByMonthRow> runRptSaleByMonthReport(long orguId, List clauseList);
}
