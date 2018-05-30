package au.com.biztune.retail.report;

import au.com.biztune.retail.dao.ReportsDao;
import au.com.biztune.retail.domain.*;
import au.com.biztune.retail.session.SessionState;
import au.com.biztune.retail.util.IdBConstant;
import au.com.biztune.retail.util.SearchClause;
import au.com.biztune.retail.util.SearchClauseBuilder;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.StreamingOutput;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by arash on 21/05/2018.
 */
public class ReportManager {
    private final Logger logger = LoggerFactory.getLogger(ReportManager.class);
    @Autowired
    private ReportsDao reportsDao;
    private Resource reportPath;
    private String rptSalesByMonth;
    private String rptSalesByTaxCodes;
    @Autowired
    private SessionState sessionState;
    /**
     * Run Report.
     * @param reportKey reportKey
     * @param reportParamList reportParamList
     * @param securityContext securityContext
     * @return PDF as output stream.
     */
    public StreamingOutput runReport(String reportKey, List<ReportParam> reportParamList, SecurityContext securityContext) {
        StreamingOutput streamingOutput = null;
        try {
            final Principal principal = securityContext.getUserPrincipal();
            AppUser appUser = null;
            if (principal instanceof AppUser) {
                appUser = (AppUser) principal;
            }
            List<SearchClause> searchClauseList = null;
            final String pathStr = reportPath.getURL().getPath();
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            List<ReportSaleRow> rowList = null;
            String reportJrxmlName = null;
            JRBeanCollectionDataSource beanColDataSource = null;
            String outputFile = null;
            final Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("SUBREPORT_DIR", pathStr + "/");
            switch (reportKey) {
                case IdBConstant.REPORT_KEY_SALES_BY_MONTH :
                    //fetch searchClause list from report params.
                    searchClauseList = SearchClauseBuilder.buildReportingSearchWhereCluase(reportParamList);
                    populateReportParams(parameters, searchClauseList);
                    rowList = reportsDao.runRptSaleByMonthReport(sessionState.getOrgUnit().getId(),
                            searchClauseList);
                            beanColDataSource = new JRBeanCollectionDataSource(rowList);
                            reportJrxmlName = pathStr + "/" + rptSalesByMonth + ".jrxml";
                             outputFile = pathStr + "/" + rptSalesByMonth + ".pdf";
                    break;
                case IdBConstant.REPORT_KEY_SALES_BY_TAX_CODE :
                    //fetch searchClause list from report params.
                    searchClauseList = SearchClauseBuilder.buildReportingSearchWhereCluase(reportParamList);
                    populateReportParams(parameters, searchClauseList);
                    rowList = reportsDao.runRptSaleByTaxCodesReport(sessionState.getOrgUnit().getId(),
                            searchClauseList);
                    beanColDataSource = new JRBeanCollectionDataSource(rowList);
                    reportJrxmlName = pathStr + "/" + rptSalesByTaxCodes + ".jrxml";
                    outputFile = pathStr + "/" + rptSalesByTaxCodes + ".pdf";
                    break;
                default:
                    break;
            }

            /* Compile the master and sub report */
            final JasperReport jasperMasterReport = JasperCompileManager.compileReport(reportJrxmlName);
            //parameters.put("REPORT_PARAMS", searchForm);
            final JasperPrint jasperPrint = JasperFillManager.fillReport(jasperMasterReport, parameters, beanColDataSource);
            final JRPdfExporter exporter = new JRPdfExporter();
            exporter.setParameter(JRPdfExporterParameter.JASPER_PRINT, jasperPrint);
            exporter.setParameter(JRPdfExporterParameter.OUTPUT_STREAM, byteArrayOutputStream);
            exporter.exportReport();
            //JasperExportManager.exportReportToPdfStream(jasperPrint, byteArrayOutputStream);

            streamingOutput = new StreamingOutput() {
            @Override
            public void write(OutputStream output) throws IOException, WebApplicationException {
                final byte[] bytes = byteArrayOutputStream.toByteArray();
                output.write(bytes);
                logger.info("PDF content = " + bytes.toString());
                logger.info("PDF length = " + bytes.length);
                output.flush();
            }
            };
            return streamingOutput;
        } catch (Exception e) {
            logger.error("Exception in exporting accounting journal as pdf: ", e);
            return null;
        }
    }
    /**
     * populate search parameters out from search clause list.
     * @param reportParams report params sending to jasper.
     * @param searchClauseList search clause of reports.
     */
    private void populateReportParams(Map<String, Object> reportParams, List<SearchClause> searchClauseList) {
        if (searchClauseList == null || searchClauseList.size() < 1 || reportParams == null) {
            return;
        }
        for (SearchClause searchClause : searchClauseList) {
            reportParams.put(searchClause.getParamName(), searchClause.getParamVal());
        }
    }
    public Resource getReportPath() {
        return reportPath;
    }
    public void setReportPath(Resource reportPath) {
        this.reportPath = reportPath;
    }

    public ReportsDao getReportsDao() {
        return reportsDao;
    }

    public void setReportsDao(ReportsDao reportsDao) {
        this.reportsDao = reportsDao;
    }

    public String getRptSalesByMonth() {
        return rptSalesByMonth;
    }

    public void setRptSalesByMonth(String rptSalesByMonth) {
        this.rptSalesByMonth = rptSalesByMonth;
    }

    public SessionState getSessionState() {
        return sessionState;
    }

    public void setSessionState(SessionState sessionState) {
        this.sessionState = sessionState;
    }

    public String getRptSalesByTaxCodes() {
        return rptSalesByTaxCodes;
    }

    public void setRptSalesByTaxCodes(String rptSalesByTaxCodes) {
        this.rptSalesByTaxCodes = rptSalesByTaxCodes;
    }
}
