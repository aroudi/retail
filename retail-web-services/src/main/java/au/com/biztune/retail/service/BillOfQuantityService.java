package au.com.biztune.retail.service;

import au.com.biztune.retail.domain.BoqDetail;
import au.com.biztune.retail.domain.PurchaseOrderHeader;
import au.com.biztune.retail.response.CommonResponse;

import javax.ws.rs.core.SecurityContext;
import java.io.InputStream;
import java.util.List;

/**
 * Created by arash on 5/04/2016.
 */
public interface BillOfQuantityService {
    /**
     * upload Bill Of Quantity.
     * @param uploadedInputStream uploadedInputStream
     * @return CommonResponse
     */
    CommonResponse uploadBillOfQuantity(InputStream uploadedInputStream);

    /**
     * get bill of quantity detail list per BOQ Id.
     * @param id id
     * @return List of BoqDetail
     */
    List<BoqDetail> getBoqDetailByBoqId(long id);

    /**
     * get list of bill of quantity .
     * @return List of BOQ
     */
    List<au.com.biztune.retail.domain.BillOfQuantity> getAllBoq();


    /**
     * get bill of quantity header per BOQ Id.
     * @param id id
     * @return BOQ
     */
    au.com.biztune.retail.domain.BillOfQuantity getBoqHeaderByBoqId(long id);
    /**
     * update bill of quantity on stock information.
     * @param billOfQuantity billOfQuantity
     * @param securityContext securityContext
     * @return Response
     */
    CommonResponse update(au.com.biztune.retail.domain.BillOfQuantity billOfQuantity, SecurityContext securityContext);

    /**
     * generate Purchase orders from bill of quantities.
     * @param billOfQuantities list of BillOfQyantity Objects.
     * @return response
     */
    List<PurchaseOrderHeader> createPurchaseOrderFromBillOfQuantities(List<au.com.biztune.retail.domain.BillOfQuantity> billOfQuantities);
    }
