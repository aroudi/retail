package au.com.biztune.retail.security;


import au.com.biztune.retail.domain.AppUser;
import au.com.biztune.retail.session.SessionState;
import com.google.common.net.HttpHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Priority;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.security.Principal;

/**
 * Created by arash on 8/07/2016.
 */

@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

    @Autowired
    private SessionState sessionState;
    private final Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);
    private AppUser appUser;
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        //GET the http Authorization header from the request
        logger.info("AuthenticationFilter called");
        final String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        //Check if the HTTP Authorization header is present and formatted correctly
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new NotAuthorizedException("Authorization header must be provided");
        }

        //Extract the token from the HTTP Authorization header
        final String token = authorizationHeader.substring("Bearer".length()).trim();
        logger.info("AuthenticationFilter token = " + token);
        try {
            //validat the token
            appUser = sessionState.geUserPerToken(token);
            if (appUser == null) {
                //throw new NotAuthorizedException("Not Authorized");
                requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
            }
            requestContext.setSecurityContext(new SecurityContext() {
                @Override
                public Principal getUserPrincipal() {
                    return appUser;
                }

                @Override
                public boolean isUserInRole(String role) {
                    return true;
                }

                @Override
                public boolean isSecure() {
                    return false;
                }

                @Override
                public String getAuthenticationScheme() {
                    return null;
                }
            });
            //sessionState.setAppUser(appUser);
        } catch (Exception e) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
        }
    }

    public AppUser getAppUser() {
        return appUser;
    }
}
