package au.com.biztune.retail.domain;

import java.sql.Timestamp;

/**
 * Created by arash on 9/03/2017.
 */
public class TxnAccPayment {
    private long id;
    private long txhdId;
    private long cadId;
    private long orguId;
    private long storeId;
    private Timestamp tapPaymentDate;
    private double tapAmountPaid;
    private String txhdTxnNr;
    private CustomerAccountDebt customerAccountDebt;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTxhdId() {
        return txhdId;
    }

    public void setTxhdId(long txhdId) {
        this.txhdId = txhdId;
    }

    public long getCadId() {
        return cadId;
    }

    public void setCadId(long cadId) {
        this.cadId = cadId;
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

    public Timestamp getTapPaymentDate() {
        return tapPaymentDate;
    }

    public void setTapPaymentDate(Timestamp tapPaymentDate) {
        this.tapPaymentDate = tapPaymentDate;
    }

    public double getTapAmountPaid() {
        return tapAmountPaid;
    }

    public void setTapAmountPaid(double tapAmountPaid) {
        this.tapAmountPaid = tapAmountPaid;
    }

    public String getTxhdTxnNr() {
        return txhdTxnNr;
    }

    public void setTxhdTxnNr(String txhdTxnNr) {
        this.txhdTxnNr = txhdTxnNr;
    }

    public CustomerAccountDebt getCustomerAccountDebt() {
        return customerAccountDebt;
    }

    public void setCustomerAccountDebt(CustomerAccountDebt customerAccountDebt) {
        this.customerAccountDebt = customerAccountDebt;
    }
}
