package au.com.biztune.retail.session;

import au.com.biztune.retail.domain.AppUser;
import au.com.biztune.retail.domain.OrgUnit;
import au.com.biztune.retail.domain.Store;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * Created by arash on 22/03/2016.
 */
@Component
public class SessionState {
    private OrgUnit orgUnit;
    private Store store;
    private HashMap<String, AppUser> tokens;
    private final Logger logger = LoggerFactory.getLogger(SessionState.class);
    private AppUser appUser;
    /**
     * determin if data has been changed for specific data. the key is UserId and the object determine if specific data has been changed or not.
     */
    private HashMap<Long, DataChangeIndicator> dataChangeIndicatorHashMap;

    /**
     * add token to the session.
     * @param key key
     * @param appUser appUser
     */
    public void addToken(String key, AppUser appUser) {
        try {
            if (tokens == null) {
                tokens = new HashMap<String, AppUser>();
            }
            if (tokens.containsKey(key)) {
                tokens.remove(key);
            }
            tokens.put(key, appUser);
        } catch (Exception e) {
            logger.error("Exception in adding token to the local session: ", e);
        }
    }

    /**
     * remove token and its associated users from tokens.
     * @param key key
     */
    public void removeToken(String key) {
        try {
            if (tokens == null) {
                return;
            }
            if (tokens.containsKey(key)) {
                tokens.remove(key);
                logger.info("token" + key + "removed");
            }
        } catch (Exception e) {
            logger.error("Exception in removing token.", e);
        }
    }

    /**
     * when specific data changed, we need to inform user about it.
     * @param key UserId
     * @param dataChangeIndicator new data indicator
     */
    public void addDataChangeIndicator(Long key, DataChangeIndicator dataChangeIndicator) {
        try {
            if (dataChangeIndicatorHashMap == null) {
                dataChangeIndicatorHashMap = new HashMap<Long, DataChangeIndicator>();
            }
            if (dataChangeIndicatorHashMap.containsKey(key)) {
                dataChangeIndicatorHashMap.remove(key);
            }
            dataChangeIndicatorHashMap.put(key, dataChangeIndicator);
        } catch (Exception e) {
            logger.error("Exception in updating data change indicator: ", e);
        }
    }

    /**
     * Return User from token.
     * @param key key
     * @return AppUser
     */
    public AppUser geUserPerToken(String key) {
        if (tokens == null) {
            return null;
        }
        if (tokens.containsKey(key)) {
            return (AppUser) tokens.get(key);
        } else {
            return null;
        }
    }

    /**
     * get datachange indicators per user id. check if data has been changed. read data and mark it as read.
     * @param key userId
     * @return all data change indicators.
     */
    public DataChangeIndicator getDataChangeIndicatorsPerUserId(Long key) {
        final DataChangeIndicator dataChangeIndicator = new DataChangeIndicator();
        final DataChangeIndicator originalDataChangeIndicator;
        if (dataChangeIndicatorHashMap == null) {
            return null;
        }
        if (dataChangeIndicatorHashMap.containsKey(key)) {
            originalDataChangeIndicator = (DataChangeIndicator) dataChangeIndicatorHashMap.get(key);
            if (originalDataChangeIndicator.isProductDataUpdated()) {
                dataChangeIndicator.setProductDataUpdated(false);
            }
            dataChangeIndicatorHashMap.remove(key);
            dataChangeIndicatorHashMap.put(key, dataChangeIndicator);
            return originalDataChangeIndicator;
        } else {
            return null;
        }
    }

    public OrgUnit getOrgUnit() {
        return orgUnit;
    }

    public void setOrgUnit(OrgUnit orgUnit) {
        this.orgUnit = orgUnit;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public HashMap<String, AppUser> getTokens() {
        return tokens;
    }

    public void setTokens(HashMap<String, AppUser> tokens) {
        this.tokens = tokens;
    }

    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }
}
