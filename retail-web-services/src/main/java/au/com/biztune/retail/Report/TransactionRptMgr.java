package au.com.biztune.retail.report;

import au.com.biztune.retail.dao.ConfigCategoryDao;
import au.com.biztune.retail.dao.CustomerAccountDebtDao;
import au.com.biztune.retail.dao.InvoiceDao;
import au.com.biztune.retail.dao.TxnDao;
import au.com.biztune.retail.domain.ConfigCategory;
import au.com.biztune.retail.domain.CustomerAccountDebt;
import au.com.biztune.retail.domain.TxnHeader;
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
import java.util.*;

/**
 * Created by arash on 4/07/2016.
 */
public class TransactionRptMgr {
    private final Logger logger = LoggerFactory.getLogger(TransactionRptMgr.class);
    @Autowired
    private ConfigCategoryDao configCategoryDao;

    @Autowired
    private TxnDao txnDao;

    @Autowired
    private InvoiceDao invoiceDao;

    private Resource reportPath;
    private String invoiceHeaderFileName;
    private String saleOrderHeaderFileName;
    private String reportTxnLineFileName;
    private String reportTxnMediaFileName;

    @Autowired
    private CustomerAccountDebtDao customerAccountDebtDao;

    /**
     * convert SaleOrder to PDF and return Outputstream.
     * @param txhdId txhdId
     * @return StreamingOutput.
     */
    public StreamingOutput convertSaleOrderToPdf(long txhdId) {
        try {

            final TxnHeader txnHeader = txnDao.getTxnHeaderPerTxhdId(txhdId);
            if (txnHeader == null) {
                logger.error("Not able to fetch invoice id " + txhdId);
                return null;
            }
            final List<TxnHeader> txnHeaders = new ArrayList<TxnHeader>();
            txnHeaders.add(txnHeader);
            txnDao.updateTxnPrintStatus(txhdId, true);
            return createTransactionPdfStream(txnHeaders, IdBConstant.TXN_TYPE_SALE);
        } catch (Exception e) {
            logger.error("Exception in returning transaction header");
            return null;
        }
    }

    /**
     * convert Invoice to PDF and return Outputstream.
     * @param inoiceId inoiceId
     * @return StreamingOutput.
     */
    public StreamingOutput convertInvoiceToPdf(long inoiceId) {
        try {
            final List<TxnHeader> txnHeaders = new ArrayList<TxnHeader>();
            final TxnHeader txnHeader = invoiceDao.getInvoiceHeaderPerInvoiceId(inoiceId);
            txnHeader.setTxhdTxnType(txnHeader.getInvoiceTxnType());
            //set txn_state to invoice
            final ConfigCategory txnState = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_TXN_STATE, IdBConstant.TXN_STATE_FINAL);
            if (txnState != null) {
                txnHeader.setTxhdState(txnState);
            }
            //get account debt for customer on this transaction
            final CustomerAccountDebt customerAccountDebt = customerAccountDebtDao.getCustomerAccountDebtPerCustomerIdAndTxhdId(txnHeader.getCustomer().getId(), inoiceId);

            if (customerAccountDebt != null) {
                txnHeader.setCustomerAccountDebt(customerAccountDebt);
            }
            txnHeaders.add(txnHeader);
            invoiceDao.updateTxnPrintStatus(inoiceId, true);
            return createTransactionPdfStream(txnHeaders, IdBConstant.TXN_TYPE_INVOICE);
        } catch (Exception e) {
            logger.error("Exception in returning transaction header");
            return null;
        }
    }

    /**
     * export Transaction to PDF.
     * @param txnHeaders txnHeaders
     * @param txnType txnType
     * @return StreamingOutput
     */
    public StreamingOutput createTransactionPdfStream(List<TxnHeader> txnHeaders, String txnType) {
        StreamingOutput streamingOutput = null;
        try {
            final String pathStr = reportPath.getURL().getPath();
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            final JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(txnHeaders);
            final String reportTxnLineJrxmlName = pathStr + "/" + reportTxnLineFileName + ".jrxml";
            final String reportTxnLineJasperName = pathStr + "/" + reportTxnLineFileName + ".jasper";
            final String reportTxnMediaJrxmlName = pathStr + "/" + reportTxnMediaFileName + ".jrxml";
            final String reportTxnMediaJasperName = pathStr + "/" + reportTxnMediaFileName + ".jasper";
            String reportHeaderJrxmlName = "";
            String outputFile = "";
            if (txnType.equals(IdBConstant.TXN_TYPE_INVOICE)) {
                reportHeaderJrxmlName = pathStr + "/" + invoiceHeaderFileName + ".jrxml";
                outputFile = pathStr + "/" + invoiceHeaderFileName + ".pdf";
            } else {
                reportHeaderJrxmlName = pathStr + "/" + saleOrderHeaderFileName + ".jrxml";
                outputFile = pathStr + "/" + saleOrderHeaderFileName + ".pdf";
            }
            /* Compile the master and sub report */
            JasperCompileManager.compileReportToFile(reportTxnLineJrxmlName, reportTxnLineJasperName);
            JasperCompileManager.compileReportToFile(reportTxnMediaJrxmlName, reportTxnMediaJasperName);
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
            logger.error("Exception in exporting Transaction as pdf: ", e);
            return null;
        }
    }

    public Resource getReportPath() {
        return reportPath;
    }

    public void setReportPath(Resource reportPath) {
        this.reportPath = reportPath;
    }

    public String getReportTxnLineFileName() {
        return reportTxnLineFileName;
    }

    public void setReportTxnLineFileName(String reportTxnLineFileName) {
        this.reportTxnLineFileName = reportTxnLineFileName;
    }

    public String getReportTxnMediaFileName() {
        return reportTxnMediaFileName;
    }

    public void setReportTxnMediaFileName(String reportTxnMediaFileName) {
        this.reportTxnMediaFileName = reportTxnMediaFileName;
    }

    public String getSaleOrderHeaderFileName() {
        return saleOrderHeaderFileName;
    }

    public void setSaleOrderHeaderFileName(String saleOrderHeaderFileName) {
        this.saleOrderHeaderFileName = saleOrderHeaderFileName;
    }

    public String getInvoiceHeaderFileName() {
        return invoiceHeaderFileName;
    }

    public void setInvoiceHeaderFileName(String invoiceHeaderFileName) {
        this.invoiceHeaderFileName = invoiceHeaderFileName;
    }
}
