package au.com.biztune.retail.service;

import au.com.biztune.retail.domain.DeliveryNoteHeader;
import au.com.biztune.retail.response.CommonResponse;

import java.util.List;

/**
 * Created by arash on 30/05/2016.
 */
public interface DeliveryNoteService {
    /**
     * save DeliveryNote into database.
     * @param deliveryNoteHeader deliveryNoteHeader
     * @return response
     */
    CommonResponse saveDeliveryNote(DeliveryNoteHeader deliveryNoteHeader);
    /**
     * get all delivery note headers.
     * @return List of DeliveryNoteHeader
     */
   List<DeliveryNoteHeader> getAllDeliveryNoteHeaders();

    /**
     * get DeliveryNote whole oblect per delnId.
     * @param delnId delnId.
     * @return DeliveryNoteHeader
     */
    DeliveryNoteHeader getDeliveryNoteWhole(long delnId);
}
