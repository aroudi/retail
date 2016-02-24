package au.com.biztune.retail.dao;

import au.com.biztune.retail.domain.Customer;

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
}
