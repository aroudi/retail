package au.com.biztune.retail.service;

import au.com.biztune.retail.domain.UnitOfMeasure;

import java.util.List;

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

    /**
     * get all unit of measure.
     * @return LIst of UnitOfMeasure
     */
    List<UnitOfMeasure> getAllUnom();

    /**
     * import Unit Of Measure Code. check if unom code exists otherwise create new one.
     * @param unomCode unomCode
     * @param unomDesc unomDesc
     * @return UnitOfMeasure
     */
    UnitOfMeasure addUnitOfMeasure(String unomCode, String unomDesc);
}
