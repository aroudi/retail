package au.com.biztune.retail.session;

import au.com.biztune.retail.domain.OrgUnit;
import au.com.biztune.retail.domain.Store;
import org.springframework.stereotype.Component;

/**
 * Created by arash on 22/03/2016.
 */
@Component
public class SessionState {
    private OrgUnit orgUnit;
    private Store store;

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
}
