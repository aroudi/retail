package au.com.biztune.retail.report;

import au.com.biztune.retail.dao.ConfigCategoryDao;
import au.com.biztune.retail.dao.PurchaseOrderDao;
import au.com.biztune.retail.domain.PurchaseOrderHeader;
import au.com.biztune.retail.util.IdBConstant;
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
public class PurchaseOrderRptMgr {
    private final Logger logger = LoggerFactory.getLogger(PurchaseOrderRptMgr.class);
    @Autowired
    private ConfigCategoryDao configCategoryDao;

    @Autowired
    private PurchaseOrderDao purchaseOrderDao;

    private Resource reportPath;
    private String reportHeaderFileName;
    private String reportDetailFileName;

    /**
     * export purchase order to PDF.
     * @param pohId pohId
     * @return StreamingOutput
     */
    public StreamingOutput createPurchaseOrderPdfStream(long pohId) {
        StreamingOutput streamingOutput = null;
        try {
            final String pathStr = reportPath.getURL().getPath();
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            final List<PurchaseOrderHeader> purchaseOrderHeaders = new ArrayList<PurchaseOrderHeader>();
            final PurchaseOrderHeader purchaseOrderHeader = purchaseOrderDao.getPurchaseOrderWholePerPohId(pohId);
            purchaseOrderHeaders.add(purchaseOrderHeader);
            final JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(purchaseOrderHeaders);
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
            if (!purchaseOrderHeader.getPohStatus().getCategoryCode().equals(IdBConstant.POH_STATUS_IN_PROGRESS)) {
                purchaseOrderDao.updatePohPrintStatus(pohId);
            }
            return streamingOutput;
        } catch (Exception e) {
            logger.error("Exception in exporting PurchaseOrder as pdf: ", e);
            return null;
        }
    }

    /**
     * export purchase order to PDF.
     * @param pohId pohId
     */
    public void createPurchaseOrderPdfFile(long pohId) {
        StreamingOutput streamingOutput = null;
        try {
            final String pathStr = reportPath.getURL().getPath();
            //logger.info("path.getFilename() = " + path.get);
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            final List<PurchaseOrderHeader> purchaseOrderHeaders = new ArrayList<PurchaseOrderHeader>();
            final PurchaseOrderHeader purchaseOrderHeader = purchaseOrderDao.getPurchaseOrderWholePerPohId(pohId);
            purchaseOrderHeaders.add(purchaseOrderHeader);
            final JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(purchaseOrderHeaders);
            final String reportDetailJrxmlName = pathStr + "/" + reportDetailFileName + ".jrxml";
            final String reportDetailJasperName = pathStr + "/" + reportDetailFileName + ".jasper";
            final String reportHeaderJrxmlName = pathStr + "/" + reportHeaderFileName + ".jrxml";
            final String outputFile = pathStr + "/" + reportHeaderFileName + ".pdf";
            /* Compile the master and sub report */
            JasperCompileManager.compileReportToFile(reportDetailJrxmlName, reportDetailJasperName);
            final JasperReport jasperMasterReport = JasperCompileManager.compileReport(reportHeaderJrxmlName);

            final Map<String, Object> parameters = new HashMap<String, Object>();
            //parameters.put("subreportParameter", jasperSubReport);
            parameters.put("SUBREPORT_DIR", pathStr + "/");

            final JasperPrint jasperPrint = JasperFillManager.fillReport(jasperMasterReport, parameters, beanColDataSource);
            JasperExportManager.exportReportToPdfFile(jasperPrint, outputFile);

            streamingOutput = new StreamingOutput() {
                @Override
                public void write(OutputStream output) throws IOException, WebApplicationException {
                    output.write(byteArrayOutputStream.toByteArray());
                    output.flush();
                }
            };
        } catch (Exception e) {
            logger.error("Exception in exporting PurchaseOrder as pdf: ", e);
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
