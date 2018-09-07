package au.com.biztune.retail.report;
import au.com.biztune.retail.dao.CustomerDao;
import au.com.biztune.retail.dao.ReportsDao;
import au.com.biztune.retail.dao.SupplierDao;
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
    @Autowired
    private SupplierDao supplierDao;
    @Autowired
    private CustomerDao customerDao;
    private Resource reportPath;
    private String rptAccountSale;
    private String rptSalesByMonth;
    private String rptSalesByTaxCodes;
    private String rptSalesByTaxCodesSummary;
    private String rptSalesdaily;
    private String rptSalesPeriod;
    private String rptWhatIsSelling;
    private String rptProfitByProduct;
    private String rptWhatIsOnOrder;
    private String rptPriceByGrade;
    private String rptGoodsReceived;
    private String rptWhatIsInStock;
    private String rptSupplierList;
    private String rptCustomerList;
    private String rptDebtor;
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
            List<SearchClause> additionalSearchClauseList = null;
            parameters.put("SUBREPORT_DIR", pathStr + "/");
            //fetch searchClause list from report params.
            searchClauseList = SearchClauseBuilder.buildReportingSearchWhereCluase(reportParamList);
            populateReportParams(parameters, searchClauseList);
            switch (reportKey) {
                case IdBConstant.REPORT_KEY_ACCOUNT_SALES :
                    rowList = reportsDao.runRptAccountSaleReport(sessionState.getOrgUnit().getId(),
                            searchClauseList);
                            beanColDataSource = new JRBeanCollectionDataSource(rowList);
                            reportJrxmlName = pathStr + "/" + rptAccountSale + ".jrxml";
                             outputFile = pathStr + "/" + rptAccountSale + ".pdf";
                    break;
                case IdBConstant.REPORT_KEY_SALES_BY_MONTH :
                    rowList = reportsDao.runRptSaleByMonthReport(sessionState.getOrgUnit().getId(),
                            searchClauseList);
                    beanColDataSource = new JRBeanCollectionDataSource(rowList);
                    reportJrxmlName = pathStr + "/" + rptSalesByMonth + ".jrxml";
                    outputFile = pathStr + "/" + rptSalesByMonth + ".pdf";
                    break;
                case IdBConstant.REPORT_KEY_SALES_BY_TAX_CODE :
                    rowList = reportsDao.runRptSaleByTaxCodesReport(sessionState.getOrgUnit().getId(),
                            searchClauseList);
                    beanColDataSource = new JRBeanCollectionDataSource(rowList);
                    reportJrxmlName = pathStr + "/" + rptSalesByTaxCodes + ".jrxml";
                    outputFile = pathStr + "/" + rptSalesByTaxCodes + ".pdf";
                    break;
                case IdBConstant.REPORT_KEY_SALES_BY_TAX_CODE_SUMMARY :
                    rowList = reportsDao.runRptSaleByTaxCodesSummary(sessionState.getOrgUnit().getId(),
                            searchClauseList);
                    beanColDataSource = new JRBeanCollectionDataSource(rowList);
                    reportJrxmlName = pathStr + "/" + rptSalesByTaxCodesSummary + ".jrxml";
                    outputFile = pathStr + "/" + rptSalesByTaxCodesSummary + ".pdf";
                    break;
                case IdBConstant.REPORT_KEY_SALES_DAILY :
                    additionalSearchClauseList = SearchClauseBuilder.buildReportingSearchAdditionalClauseList(reportParamList, IdBConstant.REPORTS_PARAM_GROUPBY);
                    //add new parameter for GroupBy
                    populateReportParams(parameters, additionalSearchClauseList);

                    rowList = reportsDao.runRptSalesDailyReport(sessionState.getOrgUnit().getId(),
                            searchClauseList, additionalSearchClauseList);
                    beanColDataSource = new JRBeanCollectionDataSource(rowList);
                    reportJrxmlName = pathStr + "/" + rptSalesdaily + ".jrxml";
                    outputFile = pathStr + "/" + rptSalesdaily + ".pdf";
                    break;
                case IdBConstant.REPORT_KEY_SALES_PERIOD :
                    additionalSearchClauseList = SearchClauseBuilder.buildReportingSearchAdditionalClauseList(reportParamList, IdBConstant.REPORTS_PARAM_GROUPBY);
                    //add new parameter for GroupBy
                    populateReportParams(parameters, additionalSearchClauseList);

                    rowList = reportsDao.runRptSalesPeriodReport(sessionState.getOrgUnit().getId(),
                            searchClauseList, additionalSearchClauseList);
                    beanColDataSource = new JRBeanCollectionDataSource(rowList);
                    reportJrxmlName = pathStr + "/" + rptSalesPeriod + ".jrxml";
                    outputFile = pathStr + "/" + rptSalesPeriod + ".pdf";
                    break;
                case IdBConstant.REPORT_KEY_WHAT_IS_SELLING :
                    additionalSearchClauseList = SearchClauseBuilder.buildReportingSearchAdditionalClauseList(reportParamList, IdBConstant.REPORTS_PARAM_GROUPBY);
                    //add new parameter for GroupBy
                    populateReportParams(parameters, additionalSearchClauseList);

                    rowList = reportsDao.runRptWhatIsSelling(sessionState.getOrgUnit().getId(),
                            searchClauseList, additionalSearchClauseList);
                    beanColDataSource = new JRBeanCollectionDataSource(rowList);
                    reportJrxmlName = pathStr + "/" + rptWhatIsSelling + ".jrxml";
                    outputFile = pathStr + "/" + rptWhatIsSelling + ".pdf";
                    break;
                case IdBConstant.REPORT_KEY_PROFIT_BY_PRODUCT :
                    additionalSearchClauseList = SearchClauseBuilder.buildReportingSearchAdditionalClauseList(reportParamList, IdBConstant.REPORTS_PARAM_GROUPBY);
                    //add new parameter for GroupBy
                    populateReportParams(parameters, additionalSearchClauseList);

                    rowList = reportsDao.runRptProfitByProduct(sessionState.getOrgUnit().getId(),
                            searchClauseList, additionalSearchClauseList);
                    beanColDataSource = new JRBeanCollectionDataSource(rowList);
                    reportJrxmlName = pathStr + "/" + rptProfitByProduct + ".jrxml";
                    outputFile = pathStr + "/" + rptProfitByProduct + ".pdf";
                    break;
                case IdBConstant.REPORT_KEY_PRICE_BY_GRADE:
                    additionalSearchClauseList = SearchClauseBuilder.buildReportingSearchAdditionalClauseList(reportParamList, IdBConstant.REPORTS_PARAM_GROUPBY);
                    //add new parameter for GroupBy
                    populateReportParams(parameters, additionalSearchClauseList);

                    rowList = reportsDao.runRptPriceByGrade(sessionState.getOrgUnit().getId(),
                            searchClauseList, additionalSearchClauseList);
                    beanColDataSource = new JRBeanCollectionDataSource(rowList);
                    reportJrxmlName = pathStr + "/" + rptPriceByGrade + ".jrxml";
                    outputFile = pathStr + "/" + rptPriceByGrade + ".pdf";
                    break;
                default:
                    break;
                case IdBConstant.REPORT_KEY_GOODS_RECEIVED :
                    List <ReportGoodReceivedRow> rowList1 = reportsDao.runRptGoodsReceived(sessionState.getOrgUnit().getId(),
                            searchClauseList);
                    beanColDataSource = new JRBeanCollectionDataSource(rowList1);
                    reportJrxmlName = pathStr + "/" + rptGoodsReceived + ".jrxml";
                    outputFile = pathStr + "/" + rptGoodsReceived + ".pdf";
                    break;
                case IdBConstant.REPORT_KEY_WHAT_IS_IN_STOCK :
                    additionalSearchClauseList = SearchClauseBuilder.buildReportingSearchAdditionalClauseList(reportParamList, IdBConstant.REPORTS_PARAM_GROUPBY);
                    //add new parameter for GroupBy
                    populateReportParams(parameters, additionalSearchClauseList);

                    rowList = reportsDao.runRptWhatIsInStock(sessionState.getOrgUnit().getId(),
                            searchClauseList, additionalSearchClauseList);
                    beanColDataSource = new JRBeanCollectionDataSource(rowList);
                    reportJrxmlName = pathStr + "/" + rptWhatIsInStock + ".jrxml";
                    outputFile = pathStr + "/" + rptWhatIsInStock + ".pdf";
                    break;
                case IdBConstant.REPORT_KEY_WHAT_IS_ON_ORDER :
                    additionalSearchClauseList = SearchClauseBuilder.buildReportingSearchAdditionalClauseList(reportParamList, IdBConstant.REPORTS_PARAM_GROUPBY);
                    //add new parameter for GroupBy
                    populateReportParams(parameters, additionalSearchClauseList);

                    rowList = reportsDao.runRptWhatIsOnOrder(sessionState.getOrgUnit().getId(),
                            searchClauseList, additionalSearchClauseList);
                    beanColDataSource = new JRBeanCollectionDataSource(rowList);
                    reportJrxmlName = pathStr + "/" + rptWhatIsOnOrder + ".jrxml";
                    outputFile = pathStr + "/" + rptWhatIsOnOrder + ".pdf";
                    break;
                case IdBConstant.REPORT_KEY_SUPPLIER_LIST :
                    final List<Supplier> supplierList = supplierDao.getAllSuppliersByOrgUnitId(sessionState.getOrgUnit().getId());
                    beanColDataSource = new JRBeanCollectionDataSource(supplierList);
                    reportJrxmlName = pathStr + "/" + rptSupplierList + ".jrxml";
                    outputFile = pathStr + "/" + rptSupplierList + ".pdf";
                    break;
                case IdBConstant.REPORT_KEY_CUSTOMER_LIST :
                    final List<Customer> customerList = customerDao.getAllCustomers();
                    beanColDataSource = new JRBeanCollectionDataSource(customerList);
                    reportJrxmlName = pathStr + "/" + rptCustomerList + ".jrxml";
                    outputFile = pathStr + "/" + rptCustomerList + ".pdf";
                    break;
                case IdBConstant.REPORT_KEY_DEBTOR :
                    final List<ReportDebtorRow> debtorList = reportsDao.runRptDebtor(sessionState.getOrgUnit().getId(), searchClauseList);
                    beanColDataSource = new JRBeanCollectionDataSource(debtorList);
                    reportJrxmlName = pathStr + "/" + rptDebtor + ".jrxml";
                    outputFile = pathStr + "/" + rptDebtor + ".pdf";
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
        if (searchClauseList == null || reportParams == null) {
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

    public String getRptSalesByTaxCodesSummary() {
        return rptSalesByTaxCodesSummary;
    }

    public void setRptSalesByTaxCodesSummary(String rptSalesByTaxCodesSummary) {
        this.rptSalesByTaxCodesSummary = rptSalesByTaxCodesSummary;
    }

    public String getRptSalesdaily() {
        return rptSalesdaily;
    }

    public void setRptSalesdaily(String rptSalesdaily) {
        this.rptSalesdaily = rptSalesdaily;
    }

    public String getRptSalesPeriod() {
        return rptSalesPeriod;
    }

    public void setRptSalesPeriod(String rptSalesPeriod) {
        this.rptSalesPeriod = rptSalesPeriod;
    }

    public String getRptWhatIsSelling() {
        return rptWhatIsSelling;
    }

    public void setRptWhatIsSelling(String rptWhatIsSelling) {
        this.rptWhatIsSelling = rptWhatIsSelling;
    }

    public String getRptAccountSale() {
        return rptAccountSale;
    }

    public void setRptAccountSale(String rptAccountSale) {
        this.rptAccountSale = rptAccountSale;
    }

    public String getRptProfitByProduct() {
        return rptProfitByProduct;
    }

    public void setRptProfitByProduct(String rptProfitByProduct) {
        this.rptProfitByProduct = rptProfitByProduct;
    }

    public String getRptWhatIsOnOrder() {
        return rptWhatIsOnOrder;
    }

    public void setRptWhatIsOnOrder(String rptWhatIsOnOrder) {
        this.rptWhatIsOnOrder = rptWhatIsOnOrder;
    }

    public String getRptPriceByGrade() {
        return rptPriceByGrade;
    }

    public void setRptPriceByGrade(String rptPriceByGrade) {
        this.rptPriceByGrade = rptPriceByGrade;
    }

    public String getRptGoodsReceived() {
        return rptGoodsReceived;
    }

    public void setRptGoodsReceived(String rptGoodsReceived) {
        this.rptGoodsReceived = rptGoodsReceived;
    }

    public String getRptWhatIsInStock() {
        return rptWhatIsInStock;
    }

    public void setRptWhatIsInStock(String rptWhatIsInStock) {
        this.rptWhatIsInStock = rptWhatIsInStock;
    }

    public String getRptSupplierList() {
        return rptSupplierList;
    }

    public void setRptSupplierList(String rptSupplierList) {
        this.rptSupplierList = rptSupplierList;
    }

    public String getRptDebtor() {
        return rptDebtor;
    }

    public void setRptDebtor(String rptDebtor) {
        this.rptDebtor = rptDebtor;
    }

    public String getRptCustomerList() {
        return rptCustomerList;
    }

    public void setRptCustomerList(String rptCustomerList) {
        this.rptCustomerList = rptCustomerList;
    }
}
