package au.com.biztune.retail.form;

import au.com.biztune.retail.domain.Customer;

import java.util.List;

/**
 * Created by arash on 9/03/2017.
 */
public class AccountDebtRptForm {
    private String toDate;
    private List<Customer> customerList;

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public List<Customer> getCustomerList() {
        return customerList;
    }

    public void setCustomerList(List<Customer> customerList) {
        this.customerList = customerList;
    }
}
