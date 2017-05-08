package au.com.biztune.retail.service;

import au.com.biztune.retail.domain.TaxLegVariance;
import au.com.biztune.retail.domain.TaxRule;

import java.util.List;

/**
 * Created by arash on 30/03/2016.
 */
public interface TaxRuleService {
    /**
     * get all tax rules.
     * @return LIst of Tax Rules
     */
    List<TaxRule> getAllTaxRules();

    /**
     * get all taxLegVariance.
     * @return LIst of TaxLegVariance
     */
    List<TaxLegVariance> getAllActiveTaxLegVariance();

    /**
     * get tax leg variance by code.
     * @param taxCode taxCode
     * @return taxLegVariance
     */
    TaxLegVariance getTaxLegVarianceByCode(String taxCode);
}
