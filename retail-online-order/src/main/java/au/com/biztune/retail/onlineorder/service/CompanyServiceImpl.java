package au.com.biztune.retail.onlineorder.service;

import au.com.biztune.retail.dao.CompanyDao;
import au.com.biztune.retail.domain.Company;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * service class for company.
 */
@Service
public class CompanyServiceImpl implements CompanyService {
    private final Logger logger = LoggerFactory.getLogger(CompanyServiceImpl.class);

    @Autowired
    private CompanyDao companyDao;
    /**
     * get list of companies.
     * @return list of companies
     */
    public List<Company> getAllCompanies() {
        try {
           return  companyDao.getAllCompanies();
        } catch (Exception e) {
            logger.error("Exception in getting list of companies from db", e);
            return null;
        }
    }
}
