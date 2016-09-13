package au.com.biztune.retail.domain;

import au.com.biztune.retail.form.TxnMediaForm;

import java.util.List;

/**
 * Created by arash on 12/09/2016.
 */
public class DebtorPaymentForm {
    private Customer customer;
    private List<CustomerAccountDebt> debtList;
    private List<TxnMediaForm> txnMediaList;
    private double total;
    private double creditAvailable;
    private double totalOwing;
    private double amountDue;
    private double amountPaid;
    private double valueRounding;

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<CustomerAccountDebt> getDebtList() {
        return debtList;
    }

    public void setDebtList(List<CustomerAccountDebt> debtList) {
        this.debtList = debtList;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getCreditAvailable() {
        return creditAvailable;
    }

    public void setCreditAvailable(double creditAvailable) {
        this.creditAvailable = creditAvailable;
    }

    public double getTotalOwing() {
        return totalOwing;
    }

    public void setTotalOwing(double totalOwing) {
        this.totalOwing = totalOwing;
    }

    public double getAmountDue() {
        return amountDue;
    }

    public void setAmountDue(double amountDue) {
        this.amountDue = amountDue;
    }

    public double getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(double amountPaid) {
        this.amountPaid = amountPaid;
    }

    public List<TxnMediaForm> getTxnMediaList() {
        return txnMediaList;
    }

    public void setTxnMediaList(List<TxnMediaForm> txnMediaList) {
        this.txnMediaList = txnMediaList;
    }

    public double getValueRounding() {
        return valueRounding;
    }

    public void setValueRounding(double valueRounding) {
        this.valueRounding = valueRounding;
    }
}
