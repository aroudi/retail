/*
 * Package: au.com.biztune.retail.response
 * Class: LoginResponse
 * Copyright: (c) 2019 Sydney Trains
 */
package au.com.biztune.retail.response;

import au.com.biztune.retail.domain.AppUser;

/**
 * LoginResponse.
 *
 * @author arash
 */
public class LoginResponse extends CommonResponse {
    private AppUser appUser;

    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }
}
