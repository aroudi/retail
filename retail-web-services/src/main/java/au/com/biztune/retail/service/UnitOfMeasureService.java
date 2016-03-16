package au.com.biztune.retail.service;

import au.com.biztune.retail.domain.UnitOfMeasure;

/**
 * Created by akhoshraft on 16/03/2016.
 */
public interface UnitOfMeasureService {
    /**
     * get unit of measure by code.
     * @param  code code
     * @return UnitOfMeasure
     */
    UnitOfMeasure getUnomByCode(String code);
}
