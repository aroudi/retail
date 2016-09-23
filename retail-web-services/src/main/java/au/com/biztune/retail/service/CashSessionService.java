package au.com.biztune.retail.service;

import au.com.biztune.retail.domain.AppUser;
import au.com.biztune.retail.domain.CashSession;
import au.com.biztune.retail.domain.TxnHeader;

/**
 * Created by arash on 23/09/2016.
 */
public interface CashSessionService {

    /**
     * create cash session for user.
     * @param user user.
     * @return CashSession
     */
    CashSession createCashSession(AppUser user);

    /**
     * assign a cash session to user logged in.
     * @param appUser appUser
     */
    void assignCashSessionToLoggedinUser(AppUser appUser);

    /**
     * close cash session after logged out.
     * @param appUser appUser.
     */
    void closeCashSessionForLoggedOutUser(AppUser appUser);

    /**
     * process session event.
     * @param txnHeader txnHeader
     * @param seevType seevType - session event type
     */
    void processSessionEvent (TxnHeader txnHeader, String seevType);

}
