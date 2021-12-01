package au.com.biztune.retail.onlineorder.service;

import au.com.biztune.retail.domain.Company;
import java.util.List;

/**
 * service interface for company.
 */
public interface CompanyService {
    /**
     * get list of companies.
     * @return list of companies
     */
    List<Company> getAllCompanies();
}
