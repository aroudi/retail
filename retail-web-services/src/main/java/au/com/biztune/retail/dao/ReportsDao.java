package au.com.biztune.retail.dao;

import au.com.biztune.retail.domain.ReportDebtorRow;
import au.com.biztune.retail.domain.ReportGoodReceivedRow;
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
     * get Account Sales reports data.
     * @param orguId organisationId
     * @param clauseList ClauseList
     * @return report data.
     */
    List<ReportSaleRow> runRptAccountSaleReport(long orguId, List clauseList);

    /**
     * get SaleByTaxCodes reports data.
     * @param orguId organisationId
     * @param clauseList ClauseList
     * @return report data.
     */
    List<ReportSaleRow> runRptSaleByTaxCodesReport(long orguId, List clauseList);
    /**
     * get SaleByTaxCodes summary reports data.
     * @param orguId organisationId
     * @param clauseList ClauseList
     * @return report data.
     */
    List<ReportSaleRow> runRptSaleByTaxCodesSummary(long orguId, List clauseList);
    /**
     * get SaleDaily reports data.
     * @param orguId organisationId
     * @param clauseList ClauseList
     * @param orderByList orderByList
     * @return report data.
     */
    List<ReportSaleRow> runRptSalesDailyReport(long orguId, List clauseList, List orderByList);

    /**
     * get SalePeriod reports data.
     * @param orguId organisationId
     * @param clauseList ClauseList
     * @param orderByList orderByList
     * @return report data.
     */
    List<ReportSaleRow> runRptSalesPeriodReport(long orguId, List clauseList, List orderByList);

    /**
     * get 'what is selling' reports data.
     * @param orguId organisationId
     * @param clauseList ClauseList
     * @param orderByList orderByList
     * @return report data.
     */
    List<ReportSaleRow> runRptWhatIsSelling(long orguId, List clauseList, List orderByList);
    /**
     * get 'Profit By Product' reports data.
     * @param orguId organisationId
     * @param clauseList ClauseList
     * @param orderByList orderByList
     * @return report data.
     */
    List<ReportSaleRow> runRptProfitByProduct(long orguId, List clauseList, List orderByList);
    /**
     * get 'What is on order' reports data.
     * @param orguId organisationId
     * @param clauseList ClauseList
     * @param orderByList orderByList
     * @return report data.
     */
    List<ReportSaleRow> runRptPriceByGrade(long orguId, List clauseList, List orderByList);

    /**
     * get 'Goods Received' reports data.
     * @param orguId organisationId
     * @param clauseList ClauseList
     * @return report data.
     */
    List<ReportGoodReceivedRow> runRptGoodsReceived(long orguId, List clauseList);

    /**
     * get 'What is on stock' reports data.
     * @param orguId organisationId
     * @param clauseList ClauseList
     * @param orderByList orderByList
     * @return report data.
     */
    List<ReportSaleRow> runRptWhatIsInStock(long orguId, List clauseList, List orderByList);

    /**
     * get 'What is On Order' reports data.
     * @param orguId organisationId
     * @param clauseList ClauseList
     * @param orderByList orderByList
     * @return report data.
     */
    List<ReportSaleRow> runRptWhatIsOnOrder(long orguId, List clauseList, List orderByList);

    /**
     * get 'Debtor' report data.
     * @param orguId orguId
     * @param clauseList clauseList
     * @return 0report data
     */
    List<ReportDebtorRow> runRptDebtor(long orguId, List clauseList);
 }
