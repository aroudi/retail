package au.com.biztune.retail.service;

import au.com.biztune.retail.domain.TxnHeader;

/**
 * Created by arash on 27/09/2016.
 */
public interface TotalerService {
    /**
     * process transaction and extract total figures from it.
     * @param txnHeader txnHeader
     */
    void extractTotalFiguresFromTxn(TxnHeader txnHeader);
}
