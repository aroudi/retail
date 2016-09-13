package au.com.biztune.retail.dao;

import au.com.biztune.retail.domain.Customer;
import au.com.biztune.retail.domain.CustomerContact;

import java.util.List;

/**
 * Created by arash on 22/02/2016.
 */
public interface CustomerDao {

    /**
     * Add new customer.
     * @param customer customer
     */
    void insert(Customer customer);

    /**
     * update existing customer per id.
     * @param customer customer
     */
    void update(Customer customer);

    /**
     * get list of all customers.
     * @return list of customers.
     */
    List<Customer> getAllCustomers();

    /**
     * get the customer by code.
     * @param code code
     * @return Customer
     */
    Customer getCustomerByCode(String code);

    /**
     * get the customer by id.
     * @param id id
     * @return Customer
     */
    Customer getCustomerById(long id);

    /**
     * get the customer by name.
     * @param name name
     * @return Customer
     */
    Customer getCustomerByCompanyName(String name);

    /**
     * get all customer contacts per customerId.
     * @param id id
     * @return List of CustomerContact
     */
    List<CustomerContact> getAllContactsPerCustomerId(long id);

    /**
     * insert customerContact.
     * @param customerContact customerContact
     */
    void insertCustomerContact(CustomerContact customerContact);

    /**
     * delete customer contact by id.
     * @param id id
     */
    void deleteCustomerContactById(long id);

    /**
     * delete customer contact by id.
     * @param customerId customerId
     * @param contactList contactList
     */
    void deleteCustomerContactWhereIdNotIn(long customerId, List contactList);

    /**
     * update customer debt amount.
     * @param customer customer
     */
    void updateCustomerDebt(Customer customer);
}
