package au.com.biztune.retail.dao;

import au.com.biztune.retail.domain.Customer;
import au.com.biztune.retail.domain.CustomerAccountDebt;
import au.com.biztune.retail.domain.TxnAccPayment;
import au.com.biztune.retail.domain.TxnDebtorPayment;
import org.apache.ibatis.annotations.Mapper;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by arash on 12/09/2016.
 */
@Mapper
public interface CustomerAccountDebtDao {
    /**
     * get customer account debt per customerId and txnId.
     * @param customerId customerId
     * @return CustomerAccountDebt
     */
    List<CustomerAccountDebt> getCustomerAccountDebtPerCustomerId(long customerId);


    /**
     * insert customerAccountDebt.
     * @param customerAccountDebt customerAccountDebt
     */
    void insert(CustomerAccountDebt customerAccountDebt);

    /**
     * update customerAccountDebt.
     * @param customerAccountDebt customerAccountDebt
     */
    void update(CustomerAccountDebt customerAccountDebt);


    /**
     * get All Customer Account Debt records for all customers.
     * @return List of CustomerAccountDebt
     */
    List<CustomerAccountDebt> getAllCustomerAccountDebt();

    /**
     * insert Txn Account Payment-from customerAccountDebt object. a payment for customer debt.
     * @param customerAccountDebt customerAccountDebt
     */
    void insertTxnAccPayment(CustomerAccountDebt customerAccountDebt);

    /**
     * insert Transaction Account Payment per its object.
     * @param txnAccPayment txnAccPayment.
     */
    void insertTxnAccPaymentObject(TxnAccPayment txnAccPayment);
    /**
     * get customer account debt per customer id and txhd id.
     * @param customerId customerId
     * @param txhdId txhdId
     * @return CustomerAccountDebt
     */
    CustomerAccountDebt getCustomerAccountDebtPerCustomerIdAndTxhdId(long customerId, long txhdId);

    /**
     * get customer account debt by CAD ID.
     * @param cadId cadId
     * @return CustomerAccountDebt
     */
    CustomerAccountDebt getCustomerAccountDebtById(long cadId);


    /**
     * get customer account payment per customer id.
     * @param customerId customerId
     * @return customer account debt
     */
    List<CustomerAccountDebt> getCustomerAccountPaymentPerCustomerId (long customerId);

    /**
     * get customer account debt report by date and customer/for multiple customer.
     * @param toDate toDate
     * @param customerList customerList
     * @return CustomerAccountDebt list.
     */
    List<CustomerAccountDebt> customerAccountPaymentReportMultiCustomer(Timestamp toDate, List<Customer> customerList);

    /**
     * get customer account debt report by date and customer - for only one customer.
     * @param toDate toDate
     * @param customerId customerId
     * @return list of account debt for customer.
     */
    List<CustomerAccountDebt> customerAccountPaymentReportPerCustomer(Timestamp toDate, long customerId);

    /**
     * get customer debt balance per customer id and date.
     * @param toDate toDate
     * @param customerId customerId
     * @return balance
     */
    double customerAccountBalancePerCustomerAndDate(Timestamp toDate, long customerId);

    /**
     * return all payment made for customer account debt per customer id.
     * @param customerId customerId
     * @return List of TxnAccPayment
     */
    List<TxnAccPayment> getTxnAccPaymentWithCustomerAccDebtyByCustomerId(long customerId);

    /**
     * get debtor payment transactions by customer id and type id.
     * @param customerId customerId
     * @param txhdTypeId txhdTypeId
     * @return list of debtor payment transaction
     */
    List<TxnDebtorPayment> getDebtorPaymentTxnByCustomerIdAndType (long customerId, long txhdTypeId);
}
