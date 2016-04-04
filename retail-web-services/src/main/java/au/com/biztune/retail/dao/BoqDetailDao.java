package au.com.biztune.retail.dao;

import au.com.biztune.retail.domain.BoqDetail;

/**
 * Created by arash on 1/04/2016.
 */
public interface BoqDetailDao {
    /**
     * get BoqDetail per id.
     * @param id id
     * @return BoqDetail
     */
    BoqDetail getBoqDetailById(long id);

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
     * delete BOQDetail per boqId.
     * @param boqId boqId
     */
    void deleteBoqDetailPerBoqId(long boqId);
}
