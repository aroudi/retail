package au.com.biztune.retail.dao;

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
     * @param storeId storeId
     * @param customerId customerId
     * @return List of TxnHeader
     */
    List<TxnHeader> getTxnHeaderPerStoreIdAndCustomerId(long storeId, long customerId);
    /**
     * get TxnHeader Per StoreId.
     * @param storeId storeId
     * @return List of TxnHeader
     */
    List<TxnHeader> getTxnHeaderPerStoreId(long storeId);
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
}
