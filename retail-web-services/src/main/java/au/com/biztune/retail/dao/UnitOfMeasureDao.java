package au.com.biztune.retail.dao;

import au.com.biztune.retail.domain.UnitOfMeasure;

/**
 * Created by akhoshraft on 29/02/2016.
 */
public interface UnitOfMeasureDao {

    /**
     * get the supplier by id.
     * @param code code
     * @return UnitOfMeasure
     */
    UnitOfMeasure getUnomByCode(String code);
}
