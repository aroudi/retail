package au.com.biztune.retail.service;

import au.com.biztune.retail.dao.ContactDao;
import au.com.biztune.retail.dao.SupplierDao;
import au.com.biztune.retail.domain.SuppOrguLink;
import au.com.biztune.retail.domain.Supplier;
import au.com.biztune.retail.response.CommonResponse;
import au.com.biztune.retail.session.SessionState;
import au.com.biztune.retail.util.IdBConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * Created by arash on 22/02/2016.
 */

@Component
public class SupplierServiceImpl implements SupplierService {

    private final Logger logger = LoggerFactory.getLogger(SupplierServiceImpl.class);

    @Autowired
    private SessionState sessionState;

    @Autowired
    private SupplierDao supplierDao;

    @Autowired
    private ContactDao contactDao;

    /**
     * return all Suppliers.
     * @return list of Supplier
     */
    public List<Supplier> getAllSuppliers() {
        try {
            return supplierDao.getAllSuppliersByOrgUnitId(sessionState.getOrgUnit().getId());
        } catch (Exception e) {
            logger.error("Error in getting supplier list: ", e);
            return null;
        }
    }

    /**
     * add new supplier.
     * @param supplier supplier
     * @return CommonResponse
     */
    public CommonResponse addSupplier(Supplier supplier) {
        final CommonResponse response = new CommonResponse();
        try {
            response.setStatus(IdBConstant.RESULT_SUCCESS);

            if (supplier == null) {
                response.setStatus(IdBConstant.RESULT_FAILURE);
                response.setMessage("supplier object is null");
                return response;
            }
            final boolean isNew = supplier.getId() > 0 ? false : true;
            final Timestamp timestamp = new Timestamp(new Date().getTime());
            supplier.setLastModifiedDate(timestamp);
            if (isNew) {
                //check if customer code is already in there
                final Supplier supplier1 = supplierDao.getSupplierByOrgUnitIdAndSuppCode(sessionState.getOrgUnit().getId(), supplier.getSupplierCode());
                if (supplier1 != null) {
                    response.setStatus(IdBConstant.RESULT_FAILURE);
                    response.setMessage("supplier with code " + supplier.getSupplierCode() + " already exists");
                    return response;
                }
                supplier.setCreateDate(timestamp);
                if (supplier.getClass() != null) {
                    contactDao.insert(supplier.getContact());
                }
                supplierDao.insertSupplier(supplier);
                //insert suppOrgUnit
                SuppOrguLink suppOrguLink = supplier.getSuppOrguLink();
                if (suppOrguLink == null) {
                    suppOrguLink = new SuppOrguLink();
                }
                suppOrguLink.setOrguId(sessionState.getOrgUnit().getId());
                suppOrguLink.setSuppId(supplier.getId());
                suppOrguLink.setStatus(supplier.getSupplierStatus());
                supplierDao.insertSuppOrguLink(suppOrguLink);
            } else {
                if (supplier.getContact() != null) {
                    contactDao.update(supplier.getContact());
                }
                supplierDao.updateSupplier(supplier);
                SuppOrguLink suppOrguLink = supplier.getSuppOrguLink();
                if (suppOrguLink == null) {
                    suppOrguLink = new SuppOrguLink();
                }
                suppOrguLink.setOrguId(sessionState.getOrgUnit().getId());
                suppOrguLink.setSuppId(supplier.getId());
                suppOrguLink.setStatus(supplier.getSupplierStatus());
                supplierDao.updateSuppOrguLink(suppOrguLink);
            }
            return response;
        } catch (Exception e) {
            logger.error("Exception in saving Supplier: ", e);
            response.setStatus(IdBConstant.RESULT_FAILURE);
            response.setMessage("Exception in saving Supplier");
            return response;
        }
    }

    /**
     * get supplier by Id.
     * @param id id
     * @return Supplier
     */
    public Supplier getSupplierById(long id) {
        try {
            final Supplier supplier =  supplierDao.getSupplierByOrgUnitIdAndSuppId(sessionState.getOrgUnit().getId(), id);
            supplier.setSupplierStatus(supplier.getSuppOrguLink().getStatus());
            return supplier;
        } catch (Exception e) {
            logger.error("Error in getting supplier with id: " + id, e);
            return null;
        }
    }

    /**
     * get supplier by Code.
     * @param code code
     * @return Supplier
     */
    public Supplier getSupplierByCode(String code) {
        try {
            final Supplier supplier = supplierDao.getSupplierByOrgUnitIdAndSuppCode(sessionState.getOrgUnit().getId(), code);
            supplier.setSupplierStatus(supplier.getSuppOrguLink().getStatus());
            return supplier;
        } catch (Exception e) {
            logger.error("Error in getting supplier with code: " + code, e);
            return null;
        }
    }
}
