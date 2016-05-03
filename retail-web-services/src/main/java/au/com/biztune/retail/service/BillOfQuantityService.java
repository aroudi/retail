package au.com.biztune.retail.service;

import au.com.biztune.retail.domain.BoqDetail;
import au.com.biztune.retail.response.CommonResponse;

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
     * get bill of quantity detail .
     * @return List of BoqDetail
     */
    List<BoqDetail> getAllBoqDetail();
}
