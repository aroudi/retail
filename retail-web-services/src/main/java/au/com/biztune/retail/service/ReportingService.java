package au.com.biztune.retail.service;

import au.com.biztune.retail.domain.ReportTreeViewNode;

import java.util.List;

/**
 * Created by arash on 7/05/2018.
 */
public interface ReportingService {

    /**
     * get report list.
     * @return list of reports.
     */
    List<ReportTreeViewNode> getReportList();

}
