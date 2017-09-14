package au.com.biztune.retail.service;

import au.com.biztune.retail.dao.ConfigCategoryDao;
import au.com.biztune.retail.dao.CustomerGradeDao;
import au.com.biztune.retail.domain.ConfigCategory;
import au.com.biztune.retail.domain.ConfigType;
import au.com.biztune.retail.domain.CustomerGrade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by arash on 10/08/2015.
 */
@Component
public class ConfigCategoryServiceImpl implements ConfigCategoryService {
    private final Logger logger = LoggerFactory.getLogger(ConfigCategoryServiceImpl.class);

    @Autowired
    private ConfigCategoryDao categoryDao;

    @Autowired
    private CustomerGradeDao customerGradeDao;

    /**
     * return all categories for specific type.
     * @param typeConstant typeConstant
     * @return List of Categories for that specific type.
     */

    public List<ConfigCategory> getCategoriesOfType(String typeConstant) {
        try {
            return categoryDao.getCategoriesOfType(typeConstant);
        } catch (Exception e) {
            logger.error("Exception in retrieving list of categories: ", e);
            return null;
        }
    }

    /**
     * get category of type and code.
     * @param type type
     * @param code code
     * @return ConfigCategory
     */
    public ConfigCategory getCategoryOfTypeAndCode(String type, String code) {
        try {
            return categoryDao.getCategoryOfTypeAndCode(type, code);
        } catch (Exception e) {
            logger.error("Exception in retrieving category: ", e);
            return null;
        }
    }

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
    public ConfigCategory addConfigCategory(String configTypeCode
            , String categoryCode
            , String displayName
            , String description
            , int categoryOrder
            , String color)
    {
        try {
            if (categoryCode == null || displayName == null) {
                return null;
            }
            final ConfigType configType = categoryDao.getConfigTypeByCode(configTypeCode);
            if (configType == null) {
                logger.error("Config Type not found.");
                return null;
            }
            ConfigCategory configCategory = categoryDao.getCategoryOfTypeAndCode(configTypeCode, categoryCode);
            if (configCategory == null) {
                configCategory = new ConfigCategory();
                configCategory.setCategoryCode(categoryCode);
                configCategory.setConfigTypeId((int) configType.getId());
                configCategory.setColor(color);
                configCategory.setCategoryOrder(categoryOrder);
                configCategory.setDisplayName(displayName);
                configCategory.setDescription(description);
                categoryDao.insertConfigCategory(configCategory);
            }
            return configCategory;
        } catch (Exception e) {
            logger.error("Exception in creating config category: ", e);
            return null;
        }

    }

    /**
     * update customerGrade.
     * @param customerGrade customerGrade
     */
    public void updateCustomerGrade(CustomerGrade customerGrade) {
        try {
            customerGradeDao.updateCustomerGrade(customerGrade);
        } catch (Exception e) {
            logger.error("Exception in updating the customer grade");
        }
    }

    /**
     * get customer grade by code.
     * @param gradeCode gradeCode
     * @return Customer grade.
     */
    public CustomerGrade getCustomerGradeByCode(String gradeCode) {
        try {
            return customerGradeDao.getCustomerGradeByCode(gradeCode);
        } catch (Exception e) {
            logger.error("Exception in getting customer grade by code");
            return null;
        }
    }
}
