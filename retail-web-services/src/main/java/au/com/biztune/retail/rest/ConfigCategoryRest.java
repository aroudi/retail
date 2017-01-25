// Sydney Trains 2015

package au.com.biztune.retail.rest;

import au.com.biztune.retail.domain.ConfigCategory;
import au.com.biztune.retail.domain.CustomerGrade;
import au.com.biztune.retail.response.CommonResponse;
import au.com.biztune.retail.security.Secured;
import au.com.biztune.retail.service.ConfigCategoryService;
import au.com.biztune.retail.util.IdBConstant;
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
 * Created by arash on 10/08/2015.
 */

@Component
@Path("categories")
public class ConfigCategoryRest {
    private final Logger logger = LoggerFactory.getLogger(ConfigCategoryRest.class);
    @Context
    private UriInfo uriInfo;

    @Context
    private Request request;

    @Autowired
    private ConfigCategoryService categoryService;

    /**
     * Get All Categories for specific type as JSON.
     * @param typeName typeName
     * @return List of categories
     */
    @Secured
    @GET
    @Path("/{typeName}")
    @Produces(MediaType.APPLICATION_JSON)
    public List getCategoriesOfType(@PathParam("typeName") String typeName) {
        return categoryService.getCategoriesOfType(typeName);
    }
    /**
     * Get Category for specific type and code as JSON.
     * @param typeName typeName
     * @param code code
     * @return ConfigCategory
     */
    @Secured
    @GET
    @Path("/{typeName}/{code}")
    @Produces(MediaType.APPLICATION_JSON)
    public ConfigCategory getCategoryOfTypeAndCode(@PathParam("typeName") String typeName, @PathParam("code") String code) {
        return categoryService.getCategoryOfTypeAndCode(typeName, code);
    }

    /**
     * update customer/pricing grades.
     * @param customerGradeList customerGradeList
     * @return List of accounts.
     */
    @Secured
    @POST
    @Path("/updatePricingGrades")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public CommonResponse updateAccountCodes(List<CustomerGrade> customerGradeList) {
        final CommonResponse response = new CommonResponse();
        response.setStatus(IdBConstant.RESULT_SUCCESS);
        try {
            if (customerGradeList == null || customerGradeList.size() < 1) {
                response.setMessage("grade list is empty");
                response.setStatus(IdBConstant.RESULT_FAILURE);
                return response;
            }
            for (CustomerGrade customerGrade:customerGradeList) {
                categoryService.updateCustomerGrade(customerGrade);
            }
            return response;
        } catch (Exception e) {
            logger.error("Exception in updating grade list");
            response.setStatus(IdBConstant.RESULT_FAILURE);
            response.setMessage(e.getMessage());
            return response;
        }
    }

    /**
     * get customer grade by code.
     * @param gradeCode gradeCode
     * @return customer grade.
     */
    @Secured
    @GET
    @Path("/getCustomerGrade/{gradeCode}")
    @Produces(MediaType.APPLICATION_JSON)
    public CustomerGrade getCustomerGradeByCode(@PathParam("gradeCode") String gradeCode) {
        return categoryService.getCustomerGradeByCode(gradeCode);
    }

}
