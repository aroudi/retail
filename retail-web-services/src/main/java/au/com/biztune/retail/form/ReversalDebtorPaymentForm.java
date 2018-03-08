package au.com.biztune.retail.form;

import au.com.biztune.retail.domain.TxnDebtorPayment;

import java.util.List;

/**
 * Created by arash on 6/03/2018.
 */
public class ReversalDebtorPaymentForm {
    private List<TxnDebtorPayment> txnDebtorPaymentList;
    private String comment;
    private long customerId;

    public List<TxnDebtorPayment> getTxnDebtorPaymentList() {
        return txnDebtorPaymentList;
    }

    public void setTxnDebtorPaymentList(List<TxnDebtorPayment> txnDebtorPaymentList) {
        this.txnDebtorPaymentList = txnDebtorPaymentList;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }
}
