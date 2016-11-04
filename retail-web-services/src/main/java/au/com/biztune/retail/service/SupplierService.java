package au.com.biztune.retail.service;

import au.com.biztune.retail.domain.SuppProdPrice;
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

    /**
     * return all Suppliers.
     * @param supplierId supplier id.
     * @return list of Supplier
     */
    List<SuppProdPrice> getSupplierProducts(long supplierId);

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
    Supplier addSupplier(String supplierCode
            , String supplierName
            , int leadTime
            , int stockCover
            , double minOrderValue
            , double creditLimit
            , int maxAdvOrder
            , String solOrderingCode
            , double solMinPoQty
            , String solAccCode
    );
}
