package au.com.biztune.retail.service;

import au.com.biztune.retail.domain.DebtorPaymentForm;
import au.com.biztune.retail.domain.TxnHeader;
import au.com.biztune.retail.form.TxnHeaderForm;
import au.com.biztune.retail.response.CommonResponse;

import javax.ws.rs.core.SecurityContext;
import java.util.List;

/**
 * Created by arash on 20/04/2016.
 */
public interface TransactionService {
    /**
     * submit a transaction and save it into database.
     * @param  txnHeaderForm txnHeaderForm
     * @param  securityContext securityContext
     * @return CommonResponse
     */
    CommonResponse saveTransaction(TxnHeaderForm txnHeaderForm, SecurityContext securityContext);
    /**
     * get all transaction of store.
     * @return List of TxnHeader
     */
   List<TxnHeader> getAllTxnHeadersForStore();
    /**
     * get Transaction Sale per Id.
     * @param  txhdId Transaction Header Id
     * @return TxnHeaderForm
     */
    TxnHeaderForm getTxnHeaderPerId(long txhdId);

    /**
     * get Invoice per Id.
     * @param  invoiceId invoiceId
     * @return TxnHeaderForm
     */
    TxnHeaderForm getInvoicePerId(long invoiceId);

    /**
     * add payment.
     * @param txnHeaderForm txnHeaderForm
     * @param securityContext securityContext
     * @return Response.
     */
    CommonResponse addPayment(TxnHeaderForm txnHeaderForm, SecurityContext securityContext);

    /**
     * create invoice from transaction.
     * @param  txnHeaderForm txnHeaderForm
     * @param  securityContext securityContext
     * @return CommonResponse
     */
    CommonResponse createInvoice(TxnHeaderForm txnHeaderForm, SecurityContext securityContext);

    /**
     * get all transaction of store.
     * @return List of TxnHeader
     */
    List<TxnHeader> getAllInvoiceHeadersForStore();

    /**
     * create Txn Account Payment.
     * @param debtorPaymentForm debtorPaymentForm
     * @param securityContext securityContext
     * @return CommonResponse
     */
    CommonResponse createTxnAccPayment(DebtorPaymentForm debtorPaymentForm, SecurityContext securityContext);

}
