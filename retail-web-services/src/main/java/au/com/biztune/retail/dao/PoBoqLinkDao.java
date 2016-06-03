package au.com.biztune.retail.dao;

import au.com.biztune.retail.domain.PoBoqLink;

import java.util.List;

/**
 * Created by arash on 13/05/2016.
 */
public interface PoBoqLinkDao {
    /**
     * getAllPoBoqLinkPerPohId.
     * @param pohId pohId
     * @return List of PoBoqLink
     */
    List<PoBoqLink> getAllPoBoqLinkPerPohId(long pohId);
    /**
     * getAllPoBoqLinkPerPolId.
     * @param polId polId
     * @return List of PoBoqLink
     */
    List<PoBoqLink> getAllPoBoqLinkPerPolId(long polId);
    /**
     * getAllPoBoqLinkPerBoqId.
     * @param boqId boqId
     * @return List of PoBoqLink
     */
    List<PoBoqLink> getAllPoBoqLinkPerBoqId(long boqId);


    /**
     * getPoBoqLinkPerBoqDetailId.
     * @param boqDetailId boqDetailId
     * @return PoBoqLink
     */
    PoBoqLink getPoBoqLinkPerBoqDetailId(long boqDetailId);

    /**
     * insert PurchaseOrder BillOfQuantity link to database.
     * @param poBoqLink poBoqLink
     */
    void insert(PoBoqLink poBoqLink);
}