package au.com.biztune.retail.dao;

import au.com.biztune.retail.domain.ReportParam;
import au.com.biztune.retail.domain.ReportParamVal;
import au.com.biztune.retail.domain.ReportTreeViewNode;

import java.util.List;

/**
 * Created by arash on 7/05/2018.
 */
public interface ReportingDao {

    /**
     * getReportHeadingListPerRepGrpId.
     * @param reportGroupId reportGroupId
     * @return List of report heading for this report group.
     */
    List<ReportTreeViewNode> getReportHeadingListPerRepGrpId(long reportGroupId);

    /**
     * getReportParamListPerRepHeadId.
     * @param reportHeadingId reportHeadingId
     * @return List of report param for this report heading.
     */
    List<ReportParam> getReportParamListPerRepHeadId(long reportHeadingId);

    /**
     * getReportParamValListPerRepParamId.
     * @param reportParamVal reportParamVal
     * @return List of report param val for this report param.
     */
    List<ReportParamVal> getReportParamValListPerRepParamId(long reportParamVal);

    /**
     * get all report list.
     * @return report groups and heading in tree view format.
     */
    List<ReportTreeViewNode> getReportList();
}
