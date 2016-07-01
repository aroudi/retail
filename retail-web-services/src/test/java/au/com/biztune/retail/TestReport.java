package au.com.biztune.retail;

import au.com.biztune.retail.dao.*;
import au.com.biztune.retail.domain.PurchaseOrderHeader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 * Created by arash on 23/03/2016.
 */
public class TestReport {
    //private static final Logger logger = LoggerFactory.getLogger(TestProduct.class);
    private static PurchaseOrderDao purchaseOrderDao = null;
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("retail-web-services-Context.xml");
        purchaseOrderDao = context.getBean(PurchaseOrderDao.class);

        String masterReportFileName = "E://projects//IReport//purchaseOrder.jrxml";
        String subReportFileName = "E://projects//IReport//purchase_line.jrxml";
        String destFileName = "E://projects//IReport//jasper_report_template.JRprint";

        List<PurchaseOrderHeader> purchaseOrderHeaders = new ArrayList<PurchaseOrderHeader>();
        PurchaseOrderHeader purchase1 = purchaseOrderDao.getPurchaseOrderWholePerPohId(1);
        purchaseOrderHeaders.add(purchase1);
        JRBeanCollectionDataSource beanColDataSource = new
                JRBeanCollectionDataSource(purchaseOrderHeaders);

        try {
         /* Compile the master and sub report */
            JasperReport jasperMasterReport = JasperCompileManager
                    .compileReport(masterReportFileName);
            JasperReport jasperSubReport = JasperCompileManager
                    .compileReport(subReportFileName);

            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("subreportParameter", jasperSubReport);
            parameters.put("SUBREPORT_DIR", "E://projects//IReport//");
            JasperFillManager.fillReportToFile(jasperMasterReport,
                    destFileName, parameters, beanColDataSource);

        } catch (JRException e) {

            e.printStackTrace();
        }
        System.out.println("Done filling!!! ...");



    }

}
