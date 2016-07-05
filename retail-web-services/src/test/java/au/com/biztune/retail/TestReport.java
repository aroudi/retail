package au.com.biztune.retail;

import au.com.biztune.retail.report.PurchaseOrderRptMgr;
import au.com.biztune.retail.dao.*;
import au.com.biztune.retail.domain.PurchaseOrderHeader;
import net.sf.jasperreports.engine.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import java.util.List;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 * Created by arash on 23/03/2016.
 */
public class TestReport {
    //private static final Logger logger = LoggerFactory.getLogger(TestProduct.class);
    private static PurchaseOrderDao purchaseOrderDao = null;
    private static PurchaseOrderRptMgr purchaseOrderRptMgr = null;
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("retail-web-services-Context.xml");
        purchaseOrderDao = context.getBean(PurchaseOrderDao.class);
        purchaseOrderRptMgr = context.getBean(PurchaseOrderRptMgr.class);
        generatePdfFromPurchaseOrder();

    }

    public static void generatePdfFromPurchaseOrder() {
        purchaseOrderRptMgr.createPurchaseOrderPdfFile(2);
    }

    public static void test () {
        String masterReportFileName = "E://projects//IReport//purchaseOrder.jrxml";
        String subReportFileName = "E://projects//IReport//purchase_line.jrxml";
        String subReportOutputFileName = "E://projects//IReport//purchase_line.jasper";
        String destFileName = "E://projects//IReport//jasper_report_template.pdf";

        List<PurchaseOrderHeader> purchaseOrderHeaders = new ArrayList<PurchaseOrderHeader>();
        PurchaseOrderHeader purchase1 = purchaseOrderDao.getPurchaseOrderWholePerPohId(1);
        purchaseOrderHeaders.add(purchase1);
        JRBeanCollectionDataSource beanColDataSource = new
                JRBeanCollectionDataSource(purchaseOrderHeaders);

        try {
         /* Compile the master and sub report */
            //JasperReport jasperSubReport =
            JasperCompileManager
                    .compileReportToFile(subReportFileName, subReportOutputFileName);
            JasperReport jasperMasterReport = JasperCompileManager
                    .compileReport(masterReportFileName);

            Map<String, Object> parameters = new HashMap<String, Object>();
            //parameters.put("subreportParameter", jasperSubReport);
            parameters.put("SUBREPORT_DIR", "E://projects//IReport//");

            JasperPrint jasperPrint =JasperFillManager.fillReport(jasperMasterReport,
                    parameters, beanColDataSource);

            //JasperExportManager.exportReportToPdfFile(jasperPrint, destFileName);
            JasperExportManager.exportReportToPdfFile(jasperPrint, destFileName);
            //JasperFillManager.fillReport(jasperMasterReport,);

        } catch (JRException e) {

            e.printStackTrace();
        }
        System.out.println("Done filling!!! ...");

    }
}
