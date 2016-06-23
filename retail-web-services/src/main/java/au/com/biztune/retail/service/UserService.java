package au.com.biztune.retail.service;

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
    AppUser getUseById(long userId);
}
