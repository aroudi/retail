package au.com.biztune.retail.dao;

import au.com.biztune.retail.domain.PoBoqLink;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by arash on 13/05/2016.
 */
@Mapper
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


    /**
     * update PoBoqLink.
     * @param poBoqLink poBoqLink
     */
    void updateQtyReceived(PoBoqLink poBoqLink);

    /**
     * delete poBoqLink per boqDetailId.
     * @param boqDetailId boqDetailId
     */
    void deletePoBoqLinkPerBoqDetailId(long boqDetailId);

    /**
     * get project name by project id.
     * @param projectId projectId
     * @return Project Name
     */
    String getProjectNameByProjectId(long projectId);

    /**
     * get all Project Code assigned to purchase order.
     * @param pohId pohId
     * @return List of PoBoqLink contains project codes.
     */
    List<PoBoqLink> getAllPoProjectCodesPerPohId(long pohId);

    /**
     * delete all po_boq link per poh id.
     * @param pohId pohId
     */
    void deletePoBoqLinkPerPohId(long pohId);

    /**
     * change PohId, PohNumber for merged PolId.
     * @param pohId pohId
     * @param pohNumber pohNumber
     * @param polId polId
     */
    void changePohIdAndNumberPerPolId(long pohId, String pohNumber, long polId);
}
