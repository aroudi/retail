package au.com.biztune.retail.service;

import au.com.biztune.retail.dao.UserDao;
import au.com.biztune.retail.domain.*;
import au.com.biztune.retail.response.CommonResponse;
import au.com.biztune.retail.util.IdBConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by arash on 22/06/2016.
 */

@Component
public class UserServiceImpl {
    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private UserDao userDao;

    /**
     * add/edit user.
     * @param appUser appUser
     * @return CommonResponse
     */
    public CommonResponse addUser(AppUser appUser) {
        final CommonResponse response = new CommonResponse();
        try {
            response.setStatus(IdBConstant.RESULT_SUCCESS);

            if (appUser == null) {
                response.setStatus(IdBConstant.RESULT_FAILURE);
                response.setMessage("user object is null");
                return response;
            }
            final boolean isNew = appUser.getId() > 0 ? false : true;
            //new user
            if (isNew) {
                //check if username is already exists
                final AppUser appUser1 = userDao.getUserByUserName(appUser.getUsrName());
                if (appUser1 != null) {
                    response.setStatus(IdBConstant.RESULT_FAILURE);
                    response.setMessage("username already exists!!");
                    return response;
                }
                userDao.insertAppUser(appUser);
            } else {
                userDao.updateUserProfilePerId(appUser);
            }
            //update user-role links.
            userDao.deleteUserRoleLinkByUserId(appUser.getId());
            AppUserRole appUserRole = null;
            for (AppRole appRole : appUser.getAppRoles()) {
                if (appRole == null) {
                    continue;
                }
                appUserRole = new AppUserRole();
                appUserRole.setRoleId(appRole.getId());
                appUserRole.setUsrId(appUser.getId());
                userDao.insertUserRoleLink(appUserRole);
            }
            //update user-access links.
            userDao.deleteUserAccessLinkByUserId(appUser.getId());
            UserAccess userAccess = null;
            for (AccessPoint accessPoint : appUser.getAccessPoints()) {
                if (accessPoint == null) {
                    continue;
                }
                userAccess = new UserAccess();
                userAccess.setUsrId(appUser.getId());
                userAccess.setAcptId(accessPoint.getId());
                userDao.insertUserAccessLink(userAccess);
            }
            return response;
        } catch (Exception e) {
            logger.error("Exception in saving User: ", e);
            response.setStatus(IdBConstant.RESULT_FAILURE);
            response.setMessage("Exception in saving User");
            return response;
        }
    }

    /**
     * activate user by id.
     * @param userId userId
     * @param isActive isActive.
     * @return CommonResponse
     */
    public CommonResponse activateUser(long userId, boolean isActive) {
        final CommonResponse response = new CommonResponse();
        try {
            response.setStatus(IdBConstant.RESULT_SUCCESS);
            if (isActive) {
                userDao.activeUserById(userId);
            } else {
                userDao.deActiveUserById(userId);
            }
            return response;
        } catch (Exception e) {
            logger.error("Exception in activating user: ", e);
            response.setStatus(IdBConstant.RESULT_FAILURE);
            response.setMessage("Error in activating user.");
            return response;
        }
    }
    /**
     * delete user by id.
     * @param userId userId
     * @return CommonResponse
     */
    public CommonResponse deleteUser(long userId) {
        final CommonResponse response = new CommonResponse();
        try {
            response.setStatus(IdBConstant.RESULT_SUCCESS);
            userDao.deleteUserById(userId);
            return response;
        } catch (Exception e) {
            logger.error("Exception in deleting user: ", e);
            response.setStatus(IdBConstant.RESULT_FAILURE);
            response.setMessage("Error in deleting user.");
            return response;
        }
    }

    /**
     * get all valid users - user not deleted.
     * @return List of AppUser
     */
    public List<AppUser> getAllValidUsers() {
        try {
            return userDao.getValidUsers();
        } catch (Exception e) {
            logger.error("Exception in retreiving user list: ", e);
            return null;
        }
    }

    /**
     * get user by id.
     * @param userId userId
     * @return AppUser
     */
    public AppUser getUseById(long userId) {
        try {
            return userDao.getUserById(userId);
        } catch (Exception e) {
            logger.error("Exception in retreiving user", e);
            return null;
        }
    }
}

