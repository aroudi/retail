package au.com.biztune.retail.dao;

import au.com.biztune.retail.domain.PoSoLink;

import java.util.List;

/**
 * Created by arash on 6/07/2017.
 */
public interface PoSoLinkDao {

    /**
     * get all po_so_link per poh Id.
     * @param pohId pohId
     * @return list of poSoLink
     */
    List<PoSoLink> getAllPoSoLinkPerPohId(long pohId);
    /**
     * get all po_so_link per pol Id.
     * @param polId polId
     * @return list of poSoLink
     */
    List<PoSoLink> getAllPoSoLinkPerPolId(long polId);
    /**
     * get all po_so_link per txnId.
     * @param txnId txnId
     * @return list of poSoLink
     */
    List<PoSoLink> getAllPoSoLinkPerTxnId(long txnId);
    /**
     * get all po_so_link per txnDetailId.
     * @param txnDetailId txnDetailId
     * @return list of poSoLink
     */
    List<PoSoLink> getPoSoLinkPerTxnDetailId(long txnDetailId);

    /**
     * insert po_so_link.
     * @param poSoLink purchase order - sale order - link
     */
    void insert(PoSoLink poSoLink);

    /**
     * update qty received.
     * @param poSoLink poSoLink
     */
    void updateQtyReceived(PoSoLink poSoLink);

    /**
     * delete link per txn-detail.
     * @param txnDetailId txnDetailId
     */
    void deletePoSoLinkPerTxnDetailId(long txnDetailId);

    /**
     * get project name by project id.
     * @param projectId projectId
     * @return Project Name
     */
    String getProjectNameByProjectId(long projectId);

    /**
     * get all project codes assigned to purchase order header creating from sale order.
     * @param pohId pohId
     * @return list of poSoLink
     */
    List<PoSoLink> getAllPoSoProjectCodesPerPohId(long pohId);

    /**
     * delete po_so_link per purchase order header id.
     * @param pohId pohId
     */
    void deletePoSoLinkPerPohId(long pohId);

    /**
     * change PohId, PohNumber for merged PolId.
     * @param pohId pohId
     * @param pohNumber pohNumber
     * @param polId polId
     */
    void changePohIdAndNumberPerPolId(long pohId, String pohNumber, long polId);
}
