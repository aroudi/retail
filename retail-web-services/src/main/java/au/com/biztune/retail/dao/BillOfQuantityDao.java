package au.com.biztune.retail.dao;

import au.com.biztune.retail.domain.BillOfQuantity;

/**
 * Created by arash on 1/04/2016.
 */
public interface BillOfQuantityDao {

    /**
     * get Bill of quantity by name.
     * @param name name
     * @return BillOfQuantity
     */
    BillOfQuantity getBillOfQuantityByName(String name);

    /**
     * get Bill of quantity by id.
     * @param id id
     * @return BillOfQuantity
     */
    BillOfQuantity getBillOfQuantityById(long id);

    /**
     * insert.
     * @param billOfQuantity billOfQuantity
     */
    void insert(BillOfQuantity billOfQuantity);

    /**
     * update Per Name.
     * @param name name.
     */
    void updatePerName(String name);

    /**
     * update Per id.
     * @param id id.
     */
    void updatePerId(long id);
}
