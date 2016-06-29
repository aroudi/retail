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
public class UserServiceImpl implements UserService {
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
                if (appRole == null || appRole.isRoleDeleted()) {
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
                if (accessPoint == null || accessPoint.isDeleted()) {
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
    public AppUser getUserById(long userId) {
        try {
            return userDao.getUserById(userId);
        } catch (Exception e) {
            logger.error("Exception in retreiving user", e);
            return null;
        }
    }

    /**
     * add/edit role.
     * @param appRole appRole
     * @return CommonResponse
     */
    public CommonResponse addRole(AppRole appRole) {
        final CommonResponse response = new CommonResponse();
        try {
            response.setStatus(IdBConstant.RESULT_SUCCESS);

            if (appRole == null) {
                response.setStatus(IdBConstant.RESULT_FAILURE);
                response.setMessage("role object is null");
                return response;
            }
            final boolean isNew = appRole.getId() > 0 ? false : true;
            //new role
            if (isNew) {
                //check if roleName is already exists
                AppRole appRole1 = userDao.getRoleByName(appRole.getRoleName());
                if (appRole1 != null) {
                    response.setStatus(IdBConstant.RESULT_FAILURE);
                    response.setMessage("role name already exists!!");
                    return response;
                }
                appRole1 = userDao.getRoleByCode(appRole.getRoleCode());
                if (appRole1 != null) {
                    response.setStatus(IdBConstant.RESULT_FAILURE);
                    response.setMessage("role code already exists!!");
                    return response;
                }
                userDao.insertAppRole(appRole);
            } else {
                userDao.updateRolePerId(appRole);
            }
            //update role-access links.
            userDao.deleteRoleAccessLinkByRoleId(appRole.getId());
            RoleAccess roleAccess = null;
            for (AccessPoint accessPoint : appRole.getAccessPoints()) {
                if (accessPoint == null || accessPoint.isDeleted()) {
                    continue;
                }
                roleAccess = new RoleAccess();
                roleAccess.setRoleId(appRole.getId());
                roleAccess.setAcptId(accessPoint.getId());
                userDao.insertRoleAccessLink(roleAccess);
            }
            //update user-role links.
            userDao.deleteRoleUserLinkByRoleId(appRole.getId());
            AppUserRole appUserRole = null;
            for (AppUser appUser : appRole.getAppUsers()) {
                if (appUser == null || appUser.isUsrDeleted()) {
                    continue;
                }
                appUserRole = new AppUserRole();
                appUserRole.setRoleId(appRole.getId());
                appUserRole.setUsrId(appUser.getId());
                userDao.insertUserRoleLink(appUserRole);
            }
            return response;
        } catch (Exception e) {
            logger.error("Exception in saving Role: ", e);
            response.setStatus(IdBConstant.RESULT_FAILURE);
            response.setMessage("Exception in saving Role");
            return response;
        }
    }

    /**
     * delete role by id.
     * @param roleId roleId
     * @return CommonResponse
     */
    public CommonResponse deleteRole(long roleId) {
        final CommonResponse response = new CommonResponse();
        try {
            response.setStatus(IdBConstant.RESULT_SUCCESS);
            userDao.deleteRoleById(roleId);
            return response;
        } catch (Exception e) {
            logger.error("Exception in deleting role: ", e);
            response.setStatus(IdBConstant.RESULT_FAILURE);
            response.setMessage("Error in deleting role.");
            return response;
        }
    }

    /**
     * get all valid roles - role not deleted.
     * @return List of AppRole
     */
    public List<AppRole> getAllValidRoles() {
        try {
            return userDao.getValidRoles();
        } catch (Exception e) {
            logger.error("Exception in retreiving role list: ", e);
            return null;
        }
    }

    /**
     * get role by id.
     * @param roleId roleId
     * @return AppRole
     */
    public AppRole getRoleById(long roleId) {
        try {
            final AppRole appRole = userDao.getRoleById(roleId);
            appRole.setAppUsers(userDao.getRoleUsersPerRoleId(roleId));
            return appRole;
        } catch (Exception e) {
            logger.error("Exception in retreiving role", e);
            return null;
        }
    }

    /**
     * get all valid roles - role not deleted.
     * @return List of Access Point
     */
    public List<AccessPoint> getAllAccessPoints() {
        try {
            return userDao.getAllAccesspoints();
        } catch (Exception e) {
            logger.error("Exception in retreiving access point list: ", e);
            return null;
        }
    }

    /**
     * login to the system and return user with all its access rights.
     * @param userName userName
     * @param password password
     * @return appUser
     */
    public AppUser doLogin (String userName, String password) {
        try {
            return userDao.doLogin(userName, password);
        } catch (Exception e) {
            logger.error("Exception in login user: ", e);
            return null;
        }
    }
}

