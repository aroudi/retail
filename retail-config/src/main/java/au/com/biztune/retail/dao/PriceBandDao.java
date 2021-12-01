package au.com.biztune.retail.dao;

import au.com.biztune.retail.domain.PriceBand;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created by arash on 18/03/2016.
 */
@Mapper
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


    /**
     * get PriceBand per Code.
     * @param code code
     * @return PriceBand
     */
    PriceBand getPriceBandPerCode (String code);
}
