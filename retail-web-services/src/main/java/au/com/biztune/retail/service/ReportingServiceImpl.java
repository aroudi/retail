package au.com.biztune.retail.service;

import au.com.biztune.retail.dao.ReportingDao;
import au.com.biztune.retail.domain.ReportTreeViewNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by arash on 7/05/2018.
 */

@Component
public class ReportingServiceImpl implements ReportingService {

    private final Logger logger = LoggerFactory.getLogger(ReportingServiceImpl.class);
    @Autowired
    private ReportingDao reportingDao;

    /**
     * get report list.
     * @return list of reports.
     */
    public List<ReportTreeViewNode> getReportList() {
        try {
            return reportingDao.getReportList();
        } catch (Exception e) {
            logger.error("Exception in getting report error.", e);
            return null;
        }
    }
}
