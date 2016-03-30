package au.com.biztune.retail.service;

import au.com.biztune.retail.dao.UnitOfMeasureDao;
import au.com.biztune.retail.domain.UnitOfMeasure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by akhoshraft on 16/03/2016.
 */

@Component
public class UnitOfMeasureServiceImpl implements UnitOfMeasureService {
    private final Logger logger = LoggerFactory.getLogger(UnitOfMeasureServiceImpl.class);

    @Autowired
    private UnitOfMeasureDao unitOfMeasureDao;

    /**
     * get unit of measure by code.
     * @param  code code
     * @return UnitOfMeasure
     */
    public UnitOfMeasure getUnomByCode(String code) {
        try {
            return unitOfMeasureDao.getUnomByCode(code);
        } catch (Exception e) {
            logger.error("Error in getting unit of measure: ", e);
            return null;
        }
    }

    /**
     * get all unit of measure.
     * @return LIst of UnitOfMeasure
     */
    public List<UnitOfMeasure> getAllUnom() {
        try {
            return unitOfMeasureDao.getAllUnom();
        } catch (Exception e) {
            logger.error("Error in getting list of unit of measure: ", e);
            return null;
        }
    }

}
