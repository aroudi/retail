package au.com.biztune.retail.service;

import au.com.biztune.retail.domain.Contact;
import au.com.biztune.retail.domain.Customer;
import au.com.biztune.retail.domain.CustomerAccountDebt;
import au.com.biztune.retail.domain.CustomerGrade;
import au.com.biztune.retail.form.AccountDebtRptForm;
import au.com.biztune.retail.response.CommonResponse;

import java.sql.Timestamp;
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

    /**
     * get customer by Id.
     * @param id id
     * @return CustomerGrade
     */
    Customer getCustomerById(long id);

    /**
     * get customer by Code.
     * @param code code
     * @return CustomerGrade
     */
    Customer getCustomerByCode(String code);

    /**
     * get all customer account debt.
     * @return List of CustomerAccountDebt
     */
    List<CustomerAccountDebt> getAllCustomerAccountDebt();

    /**
     * get Customer Account Debt list per customer Id.
     * @param customerId customerId
     * @return List of CustomerAccountDebt
     */
    List<CustomerAccountDebt> getCustomerAccountDebtPerCustomerId(long customerId);
    /**
     * get Customer Account Payment list per customer Id.
     * @param customerId customerId
     * @return List of CustomerAccountDebt
     */
    List<CustomerAccountDebt> getCustomerAccountPaymentPerCustomerId(long customerId);

    /**
     * get customer account debt report.
     * @param accountDebtRptForm accountDebtRptForm
     * @return List of customer account debt.
     */
    List<CustomerAccountDebt> customerAccountDebtReportForMultiCustomer(AccountDebtRptForm accountDebtRptForm);

    /**
     * get customer account debt report per one customer.
     * @param toDate toDate
     * @param customerId customerId
     * @return List of customer account debt.
     */
    List<CustomerAccountDebt> customerAccountDebtReportForOneCustomer(Timestamp toDate, long customerId);

    /**
     * get customer contact list by customer id.
     * @param customerId customerId
     * @return customer contact list.
     */
    List<Contact> getCustomerContactListPerCustomerId(long customerId);
}
