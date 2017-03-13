package au.com.biztune.retail.form;

import au.com.biztune.retail.domain.Customer;
import au.com.biztune.retail.domain.CustomerAccountDebt;

import java.util.List;

/**
 * Created by arash on 10/03/2017.
 */
public class CustomerStatementReport {
    private Customer customer;
    private List<CustomerAccountDebt> customerAccountDebtList;
    private double totalDebtBalance;
    public Customer getCustomer() {
        return customer;
    }


    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<CustomerAccountDebt> getCustomerAccountDebtList() {
        return customerAccountDebtList;
    }

    public void setCustomerAccountDebtList(List<CustomerAccountDebt> customerAccountDebtList) {
        this.customerAccountDebtList = customerAccountDebtList;
    }

    public double getTotalDebtBalance() {
        return totalDebtBalance;
    }

    public void setTotalDebtBalance(double totalDebtBalance) {
        this.totalDebtBalance = totalDebtBalance;
    }
}
