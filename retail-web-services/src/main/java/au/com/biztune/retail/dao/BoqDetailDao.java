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
}
