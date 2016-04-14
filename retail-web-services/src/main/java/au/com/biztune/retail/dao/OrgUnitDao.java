package au.com.biztune.retail.dao;

import au.com.biztune.retail.domain.OrgUnit;
import au.com.biztune.retail.domain.Store;

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
    /**
     * get the supplier by id.
     * @param orguId orguId
     * @return OrgUnit
     */
    OrgUnit getOrgUnitById(long orguId);

    /**
     * get store by code.
     * @param code code
     * @return Store
     */
    Store getStoreByCode(String code);

    /**
     * get store by id.
     * @param id id
     * @return Store
     */
    Store getStoreById(long id);
}
