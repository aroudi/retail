package au.com.biztune.retail.dao;

import au.com.biztune.retail.domain.PriceBand;

/**
 * Created by arash on 18/03/2016.
 */
public interface PriceBandDao {
    /**
     * get Active Price Band per OrguId.
     * @param orguId orguId
     * @return PriceBand
     */
    PriceBand getActivePriceBandPerOrgUnitId (long orguId);

    /**
     * get PriceBand per Id.
     * @param pribId pribId
     * @return PriceBand
     */
    PriceBand getPriceBandPerId (long pribId);
}
