package au.com.biztune.retail.rest;

import au.com.biztune.retail.domain.AppUser;
import au.com.biztune.retail.domain.Customer;
import au.com.biztune.retail.domain.CustomerGrade;
import au.com.biztune.retail.response.CommonResponse;
import au.com.biztune.retail.service.CustomerService;
import au.com.biztune.retail.service.UserService;
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
@Path("user")
public class UserRest {
    private final Logger logger = LoggerFactory.getLogger(UserRest.class);
    @Context
    private UriInfo uriInfo;
    @Context
    private Request request;

    @Autowired
    private UserService userService;

    /**
     * Returns list of customers.
     * @return list of Customer
     */
    @Path("/allValid")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<AppUser> getAllValidUsers() {
        try {
            return userService.getAllValidUsers();
        } catch (Exception e) {
            logger.error ("Error in retrieving user List :", e);
            return null;
        }
    }

    /**
     * crate a user.
     * @param user user
     * @return CommonResponse
     */
    @Path("/add")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public CommonResponse addUser (AppUser user) {
        return userService.addUser(user);
    }

    /**
     * activate user by Id.
     * @param id id.
     * @return Common Response
     */
    @GET
    @Path("/activate/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public CommonResponse activateUserById (@PathParam("id") long id) {
        return userService.activateUser(id, true);
    }

    /**
     * deActivate user by Id.
     * @param id id.
     * @return Common Response
     */
    @GET
    @Path("/deActivate/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public CommonResponse deActivateUserById (@PathParam("id") long id) {
        return userService.activateUser(id, false);
    }

    /**
     * get user by Id.
     * @param id id.
     * @return AppUser
     */
    @GET
    @Path("/get/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public AppUser getUserById (@PathParam("id") long id) {
        return userService.getUseById(id);
    }

    /**
     * delete user by Id.
     * @param id id.
     * @return Common Response
     */
    @GET
    @Path("/delete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public CommonResponse deleteUserById (@PathParam("id") long id) {
        return userService.deleteUser(id);
    }
}
