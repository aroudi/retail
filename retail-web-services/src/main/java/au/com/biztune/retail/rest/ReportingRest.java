package au.com.biztune.retail.rest;

import au.com.biztune.retail.report.ReportManager;
import au.com.biztune.retail.domain.ReportTreeViewNode;
import au.com.biztune.retail.security.Secured;
import au.com.biztune.retail.service.ReportingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;

/**
 * Created by arash on 28/03/2018.
 */

@Component
@Path("reporting")
public class ReportingRest {
    private final Logger logger = LoggerFactory.getLogger(ReportingRest.class);
    @Context
    private UriInfo uriInfo;
    @Context
    private SecurityContext securityContext;
    @Context
    private Request request;
    @Autowired
    private ReportManager reportManager;
    @Autowired
    private ReportingService reportingService;

    /**
     * Returns list of reporting in tree view format.
     * @return list of reporting
     */
    @Secured
    @Path("/getReportingList")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<ReportTreeViewNode> getAllReports() {
        try {
            return reportingService.getReportList();
        } catch (Exception e) {
            logger.error ("Error in retrieving reporting List(tree view format) :", e);
            return null;
        }
    }

    /**
     * run reports.
     * @param reportTreeViewNode reportTreeViewNode
     * @return PDF report.
     */
    @Secured
    @Path("/runReport")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response runReport(ReportTreeViewNode reportTreeViewNode) {
        final StreamingOutput streamingOutput = reportManager.runReport(reportTreeViewNode.getReportKey(), reportTreeViewNode.getReportParamList(), securityContext);
        return Response.ok(streamingOutput).header("Content-Disposition", "attachment; filename = report.pdf").build();
    }

}
