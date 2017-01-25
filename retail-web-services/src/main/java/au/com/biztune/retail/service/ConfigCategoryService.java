// Sydney Trains 2015
package au.com.biztune.retail.service;

import au.com.biztune.retail.domain.ConfigCategory;
import au.com.biztune.retail.domain.CustomerGrade;

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

    /**
     * Add ConfigCategory.
     * @param configTypeCode configTypeCode
     * @param categoryCode categoryCode
     * @param displayName displayName
     * @param description description
     * @param categoryOrder categoryOrder
     * @param color color
     * @return ConfigCategory
     */
    ConfigCategory addConfigCategory(String configTypeCode
            , String categoryCode
            , String displayName
            , String description
            , int categoryOrder
            , String color);
    /**
     * update customerGrade.
     * @param customerGrade customerGrade
     */
    void updateCustomerGrade(CustomerGrade customerGrade);

    /**
     * get customer grade by code.
     * @param gradeCode gradeCode
     * @return Customer grade.
     */
    CustomerGrade getCustomerGradeByCode(String gradeCode);
}
