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
    private ConfigCategoryDao configCategoryDao;
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
    @Autowired
    private ProductImportServiceImpl productImportService;
    private SecurityContext securityContext;
    @Autowired
    private UserService userService;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private SupplierDao supplierDao;
    @Autowired
    private ProductService productService;
    @Autowired
    private SupplierService supplierService;
    @Autowired
    private CustomerService customerService;
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
            final long result = importBillOfQuantity(billOfQuantity, securityContext);
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
     * @param securityContext securityContext
     * @return FAILURE if something went wrong, otherwise will return the billOfQuantity id.
     */
    public long importBillOfQuantity(BillOfQuantity billOfQuantity, SecurityContext securityContext) {
        // import BillOfQuantityHeader
        final AppUser appUser = userService.getAppUserFromSecurityContext(securityContext);
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
        importBoqDetail(productList, billOfQuantityObj, appUser.getId());
        //if it is a new customer, then set the verification status to false.
        if (!customer.isVerified()) {
            customerDao.updateCustomerVerificationStatus(false, customer.getId());
        }
        //if it is a new project, then set the verification status to false.
        if (!project.isVerified()) {
            projectDao.updateProjectVerificationStatus(false, project.getId());
        }
        return billOfQuantityObj.getId();
    }

    private void importBoqDetail(BillOfQuantity.BillOfQuantities billOfQuantities, au.com.biztune.retail.domain.BillOfQuantity billOfQuantity, long userId) {
        if (billOfQuantities == null) {
            logger.error("List of product is null");
            return;
        }
        final Timestamp currentTime = new Timestamp(new Date().getTime());
        final boolean prodIsNew = false;
        //delete all BoqDetails ...
        boqDetailDao.deleteBoqDetailPerBoqId(billOfQuantity.getId());
        for (BillOfQuantity.BillOfQuantities.Product importedProduct: billOfQuantities.getProduct()) {
            if (importedProduct == null) {
                continue;
            }
            //////////////////////////// import product //////////////////////////
            final ImportProductResult importProductResult = productImportService.importProductWhole(null, null, importedProduct.getCatalogueNo(),
                    importedProduct.getReference(), importedProduct.getDescription(),
                    null, importedProduct.getSupplier(), null, importedProduct.getUnit(),
                    importedProduct.getTaxName(), importedProduct.getCost() == null ? 0.00 : importedProduct.getCost(),
                    importedProduct.getSellPrice() == null ? 0.00 : importedProduct.getSellPrice(), 0, false, userId);
            ///////////////////////////
            final BoqDetail boqDetail = new BoqDetail();
            boqDetail.setBillOfQuantity(billOfQuantity);
            if (importedProduct.getCost() != null) {
                boqDetail.setCost(importedProduct.getCost());
            } else {
                boqDetail.setCost(0.0);
            }
            boqDetail.setProdIsNew(prodIsNew);
            boqDetail.setProduct(importProductResult.getImportedProduct());
            if (importedProduct.getQty() != null) {
                boqDetail.setQuantity(importedProduct.getQty());
            } else {
                boqDetail.setQuantity(0);
            }
            boqDetail.setSellPrice(importedProduct.getSellPrice());
            boqDetail.setUnitOfMeasure(importProductResult.getUnitOfMeasure());
            boqDetail.setSupplier(importProductResult.getImportedSupplier());
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
            //check if product is a new product then set it to a temporary product.
            //to be clarify: in adding product if product does not exist we set its verified flag to false.
            if (!importProductResult.getImportedProduct().isVerified()) {
                productDao.updateProductVerificationStatus(false, importProductResult.getImportedProduct().getId());
            }
            //check if supplier is a new supplier then set it to a temporary supplier.
            //to be clarify: in adding supplier if it does not exist we set its verified flag to false.
            if (!importProductResult.getImportedSupplier().isVerified()) {
                supplierDao.updateSupplierVerificationStatus(false, importProductResult.getImportedSupplier().getId());
            }
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
        final au.com.biztune.retail.domain.BillOfQuantity billOfQuantity;
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
        Project project = projectDao.getProjectByCodeAndClientId(inputProject.getProjectNo(), customer.getId());
        if (project != null) {
            //set verification code to true so in BOQ we know it is an existing project.
            project.setVerified(true);
            return project;
        } else {
            project = new Project();
            //set verification code to false so in BOQ we know it is a new one and need to be verified.
            project.setVerified(false);
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
            //set verification to flase so in BOQ import we know this is a new customer and need to be verified.
            customer.setVerified(false);
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
            customer.setVerified(true);
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
                if ((billOfQuantity.getBoqStatus() != null)
                        && (!billOfQuantity.getBoqStatus().getCategoryCode().equals(IdBConstant.BOQ_LINE_STATUS_PENDING)
                        && !billOfQuantity.getBoqStatus().getCategoryCode().equals(IdBConstant.BOQ_STATUS_CONFIRMED))
                )
                {
                    logger.debug("boq [{}] status is not PENDING or CONFIRMED. skip it", billOfQuantity.getId());
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
            stockEvent.setTxnTypeReservedFor(IdBConstant.TXN_TYPE_BOQ);
            stockEvent.setTxnNrReservedFor(boqHeader.getReferenceCode());
            stockEvent.setTxnIdReservedFor(boqHeader.getId());
            stockEvent.setTxnItemReservedFor(boqDetailNew.getId());
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

    /**
     * delete boq list.
     * @param boqIdList boqIdList
     * @return response.
     */
    public CommonResponse deleteBoqPerIdList(List<Long> boqIdList) {
        //TODO: system should allow only deleting the 'PENDING' BOQs
        final CommonResponse response = new CommonResponse();
        try {
            for (long boqId : boqIdList) {
                final au.com.biztune.retail.domain.BillOfQuantity billOfQuantity = billOfQuantityDao.getBillOfQuantityById(boqId);
                if (billOfQuantity == null) {
                    continue;
                }
                if (billOfQuantity.getBoqStatus() != null && !billOfQuantity.getBoqStatus().getCategoryCode().equals(IdBConstant.BOQ_STATUS_NEW)) {
                    logger.debug("boq [{}] status is not pending. can't delete it", boqId);
                    continue;
                }
                deleteBillOfQuantity(billOfQuantity);
            }
            response.setStatus(IdBConstant.RESULT_SUCCESS);
            //billOfQuantityDao.deleteBoqDetailPerBoqId(boqIdList);
            //billOfQuantityDao.deleteBoqPerId(boqIdList);
            return response;
        } catch (Exception e) {
            logger.error("Error in getting project list", e);
            response.setStatus(IdBConstant.RESULT_FAILURE);
            response.setMessage("Error in deleting BOQ" + e.getMessage());
            return  response;
        }
    }

    /**
     * delete bill of quantity.
     * @param billOfQuantity billOfQuantity
     */
    @Transactional
    public void deleteBillOfQuantity(au.com.biztune.retail.domain.BillOfQuantity billOfQuantity) {
        try {
            if (billOfQuantity == null) {
                return;
            }
            //now delete boq itself
            billOfQuantityDao.deleteBoqDetail(billOfQuantity.getId());
            billOfQuantityDao.deleteBoq(billOfQuantity.getId());
            //delete the temporary customer assigned to this boq.
            if (billOfQuantity.getProject() != null && billOfQuantity.getProject().getCustomer() != null) {
                if (!billOfQuantity.getProject().isVerified()) {
                    //project is temporary
                    if (projectDao.getNoOfBoqReferencedProject(billOfQuantity.getProject().getId()) < 1) {
                        projectDao.deleteProjectById(billOfQuantity.getProject().getId());
                    }
                }
                if (!billOfQuantity.getProject().getCustomer().isVerified()) {
                    //customer is temporary customer
                    if (projectDao.getNoOfBoqReferencedProject(billOfQuantity.getProject().getId()) < 1) {
                        customerService.deleteTemporaryCustomer(billOfQuantity.getProject().getCustomer());
                    }
                }
            }
            //iterate through products and delete them if they are temporary products.
            for (BoqDetail boqDetail : billOfQuantity.getLines()) {
                if (boqDetail == null) {
                    continue;
                }
                //check if product is temporary product.
                if (boqDetail.getProduct() != null && !boqDetail.getProduct().isVerified()) {
                    //check if product also is not used by other BOQ
                    if (productDao.getNoOfBoqReferencedProduct(boqDetail.getProduct().getId()) < 1) {
                        productService.deleteTemporaryProduct(boqDetail.getProduct());
                    }
                }
                //check if supplier if a temporary supplier
                if (boqDetail.getSupplier() != null && !boqDetail.getSupplier().isVerified()) {
                    //check if supplier also is not used by other boq
                    final long noOfSupplier = supplierDao.getNoOfBoqReferencedSupplier(boqDetail.getSupplier().getId());
                    logger.debug("noOfSuppilier = [{}]", noOfSupplier);
                    if (noOfSupplier < 1) {
                        supplierService.deleteTemporarySupplier(boqDetail.getSupplier());
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Exceptioon in deleting BOQ", e);
        }
    }

    /**
     * confirm BOQ. when BOQ is imported, the status is pending(temporary).
     * somebody need to review and confirm it.
     * @param boqIdList boqIdList
     * @return CommonResponse
     */
    public CommonResponse confirmBoq(List<Long> boqIdList) {
        final CommonResponse response = new CommonResponse();
        try {
            for (long boqId : boqIdList) {
                final au.com.biztune.retail.domain.BillOfQuantity billOfQuantity = billOfQuantityDao.getBillOfQuantityById(boqId);
                if (billOfQuantity == null) {
                    logger.debug("confirmBoq: boq [{}] not found", boqId);
                    continue;
                }
                //if boq is not in PENDING status, then we don't need to confirm it.
                if (billOfQuantity.getBoqStatus() != null && !billOfQuantity.getBoqStatus().getCategoryCode().equals(IdBConstant.BOQ_STATUS_NEW)) {
                    logger.debug("boq [{}] status is not pending. skip confirming it", boqId);
                    continue;
                }
                //change the BOQ status to confirmed.
                final ConfigCategory boqStatusConfirmed = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_BOQ_STATUS, IdBConstant.BOQ_STATUS_CONFIRMED);
                if (boqStatusConfirmed != null) {
                    billOfQuantityDao.updateBoqStatusPerId(boqStatusConfirmed.getId(), boqId);
                    confirmBoqRelatedObjects(billOfQuantity);
                }
            }
            response.setStatus(IdBConstant.RESULT_SUCCESS);
            return response;
        } catch (Exception e) {
            logger.error("Error in getting BOQ from DB", e);
            response.setStatus(IdBConstant.RESULT_FAILURE);
            response.setMessage("Error in deleting BOQ" + e.getMessage());
            return  response;
        }
    }
    private void confirmBoqRelatedObjects(au.com.biztune.retail.domain.BillOfQuantity billOfQuantity) {
        if (billOfQuantity == null) {
            return;
        }
        // update project and customer verification status to true.
        if (billOfQuantity.getProject() != null) {
            projectDao.updateProjectVerificationStatus(true, billOfQuantity.getProject().getId());
            if (billOfQuantity.getProject().getCustomer() != null) {
                customerDao.updateCustomerVerificationStatus(true, billOfQuantity.getProject().getCustomer().getId());
            }
        }
        for (BoqDetail boqDetail : billOfQuantity.getLines()) {
            if (boqDetail == null) {
                continue;
            }
            //update product verification status to true
            if (boqDetail.getProduct() != null) {
                productDao.updateProductVerificationStatus(true, boqDetail.getProduct().getId());
            }
            //update customer verification status to true
            if (boqDetail.getSupplier() != null) {
                supplierDao.updateSupplierVerificationStatus(true, boqDetail.getSupplier().getId());
            }
        }
    }
}