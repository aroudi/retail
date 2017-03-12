package au.com.biztune.retail.report;

import au.com.biztune.retail.dao.CustomerAccountDebtDao;
import au.com.biztune.retail.domain.Customer;
import au.com.biztune.retail.domain.CustomerAccountDebt;
import au.com.biztune.retail.domain.TxnHeader;
import au.com.biztune.retail.form.AccountDebtRptForm;
import au.com.biztune.retail.form.CustomerStatementReport;
import au.com.biztune.retail.processor.EMailProcessor;
import au.com.biztune.retail.processor.EmailRequest;
import au.com.biztune.retail.util.DateUtil;
import au.com.biztune.retail.util.IdBConstant;
import au.com.biztune.retail.util.queuemanager.QueueManager;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.*;

//import net.sf.jasperreports.engine.export.JRPdfExporterParameter;

/**
 * Created by arash on 4/07/2016.
 */
public class CustomerStatementRptMgr {
    private final Logger logger = LoggerFactory.getLogger(CustomerStatementRptMgr.class);
    @Autowired

    private EMailProcessor emailProcessor;

    @Autowired
    private QueueManager queueManager;


    private Resource reportPath;
    private String reportHeaderFileName;
    private String reportLineFileName;
    private String reportLineDetailFileName;

    @Autowired
    private CustomerAccountDebtDao customerAccountDebtDao;


    /**
     * convert Invoice to PDF and return Outputstream.
     * @param accountDebtRptForm accountDebtRptForm
     * @return StreamingOutput.
     */
    public StreamingOutput runReport(AccountDebtRptForm accountDebtRptForm) {
        try {
            Timestamp dateTo = null;
            final List<CustomerStatementReport> reportList = new ArrayList<CustomerStatementReport>();;
            List<CustomerAccountDebt> customerAccountDebtList = null;
            if (accountDebtRptForm == null || accountDebtRptForm.getCustomerList() == null) {
                return null;
            }
            CustomerStatementReport customerStatementReport = null;
            for (Customer customer : accountDebtRptForm.getCustomerList()) {
                //execute report for each customer.
                if (customer == null) {
                    continue;
                }
                dateTo = DateUtil.stringToDate(accountDebtRptForm.getToDate(), "yyyy-MM-dd'T'HH:mm:ss.SSSX");
                if (dateTo == null) {
                    dateTo = new Timestamp(new Date().getTime());
                }
                customerAccountDebtList = customerAccountDebtDao.customerAccountPaymentReportPerCustomer(dateTo, customer.getId());
                if (customerAccountDebtList == null) {
                    continue;
                }
                customerStatementReport = new CustomerStatementReport();
                customerStatementReport.setCustomer(customer);
                logger.info("accountDebtRptForm.getToDate() = " + accountDebtRptForm.getToDate());
                logger.info("dateTo = " + dateTo);
                customerStatementReport.setCustomerAccountDebtList(customerAccountDebtList);
                reportList.add(customerStatementReport);
            }
            return createTransactionPdfStream(reportList);
        } catch (Exception e) {
            logger.error("Exception in returning transaction header");
            return null;
        }
    }

    /**
     * export Transaction to PDF.
     * @param reportList reportList
     * @return StreamingOutput
     */
    public StreamingOutput createTransactionPdfStream(List<CustomerStatementReport> reportList) {
        StreamingOutput streamingOutput = null;
        try {
            final String pathStr = reportPath.getURL().getPath();
            final String reportHeaderJrxmlName = pathStr + "/" + reportHeaderFileName + ".jrxml";
            final String reportTxnLineJrxmlName = pathStr + "/" + reportLineFileName + ".jrxml";
            final String reportTxnLineJasperName = pathStr + "/" + reportLineFileName + ".jasper";
            final String reportLineDetailJrxmlName = pathStr + "/" + reportLineDetailFileName + ".jrxml";
            final String reportLineDetailJasperName = pathStr + "/" + reportLineDetailFileName + ".jasper";
            JRBeanCollectionDataSource beanColDataSource1 = null;
            Map<String, Object> parameters = null;
            List<JasperPrint> jasperPrintList = null;
            JasperReport jasperMasterReport = null;
            JasperPrint jasperPrint1 = null;
            JRPdfExporter exporter = null;
            List<CustomerStatementReport> reportHeaders = null;
            for (CustomerStatementReport customerStatementReport : reportList) {
                if (customerStatementReport == null) {
                    continue;
                }
                reportHeaders = new ArrayList<CustomerStatementReport>();
                reportHeaders.add(customerStatementReport);
                beanColDataSource1  = new JRBeanCollectionDataSource(reportHeaders);

                parameters = new HashMap<String, Object>();
                jasperPrintList = new ArrayList<JasperPrint>();

                parameters.put("SUBREPORT_DIR", pathStr + "/");

            /* Compile the master and sub report */
                JasperCompileManager.compileReportToFile(reportLineDetailJrxmlName, reportLineDetailJasperName);
                JasperCompileManager.compileReportToFile(reportTxnLineJrxmlName, reportTxnLineJasperName);
                jasperMasterReport = JasperCompileManager.compileReport(reportHeaderJrxmlName);
                jasperPrint1 = JasperFillManager.fillReport(jasperMasterReport, parameters, beanColDataSource1);
                jasperPrintList.add(jasperPrint1);
            }


            exporter = new JRPdfExporter();
            exporter.setExporterInput(SimpleExporterInput.getInstance(jasperPrintList));
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(byteArrayOutputStream));
            exporter.exportReport();
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

    public String getReportHeaderFileName() {
        return reportHeaderFileName;
    }

    public void setReportHeaderFileName(String reportHeaderFileName) {
        this.reportHeaderFileName = reportHeaderFileName;
    }

    public String getReportLineFileName() {
        return reportLineFileName;
    }

    public void setReportLineFileName(String reportLineFileName) {
        this.reportLineFileName = reportLineFileName;
    }

    public String getReportLineDetailFileName() {
        return reportLineDetailFileName;
    }

    public void setReportLineDetailFileName(String reportLineDetailFileName) {
        this.reportLineDetailFileName = reportLineDetailFileName;
    }
}
