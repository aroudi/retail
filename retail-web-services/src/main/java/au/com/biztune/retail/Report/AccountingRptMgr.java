package au.com.biztune.retail.report;

import au.com.biztune.retail.dao.AccountingDao;
import au.com.biztune.retail.domain.AppUser;
import au.com.biztune.retail.domain.GeneralSearchForm;
import au.com.biztune.retail.domain.JournalEntry;
import au.com.biztune.retail.util.SearchClauseBuilder;
import net.sf.jasperreports.engine.*;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by arash on 4/07/2016.
 */
public class AccountingRptMgr {
    private final Logger logger = LoggerFactory.getLogger(AccountingRptMgr.class);
    @Autowired
    private AccountingDao accountingDao;
    private Resource reportPath;
    private String reportHeaderFileName;

    /**
     * export purchase order to PDF.
     * @param searchForm searchForm
     * @param securityContext securityContext
     * @return StreamingOutput
     */
    public StreamingOutput createAccountingRptPdfStream(GeneralSearchForm searchForm, SecurityContext securityContext) {
        StreamingOutput streamingOutput = null;
        try {
            final Principal principal = securityContext.getUserPrincipal();
            AppUser appUser = null;
            if (principal instanceof AppUser) {
                appUser = (AppUser) principal;
            }
            if (appUser != null) {
                searchForm.setGeneratedBy(appUser.getUsrFirstName() +  " " + appUser.getUsrSurName());
            }
            final String pathStr = reportPath.getURL().getPath();
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            final List<JournalEntry> journalEntryList = processAccountingSummaryList(accountingDao.accountingSummaryExportRpt(
                    SearchClauseBuilder.buildSearchWhereCluase(searchForm, "CASH_SESSION")));
            final JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(journalEntryList);
            final String reportHeaderJrxmlName = pathStr + "/" + reportHeaderFileName + ".jrxml";
            final String outputFile = pathStr + "/" + reportHeaderFileName + ".pdf";
            /* Compile the master and sub report */
            final JasperReport jasperMasterReport = JasperCompileManager.compileReport(reportHeaderJrxmlName);
            final Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("SUBREPORT_DIR", pathStr + "/");
            parameters.put("REPORT_PARAMS", searchForm);

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
    public String getReportHeaderFileName() {
        return reportHeaderFileName;
    }

    public void setReportHeaderFileName(String reportHeaderFileName) {
        this.reportHeaderFileName = reportHeaderFileName;
    }

    public Resource getReportPath() {
        return reportPath;
    }

    public void setReportPath(Resource reportPath) {
        this.reportPath = reportPath;
    }

    /**
     * because summary report summs all credit and debit separately, we need to separate records for credit and debits.
     * @param summaryList list from query.
     * @return separated list per debit and credit.
     */
    private List<JournalEntry> processAccountingSummaryList(List<JournalEntry> summaryList) {
        if (summaryList == null || summaryList.size() < 1) {
            return summaryList;
        }
        JournalEntry journalEntry;
        final List<JournalEntry> journalEntryList = new ArrayList<JournalEntry>();
        for (JournalEntry journalEntryRec : summaryList) {
            if (journalEntryRec.getJrnCredit() > 0 && journalEntryRec.getJrnDebit() > 0) {
                journalEntry = new JournalEntry();
                journalEntry.setJrnDebit(journalEntryRec.getJrnDebit());
                journalEntry.setJrnCredit(0);
                journalEntry.setJrnAccCode(journalEntryRec.getJrnAccCode());
                journalEntry.setJrnAccDesc(journalEntryRec.getJrnAccDesc());
                journalEntryList.add(journalEntry);

                journalEntry = new JournalEntry();
                journalEntry.setJrnCredit(journalEntryRec.getJrnCredit());
                journalEntry.setJrnDebit(0);
                journalEntry.setJrnAccCode(journalEntryRec.getJrnAccCode());
                journalEntry.setJrnAccDesc(journalEntryRec.getJrnAccDesc());
                journalEntryList.add(journalEntry);
            } else {
                journalEntryList.add(journalEntryRec);
            }
        }
        return journalEntryList;
    }
}
