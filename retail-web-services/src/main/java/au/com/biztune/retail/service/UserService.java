package au.com.biztune.retail.service;

import au.com.biztune.retail.domain.AccessPoint;
import au.com.biztune.retail.domain.AppRole;
import au.com.biztune.retail.domain.AppUser;
import au.com.biztune.retail.response.CommonResponse;

import java.util.List;

/**
 * Created by akhoshraft on 23/06/2016.
 */
public interface UserService {
    /**
     * add/edit user.
     * @param appUser appUser
     * @return CommonResponse
     */
    CommonResponse addUser(AppUser appUser);
    /**
     * activate user by id.
     * @param userId userId
     * @param isActive isActive.
     * @return CommonResponse
     */
    CommonResponse activateUser(long userId, boolean isActive);
    /**
     * delete user by id.
     * @param userId userId
     * @return CommonResponse
     */
    CommonResponse deleteUser(long userId);
    /**
     * get all valid users - user not deleted.
     * @return List of AppUser
     */
    List<AppUser> getAllValidUsers();
    /**
     * get user by id.
     * @param userId userId
     * @return AppUser
     */
    AppUser getUserById(long userId);

    /**
     * add/edit role.
     * @param appRole appRole
     * @return CommonResponse
     */
    CommonResponse addRole(AppRole appRole);
    /**
     * delete role by id.
     * @param roleId roleId
     * @return CommonResponse
     */
    CommonResponse deleteRole(long roleId);

    /**
     * get all valid roles - role not deleted.
     * @return List of AppRole
     */
    List<AppRole> getAllValidRoles();

    /**
     * get role by id.
     * @param roleId roleId
     * @return AppRole
     */
    AppRole getRoleById(long roleId);

    /**
     * get all valid roles - role not deleted.
     * @return List of Access Point
     */
    List<AccessPoint> getAllAccessPoints();

    /**
     * login to the system and return user with all its access rights.
     * @param userName userName
     * @param password password
     * @return appUser
     */
    AppUser doLogin (String userName, String password);

    /**
     * logout user from the system. remove the token.
     * @param appUser appUser
     */
    void logOut(AppUser appUser);

}
