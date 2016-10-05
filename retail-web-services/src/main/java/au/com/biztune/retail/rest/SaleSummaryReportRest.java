// Sydney Trains 2015

package au.com.biztune.retail.rest;

import au.com.biztune.retail.domain.SaleSummaryReport;
import au.com.biztune.retail.form.SaleSummaryReportForm;
import au.com.biztune.retail.security.Secured;
import au.com.biztune.retail.service.SaleSummaryReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

/**
 * Created by arash on 10/08/2015.
 */

@Component
@Path("saleSummaryReport")
public class SaleSummaryReportRest {
    private final Logger logger = LoggerFactory.getLogger(SaleSummaryReportRest.class);
    @Context
    private UriInfo uriInfo;

    @Context
    private Request request;

    @Autowired
    private SaleSummaryReportService saleSummaryReportService;

    /**
     * get sale summary report.
     * @param saleSummaryReportForm saleSummaryReportForm
     * @return Sale Summary Report
     */
    @Secured
    @Path("/saleSummary")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public SaleSummaryReport getSaleSummaryReport (SaleSummaryReportForm saleSummaryReportForm) {
        return saleSummaryReportService.getSaleSummaryReport(saleSummaryReportForm);
    }

}
