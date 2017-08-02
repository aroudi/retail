package au.com.biztune.retail.service;

import au.com.biztune.retail.domain.DeliveryNoteHeader;
import au.com.biztune.retail.domain.GeneralSearchForm;
import au.com.biztune.retail.response.CommonResponse;

import javax.ws.rs.core.SecurityContext;
import java.util.List;

/**
 * Created by arash on 30/05/2016.
 */
public interface DeliveryNoteService {
    /**
     * save DeliveryNote into database.
     * @param deliveryNoteHeader deliveryNoteHeader
     * @param securityContext securityContext
     * @return response
     */
    CommonResponse saveDeliveryNote(DeliveryNoteHeader deliveryNoteHeader, SecurityContext securityContext);
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
    /**
     * get all delivery note headers per supplier.
     * @param supplierId supplierId.
     * @return List of DeliveryNoteHeader
     */
    List<DeliveryNoteHeader> getAllSuppliersDeliveryNotes(long supplierId);

    /**
     * search purchase order header.
     * @param searchForm searchForm
     * @return List of PurchaseOrderHeader
     */
    List<DeliveryNoteHeader> searchDeliveryNote(GeneralSearchForm searchForm);

    /**
     * search delivery note paging.
     * @param searchForm searchForm
     * @return List of Delivery Note
     */
    GeneralSearchForm searchDeliveryNotePaging(GeneralSearchForm searchForm);

    }
