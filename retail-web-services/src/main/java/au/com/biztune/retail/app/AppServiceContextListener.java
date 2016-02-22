// RailCorp 2014

package au.com.biztune.retail.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * AppServiceContextListener: intialises the servlet and loads the rtta xml files.
 * @author Huub
 */
public class AppServiceContextListener implements ServletContextListener {

    private static final Logger log = LoggerFactory.getLogger(AppServiceContextListener.class);

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
        log.info("contextInitialized");
    }

}
