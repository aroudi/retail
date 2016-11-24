package au.com.biztune.retail.util;

import au.com.biztune.retail.domain.GeneralSearchForm;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by arash on 20/10/2016.
 */
public class SearchClauseBuilder {
    /**
     * build search clause.
     * @param searchForm searchForm
     * @param searchTable searchTable
     * @return
     */
    public static List<SearchClause> buildSearchWhereCluase(GeneralSearchForm searchForm, String searchTable) {
        List<SearchClause> clauseList = null;
        SearchClause searchClause = null;
        if (searchForm != null) {
            clauseList = new ArrayList<SearchClause>();
            Timestamp dateFrom = null;
            Timestamp dateTo = null;
            dateFrom = DateUtil.stringToDate(searchForm.getDateFrom(), "yyyy-MM-dd");
            if (dateFrom != null) {
                if ("TXN_HEADER".equals(searchTable)) {
                    searchClause = new SearchClause("TXHD_TRADING_DATE", " >= ", dateFrom);
                }
                if ("INVOICE".equals(searchTable)) {
                    searchClause = new SearchClause("TXIV_TRADING_DATE", " >= ", dateFrom);
                }
                if ("PURCHASE_ORDER_HEADER".equals(searchTable)) {
                    searchClause = new SearchClause("POH_CREATED_DATE", " >= ", dateFrom);
                }
                if ("DELIVERY_NOTE_HEADER".equals(searchTable)) {
                    searchClause = new SearchClause("DELN_DELIVERY_DATE", " >= ", dateFrom);
                }
                if ("CASH_SESSION".equals(searchTable)) {
                    searchClause = new SearchClause("CSSN_RECOCILE_DATE", " >= ", dateFrom);
                }
                clauseList.add(searchClause);
            }

            dateTo = DateUtil.stringToDate(searchForm.getDateTo(), "yyyy-MM-dd");
            if (dateTo != null) {
                if ("TXN_HEADER".equals(searchTable)) {
                    searchClause = new SearchClause("TXHD_TRADING_DATE", " <= ", dateTo);
                }
                if ("INVOICE".equals(searchTable)) {
                    searchClause = new SearchClause("TXIV_TRADING_DATE", " <= ", dateTo);
                }
                if ("PURCHASE_ORDER_HEADER".equals(searchTable)) {
                    searchClause = new SearchClause("POH_CREATED_DATE", " <= ", dateTo);
                }
                if ("DELIVERY_NOTE_HEADER".equals(searchTable)) {
                    searchClause = new SearchClause("DELN_DELIVERY_DATE", " <= ", dateTo);
                }
                if ("CASH_SESSION".equals(searchTable)) {
                    searchClause = new SearchClause("CSSN_RECOCILE_DATE", " <= ", dateTo);
                }
                clauseList.add(searchClause);
            }
            if (searchForm.getNoFrom() != null && !searchForm.getNoFrom().isEmpty()) {
                if ("TXN_HEADER".equals(searchTable)) {
                    searchClause = new SearchClause("TXHD_TXN_NR", " >= ", searchForm.getNoFrom());
                }
                if ("INVOICE".equals(searchTable)) {
                    searchClause = new SearchClause("TXIV_TXN_NR", " >= ", searchForm.getNoFrom());
                }
                if ("PURCHASE_ORDER_HEADER".equals(searchTable)) {
                    searchClause = new SearchClause("POH_ORDER_NUMBER", " >= ", searchForm.getNoFrom());
                }
                if ("DELIVERY_NOTE_HEADER".equals(searchTable)) {
                    searchClause = new SearchClause("DELN_GRN", " >= ", searchForm.getNoFrom());
                }
                if ("CASH_SESSION".equals(searchTable)) {
                    searchClause = new SearchClause("CSSN_SESSION_ID", " >= ", searchForm.getNoFrom());
                }
                clauseList.add(searchClause);
            }
            if (searchForm.getNoTo() != null && !searchForm.getNoTo().isEmpty()) {
                if ("TXN_HEADER".equals(searchTable)) {
                    searchClause = new SearchClause("TXHD_TXN_NR", " <= ", searchForm.getNoTo());
                }
                if ("INVOICE".equals(searchTable)) {
                    searchClause = new SearchClause("TXIV_TXN_NR", " <= ", searchForm.getNoTo());
                }
                if ("PURCHASE_ORDER_HEADER".equals(searchTable)) {
                    searchClause = new SearchClause("POH_ORDER_NUMBER", " <= ", searchForm.getNoTo());
                }
                if ("DELIVERY_NOTE_HEADER".equals(searchTable)) {
                    searchClause = new SearchClause("DELN_GRN", " <= ", searchForm.getNoTo());
                }
                if ("CASH_SESSION".equals(searchTable)) {
                    searchClause = new SearchClause("CSSN_SESSION_ID", " <= ", searchForm.getNoTo());
                }
                clauseList.add(searchClause);

            }
            if ("INVOICE".equals(searchTable) || "TXN_HEADER".equals(searchTable)) {
                if (searchForm.getClientId() > 0) {
                    searchClause = new SearchClause("CUSTOMER_ID", " = ", searchForm.getClientId());
                    clauseList.add(searchClause);
                }
            }
            if ("DELIVERY_NOTE_HEADER".equals(searchTable) || "PURCHASE_ORDER_HEADER".equals(searchTable)) {
                if (searchForm.getSupplierId() > 0) {
                    searchClause = new SearchClause("SUPPLIER_ID", " = ", searchForm.getSupplierId());
                    clauseList.add(searchClause);
                }
            }
        }
        if (clauseList.size() > 0) {
            return clauseList;
        } else {
            return null;
        }
    }

}
