package au.com.biztune.retail.service;

import au.com.biztune.retail.domain.TxnHeader;

/**
 * Created by arash on 23/11/2016.
 */
public interface AccountingExtractService {
    /**
     * process transaction and extract accounting figures.
     * @param txnHeader txnHeader.
     */
    void extractAccountingFiguresFromTxn(TxnHeader txnHeader);
}
