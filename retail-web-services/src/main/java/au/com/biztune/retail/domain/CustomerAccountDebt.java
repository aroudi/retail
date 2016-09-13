package au.com.biztune.retail.domain;

import java.sql.Timestamp;

/**
 * Created by arash on 12/09/2016.
 */
public class CustomerAccountDebt {
    private long cadId;
    //transaction account payment id.
    private long tapId;
    private Customer customer;
    private long txhdId;
    private String txhdTxnNr;
    private double cadAmountDebt;
    private Timestamp cadStartDate;
    private Timestamp cadDueDate;
    private Timestamp cadPaymentDate;
    private boolean cadPaid;
    private double balance;
    private double paying;
    private long orguId;
    private long storeId;
    //the wrapping transaction number for account payment
    private long txnAccPayId;

    public long getCadId() {
        return cadId;
    }

    public void setCadId(long cadId) {
        this.cadId = cadId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public long getTxhdId() {
        return txhdId;
    }

    public void setTxhdId(long txhdId) {
        this.txhdId = txhdId;
    }

    public String getTxhdTxnNr() {
        return txhdTxnNr;
    }

    public void setTxhdTxnNr(String txhdTxnNr) {
        this.txhdTxnNr = txhdTxnNr;
    }

    public double getCadAmountDebt() {
        return cadAmountDebt;
    }

    public void setCadAmountDebt(double cadAmountDebt) {
        this.cadAmountDebt = cadAmountDebt;
    }

    public Timestamp getCadStartDate() {
        return cadStartDate;
    }

    public void setCadStartDate(Timestamp cadStartDate) {
        this.cadStartDate = cadStartDate;
    }

    public Timestamp getCadDueDate() {
        return cadDueDate;
    }

    public void setCadDueDate(Timestamp cadDueDate) {
        this.cadDueDate = cadDueDate;
    }

    public Timestamp getCadPaymentDate() {
        return cadPaymentDate;
    }

    public void setCadPaymentDate(Timestamp cadPaymentDate) {
        this.cadPaymentDate = cadPaymentDate;
    }

    public boolean isCadPaid() {
        return cadPaid;
    }

    public void setCadPaid(boolean cadPaid) {
        this.cadPaid = cadPaid;
    }

    public double getPaying() {
        return paying;
    }

    public void setPaying(double paying) {
        this.paying = paying;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public long getTapId() {
        return tapId;
    }

    public void setTapId(long tapId) {
        this.tapId = tapId;
    }

    public long getOrguId() {
        return orguId;
    }

    public void setOrguId(long orguId) {
        this.orguId = orguId;
    }

    public long getStoreId() {
        return storeId;
    }

    public void setStoreId(long storeId) {
        this.storeId = storeId;
    }

    public long getTxnAccPayId() {
        return txnAccPayId;
    }

    public void setTxnAccPayId(long txnAccPayId) {
        this.txnAccPayId = txnAccPayId;
    }
}
