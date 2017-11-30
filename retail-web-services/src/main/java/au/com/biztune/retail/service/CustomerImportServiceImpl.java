package au.com.biztune.retail.service;

import au.com.biztune.retail.dao.ConfigCategoryDao;
import au.com.biztune.retail.dao.SupplierDao;
import au.com.biztune.retail.response.CommonResponse;
import au.com.biztune.retail.session.SessionState;
import au.com.biztune.retail.util.IdBConstant;
import au.com.bytecode.opencsv.CSVReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ListIterator;

/**
 * Created by arash on 28/11/2017.
 */

@Component
public class CustomerImportServiceImpl {
    private final Logger logger = LoggerFactory.getLogger(CustomerImportServiceImpl.class);
    @Autowired
    private SupplierService supplierService;
    @Autowired
    private ConfigCategoryDao configCategoryDao;
    @Autowired
    private SessionState sessionState;
    @Autowired
    private SupplierDao supplierDao;

    /**
     * import location from csv file.
     * @param inputStream inputStream
     * @throws Exception Exception
     * @return CommonResponse
     */
    public CommonResponse importCustomerFromCsvInputStream(InputStream inputStream) throws Exception {
        CSVReader csvReader = null;
        final CommonResponse commonResponse = new CommonResponse();
        commonResponse.setStatus(IdBConstant.RESULT_SUCCESS);
        try {
            ListIterator<String[]> iterator = null;
            String[] csvRow = null;
            csvReader = new CSVReader(new InputStreamReader(inputStream));
            if (csvReader == null) {
                logger.error("not able to open supplier inputStream");
                commonResponse.setStatus(IdBConstant.RESULT_FAILURE);
                commonResponse.setMessage("not able to open supplier inputStream");
                commonResponse.addMessage("not able to open csv file");
                return commonResponse;
            }
            iterator = csvReader.readAll().listIterator();
            //read header
            if (iterator.hasNext()) {
                iterator.next();
            }
            while (iterator.hasNext()) {
                csvRow = iterator.next();
                if (csvRow == null) {
                    continue;
                }
            }
            return commonResponse;
        } catch (Exception e) {
            logger.error("error in importing products : ", e);
            commonResponse.setStatus(IdBConstant.RESULT_FAILURE);
            commonResponse.setMessage("error in importing suppliers. see the logs");
            commonResponse.addMessage("error in importing suppliers. see the logs");
        } finally {
            if (csvReader != null) {
                csvReader.close();
            }
            return commonResponse;
        }
    }


    /**
     * import customer whole record.
     * @param giverName giverName
     * @param surname surname
     * @param company surname
     * @param position position
     * @param grade grade
     * @param status status
     * @param inactive inactive
     * @param addrress1 addrress1
     * @param address2 address2
     * @param address3 address3
     * @param suburb suburb
     * @param state state
     * @param postcode postcode
     * @param country country
     * @param phone phone
     * @param fax fax
     * @param mobile mobile
     * @param email email
     * @param isAccountCustomer isAccountCustomer
     * @param creditLimit creditLimit
     * @param noOfDays noOfDays
     * @param deliveryDelay deliveryDelay
     * @param fromEom fromEom
     * @param abn abn
     * @return
     */
    private CommonResponse importCustomerWhole(
            String giverName,
            String surname,
            String company  ,
            String position,
            int grade,
            int status,
            int inactive,
            String addrress1,
            String address2,
            String address3,
            String suburb,
            String state,
            String postcode,
            String country,
            String phone,
            String fax,
            String mobile,
            String email,
            int isAccountCustomer,
            double creditLimit,
            int noOfDays,
            int deliveryDelay,
            int fromEom,
            String abn

    )
    {
        final CommonResponse response = new CommonResponse();
        try {
            return null;
        } catch (Exception e) {
            logger.error("Exception in importing supplier", e);
            response.setStatus(IdBConstant.RESULT_FAILURE);
            response.setMessage(e.getMessage());
            return response;
        }
    }
}
