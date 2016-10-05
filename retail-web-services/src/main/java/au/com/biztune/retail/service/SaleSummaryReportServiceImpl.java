package au.com.biztune.retail.service;

import au.com.biztune.retail.dao.TotalerDao;
import au.com.biztune.retail.domain.SaleSummaryReport;
import au.com.biztune.retail.form.SaleSummaryReportForm;
import au.com.biztune.retail.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by arash on 5/10/2016.
 */
@Component
public class SaleSummaryReportServiceImpl implements SaleSummaryReportService {
    @Autowired
    private TotalerDao totalerDao;
    private final Logger logger = LoggerFactory.getLogger(SaleSummaryReportServiceImpl.class);

    /**
     * get sale summary report in specific period.
     * @param saleSummaryReportForm saleSummaryReportForm
     * @return Sale Summary Report
     */
    public SaleSummaryReport getSaleSummaryReport(SaleSummaryReportForm saleSummaryReportForm) {
        try {
            Timestamp fromDate = null;
            Timestamp toDate = null;
            if (saleSummaryReportForm == null) {
                return  null;
            }
            fromDate = DateUtil.stringToDate(saleSummaryReportForm.getFromDate(), "yyyy-MM-dd");
            if (fromDate == null) {
                final Date date = new GregorianCalendar(2016, Calendar.JANUARY, 01).getTime();
                fromDate = new Timestamp(date.getTime());
            }

            toDate = DateUtil.stringToDate(saleSummaryReportForm.getToDate(), "yyyy-MM-dd");
            if (toDate == null) {
                final Date date = new Date();
                toDate = new Timestamp(date.getTime());
            }
            final SaleSummaryReport saleSummaryReport = new SaleSummaryReport();
            saleSummaryReport.setTotalSaleFigures(totalerDao.getTotalSaleReportPerDate(fromDate, toDate));
            saleSummaryReport.setTotalOperatorSaleFigures(totalerDao.getTotalOperatorSaleReportPerDate(fromDate, toDate));

            return saleSummaryReport;
        } catch (Exception e) {
            logger.error("Error in extracting sale summary report", e);
            return null;
        }
    }
}
