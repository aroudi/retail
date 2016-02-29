package au.com.biztune.retail.dao;

import au.com.biztune.retail.domain.ConfigCategory;

import java.util.List;

/**
 * Created by akhoshraft on 29/02/2016.
 */
public interface ConfigCategoryDao {
    /**
     * get all categories for specific type.
     * @param typeConstant type name.
     * @return list of categories for specific type
     */
    List getCategoriesOfType(String typeConstant);

    /**
     * get specific category by type and name.
     * @param typeConstant typeConstant
     * @param categoryCode categoryCode
     * @return category
     */
    ConfigCategory getCategoriesOfTypeAndCode(String typeConstant, String categoryCode);

    /**
     * get specific category by id.
     * @param categoryId categoryId
     * @return category
     */
    ConfigCategory getCategoryById(long categoryId);

}
