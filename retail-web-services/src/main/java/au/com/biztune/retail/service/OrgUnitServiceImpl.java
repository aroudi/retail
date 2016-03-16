package au.com.biztune.retail.service;

import au.com.biztune.retail.dao.OrgUnitDao;
import au.com.biztune.retail.domain.OrgUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by akhoshraft on 16/03/2016.
 */

@Component
public class OrgUnitServiceImpl implements OrgUnitService {
    private final Logger logger = LoggerFactory.getLogger(OrgUnitServiceImpl.class);

    @Autowired
    private OrgUnitDao orgUnitDao;

    /**
     * get organization unit by type and code.
     * @param type type
     * @param  code code
     * @return OrgUnit
     */
    public OrgUnit getOrgUnitByTypeAndCode(String type, String code) {
        try {
            return orgUnitDao.getOrgUnitByTypeAndCode(type, code);
        } catch (Exception e) {
            logger.error("Error in getting organization unit: ", e);
            return null;
        }
    }
}
