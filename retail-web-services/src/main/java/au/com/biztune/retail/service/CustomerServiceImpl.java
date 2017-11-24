package au.com.biztune.retail.service;

import au.com.biztune.retail.dao.*;
import au.com.biztune.retail.domain.*;
import au.com.biztune.retail.form.AccountDebtRptForm;
import au.com.biztune.retail.response.CommonResponse;
import au.com.biztune.retail.util.DateUtil;
import au.com.biztune.retail.util.IdBConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.ArrayList;
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

    @Autowired
    private ConfigCategoryDao configCategoryDao;

    @Autowired
    private ContactDao contactDao;

    @Autowired
    private CustomerAccountDebtDao customerAccountDebtDao;

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
            final boolean isNew = customer.getId() > 0 ? false : true;
            /*
            final ConfigCategory customerStatus = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_CUSTOMER_STATUS, IdBConstant.CUSTOMER_STATUS_CONFIRMED);
            if (customerStatus != null) {
                customer.setCustomerStatus(customerStatus);
            }
            */
            if (isNew) {
                //check if customer code is already in there
                final Customer customer1 = customerDao.getCustomerByCode(customer.getCode());
                if (customer1 != null) {
                    response.setStatus(IdBConstant.RESULT_FAILURE);
                    response.setMessage("customer with code " + customer.getClass() + " already exists");
                    return response;
                }
                //customer.setDateOfBirth(DateUtil.stringToDate(customer.getDateOfBirthStr(), "dd-MM-yyyy HH:mm"));
                if (customer.getContact() != null) {
                    contactDao.insert(customer.getContact());
                }
                customerDao.insert(customer);
            } else {
                if (customer.getContact() != null) {
                    final Contact existingContact = contactDao.getContactById(customer.getContact().getId());
                    if (existingContact != null) {
                        contactDao.update(customer.getContact());
                    } else {
                        contactDao.insert(customer.getContact());
                    }
                }
                customerDao.update(customer);
            }
            //update contact persons
            final ArrayList<Long> contactList = new ArrayList<Long>();
            for (Contact contact : customer.getContacts()) {
                if (contact == null) {
                    continue;
                }
                    //if contact is new
                if (contact.getId() < 0) {
                    contactDao.insert(contact);
                    final CustomerContact customerContact = new CustomerContact();
                    customerContact.setContact(contact);
                    customerContact.setCustomerId(customer.getId());
                    customerDao.insertCustomerContact(customerContact);
                // update existing one
                } else {
                    contactDao.update(contact);
                }
                contactList.add(contact.getId());
            }
            //delete contacts
            if (contactList.size() > 0) {
                //contactDao.deleteContactWhereIdNotIn(contactList);
                customerDao.deleteCustomerContactWhereIdNotIn(customer.getId(), contactList);
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

    /**
     * get customer by Id.
     * @param id id
     * @return CustomerGrade
     */
    public Customer getCustomerById(long id) {
        try {
            final Customer customer =  customerDao.getCustomerById(id);
            //customer.setDateOfBirthStr(DateUtil.dateToString(customer.getDateOfBirth(), "dd-MM-yyyy HH:mm"));
            return customer;
        } catch (Exception e) {
            logger.error("Error in getting customer with id: " + id, e);
            return null;
        }
    }

    /**
     * get customer by Code.
     * @param code code
     * @return CustomerGrade
     */
    public Customer getCustomerByCode(String code) {
        try {
            final Customer customer = customerDao.getCustomerByCode(code);
            //customer.setDateOfBirthStr(DateUtil.dateToString(customer.getDateOfBirth(), "dd-MM-yyyy HH:mm"));
            return customer;
        } catch (Exception e) {
            logger.error("Error in getting customer with code: " + code, e);
            return null;
        }
    }

    /**
     * get all customer account debt.
     * @return List of CustomerAccountDebt
     */
    public List<CustomerAccountDebt> getAllCustomerAccountDebt() {
        try {
            return customerAccountDebtDao.getAllCustomerAccountDebt();
        } catch (Exception e) {
            logger.error("Error in getting customer account debt list: ", e);
            return null;
        }
    }

    /**
     * get Customer Account Debt list per customer Id.
     * @param customerId customerId
     * @return List of CustomerAccountDebt
     */
    public List<CustomerAccountDebt> getCustomerAccountDebtPerCustomerId(long customerId) {
        try {
            return customerAccountDebtDao.getCustomerAccountDebtPerCustomerId(customerId);
        } catch (Exception e) {
            logger.error("Error in getting customer account debt list: ", e);
            return null;
        }
    }

    /**
     * get customer account debt report.
     * @param accountDebtRptForm accountDebtRptForm
     * @return List of customer account debt.
     */
    public List<CustomerAccountDebt> customerAccountDebtReportForMultiCustomer(AccountDebtRptForm accountDebtRptForm) {
        try {
            Timestamp dateTo = null;
            dateTo = DateUtil.stringToDate(accountDebtRptForm.getToDate(), "yyyy-MM-dd'T'HH:mm:ss.SSSX");
            return customerAccountDebtDao.customerAccountPaymentReportMultiCustomer(dateTo, accountDebtRptForm.getCustomerList());
        } catch (Exception e) {
            logger.error("Error in getting customer account debt list: ", e);
            return null;
        }
    }

    /**
     * get customer account debt report per one customer.
     * @param toDate toDate
     * @param customerId customerId
     * @return List of customer account debt.
     */
    public List<CustomerAccountDebt> customerAccountDebtReportForOneCustomer(Timestamp toDate, long customerId) {
        try {
            return customerAccountDebtDao.customerAccountPaymentReportPerCustomer(toDate, customerId);
        } catch (Exception e) {
            logger.error("Error in getting customer account debt list: ", e);
            return null;
        }
    }

    /**
     * get Customer Account Payment list per customer Id.
     * @param customerId customerId
     * @return List of CustomerAccountDebt
     */
    public List<CustomerAccountDebt> getCustomerAccountPaymentPerCustomerId(long customerId) {
        try {
            return customerAccountDebtDao.getCustomerAccountPaymentPerCustomerId(customerId);
        } catch (Exception e) {
            logger.error("Error in getting customer account payment list: ", e);
            return null;
        }
    }

    /**
     * get customer contact list by customer id.
     * @param customerId customerId
     * @return customer contact list.
     */
    public List<Contact> getCustomerContactListPerCustomerId(long customerId) {
        try {
            return contactDao.getCustomerContactList(customerId);
        } catch (Exception e) {
            logger.error("Error in getting customer contact list: ", e);
            return null;
        }
    }
}