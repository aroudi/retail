package au.com.biztune.retail.service;

import au.com.biztune.retail.dao.TaxRuleDao;
import au.com.biztune.retail.domain.TaxRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by arash on 30/03/2016.
 */

@Component
public class TaxRuleServiceImpl implements TaxRuleService {
    private final Logger logger = LoggerFactory.getLogger(TaxRuleServiceImpl.class);

    @Autowired
    private TaxRuleDao taxRuleDao;

    /**
     * get all tax rules.
     * @return LIst of Tax Rules
     */
    public List<TaxRule> getAllTaxRules() {
        try {
            return taxRuleDao.getAllTaxRules();
        } catch (Exception e) {
            logger.error("Error in getting list of taxRules ", e);
            return null;
        }
    }
}
