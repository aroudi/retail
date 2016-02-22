package au.com.biztune.retail.service;

import au.com.biztune.retail.dao.CustomerDao;
import au.com.biztune.retail.dao.CustomerGradeDao;
import au.com.biztune.retail.domain.Customer;
import au.com.biztune.retail.domain.CustomerGrade;
import au.com.biztune.retail.response.CommonResponse;
import au.com.biztune.retail.util.IdBConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by arash on 22/02/2016.
 */

@Component
public class CustomerServiceImpl implements CustomerService {

    private final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private CustomerGradeDao customerGradeDao;
    /**
     * return all customers.
     * @return list of Customer
     */
    public List<Customer> getAllCustomers() {
        try {
            return customerDao.getAllCustomers();
        } catch (Exception e) {
            logger.error("Error in getting customer list: ", e);
            return null;
        }
    }

    /**
     * add new customer.
     * @param customer customer
     * @return CommonResponse
     */
    public CommonResponse addCustomer(Customer customer) {
        final CommonResponse response = new CommonResponse();
        try {
            response.setStatus(IdBConstant.RESULT_SUCCESS);

            if (customer == null) {
                response.setStatus(IdBConstant.RESULT_FAILURE);
                response.setMessage("customer object is null");
                return response;
            }
            final boolean isNew = customer.getCustomerId() > 0 ? false : true;
            if (isNew) {
                //check if customer code is already in there
                final Customer customer1 = customerDao.getCustomerByCode(customer.getCode());
                if (customer1 != null) {
                    response.setStatus(IdBConstant.RESULT_FAILURE);
                    response.setMessage("customer with code " + customer.getClass() + " already exists");
                    return response;
                }
                customerDao.insert(customer);
            } else {
                customerDao.update(customer);
            }
            return response;
        } catch (Exception e) {
            logger.error("Exception in saving Customer: ", e);
            response.setStatus(IdBConstant.RESULT_FAILURE);
            response.setMessage("Exception in saving Customer");
            return response;
        }
    }

    /**
     * get all customer grades.
     * @return CustomerGrade
     */
    public List<CustomerGrade> getAllCustomerGrades() {
        try {
            return customerGradeDao.getAllCustomerGrades();
        } catch (Exception e) {
            logger.error("Error in getting customer grade list: ", e);
            return null;
        }
    }
}
