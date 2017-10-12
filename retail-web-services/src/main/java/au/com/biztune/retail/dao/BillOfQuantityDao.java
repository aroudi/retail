package au.com.biztune.retail.dao;

import au.com.biztune.retail.domain.BillOfQuantity;
import au.com.biztune.retail.util.SearchClause;

import java.sql.Timestamp;
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

    /**
     * search Bill Of Quantities.
     * @param orguId orguId
     * @param fromNo fromNo
     * @param toNo toNo
     * @param searchClauses searchClauses
     * @return List of Bill Of Quantity
     */
    List<BillOfQuantity> searchBillOfQuantityPaging(long orguId, long fromNo, long toNo, List<SearchClause> searchClauses);

    /**
     * get query total rows.
     * @param orguId orguId
     * @param searchClauses searchClauses
     * @return query total records.
     */
    long getBillOfQuantityQueryTotalRows(long orguId, List<SearchClause> searchClauses);

    /**
     * update boq status per id.
     * @param statusId statusId
     * @param boqId boqId
     */
    void updateBoqStatusPerId(long statusId, long boqId);

    /**
     * delete boq detail per id.
     * @param boqIdList boqIdList
     */
    void deleteBoqPerId(List<Long> boqIdList);

    /**
     * delete boqdetail per boq id.
     * @param boqIdList boqIdList
     */
    void deleteBoqDetailPerBoqId(List<Long> boqIdList);

    /**
     * update date released per boqId.
     * @param dateReleased dateReleased
     * @param boqId boqId
     */
    void updateBoqDateReleasedPerId(Timestamp dateReleased, long boqId);

    /**
     * get Bill Of Quantity per Name and OrderNo
     * @param boqName boqName
     * @param orderNo orderNo
     * @return Bill Of Quantity
     */
    BillOfQuantity getBillOfQuantityByNameAndOrderNo(String boqName, String orderNo);
}
