package au.com.biztune.retail.dao;

import au.com.biztune.retail.domain.SuppOrguLink;
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
    void insertSupplier(Supplier supplier);

    /**
     * Add new supplierOrguLink.
     * @param suppOrguLink suppOrguLink
     */
    void insertSuppOrguLink(SuppOrguLink suppOrguLink);

    /**
     * update existing supplier per id.
     * @param supplier supplier
     */
    void updateSupplier(Supplier supplier);

    /**
     * update existing supplier ORGU LINK per id.
     * @param suppOrguLink suppOrguLink
     */
    void updateSuppOrguLink(SuppOrguLink suppOrguLink);


    /**
     * get list of all suppliers per orgunit id.
     * @param orgUnitId orgUnitId
     * @return Supplier.
     */
    List<Supplier> getAllSuppliersByOrgUnitId(long orgUnitId);

    /**
     * get list of all suppliers per orgunit id and code.
     * @param orgUnitId orgUnitId
     * @param suppCode suppCode
     * @return Supplier.
     */
    List<Supplier> getSupplierByOrgUnitIdAndSuppCode(long orgUnitId, String suppCode);


    /**
     * get list of all suppliers per orgunit id and suppid.
     * @param orgUnitId orgUnitId
     * @param suppId suppId
     * @return Supplier.
     */
    Supplier getSupplierByOrgUnitIdAndSuppId(long orgUnitId, long suppId);

    /**
     * get list of all suppliers per orgunit id and suppid.
     * @param suppId suppId
     * @return Supplier.
     */
    Supplier getSupplierBySuppId(long suppId);


    /**
     * get list of all suppliers per orgunit id and code.
     * @param orgUnitId orgUnitId
     * @param suppName suppName
     * @return Supplier.
     */
    List<Supplier> getSupplierByOrgUnitIdAndSuppName(long orgUnitId, String suppName);
    /**
     * get supplier by solId.
     * @param solId solId
     * @return Supplier.
     */
    Supplier getSupplierBySolId(long solId);

    /**
     * get list of supplier for export.
     * @return List of supplier
     */
    List<Supplier> getSupplierListForAccExport();

    /**
     * get supplier full object(including contact).
     * @return List of Suppliers
     */
    List<Supplier> getSupplierFullBySuppId();

    /**
     * update supplier export flag.
     * @param suppId suppId
     */
    void setSupplierExportFlagPerSuppId(long suppId);

    /**
     * get no of supplier to be exported.
     * @return no of supplier to be exported.
     */
    int getNoOfSupplierToBeExported();

    /**
     * get default supplier for specific product based on orguId and prodId.
     * TODO: add SPRC_PREFER_BUY in sql
     * @param orguId Organisation Unit Id.
     * @param prodId Product id
     * @return Supplier
     */
    Supplier getDefaultSupplierOfProductPerOrguIdAndProdId(long orguId, long prodId);
}
