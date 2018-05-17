package au.com.biztune.retail.service;

import au.com.biztune.retail.dao.ReportingDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Arash Roudi on 16/05/2018.
 * Service class for reporting.
 */
@Component
public class ReportsServiceImpl {
    private final Logger logger = LoggerFactory.getLogger(ReportsServiceImpl.class);
    @Autowired
    private ReportingDao reportingDao;
}
