package au.com.biztune.retail.report;

import au.com.biztune.retail.dao.CashSessionDao;
import au.com.biztune.retail.dao.ConfigCategoryDao;
import au.com.biztune.retail.domain.SessionEvent;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by arash on 4/07/2016.
 */
public class ReconciliationRptMgr {
    private final Logger logger = LoggerFactory.getLogger(ReconciliationRptMgr.class);
    @Autowired
    private ConfigCategoryDao configCategoryDao;

    @Autowired
    private CashSessionDao cashSessionDao;

    private Resource reportPath;
    private String reportHeaderFileName;
    private String reportDetailFileName;

    /**
     * export purchase order to PDF.
     * @param seevId seevId
     * @return StreamingOutput
     */
    public StreamingOutput createReconciliationRptPdfStream(long seevId) {
        StreamingOutput streamingOutput = null;
        try {
            final String pathStr = reportPath.getURL().getPath();
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            final List<SessionEvent> sessionEventList = new ArrayList<SessionEvent>();
            final SessionEvent sessionEvent = cashSessionDao.getSessionEventPerId(seevId);
            sessionEventList.add(sessionEvent);
            final JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(sessionEventList);
            final String reportDetailJrxmlName = pathStr + "/" + reportDetailFileName + ".jrxml";
            final String reportDetailJasperName = pathStr + "/" + reportDetailFileName + ".jasper";
            final String reportHeaderJrxmlName = pathStr + "/" + reportHeaderFileName + ".jrxml";
            final String outputFile = pathStr + "/" + reportHeaderFileName + ".pdf";
            /* Compile the master and sub report */
            JasperCompileManager.compileReportToFile(reportDetailJrxmlName, reportDetailJasperName);
            final JasperReport jasperMasterReport = JasperCompileManager.compileReport(reportHeaderJrxmlName);
            final Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("SUBREPORT_DIR", pathStr + "/");

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
            logger.error("Exception in exporting session event as pdf: ", e);
            return null;
        }
    }
    public String getReportHeaderFileName() {
        return reportHeaderFileName;
    }

    public void setReportHeaderFileName(String reportHeaderFileName) {
        this.reportHeaderFileName = reportHeaderFileName;
    }

    public String getReportDetailFileName() {
        return reportDetailFileName;
    }

    public void setReportDetailFileName(String reportDetailFileName) {
        this.reportDetailFileName = reportDetailFileName;
    }

    public Resource getReportPath() {
        return reportPath;
    }

    public void setReportPath(Resource reportPath) {
        this.reportPath = reportPath;
    }
}
