package au.com.biztune.retail.service;

import au.com.biztune.retail.domain.BoqDetail;
import au.com.biztune.retail.domain.Project;
import au.com.biztune.retail.domain.PurchaseOrderHeader;
import au.com.biztune.retail.form.BoqSearchForm;
import au.com.biztune.retail.response.CommonResponse;
import org.springframework.transaction.annotation.Transactional;
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
     * @param securityContext securityContext
     * @return CommonResponse
     */
    CommonResponse uploadBillOfQuantity (InputStream uploadedInputStream, SecurityContext securityContext);

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
     * @param  securityContext securityContext
     * @return response
     */
    @Transactional
    List<PurchaseOrderHeader> createPurchaseOrderFromBillOfQuantities(List<au.com.biztune.retail.domain.BillOfQuantity> billOfQuantities, SecurityContext securityContext);

    /**
     * get client's BOQ list.
     * @param clientId clientId
     * @return List of Boq
     */
    List<au.com.biztune.retail.domain.BillOfQuantity> getClientBillOfQuantities(long clientId);

    /**
     * search boq.
     * @param boqSearchForm boqSearchForm
     * @return boq search form.
     */
    BoqSearchForm searchBoqPaging(BoqSearchForm boqSearchForm);

    /**
     * get all projects.
     * @return project list.
     */
    List<Project> getAllProjects();

    /**
     * delete boq list.
     * @param boqIdList boqIdList
     * @return response.
     */
    CommonResponse deleteBoqPerIdList(List<Long> boqIdList);

    /**
     * confirm BOQ. when BOQ is imported, the status is pending(temporary).
     * somebody need to review and confirm it.
     * @param boqIdList boqIdList
     * @return CommonResponse
     */
    CommonResponse confirmBoq(List<Long> boqIdList);
    }
