package au.com.biztune.retail.util;

import au.com.biztune.retail.domain.GeneralSearchForm;
import au.com.biztune.retail.form.BoqSearchForm;
import au.com.biztune.retail.form.ProductSearchForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
    final Logger logger = LoggerFactory.getLogger(SearchClauseBuilder.class);
    public static List<SearchClause> buildSearchWhereCluase(GeneralSearchForm searchForm, String searchTable) {
        List<SearchClause> clauseList = null;
        SearchClause searchClause = null;
        if (searchForm != null) {
            clauseList = new ArrayList<SearchClause>();
            Timestamp dateFrom = null;
            Timestamp dateTo = null;
            dateFrom = DateUtil.stringToDate(searchForm.getDateFrom(), "yyyy-MM-dd'T'HH:mm:ss.SSSX");
            if (dateFrom != null) {
                if ("TXN_HEADER".equals(searchTable)) {
                    searchClause = new SearchClause("TXHD_TRADING_DATE", " >= ", dateFrom);
                    clauseList.add(searchClause);
                }
                if ("INVOICE".equals(searchTable)) {
                    searchClause = new SearchClause("TXIV_TRADING_DATE", " >= ", dateFrom);
                    clauseList.add(searchClause);
                }
                if ("PURCHASE_ORDER_HEADER".equals(searchTable)) {
                    searchClause = new SearchClause("POH_CREATED_DATE", " >= ", dateFrom);
                    clauseList.add(searchClause);
                }
                if ("DELIVERY_NOTE_HEADER".equals(searchTable)) {
                    searchClause = new SearchClause("DELN_DELIVERY_DATE", " >= ", dateFrom);
                    clauseList.add(searchClause);
                }
                if ("CASH_SESSION".equals(searchTable) && searchForm.getSearchRange().equals("dateRange")) {
                    searchClause = new SearchClause("CSSN_RECOCILE_DATE", " >= ", dateFrom);
                    clauseList.add(searchClause);
                }
            }

            dateTo = DateUtil.stringToDate(searchForm.getDateTo(), "yyyy-MM-dd'T'HH:mm:ss.SSSX");
            if (dateTo != null) {
                //maximise the hour for dateTo
                Calendar cal = Calendar.getInstance();
                cal.setTime(dateTo);
                cal.set(Calendar.HOUR, 24);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                dateTo = new Timestamp(cal.getTime().getTime());
                if ("TXN_HEADER".equals(searchTable)) {
                    searchClause = new SearchClause("TXHD_TRADING_DATE", " <= ", dateTo);
                    clauseList.add(searchClause);
                }
                if ("INVOICE".equals(searchTable)) {
                    searchClause = new SearchClause("TXIV_TRADING_DATE", " <= ", dateTo);
                    clauseList.add(searchClause);
                }
                if ("PURCHASE_ORDER_HEADER".equals(searchTable)) {
                    searchClause = new SearchClause("POH_CREATED_DATE", " <= ", dateTo);
                    clauseList.add(searchClause);
                }
                if ("DELIVERY_NOTE_HEADER".equals(searchTable)) {
                    searchClause = new SearchClause("DELN_DELIVERY_DATE", " <= ", dateTo);
                    clauseList.add(searchClause);
                }
                if ("CASH_SESSION".equals(searchTable) && searchForm.getSearchRange().equals("dateRange")) {
                    searchClause = new SearchClause("CSSN_RECOCILE_DATE", " <= ", dateTo);
                    clauseList.add(searchClause);
                }
            }
            if (searchForm.getNoFrom() != null && !searchForm.getNoFrom().isEmpty()) {
                if ("TXN_HEADER".equals(searchTable)) {
                    searchClause = new SearchClause("TXHD_TXN_NR", " >= ", searchForm.getNoFrom());
                    clauseList.add(searchClause);
                }
                if ("INVOICE".equals(searchTable)) {
                    searchClause = new SearchClause("TXIV_TXN_NR", " >= ", searchForm.getNoFrom());
                    clauseList.add(searchClause);
                }
                if ("PURCHASE_ORDER_HEADER".equals(searchTable)) {
                    searchClause = new SearchClause("POH_ORDER_NUMBER", " >= ", searchForm.getNoFrom());
                    clauseList.add(searchClause);
                }
                if ("DELIVERY_NOTE_HEADER".equals(searchTable)) {
                    searchClause = new SearchClause("DELN_GRN", " >= ", searchForm.getNoFrom());
                    clauseList.add(searchClause);
                }
                if ("CASH_SESSION".equals(searchTable) && searchForm.getSearchRange().equals("idRange")) {
                    searchClause = new SearchClause("CSSN_SESSION_ID", " >= ", searchForm.getNoFrom());
                    clauseList.add(searchClause);
                }
            }
            if (searchForm.getNoTo() != null && !searchForm.getNoTo().isEmpty()) {
                if ("TXN_HEADER".equals(searchTable)) {
                    searchClause = new SearchClause("TXHD_TXN_NR", " <= ", searchForm.getNoTo());
                    clauseList.add(searchClause);
                }
                if ("INVOICE".equals(searchTable)) {
                    searchClause = new SearchClause("TXIV_TXN_NR", " <= ", searchForm.getNoTo());
                    clauseList.add(searchClause);
                }
                if ("PURCHASE_ORDER_HEADER".equals(searchTable)) {
                    searchClause = new SearchClause("POH_ORDER_NUMBER", " <= ", searchForm.getNoTo());
                    clauseList.add(searchClause);
                }
                if ("DELIVERY_NOTE_HEADER".equals(searchTable)) {
                    searchClause = new SearchClause("DELN_GRN", " <= ", searchForm.getNoTo());
                    clauseList.add(searchClause);
                }
                if ("CASH_SESSION".equals(searchTable) && searchForm.getSearchRange().equals("idRange")) {
                    searchClause = new SearchClause("CSSN_SESSION_ID", " <= ", searchForm.getNoTo());
                    clauseList.add(searchClause);
                }

            }
            if ("INVOICE".equals(searchTable) || "TXN_HEADER".equals(searchTable)) {
                if (searchForm.getClientId() > 0) {
                    searchClause = new SearchClause("CUSTOMER_ID", " = ", searchForm.getClientId());
                    clauseList.add(searchClause);
                }
                if (searchForm.getStatusId() > 0) {
                    searchClause = new SearchClause("TXHD_STATUS", " = ", searchForm.getStatusId());
                    clauseList.add(searchClause);
                }
            }
            if ("DELIVERY_NOTE_HEADER".equals(searchTable) || "PURCHASE_ORDER_HEADER".equals(searchTable)) {
                if (searchForm.getSupplierId() > 0) {
                    searchClause = new SearchClause("SUPPLIER_ID", " = ", searchForm.getSupplierId());
                    clauseList.add(searchClause);
                }
            }
            if ("PURCHASE_ORDER_HEADER".equals(searchTable)) {
                if ((searchForm.getProjectCode() != null) && (!searchForm.getProjectCode().isEmpty())) {
                    searchClause = new SearchClause("POH_PRJ_CODE", " like ", "%" + searchForm.getProjectCode() + "%");
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

    public static List<SearchClause> buildProductSearchWhereCluase(ProductSearchForm searchForm) {
        List<SearchClause> clauseList = null;
        SearchClause searchClause = null;
        if (searchForm != null) {
            clauseList = new ArrayList<SearchClause>();
            if (searchForm.getSupplierId() > 0) {
                searchClause = new SearchClause("sol.SUPP_ID", " = ", searchForm.getSupplierId());
                clauseList.add(searchClause);
            }
            if (searchForm.getProdSku() != null && !searchForm.getProdSku().isEmpty()) {
                searchClause = new SearchClause("prod.PROD_SKU", " like ", "%" + searchForm.getProdSku() + "%");
                clauseList.add(searchClause);
            }
            if (searchForm.getReference() != null && !searchForm.getReference().isEmpty()) {
                searchClause = new SearchClause("prod.REFERENCE", " like ", "%" + searchForm.getReference() + "%");
                clauseList.add(searchClause);
            }
            if (searchForm.getProdName() != null && !searchForm.getProdName().isEmpty()) {
                searchClause = new SearchClause("prod.PROD_NAME", " like ", "%" + searchForm.getProdName() + "%");
                clauseList.add(searchClause);
            }
            if (searchForm.getProdTypeId() > 0) {
                searchClause = new SearchClause("prod.CAT_ID_TYPE", " = ", searchForm.getProdTypeId());
                clauseList.add(searchClause);
            }
        }
        if (clauseList.size() < 1) {
            return null;
        }
        return clauseList;
    }

    public static List<SearchClause> buildBoqSearchWhereCluase(BoqSearchForm searchForm) {
        List<SearchClause> clauseList = null;
        SearchClause searchClause = null;
        if (searchForm != null) {
            clauseList = new ArrayList<SearchClause>();
            Timestamp dateFrom = null;
            Timestamp dateTo = null;
            dateFrom = DateUtil.stringToDate(searchForm.getDateFrom(), "yyyy-MM-dd'T'HH:mm:ss.SSSX");
            if (dateFrom != null) {
                searchClause = new SearchClause("boq.DATE_CREATED", " >= ", dateFrom);
                clauseList.add(searchClause);
            }

            dateTo = DateUtil.stringToDate(searchForm.getDateTo(), "yyyy-MM-dd'T'HH:mm:ss.SSSX");
            if (dateTo != null) {
                //maximise the hour for dateTo
                Calendar cal = Calendar.getInstance();
                cal.setTime(dateTo);
                cal.set(Calendar.HOUR, 24);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                dateTo = new Timestamp(cal.getTime().getTime());
                searchClause = new SearchClause("boq.DATE_CREATED", " <= ", dateTo);
                clauseList.add(searchClause);
            }

            if (searchForm.getProjectId() > 0) {
                searchClause = new SearchClause("boq.PROJECT_ID", " = ", searchForm.getProjectId());
                clauseList.add(searchClause);
            }
            if (searchForm.getClientId() > 0) {
                searchClause = new SearchClause("prj.CLIENT_ID", " = ", searchForm.getClientId());
                clauseList.add(searchClause);
            }
            if (searchForm.getStatusId() > 0) {
                searchClause = new SearchClause("boq.BOQ_STATUS", " = ", searchForm.getStatusId());
                clauseList.add(searchClause);
            }
            if (searchForm.getBoqName() != null && !searchForm.getBoqName().isEmpty()) {
                searchClause = new SearchClause("boq.BOQ_NAME", " like ", searchForm.getBoqName() + "%");
                clauseList.add(searchClause);
            }
            if (searchForm.getReferenceCode() != null && !searchForm.getReferenceCode().isEmpty()) {
                searchClause = new SearchClause("boq.REFERENCE_CODE", " like ", searchForm.getReferenceCode() + "%");
                clauseList.add(searchClause);
            }
            if (searchForm.getOrderNo() != null && !searchForm.getOrderNo().isEmpty()) {
                searchClause = new SearchClause("boq.ORDER_NO", " like ", searchForm.getOrderNo() + "%");
                clauseList.add(searchClause);
            }
        }
        if (clauseList.size() < 1) {
            return null;
        }
        return clauseList;
    }

}
