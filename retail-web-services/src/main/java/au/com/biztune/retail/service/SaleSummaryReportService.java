package au.com.biztune.retail.service;

import au.com.biztune.retail.domain.SaleSummaryReport;
import au.com.biztune.retail.form.SaleSummaryReportForm;

/**
 * Created by arash on 5/10/2016.
 */
public interface SaleSummaryReportService {
    /**
     * get sale summary report in specific period.
     * @param saleSummaryReportForm saleSummaryReportForm
     * @return Sale Summary Report
     */
    SaleSummaryReport getSaleSummaryReport(SaleSummaryReportForm saleSummaryReportForm);
}
