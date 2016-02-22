package au.com.biztune.retail.service;

import au.com.biztune.retail.domain.Customer;
import au.com.biztune.retail.domain.CustomerGrade;
import au.com.biztune.retail.response.CommonResponse;

import java.util.List;

/**
 * Created by arash on 22/02/2016.
 */
public interface CustomerService {
    /**
     * return all customers.
     * @return list of Customer
     */
    List<Customer> getAllCustomers();

    /**
     * add new customer.
     * @param customer customer
     * @return CommonResponse
     */
    CommonResponse addCustomer(Customer customer);

    /**
     * get all customer grades.
     * @return CustomerGrade
     */
    List<CustomerGrade> getAllCustomerGrades();
}
