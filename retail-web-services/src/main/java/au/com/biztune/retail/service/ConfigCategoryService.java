// Sydney Trains 2015
package au.com.biztune.retail.service;

import au.com.biztune.retail.domain.ConfigCategory;

import java.util.List;

/**
 * Created by arash on 10/08/2015.
 */
public interface ConfigCategoryService {
    /**
     * get All categories for specific type.
     * @param typeName typeName
     * @return list of categories.
     */
    List<ConfigCategory> getCategoriesOfType(String typeName);
    /**
     * get category of type and code.
     * @param type type
     * @param code code
     * @return ConfigCategory
     */
    ConfigCategory getCategoryOfTypeAndCode(String type, String code);
}
