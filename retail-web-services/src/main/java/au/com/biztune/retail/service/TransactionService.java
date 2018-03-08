package au.com.biztune.retail.service;

import au.com.biztune.retail.domain.*;
import au.com.biztune.retail.form.ReversalDebtorPaymentForm;
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
     *
     * @param txnHeaderForm   txnHeaderForm
     * @param securityContext securityContext
     * @return CommonResponse
     */
    CommonResponse saveTransaction(TxnHeaderForm txnHeaderForm, SecurityContext securityContext);

    /**
     * get all transaction of store.
     *
     * @return List of TxnHeader
     */
    List<TxnHeader> getAllTxnHeadersForStore();

    /**
     * get Transaction Sale per Id.
     *
     * @param txhdId Transaction Header Id
     * @return TxnHeaderForm
     */
    TxnHeaderForm getTxnHeaderPerId(long txhdId);

    /**
     * get Invoice per Id.
     *
     * @param invoiceId invoiceId
     * @return TxnHeaderForm
     */
    TxnHeaderForm getInvoicePerId(long invoiceId);

    /**
     * add payment.
     *
     * @param txnHeaderForm   txnHeaderForm
     * @param securityContext securityContext
     * @return Response.
     */
    CommonResponse addPayment(TxnHeaderForm txnHeaderForm, SecurityContext securityContext);

    /**
     * create invoice from transaction.
     *
     * @param txnHeaderForm   txnHeaderForm
     * @param securityContext securityContext
     * @return CommonResponse
     */
    CommonResponse createInvoice(TxnHeaderForm txnHeaderForm, SecurityContext securityContext);

    /**
     * get all transaction of store.
     *
     * @return List of TxnHeader
     */
    List<TxnHeader> getAllInvoiceHeadersForStore();

    /**
     * create Txn Account Payment.
     *
     * @param debtorPaymentForm debtorPaymentForm
     * @param securityContext   securityContext
     * @return CommonResponse
     */
    CommonResponse createTxnAccPayment(DebtorPaymentForm debtorPaymentForm, SecurityContext securityContext);

    /**
     * get all Invoice of customer.
     *
     * @param cusgtomerId customerId
     * @return List of TxnHeader
     */
    List<TxnHeader> getAllInvoiceOfCustomer(long cusgtomerId);

    /**
     * get all transaction header of type sale and quote for specific customer.
     *
     * @param customerId customerId
     * @return List of TxnHeader
     */
    List<TxnHeader> getAllSaleOrdersAndQuotesOfCustomer(long customerId);

    /**
     * refund transaction.
     *
     * @param txnHeaderForm   txnHeaderForm
     * @param securityContext securityContext
     * @return CommonResponse
     */
    CommonResponse refundTransaction(TxnHeaderForm txnHeaderForm, SecurityContext securityContext);


    /**
     * search Sale Order and Quote per parameters.
     *
     * @param searchForm searchForm
     * @return List of TxnHeader
     */
    List<TxnHeader> searchTxnHeader(GeneralSearchForm searchForm);

    /**
     * search Sale Order and Quote per parameters.
     *
     * @param searchForm searchForm
     * @return List of TxnHeader
     */
    List<TxnHeader> searchInvoice(GeneralSearchForm searchForm);

    /**
     * delete quote per id.
     *
     * @param txhdId txhdId
     * @return CommonResponse
     */
    CommonResponse deleteQuote(long txhdId);

    /**
     * search transaction paging.
     *
     * @param searchForm searchForm
     * @return List of PurchaseOrderHeader
     */
    GeneralSearchForm searchTxnHeaderPaging(GeneralSearchForm searchForm);

    /**
     * search invoice paging.
     *
     * @param searchForm searchForm
     * @return List of invoice
     */
    GeneralSearchForm searchInvoicePaging(GeneralSearchForm searchForm);

    /**
     * get transactions of product.
     *
     * @param productId productId
     * @return List of transaction for product.
     */
    List<ProductSale> getTransactionsOfProduct(long productId);

    /**
     * get invoices of product.
     *
     * @param productId productId
     * @return List of invoice for product.
     */
    List<ProductSale> getInvoicesOfProduct(long productId);

    /**
     * get txn account payments per customer id.
     * @param customerId customerId
     * @return List of TxnAccPayment for customer
     */
    List<TxnDebtorPayment> getTxnAccPaymentsPerCustomerId(long customerId);

        /**
         * reverse a debtor payment transaction..
         * @param reversalDebtorPaymentForm reversalDebtorPaymentForm
         * @param securityContext securityContext
         * @return CommonResponse.
         */
    CommonResponse reversalTxnAccPayment(ReversalDebtorPaymentForm reversalDebtorPaymentForm, SecurityContext securityContext);
}