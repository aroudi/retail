package au.com.biztune.retail.dao;

import au.com.biztune.retail.domain.BillOfQuantity;

import java.util.List;

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
     * get all bill of quantities.
     * @return list of BOQ
     */
    List<BillOfQuantity> getAllBillOfQuantities();
    /**
     * insert.
     * @param billOfQuantity billOfQuantity
     */
    void insert(BillOfQuantity billOfQuantity);

    /**
     * update Per Name.
     * @param billOfQuantity billOfQuantity.
     */
    void updatePerName(BillOfQuantity billOfQuantity);

    /**
     * update Per id.
     * @param billOfQuantity billOfQuantity.
     */
    void updatePerId(BillOfQuantity billOfQuantity);

    /**
     * update boqStatus per id.
     * @param billOfQuantity billOfQuantity
     */
    void updateStatusPerId(BillOfQuantity billOfQuantity);

    /**
     * get list of client's bill of quantity.
     * @param clientId clientId.
     * @return bill of quantity list.
     */
    List<BillOfQuantity> getBillOfQuantitiesByClientId(long clientId);
}
