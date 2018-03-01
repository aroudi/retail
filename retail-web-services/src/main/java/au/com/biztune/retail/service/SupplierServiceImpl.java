package au.com.biztune.retail.service;

import au.com.biztune.retail.dao.ConfigCategoryDao;
import au.com.biztune.retail.dao.ContactDao;
import au.com.biztune.retail.dao.SuppProdPriceDao;
import au.com.biztune.retail.dao.SupplierDao;
import au.com.biztune.retail.domain.*;
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

    @Autowired
    private SuppProdPriceDao suppProdPriceDao;

    @Autowired
    private ConfigCategoryDao configCategoryDao;
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
                Supplier supplier1 = getSupplierByCode(supplier.getSupplierCode());
                if (supplier1 != null) {
                    response.setStatus(IdBConstant.RESULT_FAILURE);
                    response.setMessage("supplier with code " + supplier.getSupplierCode() + " already exists");
                    return response;
                }
                supplier1 = getSupplierByName(supplier.getSupplierName());
                if (supplier1 != null) {
                    response.setStatus(IdBConstant.RESULT_FAILURE);
                    response.setMessage("supplier with name " + supplier.getSupplierName() + " already exists");
                    return response;
                }
                supplier.setCreateDate(timestamp);
                if (supplier.getContact() != null) {
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
                    final Contact existingContact = contactDao.getContactById(supplier.getContact().getId());
                    if (existingContact != null) {
                        contactDao.update(supplier.getContact());
                    } else {
                        contactDao.insert(supplier.getContact());
                    }
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
            final List<Supplier> supplierList = supplierDao.getSupplierByOrgUnitIdAndSuppCode(sessionState.getOrgUnit().getId(), code);
            final Supplier supplier = supplierList.get(0);
            supplier.setSupplierStatus(supplier.getSuppOrguLink().getStatus());
            return supplier;
        } catch (Exception e) {
            logger.error("Error in getting supplier with code: " + code, e);
            return null;
        }
    }

    /**
     * get supplier by name.
     * @param name name
     * @return Supplier
     */
    public Supplier getSupplierByName(String name) {
        try {
            final List<Supplier> supplierList = supplierDao.getSupplierByOrgUnitIdAndSuppName(sessionState.getOrgUnit().getId(), name);
            final Supplier supplier = supplierList.get(0);
            return supplier;
        } catch (Exception e) {
            logger.error("Error in getting supplier with code: " + name, e);
            return null;
        }
    }

    /**
     * return all Supplier's products.
     * @param supplierId supplier id.
     * @return list of Supplier's products
     */
    public List<SuppProdPrice> getSupplierProducts(long supplierId) {
        try {
            return suppProdPriceDao.getAllSupplierProducts(sessionState.getOrgUnit().getId(), supplierId);
        } catch (Exception e) {
            logger.error("Error in getting supplier's product list: ", e);
            return null;
        }
    }

    /**
     * return all Supplier's products with price.
     * @param supplierId supplier id.
     * @return list of Supplier's products
     */
    public List<SuppProdPrice> getSupplierProductsWithPrice(long supplierId) {
        try {
            return suppProdPriceDao.getAllProductPurchaseItemsWithRrpPerOrgUnitIdAndSuppId(sessionState.getOrgUnit().getId(), supplierId);
        } catch (Exception e) {
            logger.error("Error in getting supplier's product with price list: ", e);
            return null;
        }
    }

    /**
     * import supplier.
     * @param supplierCode supplierCode
     * @param supplierName supplierName
     * @param leadTime leadTime
     * @param stockCover stockCover
     * @param minOrderValue minOrderValue
     * @param creditLimit creditLimit
     * @param maxAdvOrder maxAdvOrder
     * @param solOrderingCode solOrderingCode
     * @param solMinPoQty solMinPoQty
     * @param solAccCode solAccCode
     * @return Supplier.
     */
    public Supplier addSupplier(String supplierCode
            , String supplierName
            , int leadTime
            , int stockCover
            , double minOrderValue
            , double creditLimit
            , int maxAdvOrder
            , String solOrderingCode
            , double solMinPoQty
            , String solAccCode
    )
    {
        try {
            final Timestamp currentTime = new Timestamp(new Date().getTime());
            Supplier supplier = getSupplierByName(supplierName);
            if (supplier == null) {
                supplier = new Supplier();
                supplier.setSupplierName(supplierName);
                supplier.setSupplierCode(supplierCode);
                supplier.setCreateDate(currentTime);
                supplier.setLeadTime(leadTime);
                supplier.setStockCover(stockCover);
                supplier.setMinOrderValue(minOrderValue);
                supplier.setCreditLimit(creditLimit);
                supplier.setMaxAdvOrder(maxAdvOrder);
                supplierDao.insertSupplier(supplier);

                final ConfigCategory supplierStatus = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.CONFIG_SUPLIER_STATUS, IdBConstant.SUPPLIER_STATUS_IMPORTED);
                final SuppOrguLink suppOrguLink = new SuppOrguLink();
                suppOrguLink.setOrguId(sessionState.getOrgUnit().getId());
                suppOrguLink.setSuppId(supplier.getId());
                suppOrguLink.setStatus(supplierStatus);
                suppOrguLink.setSolOrderingCode(solOrderingCode);
                suppOrguLink.setSolMinPoQty(solMinPoQty);
                suppOrguLink.setSolAccCode(solAccCode);
                supplierDao.insertSuppOrguLink(suppOrguLink);
                supplier.setSuppOrguLink(suppOrguLink);
            }
            return supplier;
        } catch (Exception e) {
            logger.error("Error in importing supplier", e);
            return null;
        }
    }

    /**
     * delete a supplier logically. set the deleted flag to true and add 'DELETED + TIMESTAMP' to some fields.
     * @param supplierIdList supplier Id List.
     * @return commonResponse.
     */
    public CommonResponse logicalDeleteSupplier(List<Long> supplierIdList) {
        final CommonResponse response = new CommonResponse();
        try {
            final Timestamp currentDate = new Timestamp(new Date().getTime());
            response.setStatus(IdBConstant.RESULT_SUCCESS);
            if (supplierIdList == null || supplierIdList.size() < 1) {
                response.setStatus(IdBConstant.RESULT_FAILURE);
                response.setMessage("empty list");
                return response;
            }
            Supplier supplier = null;
            String newValue;
            for (Long supplierId : supplierIdList) {
                supplier = supplierDao.getSupplierByOrgUnitIdAndSuppId(sessionState.getOrgUnit().getId(), supplierId);
                if (supplier == null) {
                    continue;
                }
                if (supplier.getSupplierName() != null) {
                    newValue = supplier.getSupplierName().trim() + "-DELETED-" + currentDate;
                    if (newValue.length() > 100) {
                        newValue = newValue.substring(0, 99);
                    }
                    supplier.setSupplierName(newValue);
                }
                if (supplier.getSupplierCode() != null) {
                    newValue = supplier.getSupplierCode().trim() + "-DELETED-" + currentDate;
                    if (newValue.length() > 100) {
                        newValue = newValue.substring(0, 99);
                    }
                    supplier.setSupplierCode(newValue);
                }
                supplier.setDeleted(true);
                supplierDao.updateSupplier(supplier);
                suppProdPriceDao.markSupplierAsDeletedPerSolId(supplier.getSuppOrguLink().getId());
            }
            return response;
        } catch (Exception e) {
            logger.error("Exception in deleting supplier: ", e);
            response.setStatus(IdBConstant.RESULT_FAILURE);
            response.setMessage("Exception in deleting Supplier");
            return response;
        }
    }

}
