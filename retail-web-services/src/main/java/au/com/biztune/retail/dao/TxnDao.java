package au.com.biztune.retail.dao;

import au.com.biztune.retail.domain.ProductSale;
import au.com.biztune.retail.domain.TxnDetail;
import au.com.biztune.retail.domain.TxnHeader;
import au.com.biztune.retail.domain.TxnMedia;

import java.util.List;

/**
 * Created by arash on 14/04/2016.
 */
public interface TxnDao {

    /**
     * insertTxnHeader.
     * @param txnHeader txnHeader
     */
    void insertTxnHeader(TxnHeader txnHeader);

    /**
     *  yupdate TxnHeader.
     * @param txnHeader txnHeader
     */
    void updateTxnHeader(TxnHeader txnHeader);

    /**
     * insert txn detail.
     * @param txnDetail txnDetail
     */
    void insertTxnDetail(TxnDetail txnDetail);

    /**
     * update txn detail.
     * @param txnDetail txnDetail
     */
    void updateTxnDetail(TxnDetail txnDetail);

    /**
     * insert txnMedia.
     * @param txnMedia txnMedia
     */
    void insertTxnMedia(TxnMedia txnMedia);

    /**
     * get TxnDetails Per TxhdId.
     * @param txhdId txhdId
     * @return List of TxnDetail
     */
    List<TxnDetail> getTxnDetailPerTxhdId(long txhdId);

    /**
     * void txnMedia.
     * @param txnMedia txnMedia
     */
    void voidTxnMedia (TxnMedia txnMedia);

    /**
     * get TxnMedia Per TxhdId.
     * @param txhdId txhdId
     * @return List of TxnMedia
     */
    List<TxnMedia> getTxnMediaPerTxhdId(long txhdId);
    /**
     * get TxnHeader Per StoreId, and CustomerId.
     * @param customerId customerId
     * @param txnTypeList txnTypeList
     * @return List of TxnHeader
     */
    List<TxnHeader> getSaleOrderAndQuoteOfCustomer(long customerId, List txnTypeList);
    /**
     * get TxnHeader Per StoreId.
     * @param storeId storeId
     * @param txnTypeList txnTypeList
     * @return List of TxnHeader
     */
    List<TxnHeader> getTxnHeaderPerStoreId(long storeId, List txnTypeList);
    /**
     * get TxnHeader Per StoreId, and CustomerId and TypeId.
     * @param storeId storeId
     * @param customerId customerId
     * @param typeId typeId
     * @return List of TxnHeader
     */
    List<TxnHeader> getTxnHeaderPerStoreIdAndCustomerIdAndTypeId(long storeId, long customerId, long typeId);


    /**
     * get TxnHeader Per TxhdId.
     * @param txhdId txhdId
     * @return TxnHeader
     */
    TxnHeader getTxnHeaderPerTxhdId(long txhdId);

    /**
     * assigne txnNumber.
     * @param txnHeader txnHeader
     */
    void assigneTxnNumber(TxnHeader txnHeader);

    /**
     * update balance on txn detail.
     * @param txnDetail txnDetail
     */
    void updateTxnDetailQtyBalance(TxnDetail txnDetail);

    /**
     * update txn header total values.
     * @param txnHeader txnHeader
     */
    void updateTxnHeaderTotalValues(TxnHeader txnHeader);

    /**
     * get amount paid for transaction sale.
     * @param txhdId txhdId
     * @return amount paid
     */
    Double getTxnHeaderAmountPaid(long txhdId);

    /**
     * update refund amount on txn sale.
     * @param totalRefundAmount totalRefundAmount
     * @param newBalance newBalance
     * @param id id
     */
    void updateTxnSaleRefundItem(double totalRefundAmount, double newBalance, long id);

    /**
     * get txn_detail per id.
     * @param id id
     * @return TxnDetail
     */
    TxnDetail getTxnDetailPerId(long id);

    /**
     * search txn header per type and search parameters.
     * @param storeId storeId
     * @param txnTypeList txnTypeList
     * @param clauseList clauseList
     * @return List of txnHeader
     */
    List<TxnHeader> searchTxnHeader(long storeId, List txnTypeList, List clauseList);

    /**
     * search txn header per type and search parameters.
     * @param storeId storeId
     * @param txnTypeList txnTypeList
     * @param clauseList clauseList
     * @param fromPage fromPage
     * @param toPage toPage
     * @return List of txnHeader
     */
    List<TxnHeader> searchTxnHeaderPaging(long storeId, List txnTypeList, List clauseList, long fromPage, long toPage);

    /**
     * get txn header search query total .
     * @param storeId storeId
     * @param txnTypeList txnTypeList
     * @param clauseList clauseList
     * @return total records
     */
    long getTxnHeaderQueryTotalRows(long storeId, List txnTypeList, List clauseList);
    /**
     * delete txnHeader per txhdId.
     * @param txhdId txhdId
     */
    void deleteTxnHeaderPerTxhdId(long txhdId);

    /**
     * delete txnMedia per txhdId.
     * @param txhdId txhdId
     */
    void deleteTxnMediaPerTxhdId(long txhdId);

    /**
     * delete txnDetail per txhdId.
     * @param txhdId txhdId
     */
    void deleteTxnDetailPerTxhdId(long txhdId);

    /**
     * update print status.
     * @param txhdId txhdId
     * @param printed printed
     */
    void updateTxnPrintStatus(long txhdId, boolean printed);

    /**
     * get transaction sale of specific product.
     * @param productId productId
     * @return List of transaction including product.
     */
    List<ProductSale> getTransactionsOfProduct(long productId);

    /**
     * get all txn details for txn set.
     * @param txhdIds set of txn_headers ids
     * @return List of txnDetail
     */
    List<TxnDetail> getTxnDetailsOfMultipleTxnId(List txhdIds);

    /**
     * update txn detail status.
     * @param txnDetail txnDetail.
     */
    void updateTxnDetailBackOrderAndStatus(TxnDetail txnDetail);

    /**
     * update qty received and status.
     * @param statusId statusId
     * @param qtyReceived qtyReceived
     * @param txdeId txdeId
     */
    void updateTxnDetailRcvdQtyAndStatus(long statusId, double qtyReceived, long txdeId);
    /**
     * update txn status per txhd id and status id.
     * @param txhdId transaction  header id
     * @param statusId status id
     */
    void updateTxnHeaderStatusPerTxhdId(long txhdId, long statusId);
    /**
     * update txn status.
     * @param txnHeader txnHeader.
     */
    void updateTxnHeaderStatus(TxnHeader txnHeader);

    /**
     * get all txnDetails(include status) per txhd id.
     * @param txhdId txhdId
     * @return list of txnDetail
     */
    List<TxnDetail> getTxnDetailStatusPerTxhdId(long txhdId);

    /**
     * get txn detail light object per id.
     * @param txdeId txdeId
     * @return txndetail
     */
    TxnDetail getTxnDetailLightPerId(long txdeId);
}
