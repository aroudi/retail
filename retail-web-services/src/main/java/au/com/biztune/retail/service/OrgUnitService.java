package au.com.biztune.retail.service;

import au.com.biztune.retail.domain.OrgUnit;

/**
 * Created by akhoshraft on 16/03/2016.
 */
public interface OrgUnitService {

    /**
     * get organization unit by type and code.
     * @param type type
     * @param  code code
     * @return OrgUnit
     */
    OrgUnit getOrgUnitByTypeAndCode(String type, String code);
}
