package au.com.biztune.retail.service;

import au.com.biztune.retail.dao.UnitOfMeasureDao;
import au.com.biztune.retail.domain.UnitOfMeasure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Date;
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

    /**
     * import Unit Of Measure Code. check if unom code exists otherwise create new one.
     * @param unomCode unomCode
     * @param unomDesc unomDesc
     * @return UnitOfMeasure
     */
    public UnitOfMeasure addUnitOfMeasure(String unomCode, String unomDesc) {
        try {
            final Timestamp currentTime = new Timestamp(new Date().getTime());
            //chec if unit of measure exists. if so return it otherwise create new one.
            UnitOfMeasure unitOfMeasure = unitOfMeasureDao.getUnomByCode(unomCode);
            if (unitOfMeasure == null) {
                unitOfMeasure = new UnitOfMeasure();
                unitOfMeasure.setUnomCode(unomCode);
                unitOfMeasure.setUnomDesc(unomDesc);
                unitOfMeasure.setUnomCreated(currentTime);
                unitOfMeasure.setUnomModified(currentTime);
                unitOfMeasureDao.insert(unitOfMeasure);
            }
            return unitOfMeasure;
        } catch (Exception e) {
            logger.error("Error in importing unit of measure", e);
            return null;
        }

    }
}
