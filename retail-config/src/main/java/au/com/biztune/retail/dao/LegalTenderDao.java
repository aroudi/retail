package au.com.biztune.retail.dao;

import au.com.biztune.retail.domain.LegalTender;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by arash on 22/03/2016.
 */
@Mapper
public interface LegalTenderDao {

    /**
     * get legal tender by code.
     * @param code code
     * @return Legal Tender
     */
    LegalTender getLegalTenderByCode(String code);

    /**
     * get legal tender by id.
     * @param id id
     * @return Legal Tender
     */

    LegalTender getLegalTenderById(long id);

    /**
     * get all legal tenders.
     * @return List of Legal Tender
     */
    List<LegalTender> getAllLegalTenders();
}
