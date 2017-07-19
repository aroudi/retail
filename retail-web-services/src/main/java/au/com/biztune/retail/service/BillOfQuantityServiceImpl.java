package au.com.biztune.retail.service;

import au.com.biztune.retail.dao.*;
import au.com.biztune.retail.domain.*;
import au.com.biztune.retail.form.BoqSearchForm;
import au.com.biztune.retail.generated.BillOfQuantity;
import au.com.biztune.retail.response.CommonResponse;
import au.com.biztune.retail.session.SessionState;
import au.com.biztune.retail.upload.BillOfQuantityUploader;
import au.com.biztune.retail.util.DateUtil;
import au.com.biztune.retail.util.IdBConstant;
import au.com.biztune.retail.util.SearchClause;
import au.com.biztune.retail.util.SearchClauseBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.core.SecurityContext;
import java.io.InputStream;
import java.security.Principal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by arash on 4/04/2016.
 */
@Component
public class BillOfQuantityServiceImpl implements BillOfQuantityService {

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
    @Autowired
    private PurchaseOrderService purchaseOrderService;
    @Autowired
    private PurchaseOrderDao purchaseOrderDao;
    @Autowired
    private ContactDao contactDao;
    @Autowired
    private PoBoqLinkDao poBoqLinkDao;
    @Autowired
    private StockService stockService;
    private SecurityContext securityContext;

    /**
     * upload Bill Of Quantity.
     * @param uploadedInputStream uploadedInputStream
     * @param securityContext securityContext
     * @return CommonResponse
     */
    public CommonResponse uploadBillOfQuantity (InputStream uploadedInputStream, SecurityContext securityContext) {
        this.securityContext = securityContext;
        final CommonResponse response = new CommonResponse();
        try {
            final BillOfQuantity billOfQuantity = billOfQuantityUploader.uploadBillOfQuantityFromInputStream(uploadedInputStream);
            if (billOfQuantity == null) {
                response.setStatus(IdBConstant.RESULT_FAILURE);
                response.setMessage("Not able to process input stream. please refer to the log");
                return response;
            }
            final long result = importBillOfQuantity(billOfQuantity);
            if (result > 0) {
                response.setStatus(IdBConstant.RESULT_SUCCESS);
                response.setInfo(String.valueOf(result));
            } else {
                response.setStatus(IdBConstant.RESULT_FAILURE);
                response.setMessage("Something happernd in import. please check the log file");
            }
            return response;

        } catch (Exception e) {
            logger.error("Exception in importing BOQ:", e);
            response.setStatus(IdBConstant.RESULT_FAILURE);
            response.setMessage(response.getMessage());
            return response;
        }
    }

    /**
     * import bill of quantity.
     * @param billOfQuantity billOfQuantity
     * @return FAILURE if something went wrong, otherwise will return the billOfQuantity id.
     */
    public long importBillOfQuantity(BillOfQuantity billOfQuantity) {
        // import BillOfQuantityHeader
        final List<Object> billOfQuantityObjects = billOfQuantity.getHeaderOrProjectOrClient();
        if (billOfQuantityObjects == null || billOfQuantityObjects.size() < 1) {
            logger.error("List of internal objects in bill of quantity xml is null.");
            return IdBConstant.RESULT_FAILURE;
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
            return IdBConstant.RESULT_FAILURE;
        }
        if (client == null) {
            logger.error("client is null.");
            return IdBConstant.RESULT_FAILURE;
        }
        if (inputProject == null) {
            logger.error("project is null.");
            return IdBConstant.RESULT_FAILURE;
        }
        final Customer customer = importClient(client);
        final Project project = importProject(inputProject, customer);
        final au.com.biztune.retail.domain.BillOfQuantity billOfQuantityObj = importBillOfQuantityHeader(header, project, productList);
        importBoqDetail(productList, billOfQuantityObj);
        return billOfQuantityObj.getId();
    }

    private void importBoqDetail(BillOfQuantity.BillOfQuantities billOfQuantities, au.com.biztune.retail.domain.BillOfQuantity billOfQuantity) {
        if (billOfQuantities == null) {
            logger.error("List of product is null");
            return;
        }
        final Timestamp currentTime = new Timestamp(new Date().getTime());
        Supplier supplier = null;
        boolean prodIsNew = false;
        //delete all BoqDetails ...
        boqDetailDao.deleteBoqDetailPerBoqId(billOfQuantity.getId());
        for (BillOfQuantity.BillOfQuantities.Product importedProduct: billOfQuantities.getProduct()) {
            if (importedProduct == null) {
                continue;
            }
            //get unit of measure
            UnitOfMeasure unitOfMeasure = unitOfMeasureDao.getUnomByCode(importedProduct.getUnit());
            if (unitOfMeasure == null) {
                unitOfMeasure = new UnitOfMeasure();
                unitOfMeasure.setUnomCode(importedProduct.getUnit());
                unitOfMeasure.setUnomDesc(importedProduct.getUnit());
                unitOfMeasure.setUnomCreated(currentTime);
                unitOfMeasure.setUnomModified(currentTime);
                unitOfMeasureDao.insert(unitOfMeasure);
            }
            //import supplier
            supplier = supplierDao.getSupplierByOrgUnitIdAndSuppName(sessionState.getOrgUnit().getId(), importedProduct.getSupplier());
            if (supplier == null) {
                supplier = new Supplier();
                supplier.setSupplierName(importedProduct.getSupplier());
                supplier.setSupplierCode(importedProduct.getSupplier());
                supplier.setCreateDate(currentTime);
                supplierDao.insertSupplier(supplier);

                final ConfigCategory supplierStatus = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.CONFIG_SUPLIER_STATUS, IdBConstant.SUPPLIER_STATUS_IMPORTED);
                final SuppOrguLink suppOrguLink = new SuppOrguLink();
                suppOrguLink.setOrguId(sessionState.getOrgUnit().getId());
                suppOrguLink.setSuppId(supplier.getId());
                suppOrguLink.setStatus(supplierStatus);
                supplierDao.insertSuppOrguLink(suppOrguLink);
                supplier.setSuppOrguLink(suppOrguLink);
            }
            //insert product. search product per reference. if dose not exist, add it
            Product product = productDao.getProductPerReference(importedProduct.getReference());
            if (product == null) {
                prodIsNew = true;
                product = new Product();
                product.setOrguIdOwning(sessionState.getOrgUnit().getId());
                product.setProdSku(importedProduct.getCatalogueNo());
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
                //TODO: we need to set Quantity and price for each of imported BOQ in suppProdPrice table
                //get supplier legal tender
                final LegalTender legalTender = legalTenderDao.getLegalTenderByCode(IdBConstant.LEGAL_TENDER_AU);
                final SuppProdPrice suppProdPrice = new SuppProdPrice();
                suppProdPrice.setSolId(supplier.getSuppOrguLink().getId());
                suppProdPrice.setCatalogueNo(importedProduct.getCatalogueNo());
                suppProdPrice.setUnitOfMeasure(unitOfMeasure);
                suppProdPrice.setUnitOfMeasureContent(unitOfMeasure);
                suppProdPrice.setUnomQty(1);
                suppProdPrice.setLegalTender(legalTender);
                if (importedProduct.getCost() != null) {
                    suppProdPrice.setPrice(importedProduct.getCost());
                    suppProdPrice.setCostBeforeTax(importedProduct.getCost());
                }
                suppProdPrice.setSprcCreated(currentTime);
                suppProdPrice.setSprcModified(currentTime);
                suppProdPrice.setProdId(product.getId());
                suppProdPrice.setSprcPrefferBuy(true);
                //insert product tax rule
                TaxLegVariance taxLegVariance = taxRuleDao.getTaxLegVarianceByCode(importedProduct.getTaxName());
                if (taxLegVariance == null) {
                    taxLegVariance = taxRuleDao.getTaxLegVarianceByCode(IdBConstant.DEFAULT_PRODUCT_TAX_CODE);
                }
                suppProdPrice.setTaxLegVariance(taxLegVariance);

                suppProdPriceDao.insert(suppProdPrice);

                //import product price
                //get price band
                final PriceBand priceBand = priceBandDao.getPriceBandPerCode(IdBConstant.PRICE_BAND_CODE);

                //
                final Price prodPrice = new Price();
                //get price code

                PriceCode priceCode = priceDao.getProductPriceCodePerCode(IdBConstant.SELL_PRICE_CODE);


                final Price price1 = new Price();
                //TODO: we need to set Quantity and price for each of imported BOQ in Price table
                price1.setUnomQty(1);
                //price1.setMargin(margin);
                price1.setPrcePrice(importedProduct.getSellPrice());
                //price1.setPrceTaxIncluded(taxInclude);
                price1.setPriceBand(priceBand);
                price1.setPriceCode(priceCode);
                price1.setProdId(product.getId());
                price1.setUnitOfMeasure(unitOfMeasure);
                price1.setPrceCreated(currentTime);
                price1.setPrceModified(currentTime);
                price1.setPrceFromDate(currentTime);
                price1.setPrceToDate(currentTime);
                price1.setPrceSetCentral(false);
                priceDao.insert(price1);

                //INSERT COST PRICE, SAME AS SELL PRICE
                priceCode = priceDao.getProductPriceCodePerCode(IdBConstant.COST_PRICE_CODE);
                price1.setPriceCode(priceCode);
                price1.setPrcePrice(importedProduct.getCost());
                priceDao.insert(price1);


            //if product exists, update supplier information
            } else {
                //check if this product is already assigned to the supplier
                SuppProdPrice suppProdPrice = suppProdPriceDao.checkIfSupplierProductExistsPerOrguIdAndProdIdAndSuppId(
                        sessionState.getOrgUnit().getId(), product.getId(), supplier.getId());
                if (suppProdPrice == null) {
                    //get supplier legal tender
                    final LegalTender legalTender = legalTenderDao.getLegalTenderByCode(IdBConstant.LEGAL_TENDER_AU);
                    suppProdPrice = new SuppProdPrice();
                    suppProdPrice.setSolId(supplier.getSuppOrguLink().getId());
                    suppProdPrice.setCatalogueNo(importedProduct.getCatalogueNo());
                    suppProdPrice.setUnitOfMeasure(unitOfMeasure);
                    suppProdPrice.setUnitOfMeasureContent(unitOfMeasure);
                    suppProdPrice.setUnomQty(1);
                    suppProdPrice.setLegalTender(legalTender);
                    if (importedProduct.getCost() != null) {
                        suppProdPrice.setPrice(importedProduct.getCost());
                        suppProdPrice.setCostBeforeTax(importedProduct.getCost());
                    }
                    suppProdPrice.setSprcCreated(currentTime);
                    suppProdPrice.setSprcModified(currentTime);
                    suppProdPrice.setProdId(product.getId());
                    suppProdPrice.setSprcPrefferBuy(true);
                    //insert product tax rule
                    TaxLegVariance taxLegVariance = taxRuleDao.getTaxLegVarianceByCode(importedProduct.getTaxName());
                    if (taxLegVariance == null) {
                        taxLegVariance = taxRuleDao.getTaxLegVarianceByCode(IdBConstant.DEFAULT_PRODUCT_TAX_CODE);
                    }
                    suppProdPrice.setTaxLegVariance(taxLegVariance);

                    suppProdPriceDao.insert(suppProdPrice);
                } else {
                    if (importedProduct.getCost() != null) {
                        suppProdPrice.setPrice(importedProduct.getCost());
                    }
                    suppProdPriceDao.updateValues(suppProdPrice);
                }
            }
            final BoqDetail boqDetail = new BoqDetail();
            boqDetail.setBillOfQuantity(billOfQuantity);
            if (importedProduct.getCost() != null) {
                boqDetail.setCost(importedProduct.getCost());
            } else {
                boqDetail.setCost(0.0);
            }
            boqDetail.setProdIsNew(prodIsNew);
            boqDetail.setProduct(product);
            if (importedProduct.getQty() != null) {
                boqDetail.setQuantity(importedProduct.getQty());
            } else {
                boqDetail.setQuantity(0);
            }
            boqDetail.setSellPrice(importedProduct.getSellPrice());
            boqDetail.setUnitOfMeasure(unitOfMeasure);
            boqDetail.setSupplier(supplier);
            boqDetail.setQtyOnStock(0);
            boqDetail.setQtyPurchased(0);
            boqDetail.setItemValue(boqDetail.getCost() * boqDetail.getQuantity());
            boqDetail.setQtyBalance(boqDetail.getQuantity());
            final ConfigCategory status = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_BOQ_LINE_STATUS, IdBConstant.BOQ_LINE_STATUS_PENDING);
            if (status != null) {
                boqDetail.setBqdStatus(status);
            }
            final ConfigCategory creationType = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_POH_CREATION_TYPE, IdBConstant.POH_CREATION_TYPE_AUTO);
            if (status != null) {
                boqDetail.setBqdCreationType(creationType);
            }
            boqDetailDao.insert(boqDetail);
        }
    }

    /**
     * import BillOfQuantity Header.
     * @param header header
     */
    private au.com.biztune.retail.domain.BillOfQuantity importBillOfQuantityHeader(BillOfQuantity.Header header, Project project, BillOfQuantity.BillOfQuantities products) {
        if (header == null) {
            return null;
        }
        double valueGross = 0;
        int lines = 0;
        double totalQty = 0;
        for (BillOfQuantity.BillOfQuantities.Product importedProduct: products.getProduct()) {
            if (importedProduct == null || importedProduct.getCost() == null || importedProduct.getQty() == null) {
                continue;
            }
            lines++;
            valueGross = valueGross + importedProduct.getCost() * importedProduct.getQty();
            totalQty = totalQty + importedProduct.getQty();
        }
        //TODO: we need to search if BOQ Header is already exists.
        au.com.biztune.retail.domain.BillOfQuantity billOfQuantity;
        //= billOfQuantityDao.getBillOfQuantityByName(header.getName());
        billOfQuantity = new au.com.biztune.retail.domain.BillOfQuantity();
        billOfQuantity.setProject(project);
        billOfQuantity.setBoqName(header.getName());
        billOfQuantity.setDateCreated(new java.sql.Timestamp(new Date().getTime()));
        billOfQuantity.setNote(header.getNote());
        billOfQuantity.setOrderNo(header.getOrderNumber());
        billOfQuantity.setOrguId(sessionState.getOrgUnit().getId());
        //set user
        final Principal principal = securityContext.getUserPrincipal();
        AppUser appUser = null;
        if (principal instanceof AppUser) {
            appUser = (AppUser) principal;
            billOfQuantity.setBoqLastModifiedBy(appUser.getId());
        }

        final ConfigCategory staus = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_BOQ_STATUS, IdBConstant.BOQ_STATUS_NEW);
        if (staus != null) {
            billOfQuantity.setBoqStatus(staus);
        }
        billOfQuantity.setBoqValueGross(valueGross);
        billOfQuantity.setBoqTotalQty(totalQty);
        billOfQuantity.setBoqTotalLines(lines);
        billOfQuantityDao.insert(billOfQuantity);
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
            final ConfigCategory customerType = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_CUSTOMER_TYPE, IdBConstant.CUSTOMER_TYPE_CASH_ONLY);
            if (customerType != null) {
                customer.setCustomerType(customerType);
            }
            final ConfigCategory customerStatus = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_CUSTOMER_STATUS, IdBConstant.CUSTOMER_STATUS_NEW);
            if (customerStatus != null) {
                customer.setCustomerStatus(customerStatus);
            }
            customer.setAddress(client.getAdd1());
            customer.setCompanyName(client.getClientName());
            customerDao.insert(customer);
            //insert conftact info
            try {
                if (client.getContactName() != null) {
                    final String[] splited = client.getContactName().split("\\s+");
                    final Contact contact = new Contact();
                    contact.setFirstName(splited[0]);
                    contact.setSurName(splited[1]);
                    final ConfigCategory  contactType = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_CONTACT_TYPE, IdBConstant.CONTACT_TYPE_CONTACT_PERSON);
                    contact.setContactType(contactType);
                    contactDao.insert(contact);
                    final CustomerContact customerContact = new CustomerContact();
                    customerContact.setCustomerId(customer.getId());
                    customerContact.setContact(contact);
                    customerDao.insertCustomerContact(customerContact);
                }
            } catch (Exception e) {
                logger.error("Error in splitting the customer name:", e);
            }
        } else {
            //customer.setCustomerType(IdBConstant.CUSTOMER_TYPE_COMPANY);
            customer.setAddress(client.getAdd1());
            customer.setCompanyName(client.getClientName());
            try {
                if (client.getContactName() != null) {
                    final String[] splited = client.getContactName().split("\\s+");
                    customer.setFirstName(splited[0]);
                    customer.setSurName(splited[1]);
                }

            } catch (Exception e) {
                logger.error("Error in splitting the customer name:", e);
            }
            customerDao.update(customer);

        }
        return customer;
    }

    /**
     * get bill of quantity detail list per BOQ Id.
     * @param id id
     * @return List of BoqDetail
     */
    public List<BoqDetail> getBoqDetailByBoqId(long id) {
        try {
            return boqDetailDao.getBoqDetailByBoqId(id);
        } catch (Exception e) {
            logger.error("Error in getting Bill Of Quantity Details", e);
            return  null;
        }
    }

    /**
     * get bill of quantity header per BOQ Id.
     * @param id id
     * @return BOQ
     */
    public au.com.biztune.retail.domain.BillOfQuantity getBoqHeaderByBoqId(long id) {
        try {
            return billOfQuantityDao.getBillOfQuantityById(id);
        } catch (Exception e) {
            logger.error("Error in getting Bill Of Quantity", e);
            return  null;
        }
    }


    /**
     * get list of bill of quantity .
     * @return List of BOQ
     */
    public List<au.com.biztune.retail.domain.BillOfQuantity> getAllBoq() {
        try {
            return billOfQuantityDao.getAllBillOfQuantities();
        } catch (Exception e) {
            logger.error("Error in getting Bill Of Quantities", e);
            return  null;
        }
    }

    /**
     * update bill of quantity on stock information.
     * @param billOfQuantity billOfQuantity
     * @param securityContext securityContext
     * @return Response
     */
    public CommonResponse update(au.com.biztune.retail.domain.BillOfQuantity billOfQuantity, SecurityContext securityContext) {
        this.securityContext = securityContext;
        final CommonResponse response = new CommonResponse();
        try {
            response.setStatus(IdBConstant.RESULT_SUCCESS);
            BoqDetail boqDetailBeforeUpdate = null;
            if (billOfQuantity == null) {
                response.setStatus(IdBConstant.RESULT_FAILURE);
                response.setMessage("bill of quantity object is null");
                return response;
            }
            // update bill of quantity details for current items. add new items
            for (BoqDetail boqDetail: billOfQuantity.getLines()) {
                boqDetailBeforeUpdate = boqDetailDao.getBoqDetailById(boqDetail.getId());
                if (boqDetail.isDeleted()) {
                    continue;
                }
                //if line has been voided AND IS MANUALLY CREATED, delete linked purchase orders lines and delete line itself.
                if (boqDetail.getBqdStatus().getCategoryCode().equals(IdBConstant.BOQ_LINE_STATUS_VOID)
                        && boqDetail.getBqdCreationType().getCategoryCode().equals(IdBConstant.POH_CREATION_TYPE_MANUAL))
                {
                    if (boqDetail.getLinkedPurchaseOrders() != null && boqDetail.getLinkedPurchaseOrders().size() > 0) {
                        for (PoBoqLink poBoqLink : boqDetail.getLinkedPurchaseOrders()) {
                            if (poBoqLink == null) {
                                continue;
                            }
                            poBoqLinkDao.deletePoBoqLinkPerBoqDetailId(poBoqLink.getBoqDetailId());
                        }
                    }
                    boqDetailDao.deleteBoqDetailPerId(boqDetail.getId());
                }
                if (boqDetail.getId() >= 0) {
                    boqDetailDao.updateBoqLine(boqDetail);
                } else {
                    boqDetail.setBillOfQuantity(billOfQuantity);
                    boqDetailDao.insert(boqDetail);
                    //if a line from purchase order had been added, then handle it here
                    if (boqDetail.getLinkedPurchaseOrders() != null && boqDetail.getLinkedPurchaseOrders().size() > 0) {
                        for (PoBoqLink poBoqLink : boqDetail.getLinkedPurchaseOrders()) {
                            if (poBoqLink == null) {
                                continue;
                            }
                            poBoqLink.setBoqDetailId(boqDetail.getId());
                            poBoqLink.setBoqId(billOfQuantity.getId());
                            poBoqLinkDao.insert(poBoqLink);
                        }
                    }
                }
                //update stock quantity.
                updateStockQuantity(billOfQuantity, boqDetail, boqDetailBeforeUpdate);
            }
            //set user
            final Principal principal = securityContext.getUserPrincipal();
            AppUser appUser = null;
            if (principal instanceof AppUser) {
                appUser = (AppUser) principal;
                billOfQuantity.setBoqLastModifiedBy(appUser.getId());
            }
            //update boq header
            billOfQuantityDao.updatePerId(billOfQuantity);

            return response;
        } catch (Exception e) {
            logger.error("Exception in updating bill of quantity:", e);
            response.setStatus(IdBConstant.RESULT_FAILURE);
            response.setMessage("exception in saving data");
            return response;
        }
    }

    /**
     * generate Purchase orders from bill of quantities.
     * @param billOfQuantities list of BillOfQyantity Objects.
     * @param  securityContext securityContext
     * @return response
     */
    @Transactional
    public List<PurchaseOrderHeader> createPurchaseOrderFromBillOfQuantities(List<au.com.biztune.retail.domain.BillOfQuantity> billOfQuantities, SecurityContext securityContext) {
        this.securityContext = securityContext;
        AppUser appUser = null;
        //set user
        final Principal principal = securityContext.getUserPrincipal();
        if (principal instanceof AppUser) {
            appUser = (AppUser) principal;
        }
        final List<PurchaseOrderHeader> result = new ArrayList<PurchaseOrderHeader>();
        try {
            if (billOfQuantities == null) {
                logger.debug("Bill Of Quantity list to create Purchase Order From is empty");
                return null;
            }
            //extract boqIds from list.
            final List<Long> boqIdList = new ArrayList<Long>();
            for (au.com.biztune.retail.domain.BillOfQuantity billOfQuantity : billOfQuantities) {
                //not generate for voided items.
                if (billOfQuantity == null) {
                    continue;
                }
                boqIdList.add(billOfQuantity.getId());
            }
            //fetch all Bill Of Quantity details from db
            final List<BoqDetail> boqDetailList = boqDetailDao.getBoqDetailForMultipleBoqId(boqIdList);
            //iterate the boqDetailList and group them per supplier.
            final HashMap<Long, List<BoqDetail>> supplierBoqDetailMap = new HashMap<Long, List<BoqDetail>>();
            for (BoqDetail boqDetail: boqDetailList) {
                //only generate for pending items.
                if (!boqDetail.getBqdStatus().getCategoryCode().equals(IdBConstant.BOQ_LINE_STATUS_PENDING)) {
                    continue;
                }
                final Long key = boqDetail.getSupplier().getId();
                if (!supplierBoqDetailMap.containsKey(key)) {
                    final List<BoqDetail> supplierBoqList = new ArrayList<BoqDetail>();
                    supplierBoqList.add(boqDetail);
                    supplierBoqDetailMap.put(key, supplierBoqList);
                } else {
                    supplierBoqDetailMap.get(key).add(boqDetail);
                }
            }

            for (Long supplierKey: supplierBoqDetailMap.keySet()) {
                final List<BoqDetail> supplierBoqItems = supplierBoqDetailMap.get(supplierKey);
                if (supplierBoqItems == null || supplierBoqItems.size() < 1) {
                    continue;
                }
                //get the first item from the list and create the Purchase Order Header.
                final BoqDetail boqDetailItem = supplierBoqItems.get(0);
                final PurchaseOrderHeader purchaseOrderHeader = purchaseOrderService.createPoFromBoq(boqDetailItem, appUser);
                for (BoqDetail item : supplierBoqItems) {
                    if (item == null) {
                        continue;
                    }
                    //now we have item. let's create the header
                    if (purchaseOrderService.addLineToPoFromBoqDetail(purchaseOrderHeader, item)) {
                        //update billOfQuantity Item : purchased and balance values
                        item.setQtyPurchased(item.getQtyBalance());
                        //item.setQtyBalance(0.00);
                        final ConfigCategory status = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_BOQ_LINE_STATUS, IdBConstant.BOQ_LINE_STATUS_PO_CREATED);
                        if (status != null) {
                            item.setBqdStatus(status);
                        }
                        boqDetailDao.updatePerId(item);
                    } else {
                        logger.debug("not able to create purchase order line for boq item : " + purchaseOrderHeader.getPohOrderNumber() + " item: " + item.getId());
                    }
                }
                //update the total fields in purchase order header.
                purchaseOrderDao.updatePurchaseOrderHeader(purchaseOrderHeader);
                //fetch all project codes assigned to PO and insert as comma separated list to field PO_PRJ_CODE just for search.

                final List<PoBoqLink> linkedProjects = poBoqLinkDao.getAllPoProjectCodesPerPohId(purchaseOrderHeader.getId());
                if (linkedProjects != null && linkedProjects.size() > 0) {
                    final String commaSeparatedProjectCodes = linkedProjects.stream().map(PoBoqLink::getProjectCode).collect(Collectors.joining(","));
                    purchaseOrderDao.updatePohProjectCode(purchaseOrderHeader.getId(), commaSeparatedProjectCodes);
                }
                result.add(purchaseOrderHeader);
            }
            //now change the status of BillOfQuantities merged
            final ConfigCategory status = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_BOQ_STATUS, IdBConstant.BOQ_STATUS_ON_ORDER);
            for (au.com.biztune.retail.domain.BillOfQuantity billOfQuantity : billOfQuantities) {
                if (billOfQuantity == null) {
                    continue;
                }
                billOfQuantity.setBoqStatus(status);
                billOfQuantityDao.updateStatusPerId(billOfQuantity);
            }
            return result;

        } catch (Exception e) {
            logger.error("Exception in creating Purchase Order from Bill Of Quantities:", e);
            return null;
        }
    }

    /**
     * update stock qty.
     * @param boqDetailNew boqDetailNew
     * @param  boqDetailOld boqDetailOld
     * @param  boqHeader boqHeader
     */
    public void updateStockQuantity(au.com.biztune.retail.domain.BillOfQuantity boqHeader, BoqDetail boqDetailNew, BoqDetail boqDetailOld) {
        try {
            //get current user from security context.
            final Principal principal = securityContext.getUserPrincipal();
            AppUser appUser = null;
            if (principal instanceof AppUser) {
                appUser = (AppUser) principal;
            }

            String txnType = null;
            double quantity = 0;
            if (boqDetailOld == null && boqDetailNew.getQtyOnStock() > 0) {
                txnType = IdBConstant.TXN_TYPE_GOODS_RESERVE;
                quantity = boqDetailNew.getQtyOnStock();
            } else if (boqDetailNew.getBqdStatus().getCategoryCode().equals(IdBConstant.BOQ_LINE_STATUS_VOID)) {
                txnType = IdBConstant.TXN_TYPE_GOODS_CANCEL_RESERVE;
                quantity = boqDetailOld.getQtyOnStock();
            } else if (boqDetailNew.getQtyOnStock() > boqDetailOld.getQtyOnStock()) {
                txnType = IdBConstant.TXN_TYPE_GOODS_RESERVE;
                quantity = boqDetailNew.getQtyOnStock() - boqDetailOld.getQtyOnStock();
            } else if (boqDetailNew.getQtyOnStock() < boqDetailOld.getQtyOnStock()) {
                txnType = IdBConstant.TXN_TYPE_GOODS_CANCEL_RESERVE;
                quantity = boqDetailOld.getQtyOnStock() - boqDetailNew.getQtyOnStock();
            } else {
                //on stock qty has not changed. no need to proceed.
                return;
            }
            final Timestamp currentTime = new Timestamp(new Date().getTime());
            final StockEvent stockEvent = new StockEvent();
            stockEvent.setTxnTypeConst(txnType);
            stockEvent.setStckQty(quantity);
            stockEvent.setUnomId(boqDetailNew.getUnitOfMeasure().getId());
            stockEvent.setSupplierId(boqDetailNew.getSupplier().getId());
            stockEvent.setCostPrice(boqDetailNew.getCost());
            stockEvent.setProdId(boqDetailNew.getProduct().getId());
            stockEvent.setSellPrice(boqDetailNew.getSellPrice());
            stockEvent.setStckEvntDate(currentTime);
            stockEvent.setTxnDate(boqHeader.getBoqLastModifiedDate());
            stockEvent.setTxnHeader(boqHeader.getId());
            stockEvent.setTxnLine(boqDetailNew.getId());
            stockEvent.setUserId(appUser.getId());
            stockEvent.setTxnNumber(boqHeader.getProject().getProjectCode());
            stockEvent.setOrguId(sessionState.getOrgUnit().getId());
            stockService.pushStockEvent(stockEvent);
        } catch (Exception e) {
            logger.error("Exception in creating stock event:", e);
        }
    }

    /**
     * get client's BOQ list.
     * @param clientId clientId
     * @return List of Boq
     */
    public List<au.com.biztune.retail.domain.BillOfQuantity> getClientBillOfQuantities(long clientId) {
        try {
            return billOfQuantityDao.getBillOfQuantitiesByClientId(clientId);
        } catch (Exception e) {
            logger.error("Error in getting Bill Of Quantity for client", e);
            return  null;
        }
    }

    /**
     * search boq.
     * @param boqSearchForm boqSearchForm
     * @return boq search form.
     */
    public BoqSearchForm searchBoqPaging(BoqSearchForm boqSearchForm) {
        try {
            final long rowNoFrom = (boqSearchForm.getPageNo() - 1) * boqSearchForm.getPageSize() + 1;
            final long rowNoTo = rowNoFrom + boqSearchForm.getPageSize() - 1;
            //final ProductSearchForm productSearchForm = new ProductSearchForm();
            final List<SearchClause> searchClauseList = SearchClauseBuilder.buildBoqSearchWhereCluase(boqSearchForm);
            boqSearchForm.setResult(billOfQuantityDao.searchBillOfQuantityPaging(sessionState.getOrgUnit().getId(), rowNoFrom, rowNoTo, searchClauseList));
            boqSearchForm.setTotalRecords(billOfQuantityDao.getBillOfQuantityQueryTotalRows(sessionState.getOrgUnit().getId(), searchClauseList));
            return boqSearchForm;
        } catch (Exception e) {
            logger.error("Error in queyring boq", e);
            return null;
        }
    }

    /**
     * get all projects.
     * @return project list.
     */
    public List<Project> getAllProjects() {
        try {
            return projectDao.getAllProject();
        } catch (Exception e) {
            logger.error("Error in getting project list", e);
            return  null;
        }
    }
}