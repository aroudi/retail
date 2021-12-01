package au.com.biztune.retail.onlineorder.rest;

import au.com.biztune.retail.domain.Company;
import au.com.biztune.retail.onlineorder.service.CompanyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * rest controller providing company info.
 */
@CrossOrigin
@RestController
public class CompanyController {
    @Autowired
    private CompanyService companyService;
    private final Logger logger = LoggerFactory.getLogger(CompanyController.class);

    /**
     * get all companies.
     * @return list of companies
     */
    @GetMapping("/companyList")
    public List<Company> getAllCompany() {
        return companyService.getAllCompanies();
    }
}
