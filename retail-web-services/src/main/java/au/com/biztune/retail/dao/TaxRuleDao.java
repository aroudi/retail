package au.com.biztune.retail.dao;

import au.com.biztune.retail.domain.TaxLegVariance;
import au.com.biztune.retail.domain.TaxRule;
import au.com.biztune.retail.domain.TaxRuleName;

import java.util.List;

/**
 * Created by arash on 18/03/2016.
 */
public interface TaxRuleDao {
    /**
     * get all tax rules from database.
     * @return List of TaxRule
     */
    List<TaxRule> getAllTaxRules();

    /**
     * get tax rule by id.
     * @param txrlId txrlId
     * @return TAX RULE
     */
    TaxRule getTaxRuleById(long txrlId);
    /**
     * get tax rule by Code.
     * @param code code
     * @return TaxRule
     */
    TaxRule getTaxRuleByCode(String code);

    /**
     * get tax rules of specific product.
     * @param prouId prouId
     * @return TaxRule
     */
    List<TaxRule> getTaxRulesOfProduct(long prouId);

    /**
     * get TaxLegVariance By Id.
     * @param id id
     * @return TaxLegVariance.
     */
    TaxLegVariance getTaxLegVarianceById(long id);

    /**
     * get all tax rule name.
     * @return list of tax rule name
     */
    List<TaxRuleName> getAllTaxCodes();
}
