package au.com.biztune.retail.app;

import au.com.biztune.retail.dao.OrgUnitDao;
import au.com.biztune.retail.domain.OrgUnit;
import au.com.biztune.retail.domain.Store;
import au.com.biztune.retail.session.SessionState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * Created by arash on 11/03/2015.
 */
@Component
public class Loader implements ApplicationListener<ContextRefreshedEvent> {

    private final Logger logger = LoggerFactory.getLogger(Loader.class);

    @Autowired
    private SessionState sessionState;

    @Autowired
    private OrgUnitDao orgUnitDao;

    /**
     * set default version to active version on startup.
     * @param event event
     */
    public void onApplicationEvent(ContextRefreshedEvent event) {
        //TODO : READ ORGUNIT_TYPE AND CODE FROM CONFIG FILE
        final OrgUnit orgUnit = orgUnitDao.getOrgUnitByTypeAndCode("COMPANY", "JOMON");
        final Store store = orgUnitDao.getStoreByCode("JOMON_SYD");
        sessionState.setOrgUnit(orgUnit);
        sessionState.setStore(store);
    }
}
