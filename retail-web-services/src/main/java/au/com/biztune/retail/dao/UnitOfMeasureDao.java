package au.com.biztune.retail.dao;

import au.com.biztune.retail.domain.UnitOfMeasure;

import java.util.List;

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
    /**
     * get the supplier by id.
     * @param id id
     * @return UnitOfMeasure
     */
    UnitOfMeasure getUnomById(long id);


    /**
     * get all unit of measures.
     * @return List of UnitOfMeasure
     */
    List<UnitOfMeasure> getAllUnom();

    /**
     * insert unit of measure object.
     * @param unitOfMeasure unitOfMeasure
     */
    void insert(UnitOfMeasure unitOfMeasure);
}
