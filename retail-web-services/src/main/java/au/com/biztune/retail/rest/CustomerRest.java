package au.com.biztune.retail.rest;

import au.com.biztune.retail.domain.Customer;
import au.com.biztune.retail.domain.CustomerGrade;
import au.com.biztune.retail.response.CommonResponse;
import au.com.biztune.retail.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;
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

    /**
     * Returns list of customers.
     * @return list of Customer
     */

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
    @Path("/add")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public CommonResponse addCustomer (Customer customer) {
        return customerService.addCustomer(customer);
    }

}
