package au.com.biztune.retail.dao;

import au.com.biztune.retail.domain.Company;
import au.com.biztune.retail.domain.Contact;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by arash on 22/02/2016.
 */
@Mapper
public interface CompanyDao {

    /**
     * get list of all companies.
     * @return list of companies.
     */
    List<Company> getAllCompanies();

    /**
     * get the company by id.
     * @param id id
     * @return Company
     */
    Company getCompanyById(long id);

    /**
     * get All company contacts by company Id.
     * @param id company id.
     * @return all contacts of company
     */
    List<Contact> getAllCompanyContactsByCompanyId(long id);

}
