package au.com.biztune.retail.dao;

import au.com.biztune.retail.domain.ReportSaleRow;
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
    List<ReportSaleRow> runRptSaleByMonthReport(long orguId, List clauseList);
    /**
     * get SaleByTaxCodes reports data.
     * @param orguId organisationId
     * @param clauseList ClauseList
     * @return report data.
     */
    List<ReportSaleRow> runRptSaleByTaxCodesReport(long orguId, List clauseList);
}
