package au.com.biztune.retail.report;

import au.com.biztune.retail.dao.ConfigCategoryDao;
import au.com.biztune.retail.dao.CustomerAccountDebtDao;
import au.com.biztune.retail.dao.InvoiceDao;
import au.com.biztune.retail.dao.TxnDao;
import au.com.biztune.retail.domain.ConfigCategory;
import au.com.biztune.retail.domain.CustomerAccountDebt;
import au.com.biztune.retail.domain.TxnHeader;
import au.com.biztune.retail.processor.EMailProcessor;
import au.com.biztune.retail.processor.EmailRequest;
import au.com.biztune.retail.util.IdBConstant;
import au.com.biztune.retail.util.queuemanager.QueueManager;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
//import net.sf.jasperreports.engine.export.JRPdfExporterParameter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by arash on 4/07/2016.
 */
public class TransactionRptMgr {
    private static final boolean IS_WINDOWS = System.getProperty("os.name").contains("indow");
    private final Logger logger = LoggerFactory.getLogger(TransactionRptMgr.class);
    @Autowired
    private ConfigCategoryDao configCategoryDao;

    @Autowired
    private EMailProcessor emailProcessor;

    @Autowired
    private QueueManager queueManager;

    @Autowired
    private TxnDao txnDao;

    @Autowired
    private InvoiceDao invoiceDao;

    private Resource reportPath;
    private String invoiceHeaderFileName;
    private String saleOrderHeaderFileName;
    private String reportTxnLineFileName;
    private String saleOrderLineFileName;
    private String reportTxnMediaFileName;
    private String deliveryDocketHeaderFileName;
    private String deliveryDocketLineFileName;

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
            return createTransactionPdfStream(txnHeaders, txnHeader.getInvoiceTxnType().getCategoryCode());
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
            final JRBeanCollectionDataSource beanColDataSource1 = new JRBeanCollectionDataSource(txnHeaders);
            final JRBeanCollectionDataSource beanColDataSource2 = new JRBeanCollectionDataSource(txnHeaders);
            final String reportTxnLineJrxmlName = pathStr + "/" + reportTxnLineFileName + ".jrxml";
            final String reportTxnLineJasperName = IS_WINDOWS ? (pathStr + "/" + reportTxnLineFileName + ".jasper").substring(1) : (pathStr + "/" + reportTxnLineFileName + ".jasper");
            final String saleOrderLineJrxmlName = pathStr + "/" + saleOrderLineFileName + ".jrxml";
            final String saleOrderLineJasperName = IS_WINDOWS ? (pathStr + "/" + saleOrderLineFileName + ".jasper").substring(1) : (pathStr + "/" + saleOrderLineFileName + ".jasper");
            final String reportTxnMediaJrxmlName = pathStr + "/" + reportTxnMediaFileName + ".jrxml";
            final String reportTxnMediaJasperName = IS_WINDOWS ? (pathStr + "/" + reportTxnMediaFileName + ".jasper").substring(1) : (pathStr + "/" + reportTxnMediaFileName + ".jasper");
            final String deliveryDocketHeaderJrxmlName = pathStr + "/" + deliveryDocketHeaderFileName + ".jrxml";
            final String deliveryDocketLineJrxmlName = pathStr + "/" + deliveryDocketLineFileName + ".jrxml";
            final String deliveryDocketLineJasperName = IS_WINDOWS ? (pathStr + "/" + deliveryDocketLineFileName + ".jasper").substring(1) : (pathStr + "/" + deliveryDocketLineFileName + ".jasper");
            JasperReport jasperDeliveryDocketReport = null;
            String reportHeaderJrxmlName = "";
            JasperPrint jasperPrint2 = null;
            final Map<String, Object> parameters = new HashMap<String, Object>();
            final List<JasperPrint> jasperPrintList = new ArrayList<JasperPrint>();
            parameters.put("SUBREPORT_DIR", pathStr + "/");

            if (txnType.equals(IdBConstant.TXN_TYPE_INVOICE)) {
                reportHeaderJrxmlName = pathStr + "/" + invoiceHeaderFileName + ".jrxml";
                if (Files.notExists(Paths.get(reportTxnLineJasperName))) {
                    JasperCompileManager.compileReportToFile(reportTxnLineJrxmlName, reportTxnLineJasperName);
                }
            } else if (txnType.equals(IdBConstant.TXN_TYPE_REFUND)) {
                reportHeaderJrxmlName = pathStr + "/" + invoiceHeaderFileName + ".jrxml";
                if (Files.notExists(Paths.get(saleOrderLineJasperName))) {
                    JasperCompileManager.compileReportToFile(saleOrderLineJrxmlName, saleOrderLineJasperName);
                }
            } else {
                reportHeaderJrxmlName = pathStr + "/" + saleOrderHeaderFileName + ".jrxml";
                if (Files.notExists(Paths.get(saleOrderLineJasperName))) {
                    JasperCompileManager.compileReportToFile(saleOrderLineJrxmlName, saleOrderLineJasperName);
                }
            }
            if (Files.notExists(Paths.get(reportTxnMediaJasperName))) {
                JasperCompileManager.compileReportToFile(reportTxnMediaJrxmlName, reportTxnMediaJasperName);
            }

            final JasperReport jasperMasterReport = JasperCompileManager.compileReport(reportHeaderJrxmlName);
            final JasperPrint jasperPrint1 = JasperFillManager.fillReport(jasperMasterReport, parameters, beanColDataSource1);
            jasperPrintList.add(jasperPrint1);

            //for new invoices print delivery docket as well.
            if (txnType.equals(IdBConstant.TXN_TYPE_INVOICE) && (!txnHeaders.get(0).isTxhdPrinted())) {
                if (Files.notExists(Paths.get(deliveryDocketLineJasperName))) {
                    JasperCompileManager.compileReportToFile(deliveryDocketLineJrxmlName, deliveryDocketLineJasperName);
                }
                jasperDeliveryDocketReport = JasperCompileManager.compileReport(deliveryDocketHeaderJrxmlName);
                jasperPrint2 = JasperFillManager.fillReport(jasperDeliveryDocketReport, parameters, beanColDataSource2);
                jasperPrintList.add(jasperPrint2);
            }

            final JRPdfExporter exporter = new JRPdfExporter();
            exporter.setExporterInput(SimpleExporterInput.getInstance(jasperPrintList));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(byteArrayOutputStream));
            //exporter.setParameter(JRPdfExporterParameter.JASPER_PRINT_LIST, jasperPrintList);
            //exporter.setParameter(JRPdfExporterParameter.OUTPUT_STREAM, byteArrayOutputStream);
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

            //email the invoice
            if (txnHeaders.get(0).getTxhdEmailTo() != null && !txnHeaders.get(0).getTxhdEmailTo().isEmpty()) {
                sendEmail(txnHeaders.get(0), byteArrayOutputStream, txnType);
            }

            return streamingOutput;
        } catch (Exception e) {
            logger.error("Exception in exporting Transaction as pdf: ", e);
            return null;
        }
    }

    private void sendEmail(TxnHeader txnHeader, ByteArrayOutputStream attachment, String txnType) {
        final EmailRequest emailRequest = new EmailRequest();
        String attachmentfileName;
        emailRequest.setProcessor(emailProcessor);
        emailRequest.setSubject("JOMON Architectural Hardware Invoice/Sale Order");
        emailRequest.setMessage("Dear valued customer, \r\n Please find your Invoice/Sale Order as attachment.");
        emailRequest.setAttachFileAsStream(attachment);
        emailRequest.setSendAsStream(true);
        if (txnHeader != null && txnHeader.getTxhdEmailTo() != null) {
            emailRequest.setToAddress(txnHeader.getTxhdEmailTo());
        }
        if (txnType.equals(IdBConstant.TXN_TYPE_INVOICE)) {
            attachmentfileName = "Invoice" + txnHeader.getTxhdTxnNr();
        } else {
            attachmentfileName = "SaleOrder" + txnHeader.getTxhdTxnNr();
        }
        emailRequest.setAttachFileName(attachmentfileName);
        queueManager.push(emailRequest);
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

    public String getDeliveryDocketHeaderFileName() {
        return deliveryDocketHeaderFileName;
    }

    public void setDeliveryDocketHeaderFileName(String deliveryDocketHeaderFileName) {
        this.deliveryDocketHeaderFileName = deliveryDocketHeaderFileName;
    }

    public String getDeliveryDocketLineFileName() {
        return deliveryDocketLineFileName;
    }

    public void setDeliveryDocketLineFileName(String deliveryDocketLineFileName) {
        this.deliveryDocketLineFileName = deliveryDocketLineFileName;
    }

    public String getSaleOrderLineFileName() {
        return saleOrderLineFileName;
    }

    public void setSaleOrderLineFileName(String saleOrderLineFileName) {
        this.saleOrderLineFileName = saleOrderLineFileName;
    }
}
