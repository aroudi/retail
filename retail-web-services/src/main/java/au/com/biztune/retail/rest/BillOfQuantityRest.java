// Sydney Trains 2015

package au.com.biztune.retail.rest;

import au.com.biztune.retail.domain.BillOfQuantity;
import au.com.biztune.retail.domain.BoqDetail;
import au.com.biztune.retail.domain.PurchaseOrderHeader;
import au.com.biztune.retail.report.BillOfQuantityRptMgr;
import au.com.biztune.retail.response.CommonResponse;
import au.com.biztune.retail.security.Secured;
import au.com.biztune.retail.service.BillOfQuantityService;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.InputStream;
import java.util.List;

/**
 * Created by arash on 10/08/2015.
 */

@Component
@Path("billOfQuantity")
public class BillOfQuantityRest {
    private final Logger logger = LoggerFactory.getLogger(BillOfQuantityRest.class);
    @Context
    private UriInfo uriInfo;

    @Context
    private Request request;

    @Autowired
    private BillOfQuantityService billOfQuantityService;

    @Autowired
    private BillOfQuantityRptMgr billOfQuantityRptMgr;
    /**
     * upload bill of quantity.
     * @param uploadedInputStream uploadedInputStream
     * @return CommonResponse
     */
    @Secured
    @POST
    @Path("/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public CommonResponse uploadBillOfQuantity(@FormDataParam("file")InputStream uploadedInputStream) {
        return billOfQuantityService.uploadBillOfQuantity(uploadedInputStream);
    }

    /**
     * get BillOfQuantity Detail by BOQId.
     * @param id id.
     * @return List of BoqDetail
     */
    @Secured
    @GET
    @Path("/detail/getPerBoqId/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<BoqDetail> getBoqDetailByBoqId (@PathParam("id") long id) {
        return billOfQuantityService.getBoqDetailByBoqId(id);
    }

    /**
     * get BillOfQuantity by BOQId.
     * @param id id.
     * @return Bill Of Quantity
     */
    @Secured
    @GET
    @Path("/header/get/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public BillOfQuantity getBoqById (@PathParam("id") long id) {
        return billOfQuantityService.getBoqHeaderByBoqId(id);
    }


    /**
     * get List of BillOfQuantity .
     * @return List of
     */
    @Secured
    @GET
    @Path("/header/getAll")
    @Produces(MediaType.APPLICATION_JSON)
    public List<BillOfQuantity> getAllBoq () {
        return billOfQuantityService.getAllBoq();
    }

    /**
     * update Bill Of Quantity Stock quantity.
     * @param billOfQuantity billOfQuantity
     * @return CommonResponse
     */
    @Secured
    @Path("/updateStockQty")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public CommonResponse updateBoqStockQty (BillOfQuantity billOfQuantity) {
        return billOfQuantityService.update(billOfQuantity);
    }

    /**
     * create PurchaseOrder from Bill Of Quantities.
     * @param billOfQuantityList billOfQuantityList
     * @return list of created PurchaseOrderHeader
     */
    @Secured
    @Path("/generatePurchaseOrder")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<PurchaseOrderHeader> generatePurchaseOrderFromBoqs (List<BillOfQuantity> billOfQuantityList) {
        return billOfQuantityService.createPurchaseOrderFromBillOfQuantities(billOfQuantityList);
    }

    /**
     * export purchase order as PDF.
     * @param boqId boqId
     * @return Stream output.
     */
    @Secured
    @Path("/pickingSlip/exportPdf/{boqId}")
    @GET
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response exportBoqToPdf(@PathParam("boqId") long boqId) {
        final StreamingOutput streamingOutput = billOfQuantityRptMgr.createPickingSlipPdfStream(boqId);
        return Response.ok(streamingOutput).header("Content-Disposition", "attachment; filename = PickingSlip" + boqId + ".pdf").build();
    }
}
