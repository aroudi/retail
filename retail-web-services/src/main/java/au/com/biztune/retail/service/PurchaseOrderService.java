package au.com.biztune.retail.service;

import au.com.biztune.retail.domain.BoqDetail;
import au.com.biztune.retail.domain.PurchaseOrderHeader;
import au.com.biztune.retail.response.CommonResponse;

/**
 * Created by arash on 14/05/2016.
 */
public interface PurchaseOrderService {
    /**
     * create Purchase Order From Boq.
     * @param boqDetail boqDetail
     * @return PurchaseOrderHeader
     */
    PurchaseOrderHeader createPoFromBoq(BoqDetail boqDetail);
    /**
     * add lines to Purchase Order Header.
     * @param purchaseOrderHeader purchaseOrderHeader
     * @param boqDetail boqDetail
     */
    void addLineToPoFromBoqDetail(PurchaseOrderHeader purchaseOrderHeader, BoqDetail boqDetail);

    /**
     * save Purchase Order Header into database.
     * @param purchaseOrderHeader purchaseOrderHeader
     * @return response
     */
    CommonResponse savePurchaseOrder(PurchaseOrderHeader purchaseOrderHeader);
}
