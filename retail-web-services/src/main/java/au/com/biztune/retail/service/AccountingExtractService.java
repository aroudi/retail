package au.com.biztune.retail.service;

import au.com.biztune.retail.domain.GeneralSearchForm;
import au.com.biztune.retail.domain.JournalEntry;
import au.com.biztune.retail.domain.TxnHeader;

import java.util.List;

/**
 * Created by arash on 23/11/2016.
 */
public interface AccountingExtractService {
    /**
     * process transaction and extract accounting figures.
     * @param txnHeader txnHeader.
     */
    void extractAccountingFiguresFromTxn(TxnHeader txnHeader);

    /**
     * search Sale Order and Quote per parameters.
     * @param searchForm searchForm
     * @return List of Journal Entry
     */
    List<JournalEntry> accountingExportReport(GeneralSearchForm searchForm);
}
