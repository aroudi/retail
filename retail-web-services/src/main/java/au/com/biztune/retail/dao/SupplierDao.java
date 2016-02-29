package au.com.biztune.retail.dao;

import au.com.biztune.retail.domain.Supplier;

import java.util.List;

/**
 * Created by akhoshraft on 29/02/2016.
 */
public interface SupplierDao {
    /**
     * Add new supplier.
     * @param supplier supplier
     */
    void insert(Supplier supplier);

    /**
     * update existing supplier per id.
     * @param supplier supplier
     */
    void update(Supplier supplier);

    /**
     * get list of all suppliers.
     * @return list of suppliers.
     */
    List<Supplier> getAllSuppliers();

    /**
     * get the supplier by code.
     * @param code code
     * @return Supplier
     */
    Supplier getSupplierByCode(String code);

    /**
     * get the supplier by id.
     * @param id id
     * @return Supplier
     */
    Supplier getSupplierById(long id);

}
