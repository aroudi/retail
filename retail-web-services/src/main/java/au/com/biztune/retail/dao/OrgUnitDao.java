package au.com.biztune.retail.dao;

import au.com.biztune.retail.domain.OrgUnit;

/**
 * Created by akhoshraft on 29/02/2016.
 */
public interface OrgUnitDao {

    /**
     * get the supplier by id.
     * @param type type
     * @param code code
     * @return OrgUnit
     */
    OrgUnit getOrgUnitByTypeAndCode(String type, String code);
}
