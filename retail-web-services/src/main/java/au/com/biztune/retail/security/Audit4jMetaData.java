package au.com.biztune.retail.security;

import au.com.biztune.retail.domain.AppUser;
import au.com.biztune.retail.service.UserService;
import org.audit4j.core.MetaData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

public class Audit4jMetaData implements MetaData {

    @Context
    private SecurityContext securityContext;

    @Autowired
    UserService userService;

    private final Logger logger = LoggerFactory.getLogger(Audit4jMetaData.class);

    @Override
    @Secured
    public String getActor() {
        try {
            logger.debug("getActore called....");
            final AppUser appUser = userService.getAppUserFromSecurityContext(securityContext);
            final StringBuilder userInfo = new StringBuilder();
            if (appUser != null) {
                userInfo.append(appUser.getUsrFirstName());
                userInfo.append(" ");
                userInfo.append(appUser.getUsrSurName());
                return userInfo.toString();
            }
            return null;
        } catch (Exception e) {
            logger.error("Exception in Audit4jMetaData:getActor()", e);
            return null;
        }
    }

    @Override
    public String getOrigin() {
        return null;
    }

}
