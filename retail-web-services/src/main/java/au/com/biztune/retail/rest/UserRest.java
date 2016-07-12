package au.com.biztune.retail.rest;

import au.com.biztune.retail.domain.*;
import au.com.biztune.retail.form.LoginForm;
import au.com.biztune.retail.response.CommonResponse;
import au.com.biztune.retail.security.Secured;
import au.com.biztune.retail.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.ws.rs.core.MediaType;
import java.security.Principal;
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

    @Context
    private SecurityContext securityContext;

    @Autowired
    private UserService userService;

    /**
     * Returns list of customers.
     * @return list of Customer
     */
    @Secured
    @Path("/allValidUser")
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
    @Secured
    @Path("/addUser")
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
    @Secured
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
    @Secured
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
    @Secured
    @GET
    @Path("/getUser/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public AppUser getUserById (@PathParam("id") long id) {
        return userService.getUserById(id);
    }

    /**
     * delete user by Id.
     * @param id id.
     * @return Common Response
     */
    @Secured
    @GET
    @Path("/deleteUser/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public CommonResponse deleteUserById (@PathParam("id") long id) {
        return userService.deleteUser(id);
    }

    /**
     * crate a role.
     * @param role role
     * @return CommonResponse
     */
    @Secured
    @Path("/addRole")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public CommonResponse addRole (AppRole role) {
        return userService.addRole(role);
    }

    /**
     * Returns list of roles.
     * @return list of roles
     */
    @Secured
    @Path("/allValidRole")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<AppRole> getAllValidRoles() {
        try {
            return userService.getAllValidRoles();
        } catch (Exception e) {
            logger.error ("Error in retrieving role List :", e);
            return null;
        }
    }

    /**
     * get role by Id.
     * @param id id.
     * @return AppRole
     */
    @Secured
    @GET
    @Path("/getRole/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public AppRole getRoleById (@PathParam("id") long id) {
        return userService.getRoleById(id);
    }
    /**
     * delete role by Id.
     * @param id id.
     * @return Common Response
     */
    @Secured
    @GET
    @Path("/deleteRole/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public CommonResponse deleteRoleById (@PathParam("id") long id) {
        return userService.deleteRole(id);
    }
    /**
     * Returns list of Access Point.
     * @return list of Access Point
     */
    @Secured
    @Path("/allAccessPoints")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<AccessPoint> getAllAccessPoint() {
        try {
            return userService.getAllAccessPoints();
        } catch (Exception e) {
            logger.error ("Error in retrieving Access Point List :", e);
            return null;
        }
    }
    /**
     * check user login credentials.
     * @param loginForm loginForm
     * @return StaffUserModel
     */
    @Path("/login")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public AppUser logon (LoginForm loginForm) {
        try {
            return userService.doLogin (loginForm.getUserName(), loginForm.getPassword());
        } catch (Exception e) {
            logger.error ("UserLoginService: Error in user logon: ", e);
            return null;
        }
    }

    /**
     * Logout from the system.
     */
    @Secured
    @Path("/logout")
    @GET
    public void setLogger() {
        try {
            final Principal principal = securityContext.getUserPrincipal();
            if (principal instanceof AppUser) {
                logger.info("logout 1");
                final AppUser appUser = (AppUser) principal;
                logger.info("logout 2");
                userService.logOut(appUser.getToken());
            }
        } catch (Exception e) {
            logger.error ("Error in retrieving Access Point List :", e);
        }
    }
}
