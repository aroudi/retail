package au.com.biztune.retail.dao;

import au.com.biztune.retail.domain.DeliveryNoteHeader;
import au.com.biztune.retail.domain.DeliveryNoteLine;

import java.util.List;

/**
 * Created by arash on 27/05/2016.
 */
public interface DeliveryNoteDao {

    /**
     * insert deliveyNoteHeader.
     * @param deliveryNoteHeader deliveryNoteHeader
     */
    void insertDeliveryNoteHeader(DeliveryNoteHeader deliveryNoteHeader);

    /**
     * update DeliveryNoteHeader.
     * @param deliveryNoteHeader deliveryNoteHeader
     */
    void updateDeliveryNoteHeader(DeliveryNoteHeader deliveryNoteHeader);

    /**
     * insert DeliveryNoteLine.
     * @param deliveryNoteLine deliveryNoteLine
     */
    void insertDeliveryNoteLine(DeliveryNoteLine deliveryNoteLine);

    /**
     * update DeliveryNoteLine.
     * @param deliveryNoteLine deliveryNoteLine
     */
    void updateDeliveryNoteLine(DeliveryNoteLine deliveryNoteLine);

    /**
     * update delivery note grn number per id.
     * @param deliveryNoteHeader deliveryNoteHeader
     */
    void updateDeliveryNoteGrnNo(DeliveryNoteHeader deliveryNoteHeader);
    /**
     * get all lines of delivery note by id.
     * @param delnId delnId
     * @return List of DeliveryNote Lines
     */
    List<DeliveryNoteLine> getDelNoteLinesPerDlnId(long delnId);

    /**
     * get all delivery note headers pe orguId.
     * @param orguId orguId
     * @return List of DeliveryNoteHeaders
     */
    List<DeliveryNoteHeader> getAllDelNoteHeaderPerOrguId(long orguId);

    /**
     * get delivery note per note number.
     * @param deliveryNoteNo deliveryNoteNo
     * @return DeliveryNoteHeader
     */
    DeliveryNoteHeader getDelNoteHeaderPerNoteNumber(String deliveryNoteNo);

    /**
     * get deliveryNoteHeader whole object by id.
     * @param delnId delnId
     * @return DeliveryNoteHeader
     */
    DeliveryNoteHeader getDelNoteWholePerDelnId(long delnId);

    /**
     * delete all deliverynote lines where line id not in the list, per deliverynote id.
     * @param deliveryNoteId deliveryNoteId
     * @param deliveryNoteLines deliveryNoteLines
     */
    void deleteDeliveryNoteLineWhereIdNotIn(long deliveryNoteId, List deliveryNoteLines);
}
