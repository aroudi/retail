package au.com.biztune.retail.dao;

import au.com.biztune.retail.domain.*;

import java.util.List;

/**
 * Created by arash on 22/06/2016.
 */
public interface UserDao {

    /**
     * insert user.
     * @param appUser appUser
     */
    void insertAppUser(AppUser appUser);

    /**
     * change password for specific user.
     * @param appUser appUser
     */
    void changePasswordPerUserId(AppUser appUser);

    /**
     * update user profile per user id.
     * @param appUser appUser
     */
    void updateUserProfilePerId(AppUser appUser);

    /**
     * delete user by id.
     * @param userId userId
     */
    void deleteUserById(long userId);

    /**
     * activate user by id.
     * @param userId userId
     */
    void activeUserById(long userId);

    /**
     * deActivate user by id.
     * @param userId userId
     */
    void deActiveUserById(long userId);

    /**
     * login.
     * @param userName userName
     * @param password password
     * @return AppUser
     */
    AppUser login(String userName, String password);

    /**
     * get user by id.
     * @param userId userId
     * @return AppUser
     */
    AppUser getUserById(long userId);

    /**
     * get user object by id.
     * @param userId userId
     * @return AppUser
     */
    AppUser getLiteUserById(long userId);

    /**
     * get user by username.
     * @param userName userName
     * @return AppUser
     */
    AppUser getUserByUserName(String userName);

    /**
     * get valid users.
     * @return AppUser
     */
    List<AppUser> getValidUsers();

    /**
     * insert app user.
     * @param appRole appRole
     */
    void insertAppRole(AppRole appRole);

    /**
     * update role per id.
     * @param appRole appRole
     */
    void updateRolePerId(AppRole appRole);

    /**
     * delete  role per id.
     * @param roleId roleId
     */
    void deleteRoleById(long roleId);

    /**
     * get role by id.
     * @param roleId roleId
     * @return AppRole
     */
    AppRole getRoleById(long roleId);

    /**
     * get role by NAME.
     * @param name name
     * @return AppRole
     */
    AppRole getRoleByName(String name);

    /**
     * get role by NAME.
     * @param code code
     * @return AppRole
     */
    AppRole getRoleByCode(String code);


    /**
     * get valid roles.
     * @return AppRole
     */
    List<AppRole> getValidRoles();

    /**
     * get user roles per user id.
     * @param userId userId
     * @return AppRoles
     */
    List<AppRole> getUserRolesPerUserId(long userId);

    /**
     * get Role users per roleId.
     * @param roleId roleId
     * @return List of Users
     */
    List<AppUser> getRoleUsersPerRoleId(long roleId);
    /**
     * insert Access Point.
     * @param accessPoint accessPoint
     */
    void insertAccessPoint(AccessPoint accessPoint);

    /**
     * update Access point.
     * @param accessPointId accessPointId
     */
    void updateAccessPointPerId(long accessPointId);

    /**
     * get access point by id.
     * @param accessPoinId accessPoinId
     */
    void getAccesspointById(long accessPoinId);

    /**
     * get user accesspoints by user id.
     * @param userId userId
     * @return UserAccessPoint
     */
    List<AccessPoint> getUserAccesspointsByUserId(long userId);

    /**
     * get RoleAccessPoint By Roleid.
     * @param roleId roleId
     * @return List of AccessPoint
     */
    List<AccessPoint> getRoleAccesspointsByRoleId(long roleId);

    /**
     * insert User Role Link.
     * @param appUserRole appUserRole
     */
    void insertUserRoleLink(AppUserRole appUserRole);

    /**
     * delete User Role Link by User Id.
     * @param userId userId
     */
    void deleteUserRoleLinkByUserId(long userId);

    /**
     * delete User Role Link by role id.
     * @param roleId roleId
     */
    void deleteUserRoleLinkByRoleId(long roleId);

    /**
     * insert User Access Link.
     * @param userAccess userAccess
     */
    void insertUserAccessLink(UserAccess userAccess);

    /**
     * delete User Access Link By User Id.
     * @param userId userId
     */
    void deleteUserAccessLinkByUserId(long userId);

    /**
     * insert Role Access Link.
     * @param roleAccess roleAccess
     */
    void insertRoleAccessLink(RoleAccess roleAccess);

    /**
     * delete Role access link by role id.
     * @param roleId roleId
     */
    void deleteRoleAccessLinkByRoleId(long roleId);

    /**
     * get all access ponts.
     * @return List of AccessPoints
     */
    List<AccessPoint> getAllAccesspoints();

    /**
     * delete role user link by roleId.
     * @param roleId roleId
     */
    void deleteRoleUserLinkByRoleId(long roleId);

    /**
     * login to the system.
     * @param userName userName
     * @param password password
     * @return User Object
     */
    AppUser doLogin(String userName, String password);

    /**
     * get user by userId and password.
     * @param userId userId
     * @param password password
     * @return user
     */
    AppUser getUserByUserIdAndPassword(long userId, String password);

    /**
     * get all active users.
     * @return list of active users
     */
    List<AppUser> getAllActiveUsers();
}
