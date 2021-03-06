package au.com.biztune.retail.util;

import au.com.biztune.retail.domain.GeneralSearchForm;
import au.com.biztune.retail.domain.ReportParam;
import au.com.biztune.retail.domain.ReportParamVal;
import au.com.biztune.retail.form.BoqSearchForm;
import au.com.biztune.retail.form.ProductSearchForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by arash on 20/10/2016.
 */
public class SearchClauseBuilder {
    /**
     * build search clause.
     *
     * @param searchForm searchForm
     * @param searchTable searchTable
     * @return
     */
    final static Logger logger = LoggerFactory.getLogger(SearchClauseBuilder.class);

    public static List<SearchClause> buildSearchWhereCluase(GeneralSearchForm searchForm, String searchTable) {
        List<SearchClause> clauseList = null;
        SearchClause searchClause = null;
        if (searchForm != null) {
            clauseList = new ArrayList<SearchClause>();
            Timestamp dateFrom = null;
            Timestamp dateTo = null;
            Timestamp expDateTo = null;
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
            expDateTo = DateUtil.stringToDate(searchForm.getExpDateTo(), "yyyy-MM-dd'T'HH:mm:ss.SSSX");
            if (expDateTo != null) {
                //maximise the hour for dateTo
                final Calendar cal = Calendar.getInstance();
                cal.setTime(expDateTo);
                cal.set(Calendar.HOUR, 24);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                expDateTo = new Timestamp(cal.getTime().getTime());
                if ("PURCHASE_ORDER_HEADER".equals(searchTable)) {
                    searchClause = new SearchClause("POH_EXP_DELIVERY", " <= ", expDateTo);
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
            if ((searchForm.getProjectCode() != null) && (!searchForm.getProjectCode().isEmpty())) {
                if ("PURCHASE_ORDER_HEADER".equals(searchTable)) {
                    searchClause = new SearchClause("POH_PRJ_CODE", " like ", "%" + searchForm.getProjectCode() + "%");
                    clauseList.add(searchClause);
                }
                if ("TXN_HEADER".equals(searchTable)) {
                    searchClause = new SearchClause("TXHD_PRJ_CODE", " like ", "%" + searchForm.getProjectCode() + "%");
                    clauseList.add(searchClause);
                }
                if ("INVOICE".equals(searchTable)) {
                    searchClause = new SearchClause("TXIV_PRJ_CODE", " like ", "%" + searchForm.getProjectCode() + "%");
                    clauseList.add(searchClause);
                }
                if ("DELIVERY_NOTE_HEADER".equals(searchTable)) {
                    searchClause = new SearchClause("DELN_PRJ_CODE", " like ", "%" + searchForm.getProjectCode() + "%");
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
                if (searchForm.getStatusId() > 0) {
                    searchClause = new SearchClause("POH_STATUS", " = ", searchForm.getStatusId());
                    clauseList.add(searchClause);
                }
                if (searchForm.getCreationTypeId() > 0) {
                    searchClause = new SearchClause("POH_CREATION_TYPE", " = ", searchForm.getCreationTypeId());
                    clauseList.add(searchClause);
                }
            }
            if ("INVOICE".equals(searchTable)) {
                if (searchForm.isImported()) {
                    searchClause = new SearchClause("TXIV_IMPORTED", " = ", 1);
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
                searchClause = new SearchClause("SUPP_ORGU_LINK.SUPP_ID", " = ", searchForm.getSupplierId());
                clauseList.add(searchClause);
            }
            if (searchForm.getProdSku() != null && !searchForm.getProdSku().isEmpty()) {
                searchClause = new SearchClause("PRODUCT.PROD_SKU", " like ", "%" + searchForm.getProdSku() + "%");
                clauseList.add(searchClause);
            }
            if (searchForm.getReference() != null && !searchForm.getReference().isEmpty()) {
                searchClause = new SearchClause("PRODUCT.REFERENCE", " like ", "%" + searchForm.getReference() + "%");
                clauseList.add(searchClause);
            }
            if (searchForm.getProdName() != null && !searchForm.getProdName().isEmpty()) {
                searchClause = new SearchClause("PRODUCT.PROD_NAME", " like ", "%" + searchForm.getProdName() + "%");
                clauseList.add(searchClause);
            }
            if (searchForm.getProdTypeId() > 0) {
                searchClause = new SearchClause("PRODUCT.CAT_ID_TYPE", " = ", searchForm.getProdTypeId());
                clauseList.add(searchClause);
            }
            if (searchForm.getProdStatusId() > 0) {
                searchClause = new SearchClause("PROD_ORGU_LINK.CAT_ID_STATUS", " = ", searchForm.getProdStatusId());
                clauseList.add(searchClause);
            }
            if (searchForm.getInStockQtyFrom() > 0) {
                searchClause = new SearchClause("STOCK.STCK_QTY", " >= ", searchForm.getInStockQtyFrom());
                clauseList.add(searchClause);
            }
            if (searchForm.getInStockQtyTo() > 0) {
                searchClause = new SearchClause("STOCK.STCK_QTY", " <= ", searchForm.getInStockQtyTo());
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

    /**
     * build report search clause.
     *
     * @param reportParamList reportParamList
     * @return
     */
    public static List<SearchClause> buildReportingSearchWhereCluase(List<ReportParam> reportParamList) {
        if (reportParamList == null || reportParamList.size() < 1) {
            return null;
        }
        List<SearchClause> clauseList = new ArrayList<SearchClause>();
        SearchClause searchClause = null;
        ReportParamVal reportParamVal = null;
        Timestamp dateFrom = null;
        Timestamp dateTo = null;
        for (ReportParam reportParam : reportParamList) {
            switch (reportParam.getRepParamName()) {
                case IdBConstant.REPORTS_PARAM_DATE:
                    reportParamVal = getReportParamValByKey(reportParam.getReportParamValList(), IdBConstant.REPORTS_PARAM_VAL_DATE_FROM);
                    if (reportParamVal != null) {
                        dateFrom = DateUtil.stringToDate(reportParamVal.getRepParamVal(), "yyyy-MM-dd'T'HH:mm:ss.SSSX");
                        if (dateFrom != null) {
                            searchClause = new SearchClause(reportParamVal.getTableAlias(), " >= ",
                                    dateFrom, IdBConstant.REPORTS_PARAM_VAL_DATE_FROM);
                            clauseList.add(searchClause);
                        }
                    }
                    reportParamVal = getReportParamValByKey(reportParam.getReportParamValList(), IdBConstant.REPORTS_PARAM_VAL_DATE_TO);
                    if (reportParamVal != null) {
                        dateTo = DateUtil.stringToDate(reportParamVal.getRepParamVal(), "yyyy-MM-dd'T'HH:mm:ss.SSSX");
                        if (dateTo != null) {
                            //maximise the hour for dateTo
                            final Calendar cal = Calendar.getInstance();
                            cal.setTime(dateTo);
                            cal.set(Calendar.HOUR, 24);
                            cal.set(Calendar.MINUTE, 0);
                            cal.set(Calendar.SECOND, 0);
                            dateTo = new Timestamp(cal.getTime().getTime());
                            searchClause = new SearchClause(reportParamVal.getTableAlias(), " <= ", dateTo, IdBConstant.REPORTS_PARAM_VAL_DATE_TO);
                            clauseList.add(searchClause);
                        }
                    }
                    break;
                case (IdBConstant.REPORTS_PARAM_RANGE):
                    reportParamVal = getReportParamValByKey(reportParam.getReportParamValList(), IdBConstant.REPORTS_PARAM_VAL_RANGE_FROM);
                    if (reportParamVal != null) {
                        searchClause = new SearchClause(reportParamVal.getTableAlias(), " >= ", reportParamVal.getRepParamVal(), IdBConstant.REPORTS_PARAM_VAL_RANGE_FROM);
                        clauseList.add(searchClause);
                    }
                    reportParamVal = getReportParamValByKey(reportParam.getReportParamValList(), IdBConstant.REPORTS_PARAM_VAL_RANGE_TO);
                    if (reportParamVal != null) {
                        searchClause = new SearchClause(reportParamVal.getTableAlias(), " <= ", reportParamVal.getRepParamVal(), IdBConstant.REPORTS_PARAM_VAL_RANGE_TO);
                        clauseList.add(searchClause);
                    }
                    break;
                case (IdBConstant.REPORTS_PARAM_SUPPLIER):
                    if (!checkParamValListIsEmpty(reportParam.getReportParamValList())) {
                        final String commaSeparatedList = convertListToCommaSeparatedString(reportParam.getReportParamValList());
                        logger.debug("commaSeparatedList = " + commaSeparatedList);
                        searchClause = new SearchClause(reportParam.getTableAlias(), " in ", commaSeparatedList);
                        clauseList.add(searchClause);
                    }
                    break;
                case (IdBConstant.REPORTS_PARAM_CUSTOMER):
                    if (!checkParamValListIsEmpty(reportParam.getReportParamValList())) {
                        searchClause = new SearchClause(reportParam.getTableAlias(), " in ", convertListToCommaSeparatedString(reportParam.getReportParamValList()));
                        clauseList.add(searchClause);
                    }
                    break;
                case (IdBConstant.REPORTS_PARAM_STAFF):
                    if (!checkParamValListIsEmpty(reportParam.getReportParamValList())) {
                        searchClause = new SearchClause(reportParam.getTableAlias(), " in ", convertListToCommaSeparatedString(reportParam.getReportParamValList()));
                        clauseList.add(searchClause);
                    }
                    break;
                case (IdBConstant.REPORTS_PARAM_CATEGORY):
                    if (!checkParamValListIsEmpty(reportParam.getReportParamValList())) {
                        final String commaSeparatedList = convertListToCommaSeparatedString(reportParam.getReportParamValList());
                        searchClause = new SearchClause(reportParam.getTableAlias(), " in ", commaSeparatedList);
                        clauseList.add(searchClause);
                    }
                    break;
            }
        }
        return clauseList.size() > 0 ? clauseList : null;
    }

    /**
     * get report group by/sort by list.
     * @param reportParamList reportParamList
     * @param paramKey reportParamList
     * @return list of group by list.
     */
    public static List<SearchClause> buildReportingSearchAdditionalClauseList(List<ReportParam> reportParamList, String paramKey) {
        if (reportParamList == null || reportParamList.size() < 1) {
            return null;
        }
        final List<SearchClause> searchClauseList = new ArrayList<SearchClause>();

        for (ReportParam reportParam : reportParamList) {
            if (reportParam.getRepParamName().equals(paramKey)) {
                    for (ReportParamVal reportParamVal : reportParam.getReportParamValList()) {
                        if (reportParamVal.getRepParamVal().equals("None")) {
                            continue;
                        }
                        searchClauseList.add(new SearchClause(null, null, reportParamVal.getTableAlias(), reportParam.getRepParamName(), reportParamVal.getRepParamVal()));
                    }
            }
        }
        return searchClauseList.size() < 1 ? null : searchClauseList;
    }
    private static boolean checkParamValListIsEmpty(List<ReportParamVal> paramValList) {
        try {
            if (paramValList == null || paramValList.size() < 1) {
                return true;
            }
            for (ReportParamVal reportParamVal : paramValList) {
                if (!reportParamVal.getRepParamVal().isEmpty()
                        && reportParamVal.getRepParamVal() != null
                        && !reportParamVal.getRepParamVal().trim().isEmpty())
                {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            logger.error("Exception in converting list to comma separated string", e);
            return true;
        }

    }
    private static String convertListToCommaSeparatedString (List<ReportParamVal> paramValList) {
        try {
            if (paramValList == null || paramValList.size() < 1) {
                return null;
            }
            StringBuilder result = new StringBuilder("( ");
            for (ReportParamVal reportParamVal : paramValList) {
                if (!reportParamVal.getRepParamVal().isEmpty()
                        && reportParamVal.getRepParamVal() != null
                        && !reportParamVal.getRepParamVal().trim().isEmpty())
                {
                    result.append(reportParamVal.getRepParamVal());
                    result.append(", ");
                }
            }
            result.append("-1");
            result.append(" )");
            return result.toString();
        } catch (Exception e) {
            logger.error("Exception in converting list to comma separated string", e);
            return null;
        }
    }
    private static ReportParamVal getReportParamValByKey(List<ReportParamVal> reportParamValList, String key) {
        if (reportParamValList == null) {
            return null;
        }
        for (ReportParamVal reportParamVal : reportParamValList) {
            if (reportParamVal.getRepParamKey().equals(key)) {
                return reportParamVal;
            }
        }
        return null;
    }
}
