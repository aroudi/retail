package au.com.biztune.retail.service;

import au.com.biztune.retail.domain.Supplier;
import au.com.biztune.retail.response.CommonResponse;

import java.util.List;

/**
 * Created by arash on 1/03/2016.
 */
public interface SupplierService {

    /**
     * return all Suppliers.
     * @return list of Supplier
     */
    List<Supplier> getAllSuppliers();

    /**
     * add new supplier.
     * @param supplier supplier
     * @return CommonResponse
     */
    CommonResponse addSupplier(Supplier supplier);
    /**
     * get supplier by Id.
     * @param id id
     * @return Supplier
     */
   Supplier getSupplierById(long id);

    /**
     * get supplier by Code.
     * @param code code
     * @return Supplier
     */
    Supplier getSupplierByCode(String code);
}
