package au.com.biztune.retail.service;

import au.com.biztune.retail.domain.TxnHeader;
import au.com.biztune.retail.form.TxnHeaderForm;
import au.com.biztune.retail.response.CommonResponse;

import java.util.List;

/**
 * Created by arash on 20/04/2016.
 */
public interface TransactionService {
    /**
     * submit a transaction and save it into database.
     * @param  txnHeaderForm txnHeaderForm
     * @return CommonResponse
     */
    CommonResponse addTransaction(TxnHeaderForm txnHeaderForm);
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
}
