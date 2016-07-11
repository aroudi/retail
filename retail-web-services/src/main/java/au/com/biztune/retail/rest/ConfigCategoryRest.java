// Sydney Trains 2015

package au.com.biztune.retail.rest;

import au.com.biztune.retail.domain.ConfigCategory;
import au.com.biztune.retail.security.Secured;
import au.com.biztune.retail.service.ConfigCategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
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
}
