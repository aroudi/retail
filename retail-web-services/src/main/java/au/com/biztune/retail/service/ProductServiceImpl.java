package au.com.biztune.retail.service;

import au.com.biztune.retail.dao.ConfigCategoryDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by arash on 23/03/2016.
 */
public class ProductServiceImpl {
    private final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Autowired
    private ConfigCategoryDao categoryDao;
}
