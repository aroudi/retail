// RailCorp 2014

package au.com.biztune.retail.app;

import au.com.biztune.retail.util.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.File;

/**
 * AppServiceContextListener: intialises the servlet and loads the rtta xml files.
 * @author Huub
 */
public class AppServiceContextListener implements ServletContextListener {

    private static final Logger log = LoggerFactory.getLogger(AppServiceContextListener.class);

    /*
    @Autowired
    private SessionState sessionState;

    @Autowired
    private OrgUnitDao orgUnitDao;
    */

    /**
     * {@inheritDoc}
     * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
     */
    @Override
    public void contextDestroyed(ServletContextEvent arg0) {

    }

    /**
     * {@inheritDoc}
     * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
     */
    @Override
    public void contextInitialized(ServletContextEvent arg0) {
        //arg0.getServletContext().getRealPath(File.separator);
        log.info("Servlent Context= " + arg0.getServletContext().getRealPath(File.separator));
        Environment.refresh();
        Environment.setWebAppContextDir(arg0.getServletContext().getRealPath(File.separator));

        log.info("contextInitialized");
        //TODO : READ ORGUNIT_TYPE AND CODE FROM CONFIG FILE
        //final OrgUnit orgUnit = orgUnitDao.getOrgUnitByTypeAndCode("COMPANY", "JOMON");
        //sessionState.setOrgUnit(orgUnit);

    }

}
