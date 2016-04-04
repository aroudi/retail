package au.com.biztune.retail.service;

import au.com.biztune.retail.dao.*;
import au.com.biztune.retail.domain.*;
import au.com.biztune.retail.generated.BillOfQuantity;
import au.com.biztune.retail.response.CommonResponse;
import au.com.biztune.retail.session.SessionState;
import au.com.biztune.retail.upload.BillOfQuantityUploader;
import au.com.biztune.retail.util.DateUtil;
import au.com.biztune.retail.util.IdBConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * Created by arash on 4/04/2016.
 */
@Component
public class BillOfQuantityService {

    @Autowired
    private SessionState sessionState;
    private final Logger logger = LoggerFactory.getLogger(ConfigCategoryServiceImpl.class);
    @Autowired
    private BillOfQuantityUploader billOfQuantityUploader;
    @Autowired
    private BillOfQuantityDao billOfQuantityDao;
    @Autowired
    private BoqDetailDao boqDetailDao;
    @Autowired
    private ProjectDao projectDao;
    @Autowired
    private CustomerDao customerDao;
    @Autowired
    private SupplierDao supplierDao;
    @Autowired
    private ConfigCategoryDao configCategoryDao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private TaxRuleDao taxRuleDao;
    @Autowired
    private UnitOfMeasureDao unitOfMeasureDao;
    @Autowired
    private LegalTenderDao legalTenderDao;
    @Autowired
    private SuppProdPriceDao suppProdPriceDao;
    @Autowired
    private PriceBandDao priceBandDao;
    @Autowired
    private PriceDao priceDao;
    /**
     * upload Bill Of Quantity.
     * @param uploadedInputStream uploadedInputStream
     * @return CommonResponse
     */
    public CommonResponse uploadBillOfQuantity(InputStream uploadedInputStream) {
        final CommonResponse response = new CommonResponse();
        try {
            final BillOfQuantity billOfQuantity = billOfQuantityUploader.uploadBillOfQuantityFromInputStream(uploadedInputStream);
            if (billOfQuantity == null) {
                response.setStatus(IdBConstant.RESULT_FAILURE);
                response.setMessage("Not able to process input stream. please refer to the log");
                return response;
            }
            importBillOfQuantity(billOfQuantity);
            return response;

        } catch (Exception e) {
            response.setStatus(IdBConstant.RESULT_FAILURE);
            response.setMessage(response.getMessage());
            return response;
        }
    }

    /**
     * import bill of quantity.
     * @param billOfQuantity billOfQuantity
     */
    public void importBillOfQuantity(BillOfQuantity billOfQuantity) {
        // import BillOfQuantityHeader
        final List<Object> billOfQuantityObjects = billOfQuantity.getHeaderOrProjectOrClient();
        if (billOfQuantityObjects == null || billOfQuantityObjects.size() < 1) {
            logger.error("List of internal objects in bill of quantity xml is null.");
            return;
        }
        BillOfQuantity.Header header = null;
        BillOfQuantity.Project inputProject = null;
        BillOfQuantity.Client client = null;
        BillOfQuantity.BillOfQuantities productList = null;
        for (Object obj: billOfQuantityObjects) {
            if (obj instanceof BillOfQuantity.Header) {
                header = (BillOfQuantity.Header) obj;
            }
            if (obj instanceof BillOfQuantity.Client) {
                client = (BillOfQuantity.Client) obj;
            }
            if (obj instanceof BillOfQuantity.Project) {
                inputProject = (BillOfQuantity.Project) obj;
            }
            if (obj instanceof BillOfQuantity.BillOfQuantities) {
                productList = (BillOfQuantity.BillOfQuantities) obj;
            }
        }
        if (header == null) {
            logger.error("header is null.");
            return;
        }
        if (client == null) {
            logger.error("client is null.");
            return;
        }
        if (inputProject == null) {
            logger.error("project is null.");
            return;
        }
        final Customer customer = importClient(client);
        final Project project = importProject(inputProject, customer);
        final au.com.biztune.retail.domain.BillOfQuantity billOfQuantityObj = importBillOfQuantityHeader(header, project);
        importBoqDetail(productList, billOfQuantityObj);

    }

    private void importBoqDetail(BillOfQuantity.BillOfQuantities billOfQuantities, au.com.biztune.retail.domain.BillOfQuantity billOfQuantity) {
        if (billOfQuantities == null) {
            logger.error("List of product is null");
            return;
        }
        Supplier supplier = null;
        boolean prodIsNew = false;
        //delete all BoqDetails ...
        boqDetailDao.deleteBoqDetailPerBoqId(billOfQuantity.getId());
        for (BillOfQuantity.BillOfQuantities.Product importedProduct: billOfQuantities.getProduct()) {
            if (importedProduct == null) {
                continue;
            }
            //get supplier unit of measure
            final UnitOfMeasure unitOfMeasure = unitOfMeasureDao.getUnomByCode(importedProduct.getUnit());
            //import supplier
            supplier = supplierDao.getSupplierByOrgUnitIdAndSuppName(sessionState.getOrgUnit().getId(), importedProduct.getSupplier());
            if (supplier == null) {
                supplier = new Supplier();
                supplier.setSupplierName(importedProduct.getSupplier());
                supplier.setSupplierCode(importedProduct.getSupplier());
                supplier.setCreateDate(new Timestamp(new Date().getTime()));
                supplierDao.insertSupplier(supplier);

                final ConfigCategory supplierStatus = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.CONFIG_SUPLIER_STATUS, IdBConstant.SUPPLIER_STATUS_IMPORTED);
                final SuppOrguLink suppOrguLink = new SuppOrguLink();
                suppOrguLink.setOrguId(sessionState.getOrgUnit().getId());
                suppOrguLink.setSuppId(supplier.getId());
                suppOrguLink.setStatus(supplierStatus);
                supplierDao.insertSuppOrguLink(suppOrguLink);
                supplier.setSuppOrguLink(suppOrguLink);
            }
            //insert product
            Product product = productDao.getProductPerReference(importedProduct.getReference());
            if (product == null) {
                prodIsNew = true;
                product = new Product();
                product.setOrguIdOwning(sessionState.getOrgUnit().getId());
                product.setProdSku(importedProduct.getReference());
                product.setReference(importedProduct.getReference());
                product.setProdName(importedProduct.getDescription());
                product.setProdDesc(importedProduct.getDescription());
                product.setProdOwnBrand(false);
                productDao.insertProduct(product);

                //insert prod orgunit
                final ProdOrguLink prodOrguLink = new ProdOrguLink();
                prodOrguLink.setId(product.getId());
                prodOrguLink.setOrguId(sessionState.getOrgUnit().getId());
                //get product status IMPORTED
                final ConfigCategory productStatus = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.CONFIG_PRODUCT_STATUS, IdBConstant.PRODUCT_STATUS_IMPORTED);
                prodOrguLink.setStatus(productStatus);
                prodOrguLink.setProdId(product.getId());
                productDao.insertProdOrguLink(prodOrguLink);

                //insert product tax rule
                final TaxRule taxRule = taxRuleDao.getTaxRuleByCode(importedProduct.getTaxName());
                if (taxRule != null) {
                    final ProuTxrlLink prouTxrlLink = new ProuTxrlLink();
                    prouTxrlLink.setProuId(prodOrguLink.getId());
                    prouTxrlLink.setTxrlId(taxRule.getId());
                    productDao.insertProdTaxLink(prouTxrlLink);
                }


                //get supplier legal tender
                final LegalTender legalTender = legalTenderDao.getLegalTenderByCode(IdBConstant.LEGAL_TENDER_AU);
                final SuppProdPrice suppProdPrice = new SuppProdPrice();
                suppProdPrice.setSolId(supplier.getSuppOrguLink().getId());
                suppProdPrice.setCatalogueNo(importedProduct.getCatalogueNo());
                suppProdPrice.setUnitOfMeasure(unitOfMeasure);
                suppProdPrice.setUnomQty(importedProduct.getQty());
                suppProdPrice.setLegalTender(legalTender);
                suppProdPrice.setPrice(importedProduct.getCost());
                suppProdPrice.setSprcCreated(new Timestamp(new Date().getTime()));
                suppProdPrice.setSprcModified(new Timestamp(new Date().getTime()));
                suppProdPrice.setProdId(product.getId());
                suppProdPriceDao.insert(suppProdPrice);

                //import product price
                //get price band
                final PriceBand priceBand = priceBandDao.getPriceBandPerCode(IdBConstant.PRICE_BAND_CODE);

                //
                final Price prodPrice = new Price();
                //get price code

                final PriceCode priceCode = priceDao.getProductPriceCodePerCode(IdBConstant.SELL_PRICE_CODE);


                final Price price1 = new Price();
                price1.setUnomQty(importedProduct.getQty());
                //price1.setMargin(margin);
                price1.setPrcePrice(importedProduct.getSellPrice());
                //price1.setPrceTaxIncluded(taxInclude);
                price1.setPriceBand(priceBand);
                price1.setPriceCode(priceCode);
                price1.setProdId(product.getId());
                price1.setUnitOfMeasure(unitOfMeasure);
                price1.setPrceCreated(new Timestamp(new Date().getTime()));
                price1.setPrceModified(new Timestamp(new Date().getTime()));
                price1.setPrceFromDate(new Timestamp(new Date().getTime()));
                price1.setPrceToDate(new Timestamp(new Date().getTime()));
                price1.setPrceSetCentral(false);
                priceDao.insert(price1);
            }
            final BoqDetail boqDetail = new BoqDetail();
            boqDetail.setBillOfQuantity(billOfQuantity);
            boqDetail.setCost(importedProduct.getCost());
            boqDetail.setProdIsNew(prodIsNew);
            boqDetail.setProduct(product);
            boqDetail.setQuantity(importedProduct.getQty());
            boqDetail.setSellPrice(importedProduct.getSellPrice());
            boqDetail.setUnitOfMeasure(unitOfMeasure);
            boqDetailDao.insert(boqDetail);
        }
    }

    /**
     * import BillOfQuantity Header.
     * @param header header
     */
    private au.com.biztune.retail.domain.BillOfQuantity importBillOfQuantityHeader(BillOfQuantity.Header header, Project project) {
        if (header == null) {
            return null;
        }
        au.com.biztune.retail.domain.BillOfQuantity billOfQuantity = billOfQuantityDao.getBillOfQuantityByName(header.getName());
        if (billOfQuantity == null) {
            billOfQuantity = new au.com.biztune.retail.domain.BillOfQuantity();
            billOfQuantity.setProject(project);
            billOfQuantity.setBoqName(header.getName());
            billOfQuantity.setDateCreated(new java.sql.Timestamp(new Date().getTime()));
            billOfQuantity.setNote(header.getNote());
            billOfQuantity.setOrderNo(header.getOrderNumber());
            billOfQuantityDao.insert(billOfQuantity);
        }
        return billOfQuantity;
    }

    /**
     * import project.
     * @param inputProject inputProject
     * @return Project
     */
    private Project importProject(BillOfQuantity.Project inputProject, Customer customer) {
        if (inputProject == null) {
            logger.error("Project is null");
            return  null;
        }
        //search for project. if not found import it.
        Project project = projectDao.getProjectByCode(inputProject.getProjectNo());
        if (project != null) {
            return project;
        } else {
            project = new Project();
            project.setCustomer(customer);
            project.setProjectCode(inputProject.getProjectNo());
            project.setProjectName(inputProject.getProjectName());
            project.setProjectAddress(inputProject.getProjectAddress());
            project.setQuoteNo(inputProject.getQuoteNo());
            project.setReferenceNo(inputProject.getReferenceNo());
            try {
                //
                project.setDateRequired(DateUtil.stringToDate(inputProject.getDateRequired(), "yyyy-MM-dd"));

            } catch (Exception e) {
                logger.error("Error in formating the date ", e);
            }

            try {
                //
                project.setTotalCost(inputProject.getTotalCost());
                project.setTotalPrice(inputProject.getTotalPrice());

            } catch (Exception e) {
                logger.error("Error in formating the date ", e);
            }
            projectDao.insert(project);
            return project;
        }
    }

    /**
     * import client.
     * @param client client
     * @return Customer.
     */
    public Customer importClient(BillOfQuantity.Client client) {
        if (client == null) {
            logger.error("Client is null");
            return  null;
        }
        Customer customer = customerDao.getCustomerByCompanyName(client.getClientName());
        if (customer == null) {
            customer = new Customer();
            customer.setCustomerType(IdBConstant.CUSTOMER_TYPE_COMPANY);
            customer.setAddress(client.getAdd1());
            customer.setSurName(client.getContactName());
            customerDao.insert(customer);
        }
        return customer;
    }
}