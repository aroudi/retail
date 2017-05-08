package au.com.biztune.retail.service;

import au.com.biztune.retail.dao.TaxRuleDao;
import au.com.biztune.retail.domain.TaxLegVariance;
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

    /**
     * get all taxLegVariance.
     * @return LIst of TaxLegVariance
     */
    public List<TaxLegVariance> getAllActiveTaxLegVariance() {
        try {
            return taxRuleDao.getAllActiveTaxLegVariance();
        } catch (Exception e) {
            logger.error("Error in getting list of tax leg variance ", e);
            return null;
        }
    }

    /**
     * get tax leg variance by code.
     * @param taxCode taxCode
     * @return taxLegVariance
     */
    public TaxLegVariance getTaxLegVarianceByCode(String taxCode) {
        try {
            return taxRuleDao.getTaxLegVarianceByCode(taxCode);
        } catch (Exception e) {
            logger.error("Error in getting tax leg variance by code ", e);
            return null;
        }
    }

}
