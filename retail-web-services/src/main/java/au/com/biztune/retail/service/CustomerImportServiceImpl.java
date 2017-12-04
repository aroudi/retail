package au.com.biztune.retail.service;

import au.com.biztune.retail.dao.ConfigCategoryDao;
import au.com.biztune.retail.dao.CustomerDao;
import au.com.biztune.retail.dao.CustomerGradeDao;
import au.com.biztune.retail.domain.ConfigCategory;
import au.com.biztune.retail.domain.Contact;
import au.com.biztune.retail.domain.Customer;
import au.com.biztune.retail.domain.CustomerGrade;
import au.com.biztune.retail.response.CommonResponse;
import au.com.biztune.retail.session.SessionState;
import au.com.biztune.retail.util.IdBConstant;
import au.com.biztune.retail.util.StringUtil;
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
public class CustomerImportServiceImpl implements CustomerImportService {
    private final Logger logger = LoggerFactory.getLogger(CustomerImportServiceImpl.class);
    @Autowired
    private SupplierService supplierService;
    @Autowired
    private ConfigCategoryDao configCategoryDao;
    @Autowired
    private SessionState sessionState;
    @Autowired
    private CustomerDao customerDao;
    @Autowired
    private CustomerGradeDao customerGradeDao;
    @Autowired
    private CustomerService customerService;

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
                logger.error("not able to open customer inputStream");
                commonResponse.setStatus(IdBConstant.RESULT_FAILURE);
                commonResponse.setMessage("not able to open customer inputStream");
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
                final CommonResponse result = importCustomerWhole(csvRow[2], csvRow[3], csvRow[4], csvRow[5], StringUtil.strToInt(csvRow[6])
                        , StringUtil.strToInt(csvRow[7]), csvRow[9], csvRow[10], csvRow[11], csvRow[12], csvRow[13], csvRow[14], csvRow[15]
                        , csvRow[16], csvRow[17], csvRow[18], csvRow[19], StringUtil.strToInt(csvRow[20]), StringUtil.strToDouble(csvRow[23])
                        , StringUtil.strToInt(csvRow[24]), StringUtil.strToInt(csvRow[25]), csvRow[26]);

                if (result.getStatus() == IdBConstant.RESULT_FAILURE) {
                    commonResponse.addMessage("Not able to import customer [name, company] = [" + csvRow[3] + "," + csvRow[4] + "] Error: " + result.getMessage());
                } else {
                    commonResponse.addMessage("customer [name, company] = [" + csvRow[3] + "," + csvRow[4] + "] Imported successfully");
                }
                commonResponse.addAll(result.getMessageList());
                commonResponse.setStatus(commonResponse.getStatus() * result.getStatus());
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
     * @param givenName givenName
     * @param surname surname
     * @param company surname
     * @param position position
     * @param grade grade
     * @param status status
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
     * @param fromEom fromEom
     * @param abn abn
     * @return
     */
    private CommonResponse importCustomerWhole(
            String givenName,
            String surname,
            String company  ,
            String position,
            int grade,
            int status,
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
            int fromEom,
            String abn

    )
    {
        final CommonResponse response = new CommonResponse();
        try {
            if (company == null && abn == null && givenName == null && surname == null) {
                response.setStatus(IdBConstant.RESULT_FAILURE);
                response.addMessage("Customer record is not valid.");
                return response;
            }
            Customer customer = null;
            //search based on abn and company
            if (abn != null && !abn.isEmpty()) {
                customer = customerDao.getCustomerByCode(abn.trim());
            }
            if (customer == null) {
                if (company != null && !company.isEmpty()) {
                    customer = customerDao.getCustomerByCompanyName(company.trim());
                    if (customer != null) {
                        response.addMessage("Customer with company name [" + company + "] exists. record was updated");
                    }
                }
            } else {
                response.addMessage("Customer with ABN [" + abn + "] exists. record was updated");
            }
            if (customer == null) {
                customer = new Customer();
                customer.setId(-1);
            }
            customer.setFirstName(givenName);
            customer.setSurName(surname);
            customer.setCompanyName(company);
            customer.setCode(abn);
            customer.setCustPosition(position);
            ConfigCategory customerStatus;
            if (status == 0) {
                customerStatus = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_CUSTOMER_STATUS, IdBConstant.CUSTOMER_STATUS_IN_ACTIVE);
            } else {
                customerStatus = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_CUSTOMER_STATUS, IdBConstant.CUSTOMER_STATUS_CONFIRMED);
            }
            customer.setCustomerStatus(customerStatus);
            try {
                String gradeLabel = Character.toString((char) (grade + 64));
                if (grade == 0) {
                    gradeLabel = IdBConstant.CUSTOMER_GRADE_DEFAULT;
                }
                final CustomerGrade customerGrade = customerGradeDao.getCustomerGradeByCode(gradeLabel);
                if (customerGrade != null) {
                    customer.setGrade(customerGrade);
                } else {
                    response.addMessage("Customer grade [" + grade + "] dose not exists. valid grades : [0..5]");
                }
            } catch (Exception e) {
                response.addMessage("Customer grade [" + grade + "] dose not exists");
            }
            ConfigCategory customerType;
            if (isAccountCustomer == 1) {
                customerType = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_CUSTOMER_TYPE, IdBConstant.CUSTOMER_TYPE_ACCOUNT);
            } else {
                customerType = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_CUSTOMER_TYPE, IdBConstant.CUSTOMER_TYPE_CASH_ONLY);
            }
            customer.setCustomerType(customerType);
            customer.setCreditLimit(creditLimit);
            customer.setCreditDuration(noOfDays);
            customer.setCreditStartEom(fromEom == 1);
            // contact
            final Contact contact = new Contact();
            contact.setCountry(country);
            contact.setState(state);
            contact.setAddress1(addrress1);
            contact.setAddress2(address2 + " " + address3);
            contact.setEmail(email);
            contact.setFax(fax);
            contact.setPhone(phone);
            contact.setSuburb(suburb);
            contact.setPostCode(postcode);
            contact.setMobile(mobile);
            customer.setContact(contact);
            final CommonResponse result = customerService.addCustomer(customer);
            response.addMessage(result.getMessage());
            response.setStatus(result.getStatus());
            return response;

        } catch (Exception e) {
            logger.error("Exception in importing customer", e);
            response.setStatus(IdBConstant.RESULT_FAILURE);
            response.setMessage(e.getMessage());
            return response;
        }
    }
}
