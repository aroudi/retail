package au.com.biztune.retail.dao;

import au.com.biztune.retail.domain.CustomerAccountDebt;

import java.util.List;

/**
 * Created by arash on 12/09/2016.
 */
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
     * insert Txn Account Payment. a payment for customer debt.
     * @param customerAccountDebt customerAccountDebt
     */
    void insertTxnAccPayment(CustomerAccountDebt customerAccountDebt);

    /**
     * get customer account debt per customer id and txhd id.
     * @param customerId customerId
     * @param txhdId txhdId
     * @return CustomerAccountDebt
     */
    CustomerAccountDebt getCustomerAccountDebtPerCustomerIdAndTxhdId(long customerId, long txhdId);

    /**
     * get customer account payment per customer id.
     * @param customerId customerId
     * @return customer account debt
     */
    List<CustomerAccountDebt> getCustomerAccountPaymentPerCustomerId (long customerId);

}
