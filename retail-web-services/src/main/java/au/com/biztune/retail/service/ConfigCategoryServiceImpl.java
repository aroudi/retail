package au.com.biztune.retail.service;

import au.com.biztune.retail.dao.ConfigCategoryDao;
import au.com.biztune.retail.domain.ConfigCategory;
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

}
