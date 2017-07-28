package au.com.biztune.retail.dao;

import au.com.biztune.retail.domain.BoqDetail;

import java.util.List;

/**
 * Created by arash on 1/04/2016.
 */
public interface BoqDetailDao {

    /**
     * insert BOQDetail.
     * @param boqDetail boqDetail
     */
    void insert(BoqDetail boqDetail);

    /**
     * update BOQDetail.
     * @param boqDetail boqDetail
     */
    void updatePerId(BoqDetail boqDetail);

    /**
     * update boqDetail Stock info.
     * @param boqDetail boqDetail
     */
    void updateStockQty(BoqDetail boqDetail);


    /**
     * update boqDetail info.
     * @param boqDetail boqDetail
     */
    void updateBoqLine(BoqDetail boqDetail);
    /**
     * delete BOQDetail per boqId.
     * @param boqId boqId
     */
    void deleteBoqDetailPerBoqId(long boqId);

    /**
     * get list of BoqDetail by BoqId.
     * @param id id
     * @return List of BoqDetail
     */
    List<BoqDetail> getBoqDetailByBoqId(long id);

    /**
     * get boq detail light records with boq id.
     * @param boqId boqId
     * @return list of boq detail.
     */
    List<BoqDetail> getBoqDetailLightByBoqId(long boqId);
    /**
     * get all BOQ Details for multiple boq ids.
     * @param boqIds list of BOQ id.
     * @return List of BoqDetail
     */
    List<BoqDetail> getBoqDetailForMultipleBoqId(List boqIds);

    /**
     * get BOQDetail by id.
     * @param id id
     * @return BOQDetail
     */
    BoqDetail getBoqDetailById(long id);

    /**
     * update received, balance and status.
     * @param boqDetail boqDetail
     */
    void updateQtyReceived(BoqDetail boqDetail);

    /**
     * delete BoqDetail per lineid.
     * @param boqLineId boqLineId
     */
    void deleteBoqDetailPerId(long boqLineId);

    /**
     * update boq detail qty and stauts.
     * @param qtyReceived qtyReceived
     * @param qtyBalance qtyBalance
     * @param statusId statusId
     * @param boqDetailId boqDetailId
     */
    void updateQtyAndStatus(double qtyReceived, double qtyBalance, long statusId, long boqDetailId);
}
