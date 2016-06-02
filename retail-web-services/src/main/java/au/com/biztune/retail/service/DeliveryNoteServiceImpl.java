package au.com.biztune.retail.service;

import au.com.biztune.retail.dao.*;
import au.com.biztune.retail.domain.*;
import au.com.biztune.retail.response.CommonResponse;
import au.com.biztune.retail.session.SessionState;
import au.com.biztune.retail.util.DateUtil;
import au.com.biztune.retail.util.IdBConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by arash on 14/05/2016.
 */

@Component
public class DeliveryNoteServiceImpl implements DeliveryNoteService {

    @Autowired
    private SessionState sessionState;
    private final Logger logger = LoggerFactory.getLogger(DeliveryNoteServiceImpl.class);
    @Autowired
    private ConfigCategoryDao configCategoryDao;

    @Autowired
    private DeliveryNoteDao deliveryNoteDao;
    @Autowired
    private PurchaseOrderDao purchaseOrderDao;
    @Autowired
    private PoDelNoteLinkDao poDelNoteLinkDao;
    /**
     * save DeliveryNote into database.
     * @param deliveryNoteHeader deliveryNoteHeader
     * @return response
     */
    public CommonResponse saveDeliveryNote(DeliveryNoteHeader deliveryNoteHeader) {
        final CommonResponse response = new CommonResponse();
        try {
            response.setStatus(IdBConstant.RESULT_SUCCESS);
            if (deliveryNoteHeader == null) {
                response.setStatus(IdBConstant.RESULT_FAILURE);
                response.setMessage("deliveryNote object is null");
                return response;
            }
            final boolean isNew = deliveryNoteHeader.getId() > 0 ? false : true;
            final Timestamp currentDate = new Timestamp(new Date().getTime());
            deliveryNoteHeader.setOrguId(sessionState.getOrgUnit().getId());
            String grnNumber = deliveryNoteHeader.getDelnGrn();
            deliveryNoteHeader.setDelnLastModifiedDate(currentDate);
            deliveryNoteHeader.setDelnDeliveryDate(DateUtil.stringToDate(deliveryNoteHeader.getDeliveryDate(), "yyyy-MM-dd"));
            if (isNew) {
                //final ConfigCategory status = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_DLV_NOTE_STATUS, IdBConstant.DLV_NOTE_STATUS_IN_PROGRESS);
                //deliveryNoteHeader.setDelnStatus(status);
                deliveryNoteHeader.setDelnCreatedDate(currentDate);
                deliveryNoteDao.insertDeliveryNoteHeader(deliveryNoteHeader);
                //generate purchase order number
                grnNumber = generateGrnNumber(deliveryNoteHeader.getId());
                deliveryNoteHeader.setDelnGrn(grnNumber);
                deliveryNoteDao.updateDeliveryNoteGrnNo(deliveryNoteHeader);

            } else {
                deliveryNoteDao.updateDeliveryNoteHeader(deliveryNoteHeader);
            }
            final List<Long> updatedLines = new ArrayList<Long>();
            for (DeliveryNoteLine deliveryNoteLine : deliveryNoteHeader.getLines()) {
                //if line id is less than 0; the line is new then insert it
                if (deliveryNoteLine.getId() < 0) {
                    deliveryNoteLine.setDelnGrn(grnNumber);
                    deliveryNoteLine.setDelnId(deliveryNoteHeader.getId());
                    deliveryNoteDao.insertDeliveryNoteLine(deliveryNoteLine);
                } else {
                    deliveryNoteDao.updateDeliveryNoteLine(deliveryNoteLine);
                }
                updatedLines.add(deliveryNoteLine.getId());
            }
           //delete from db, removed lines.
            deliveryNoteDao.deleteDeliveryNoteLineWhereIdNotIn(deliveryNoteHeader.getId(), updatedLines);
            //update the linked purchase order and its items.
            updateLinkedPurchaseOrder(deliveryNoteHeader);
            //include the GRN number in the response
            response.setInfo(deliveryNoteHeader.getDelnGrn());
            return response;
        } catch (Exception e) {
            logger.error("Exception in saving DeliveryNote:", e);
            response.setStatus(IdBConstant.RESULT_FAILURE);
            response.setMessage("Exception in saving DeliveryNote");
            return response;
        }
    }
    /**
     * generate GRN Number.
     * @param delnId delnId
     * @return PohNumber
     */
    public String generateGrnNumber(long delnId) {
        final Timestamp currentDate = new Timestamp(new Date().getTime());
        return IdBConstant.GRN_PREFIX + DateUtil.dateToString(currentDate, "yyyy-MM-dd") + "-" + delnId;
    }

    /**
     * get all delivery note headers.
     * @return List of DeliveryNoteHeader
     */
    public List<DeliveryNoteHeader> getAllDeliveryNoteHeaders() {
        try {
            return deliveryNoteDao.getAllDelNoteHeaderPerOrguId(sessionState.getOrgUnit().getId());
        } catch (Exception e) {
            logger.error("Exception in getting delivery note header list:", e);
            return null;
        }
    }

    /**
     * get DeliveryNote whole oblect per delnId.
     * @param delnId delnId.
     * @return DeliveryNoteHeader
     */
    public DeliveryNoteHeader getDeliveryNoteWhole(long delnId) {
        try {
            final DeliveryNoteHeader deliveryNoteHeader = deliveryNoteDao.getDelNoteWholePerDelnId(delnId);
            deliveryNoteHeader.setDeliveryDate(DateUtil.dateToString(deliveryNoteHeader.getDelnDeliveryDate(), "yyyy-MM-dd"));
            return deliveryNoteHeader;
        } catch (Exception e) {
            logger.error("Exception in getting delivery note whole per delnId:", e);
            return null;
        }
    }

    /**
     * pudate linked purchase order.
     * @param deliveryNoteHeader deliveryNoteHeader
     * @return true is ok; otherwise false
     */
    public boolean updateLinkedPurchaseOrder(DeliveryNoteHeader deliveryNoteHeader) {
        try {
            if (!deliveryNoteHeader.getDelnStatus().getCategoryCode().equals(IdBConstant.DLV_NOTE_STATUS_COMPLETE)) {
                logger.debug("DeliveryNote is note in complete state. so we don't update the purchase order");
                return true;
            }
                //create a link to purchase order header
            final PoDelNoteLink poDelNoteLink = new PoDelNoteLink();
            poDelNoteLink.setDelnId(deliveryNoteHeader.getId());
            poDelNoteLink.setDelnDeliveryDate(deliveryNoteHeader.getDelnDeliveryDate());
            poDelNoteLink.setDelnGrn(deliveryNoteHeader.getDelnGrn());
            poDelNoteLink.setDelnNoteNumber(deliveryNoteHeader.getDelnNoteNumber());
            poDelNoteLink.setPohId(deliveryNoteHeader.getPohId());
            poDelNoteLink.setPohOrderNumber(deliveryNoteHeader.getPohOrderNumber());
            poDelNoteLinkDao.insert(poDelNoteLink);

            //update qty received and status on purchase order header if delivery note is confirmed
            PurchaseLine purchaseLine = null;
            double qtyReceived = 0;
            ConfigCategory polStatus = null;
            for (DeliveryNoteLine deliveryNoteLine : deliveryNoteHeader.getLines()) {
                qtyReceived = 0;
                if (deliveryNoteLine == null) {
                    continue;
                }
                purchaseLine = purchaseOrderDao.getPurchaseLinePerId(deliveryNoteLine.getPolId());
                if (purchaseLine.getPolQtyReceived() >= 0) {
                    qtyReceived = purchaseLine.getPolQtyReceived() + deliveryNoteLine.getRcvdQty();
                } else {
                    qtyReceived = deliveryNoteLine.getRcvdQty();
                }
                purchaseLine.setPolQtyReceived(qtyReceived);
                purchaseLine.setPolValueReceived(purchaseLine.getPolUnitCost() * qtyReceived);
                purchaseLine.setPolDateReceived(deliveryNoteHeader.getDelnDeliveryDate());
                if (qtyReceived >= purchaseLine.getPolQtyOrdered()) {
                    polStatus = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_POH_STATUS, IdBConstant.POH_STATUS_GOOD_RECEIVED);
                } else {
                    polStatus = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_POH_STATUS, IdBConstant.POH_STATUS_PARTIAL_REC);
                }
                purchaseLine.setPolStatus(polStatus);
                purchaseOrderDao.updatePurchaseLine(purchaseLine);
            }
            //change the status of purchase order
            //retreive purchase order whole object from db.
            final PurchaseOrderHeader linkedPurchaseOrder = purchaseOrderDao.getPurchaseOrderWholePerPohId(deliveryNoteHeader.getPohId());
            boolean fullyReceived = true;
            for (PurchaseLine line : linkedPurchaseOrder.getLines()) {
                if (line.getPolStatus() == null || line.getPolStatus() != null && !line.getPolStatus().getCategoryCode().equals(IdBConstant.POH_STATUS_GOOD_RECEIVED)) {
                    fullyReceived = false;
                    break;
                }
            }
            final ConfigCategory pohStatus;
            if (fullyReceived) {
                pohStatus = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_POH_STATUS, IdBConstant.POH_STATUS_GOOD_RECEIVED);
            } else {
                pohStatus = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_POH_STATUS, IdBConstant.POH_STATUS_PARTIAL_REC);
            }
            if (pohStatus != null) {
                linkedPurchaseOrder.setPohStatus(pohStatus);
                linkedPurchaseOrder.setPohLastModifiedDate(deliveryNoteHeader.getDelnLastModifiedDate());
                linkedPurchaseOrder.setPohDelvDate(deliveryNoteHeader.getDelnDeliveryDate());
                linkedPurchaseOrder.setPohLastDelvDate(deliveryNoteHeader.getDelnDeliveryDate());
                purchaseOrderDao.updatePurchaseOrderHeader(linkedPurchaseOrder);
            }
            return true;

        } catch (Exception e) {
            logger.error("Exception in updating related purchase order", e);
            return false;
        }
    }
}
