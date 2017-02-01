package au.com.biztune.retail.dao;

import au.com.biztune.retail.domain.TxnDetail;
import au.com.biztune.retail.domain.TxnHeader;
import au.com.biztune.retail.domain.TxnMedia;

import java.util.List;

/**
 * Created by arash on 19/08/2016.
 */
public interface InvoiceDao {
    /**
     * insert invoice.
     * @param txnHeader txnHeader
     */
    void insertInvoice(TxnHeader txnHeader);

    /**
     * insert invoice detail.
     * @param txnDetail txnDetail
     */
    void insertInvoiceDetail(TxnDetail txnDetail);

    /**
     * insert invoice media.
     * @param txnMedia txnMedia
     */
    void insertIncoiceMedia(TxnMedia txnMedia);

    /**
     * get invoiceDetail per invoice id.
     * @param invoiceId invoiceId
     * @return list of TxnDetail
     */
    List<TxnDetail> getInvoiceDetailPerInvoiceId(long invoiceId);

    /**
     * get invoice media per invoice id.
     * @param invoiceId invoiceId
     * @return List of TxnMedia.
     */
    List<TxnMedia> getInvoiceMediaPerInvoiceId(long invoiceId);

    /**
     * get invoice media per invoice id.
     * @param txhdId txhdId
     * @return list of TxnMedia
     */
    List<TxnHeader> getInvoiceHeaderPerTxhdId(long txhdId);

    /**
     * get invoiceinvoice id.
     * @param invoiceId invoiceId
     * @return Invoice
     */
    TxnHeader getInvoiceHeaderPerInvoiceId(long invoiceId);

    /**
     * assigne invoice number to invoice.
     * @param txnHeader txnHeader
     */
    void assigneInvoiceNumber(TxnHeader txnHeader);

    /**
     * get all invoices.
     * @param storeId storeId
     * @return List of Invoice
     */
    List<TxnHeader> getAllInvoiceHeaderPerStoreId(long storeId);

    /**
     * get invoice list of customer.
     * @param customerId customerId
     * @return List of Invoice.
     */
    List<TxnHeader> getAllInvoiceOfCustomer(long customerId);

    /**
     * update refund amount on invoice detail after refunding an item.
     * @param totalRefunaAmount totalRefunaAmount
     * @param id id
     */
    void updateInvoiceRefundItem(double totalRefunaAmount, long id);

    /**
     * search invoices.
     * @param storeId storeId
     * @param clauseList clauseList
     * @param txnTypeList txnTypeList
     * @return List of invoices
     */
    List<TxnHeader> searchInvoice(long storeId, List txnTypeList, List clauseList);

    /**
     * search invoices.
     * @param storeId storeId
     * @param clauseList clauseList
     * @param txnTypeList txnTypeList
     * @param fromPage fromPage
     * @param toPage toPage
     * @return List of invoices
     */
    List<TxnHeader> searchInvoicePaging(long storeId, List txnTypeList, List clauseList, long fromPage, long toPage);

    /**
     * get invoice search query total .
     * @param storeId storeId
     * @param txnTypeList txnTypeList
     * @param clauseList clauseList
     * @return total records
     */
    long getInvoiceQueryTotalRows(long storeId, List txnTypeList, List clauseList);
    /**
     * update print status.
     * @param invoiceId invoiceId
     * @param printed printed
     */
    void updateTxnPrintStatus(long invoiceId, boolean printed);
}
