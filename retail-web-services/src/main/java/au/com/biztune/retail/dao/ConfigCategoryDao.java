package au.com.biztune.retail.dao;

import au.com.biztune.retail.domain.ConfigCategory;
import au.com.biztune.retail.domain.ConfigType;

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
    ConfigCategory getCategoryOfTypeAndCode(String typeConstant, String categoryCode);

    /**
     * get specific category by id.
     * @param categoryId categoryId
     * @return category
     */
    ConfigCategory getCategoryById(long categoryId);

    /**
     * insert configCategory.
     * @param configCategory configCategory
     */
    void insertConfigCategory(ConfigCategory configCategory);

    /**
     * get config type by code.
     * @param typeCode typeCode
     * @return ConfigType
     */
    ConfigType getConfigTypeByCode(String typeCode);

    /**
     * get category by type and display name.
     * @param typeConstant typeConstant
     * @param catDisplayName catDisplayName
     * @return ConfigCategory
     */
    List<ConfigCategory> getCategoryOfTypeAndDisplayName(String typeConstant, String catDisplayName);
}
