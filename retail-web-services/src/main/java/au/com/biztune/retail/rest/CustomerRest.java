package au.com.biztune.retail.rest;

import au.com.biztune.retail.domain.*;
import au.com.biztune.retail.form.AccountDebtRptForm;
import au.com.biztune.retail.report.CustomerStatementRptMgr;
import au.com.biztune.retail.response.CommonResponse;
import au.com.biztune.retail.security.Secured;
import au.com.biztune.retail.service.BillOfQuantityService;
import au.com.biztune.retail.service.CustomerImportService;
import au.com.biztune.retail.service.CustomerService;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.ws.rs.core.MediaType;
import java.io.InputStream;
import java.util.List;

/**
 * Created by arash on 22/02/2016.
 */
@Component
@Path("customer")
public class CustomerRest {
    private final Logger logger = LoggerFactory.getLogger(CustomerRest.class);
    @Context
    private UriInfo uriInfo;
    @Context
    private Request request;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerStatementRptMgr customerStatementRptMgr;

    @Autowired
    private BillOfQuantityService billOfQuantityService;

    @Autowired
    private CustomerImportService customerImportService;
    /**
     * Returns list of customers.
     * @return list of Customer
     */

    @Secured
    @Path("/all")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Customer> getAllCustomers() {
        try {
            return customerService.getAllCustomers();
        } catch (Exception e) {
            logger.error ("Error in retrieving customer List :", e);
            return null;
        }
    }

    /**
     * Returns list of grade.
     * @return list of CustomerCrade
     */

    @Secured
    @Path("/allGrades")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<CustomerGrade> getAllCustomerGrades() {
        try {
            return customerService.getAllCustomerGrades();
        } catch (Exception e) {
            logger.error ("Error in retrieving customer List :", e);
            return null;
        }
    }

    /**
     * crate a customer.
     * @param customer customer
     * @return CommonResponse
     */
    @Secured
    @Path("/add")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public CommonResponse addCustomer (Customer customer) {
        return customerService.addCustomer(customer);
    }

    /**
     * get customer by Id.
     * @param id id.
     * @return Customer
     */
    @Secured
    @GET
    @Path("/get/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Customer getCustomerById (@PathParam("id") long id) {
        return customerService.getCustomerById(id);
    }

    /**
     * get customer by code.
     * @param code code.
     * @return Customer
     */
    @Secured
    @GET
    @Path("/getByCode/{code}")
    @Produces(MediaType.APPLICATION_JSON)
    public Customer getCustomerByCode (@PathParam("code") String code) {
        return customerService.getCustomerByCode(code);
    }

    /**
     * get customer account debt by  customer Id.
     * @param id id.
     * @return Customer
     */
    @Secured
    @GET
    @Path("/getAccountDebt/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<CustomerAccountDebt> getCustomerAccountDebtByCustomerId (@PathParam("id") long id) {
        return customerService.getCustomerAccountDebtPerCustomerId(id);
    }

    /**
     * get customer account payment by  customer Id.
     * @param id id.
     * @return Customer
     */
    @Secured
    @GET
    @Path("/getAccountPayment/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<CustomerAccountDebt> getCustomerAccountPaymentByCustomerId (@PathParam("id") long id) {
        return customerService.getCustomerAccountPaymentPerCustomerId(id);
    }

    /**
     * Returns All Customer Account Debt.
     * @return list of Customer account debt for all customers.
     */

    @Secured
    @Path("/getAccountDebt/all")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<CustomerAccountDebt> getAllCustomerAccountDebt() {
        try {
            return customerService.getAllCustomerAccountDebt();
        } catch (Exception e) {
            logger.error ("Error in retrieving customer List :", e);
            return null;
        }
    }
    /**
     * get customer account debt by  customer Id.
     * @param id id.
     * @return Customer
     */
    @Secured
    @GET
    @Path("/getBillOfQuantities/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<BillOfQuantity> getCustomerBillOfQuantityListCustomerId (@PathParam("id") long id) {
        return billOfQuantityService.getClientBillOfQuantities(id);
    }

    /**
     * generate customer statements report.
     * @param accountDebtRptForm accountDebtRptForm
     * @return Stream output.
     */
    @Secured
    @Path("/generateCustomerStatements")
    @POST
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response generateCustomerStatements(AccountDebtRptForm accountDebtRptForm) {
        final StreamingOutput streamingOutput = customerStatementRptMgr.runReport(accountDebtRptForm);
        return Response.ok(streamingOutput).header("Content-Disposition", "attachment; filename = customerStatementsRpt.pdf").build();
    }

    /**
     * get customer by Id.
     * @param id id.
     * @return Customer
     */
    @Secured
    @GET
    @Path("/getContactList/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Contact> getCustomerContactListById (@PathParam("id") long id) {
        return customerService.getCustomerContactListPerCustomerId(id);
    }
    /**
     * import customer records from csv.
     * @param uploadedInputStream uploadedInputStream
     * @return CommonResponse
     */
    @Secured
    @POST
    @Path("/importCsv")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public CommonResponse importSupplier(@FormDataParam("file")InputStream uploadedInputStream) {
        try {
            return customerImportService.importCustomerFromCsvInputStream(uploadedInputStream);
        } catch (Exception e) {
            logger.error("Exception in importing customer from csv.", e);
            return null;
        }
    }

    /**
     * crate a Sale Transaction.
     * @param customerIdList txhdIdList
     * @return List of generated purchase orders.
     */
    @Secured
    @Path("/logicalDelete")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public CommonResponse logicalDelete (List<Long> customerIdList) {
        return customerService.logicalDeleteCustomer(customerIdList);
    }


}
