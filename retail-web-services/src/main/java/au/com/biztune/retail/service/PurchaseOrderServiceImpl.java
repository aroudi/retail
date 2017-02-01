package au.com.biztune.retail.service;

import au.com.biztune.retail.dao.*;
import au.com.biztune.retail.domain.*;
import au.com.biztune.retail.response.CommonResponse;
import au.com.biztune.retail.session.SessionState;
import au.com.biztune.retail.util.DateUtil;
import au.com.biztune.retail.util.IdBConstant;
import au.com.biztune.retail.util.SearchClause;
import au.com.biztune.retail.util.SearchClauseBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.SecurityContext;
import java.security.Principal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by arash on 14/05/2016.
 */

@Component
public class PurchaseOrderServiceImpl implements PurchaseOrderService {

    @Autowired
    private SessionState sessionState;
    private final Logger logger = LoggerFactory.getLogger(ConfigCategoryServiceImpl.class);
    @Autowired
    private ConfigCategoryDao configCategoryDao;

    @Autowired
    private PurchaseOrderDao purchaseOrderDao;

    @Autowired
    private SuppProdPriceDao suppProdPriceDao;

    @Autowired
    private PoBoqLinkDao poBoqLinkDao;

    @Autowired
    private BoqDetailDao boqDetailDao;

    @Autowired
    private BillOfQuantityDao billOfQuantityDao;

    private SecurityContext securityContext;

    /**
     * save Purchase Order Header into database.
     * @param purchaseOrderHeader purchaseOrderHeader
     * @param  securityContext securityContext
     * @return response
     */
    public CommonResponse savePurchaseOrder(PurchaseOrderHeader purchaseOrderHeader, SecurityContext securityContext) {
        this.securityContext = securityContext;
        final CommonResponse response = new CommonResponse();
        try {
            response.setStatus(IdBConstant.RESULT_SUCCESS);
            if (purchaseOrderHeader == null) {
                response.setStatus(IdBConstant.RESULT_FAILURE);
                response.setMessage("purchase order object or its related objects are null");
                return response;
            }
            final boolean isNew = purchaseOrderHeader.getId() > 0 ? false : true;
            final Timestamp currentDate = new Timestamp(new Date().getTime());
            purchaseOrderHeader.setOrgUnit(sessionState.getOrgUnit());
            String pohNumber = purchaseOrderHeader.getPohOrderNumber();
            //set user
            final Principal principal = securityContext.getUserPrincipal();
            AppUser appUser = null;
            if (principal instanceof AppUser) {
                appUser = (AppUser) principal;
                purchaseOrderHeader.setPohLastModifiedBy(appUser.getId());
            }
            //check if status is confirmed
            if (purchaseOrderHeader.getPohStatus() != null && purchaseOrderHeader.getPohStatus().getCategoryCode().equals(IdBConstant.POH_STATUS_CONFIRMED)) {
                purchaseOrderHeader.setPohConfirmDate(currentDate);
                purchaseOrderHeader.setPohApproved(true);
            }
            purchaseOrderHeader.setPohExpDelivery(DateUtil.stringToDate(purchaseOrderHeader.getPohExpDeliveryStr(), "yyyy-MM-dd"));
            purchaseOrderHeader.setPohCreatedDate(DateUtil.stringToDate(purchaseOrderHeader.getPohCreatedDateStr(), "yyyy-MM-dd"));

            if (isNew) {
                final ConfigCategory creationType = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_POH_CREATION_TYPE, IdBConstant.POH_CREATION_TYPE_MANUAL);
                purchaseOrderHeader.setPohCreationType(creationType);
                purchaseOrderHeader.setPohCreatedDate(currentDate);
                purchaseOrderHeader.setPohLastModifiedDate(currentDate);
                purchaseOrderHeader.setPohRevision(0);
                //final ConfigCategory status = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_POH_STATUS, IdBConstant.POH_STATUS_IN_PROGRESS);
                //purchaseOrderHeader.setPohStatus(status);
                purchaseOrderDao.insertPurchaseOrderHeader(purchaseOrderHeader);
                //generate purchase order number
                pohNumber = generatePohNumber(purchaseOrderHeader.getId(), IdBConstant.POH_NUMBER_PREFIX_MANUAL);
                purchaseOrderHeader.setPohOrderNumber(pohNumber);
                purchaseOrderDao.updatePurchaseOrderHeader(purchaseOrderHeader);

            } else {
                if (purchaseOrderHeader.getPohStatus().getCategoryCode().equals(IdBConstant.POH_STATUS_CONFIRMED)) {
                    purchaseOrderHeader.setPohRevision(purchaseOrderHeader.getPohRevision() + 1);
                }
                purchaseOrderDao.updatePurchaseOrderHeader(purchaseOrderHeader);
            }
            for (PurchaseLine purchaseLine : purchaseOrderHeader.getLines()) {
                if (purchaseLine.isDeleted()) {
                    if (purchaseLine.getId() >= 0) {
                        purchaseOrderDao.deletePurchaseLinePerId(purchaseLine.getId());
                    }
                    continue;
                }
                //if line id is less than 0; the line is new then insert it
                if (purchaseLine.getId() < 0) {
                    purchaseLine.setPohOrderNumber(pohNumber);
                    purchaseLine.setPohId(purchaseOrderHeader.getId());
                    purchaseLine.setPolProdId(purchaseLine.getPurchaseItem().getProdId());
                    purchaseLine.setPolSuppId(purchaseOrderHeader.getSupplier().getId());
                    purchaseOrderDao.insertPurchaseLine(purchaseLine);
                } else {
                    purchaseOrderDao.updatePurchaseLine(purchaseLine);
                }
            }
            //include the purchase order number in the response
            response.setInfo(purchaseOrderHeader.getPohOrderNumber());
            return response;
        } catch (Exception e) {
            logger.error("Exception in saving Purchase Order Header:", e);
            response.setStatus(IdBConstant.RESULT_FAILURE);
            response.setMessage("Exception in saving Transaction");
            return response;
        }
    }

    /**
     * when PurchaseOrder status is equal to Good Received, we need to update figures on lined BOQs.
     * @param purchaseOrderHeader purchaseOrderHeader
     * @return Response
     */
    public CommonResponse updateLinkedBqos(PurchaseOrderHeader purchaseOrderHeader) {
        final CommonResponse response = new CommonResponse();
        final HashMap<Long, String> poLinkedBoqs = new HashMap<Long, String>();
        ConfigCategory status = null;
        try {
            response.setStatus(IdBConstant.RESULT_SUCCESS);
            if (purchaseOrderHeader == null) {
                response.setStatus(IdBConstant.RESULT_FAILURE);
                response.setMessage("purchase order object or its related objects are null");
                return response;
            }
            if (!purchaseOrderHeader.getPohStatus().getCategoryCode().equals(IdBConstant.POH_STATUS_PARTIAL_REC)
                    && !purchaseOrderHeader.getPohStatus().getCategoryCode().equals(IdBConstant.POH_STATUS_GOOD_RECEIVED))
            {
                response.setStatus(IdBConstant.RESULT_FAILURE);
                response.setMessage("goods still yet to be received!!!");
                return response;
            }
            for (PurchaseLine purchaseLine: purchaseOrderHeader.getLines()) {
                //WE CHANGE THE BOQ LINKED ONLY IF GOOD PARTIALLY OR TOTALLY HAD BEEN RECEIVED
                if (purchaseLine == null || !purchaseLine.getPolStatus().getCategoryCode().equals(IdBConstant.POH_STATUS_GOOD_RECEIVED)
                        && !purchaseLine.getPolStatus().getCategoryCode().equals(IdBConstant.POH_STATUS_PARTIAL_REC))
                {
                    continue;
                }
                for (PoBoqLink linkedBoq: purchaseLine.getPoBoqLinks()) {
                    status = null;
                    if (linkedBoq == null) {
                        continue;
                    }
                    if (linkedBoq.getBoqQtyBalance() == 0) {
                        status = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_BOQ_LINE_STATUS, IdBConstant.BOQ_LINE_STATUS_GOOD_RECEIVED);
                        linkedBoq.setStatus(status);
                    } else if (linkedBoq.getBoqQtyBalance() < linkedBoq.getBoqQtyTotal()) {
                        status = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_BOQ_LINE_STATUS, IdBConstant.BOQ_LINE_STATUS_PARTIAL_RECEIVED);
                        linkedBoq.setStatus(status);
                    }
                    poBoqLinkDao.updateQtyReceived(linkedBoq);

                    //add to hashmap for later update
                    if (!poLinkedBoqs.containsKey(linkedBoq.getBoqId())) {
                        poLinkedBoqs.put(linkedBoq.getBoqId(), linkedBoq.getBoqName());
                    }
                    //get the related BOQ line and update its figure and status as well.
                    final BoqDetail linkedBoqLine = boqDetailDao.getBoqDetailById(linkedBoq.getBoqDetailId());
                    if (linkedBoqLine != null) {
                        if (status != null) {
                            linkedBoqLine.setBqdStatus(status);
                        }
                        linkedBoqLine.setQtyReceived(linkedBoq.getPoQtyReceived());
                        linkedBoqLine.setQtyBalance(linkedBoqLine.getQtyPurchased() - linkedBoqLine.getQtyReceived());
                        boqDetailDao.updateQtyReceived(linkedBoqLine);
                    }
                }
            }

            //update the status of BOQ object
            //ConfigCategory status = null;
            boolean areAllLineReceived = true;
            boolean partiallyReceived = false;
            for (Long boqId: poLinkedBoqs.keySet()) {
                //get boq object
                final BillOfQuantity billOfQuantity = billOfQuantityDao.getBillOfQuantityById(boqId);
                if (billOfQuantity == null) {
                    continue;
                }
                for (BoqDetail boqDetail : billOfQuantity.getLines()) {
                    if (boqDetail == null || boqDetail.getBqdStatus().getCategoryCode().equals(IdBConstant.BOQ_LINE_STATUS_VOID)) {
                        continue;
                    }
                    if (boqDetail.getBqdStatus() != null && (boqDetail.getBqdStatus().getCategoryCode().equals(IdBConstant.BOQ_LINE_STATUS_GOOD_RECEIVED)
                            || (boqDetail.getBqdStatus().getCategoryCode().equals(IdBConstant.BOQ_LINE_STATUS_PARTIAL_RECEIVED))))
                    {
                        partiallyReceived = true;
                    }
                    if (boqDetail.getBqdStatus() == null || boqDetail.getBqdStatus() != null && !boqDetail.getBqdStatus().getCategoryCode().equals(IdBConstant.BOQ_LINE_STATUS_GOOD_RECEIVED)) {
                        areAllLineReceived = false;
                    }
                }

                if (areAllLineReceived) {
                    status = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_BOQ_STATUS, IdBConstant.BOQ_STATUS_RECEIVED);

                } else if (partiallyReceived) {
                    status = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_BOQ_STATUS, IdBConstant.BOQ_STATUS_PARTIAL_REC);

                } else {
                    status = billOfQuantity.getBoqStatus();
                }
                billOfQuantity.setBoqStatus(status);
                billOfQuantityDao.updateStatusPerId(billOfQuantity);
            }
            return response;

        } catch (Exception e) {
            logger.error("Exception in updating linked BOQs:", e);
            response.setStatus(IdBConstant.RESULT_FAILURE);
            response.setMessage("Exception in updating linked BOQs");
            return response;
        }
    }

    /**
     * create Purchase Order From Boq.
     * @param boqDetail boqDetail
     * @param appUser appUser
     * @return PurchaseOrderHeader
     */
    public PurchaseOrderHeader createPoFromBoq(BoqDetail boqDetail, AppUser appUser) {
        try {
            if (boqDetail == null) {
                return  null;
            }
            final Timestamp currentDate = new Timestamp(new Date().getTime());
            final PurchaseOrderHeader purchaseOrderHeader = new PurchaseOrderHeader();
            purchaseOrderHeader.setOrgUnit(sessionState.getOrgUnit());
            purchaseOrderHeader.setSupplier(boqDetail.getSupplier());
            purchaseOrderHeader.setPohCreatedDate(currentDate);
            purchaseOrderHeader.setPohLastModifiedDate(currentDate);
            //purchaseOrderHeader.setPohValueNett(boqDetail.getBillOfQuantity().getBoqValueNett());
            //purchaseOrderHeader.setPoh(boqDetail.getBillOfQuantity().getBoqValueNett());
            final ConfigCategory pohType = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_POH_TYPE, IdBConstant.POH_TYPE_PROJECT);
            final ConfigCategory pohCreationType = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_POH_CREATION_TYPE, IdBConstant.POH_CREATION_TYPE_AUTO);
            final ConfigCategory pohStatus = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_POH_STATUS, IdBConstant.POH_STATUS_IN_PROGRESS);
            purchaseOrderHeader.setPohType(pohType);
            purchaseOrderHeader.setPohCreationType(pohCreationType);
            purchaseOrderHeader.setPohStatus(pohStatus);
            purchaseOrderHeader.setPohLastModifiedBy(appUser.getId());
            //save purchase order header
            purchaseOrderDao.insertPurchaseOrderHeader(purchaseOrderHeader);
            //assinge number to purchase order header.
            final String pohNumber = generatePohNumber(purchaseOrderHeader.getId(), IdBConstant.POH_NUMBER_PREFIX_AUTO);
            purchaseOrderHeader.setPohOrderNumber(pohNumber);
            purchaseOrderDao.updatePurchaseOrderHeader(purchaseOrderHeader);
            return purchaseOrderHeader;
        } catch (Exception e) {
            logger.error("Exception in creating Purchase Order Header from Bill Of Quantity:", e);
            return null;
        }
    }

    /**
     * add lines to Purchase Order Header.
     * @param purchaseOrderHeader purchaseOrderHeader
     * @param boqDetail boqDetail
     * @return true if successfull, otherwise return false;
     */
    public boolean addLineToPoFromBoqDetail(PurchaseOrderHeader purchaseOrderHeader, BoqDetail boqDetail) {
        try {
            if (purchaseOrderHeader == null || boqDetail == null) {
                return false;
            }
            //check if line is already exists. then we need to modify it
            final PurchaseLine purchaseLine = new PurchaseLine();
            purchaseLine.setPohId(purchaseOrderHeader.getId());
            purchaseLine.setPohOrderNumber(purchaseOrderHeader.getPohOrderNumber());
            //retreive Product Purchase Item from database
            final ProductPurchaseItem productPurchaseItem = suppProdPriceDao.getProductPurchaseItemByOrguIdAndProdIdAndSuppId(purchaseOrderHeader.getOrgUnit().getId(),
                    boqDetail.getProduct().getId(), boqDetail.getSupplier().getId());
            purchaseLine.setPurchaseItem(productPurchaseItem);
            //todo: should it be the unit cost or the total cost????
            purchaseLine.setPolUnitCost(boqDetail.getCost());
            purchaseLine.setPolSpecialBuy(false);
            purchaseLine.setUnitOfMeasure(productPurchaseItem.getUnitOfMeasure());
            purchaseLine.setPolQtyOrdered(boqDetail.getQtyBalance());
            purchaseLine.setPolQtyReserved(boqDetail.getQtyBalance());
            purchaseLine.setPolValueOrdered(purchaseLine.getPolUnitCost() * purchaseLine.getPolQtyOrdered());
            if (productPurchaseItem.getUnitOfMeasureContent() != null) {
                purchaseLine.setUnomContents(productPurchaseItem.getUnitOfMeasureContent());
            }
            purchaseLine.setPolContents(productPurchaseItem.getUnomQty());
            final ConfigCategory polCreationType = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_POH_CREATION_TYPE, IdBConstant.POH_CREATION_TYPE_AUTO);
            if (polCreationType != null) {
                purchaseLine.setPolCreationType(polCreationType);
            }
            final ConfigCategory status = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_POH_STATUS, IdBConstant.POH_STATUS_IN_PROGRESS);
            if (status != null) {
                purchaseLine.setPolStatus(status);
            }
            //check if line already exists in purchase order header. if so update existing line; otherwise create new line
            boolean lineFound = false;
            if (purchaseOrderHeader.getLines() != null) {
                for (PurchaseLine pl: purchaseOrderHeader.getLines()) {
                    if (pl.getPurchaseItem().getProdId() == purchaseLine.getPurchaseItem().getProdId()) {
                        pl.setPolQtyOrdered(pl.getPolQtyOrdered() + purchaseLine.getPolQtyOrdered());
                        pl.setPolQtyReserved(pl.getPolQtyReserved() + purchaseLine.getPolQtyReserved());
                        pl.setPolValueOrdered(pl.getPolValueOrdered() + purchaseLine.getPolValueOrdered());
                        //pl.getPoBoqLinks().addAll(purchaseLine.getPoBoqLinks());
                        purchaseOrderDao.updatePurchaseLine(pl);
                        createPoBoqLink(pl, boqDetail);
                        lineFound = true;
                        break;
                    }
                }
            }
            if (!lineFound) {
                purchaseLine.setPolProdId(boqDetail.getProduct().getId());
                purchaseLine.setPolSuppId(boqDetail.getSupplier().getId());
                purchaseOrderDao.insertPurchaseLine(purchaseLine);
                createPoBoqLink(purchaseLine, boqDetail);
                purchaseOrderHeader.addLine(purchaseLine);
            }
            return true;
        } catch (Exception e) {
            logger.error("Exception in adding line to Purchase Order Header:", e);
            return false;
        }
    }

    private void createPoBoqLink(PurchaseLine purchaseLine, BoqDetail boqDetail) {
        try {
            if (purchaseLine == null || boqDetail == null) {
                return;
            }
            final PoBoqLink poBoqLink = new PoBoqLink();
            poBoqLink.setBoqDetailId(boqDetail.getId());
            poBoqLink.setBoqId(boqDetail.getBillOfQuantity().getId());
            poBoqLink.setBoqName(boqDetail.getBillOfQuantity().getBoqName());
            poBoqLink.setBoqQtyBalance(boqDetail.getQtyBalance());
            poBoqLink.setBoqQtyTotal(boqDetail.getQuantity());
            poBoqLink.setPohId(purchaseLine.getPohId());
            poBoqLink.setPohOrderNumber(purchaseLine.getPohOrderNumber());
            poBoqLink.setPolId(purchaseLine.getId());
            poBoqLink.setPoQtyReceived(0);
            poBoqLink.setProjectId(boqDetail.getBillOfQuantity().getProject().getId());
            poBoqLink.setProjectCode(boqDetail.getBillOfQuantity().getProject().getProjectCode());
            final ConfigCategory status = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_BOQ_LINE_STATUS, IdBConstant.BOQ_LINE_STATUS_PENDING);
            poBoqLink.setStatus(status);
            poBoqLinkDao.insert(poBoqLink);
            purchaseLine.addPoBoqLink(poBoqLink);
        } catch (Exception e) {
            logger.error("Exception in creating purchase order to boq link:", e);
            return;
        }
    }
    /**
     * generate Purchase Order Header Number.
     * @param pohId pohId
     * @param preFix preFix
     * @return PohNumber
     */
    public String generatePohNumber(long pohId, String preFix) {
        //final Timestamp currentDate = new Timestamp(new Date().getTime());
        //return preFix + DateUtil.dateToString(currentDate, "yyyy-MM-dd") + "-" + pohId;
        return String.valueOf(pohId);
    }

    /**
     * get all purchase Order Header.
     * @return List of PurchaseOrderHeader
     */
    public List<PurchaseOrderHeader> getAllPurchaseOrderHeaders() {
        try {
            return purchaseOrderDao.getAllPurchaseOrderHeaderPerOrguId(sessionState.getOrgUnit().getId());
        } catch (Exception e) {
            logger.error("Exception in getting purchase order header list:", e);
            return null;
        }
    }

    /**
     * get PurchaseOrderHeader whole oblect per pohId.
     * @param pohId pohId.
     * @return PurchaseOrderHeader
     */
    public PurchaseOrderHeader getPurchaseOrderHeaderWhole(long pohId) {
        try {
            final PurchaseOrderHeader purchaseOrderHeader = purchaseOrderDao.getPurchaseOrderWholePerPohId(pohId);
            purchaseOrderHeader.setPohExpDeliveryStr(DateUtil.dateToString(purchaseOrderHeader.getPohExpDelivery(), "yyyy-MM-dd"));
            purchaseOrderHeader.setPohCreatedDateStr(DateUtil.dateToString(purchaseOrderHeader.getPohCreatedDate(), "yyyy-MM-dd"));
            return purchaseOrderHeader;

        } catch (Exception e) {
            logger.error("Exception in getting purchase order header per pohId:", e);
            return null;
        }
    }

    /**
     * get product purchase items for specific supplier.
     * @param suppId suppId
     * @return List of PruductPurchaseItem
     */
    public List<ProductPurchaseItem> getAllSupplierProductPurchaseItems(long suppId) {
        try {
            return suppProdPriceDao.getAllProductPurchaseItemsPerOrgUnitIdAndSuppId(sessionState.getOrgUnit().getId(), suppId);
        } catch (Exception e) {
            logger.error("Exception in getting product purchase items per supplier:", e);
            return null;
        }
    }

    /**
     * get product purchase item for specific supplier and catalog no.
     * @param suppId suppId
     * @param catalogNo catalogNo
     * @return List of PruductPurchaseItem
     */
    public ProductPurchaseItem getSupplierProductPurchaseItemPerCatalogNo(long suppId, String catalogNo) {
        try {
            return suppProdPriceDao.getProductPurchaseItemPerOrgUnitIdAndSuppIdAndCatalogId(sessionState.getOrgUnit().getId(), suppId, catalogNo);
        } catch (Exception e) {
            logger.error("Exception in getting product purchase item per supplier and catalog no:", e);
            return null;
        }
    }

    /**
     * get all purchase Order Header.
     * @param supplierId supplierId
     * @return List of PurchaseOrderHeader
     */
    public List<PurchaseOrderHeader> getAllPurchaseOrderHeaderPerOrguIdAndSupplierIdAndStatusCode(long supplierId) {
        try {
            final List<Long> statusIds = new ArrayList<Long>();
            ConfigCategory status = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_POH_STATUS, IdBConstant.POH_STATUS_CONFIRMED);
            if (status != null) {
                statusIds.add(status.getId());
            }
            status = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_POH_STATUS, IdBConstant.POH_STATUS_PARTIAL_REC);
            if (status != null) {
                statusIds.add(status.getId());
            }
            return purchaseOrderDao.getAllPurchaseOrderHeaderPerOrguIdAndSupplierIdAndStatusIds(sessionState.getOrgUnit().getId(), supplierId, statusIds);
        } catch (Exception e) {
            logger.error("Exception in getting purchase order header list:", e);
            return null;
        }
    }
    /**
     * get all purchase Order Header per orguid and status.
     * @return List of PurchaseOrderHeader
     */
    public List<PurchaseOrderHeader> getAllPurchaseOrderHeaderNotFullyReceived() {
        try {
            final List<Long> statusIds = new ArrayList<Long>();
            ConfigCategory status = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_POH_STATUS, IdBConstant.POH_STATUS_IN_PROGRESS);
            if (status != null) {
                statusIds.add(status.getId());
            }
            status = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_POH_STATUS, IdBConstant.POH_STATUS_CONFIRMED);
            if (status != null) {
                statusIds.add(status.getId());
            }
            status = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_POH_STATUS, IdBConstant.POH_STATUS_PARTIAL_REC);
            if (status != null) {
                statusIds.add(status.getId());
            }
            return purchaseOrderDao.getAllPurchaseOrderHeaderNotFullyReceived(sessionState.getOrgUnit().getId(), statusIds);
        } catch (Exception e) {
            logger.error("Exception in getting purchase order header list:", e);
            return null;
        }
    }

    /**
     * get all purchase Order Header for specific supplier.
     * @param supplierId supplierId
     * @return List of PurchaseOrderHeader
     */
    public List<PurchaseOrderHeader> getAllPurchaseOrderHeaderPerOrguIdAndSupplierId(long supplierId) {
        try {
            return purchaseOrderDao.getAllPurchaseOrderHeaderPerOrguIdAndSupplierId(sessionState.getOrgUnit().getId(), supplierId);
        } catch (Exception e) {
            logger.error("Exception in getting purchase order header list per supplier:", e);
            return null;
        }
    }
    /**
     * search purchase order header.
     * @param searchForm searchForm
     * @return List of PurchaseOrderHeader
     */
    public List<PurchaseOrderHeader> searchPurchaseOrderHeaders(GeneralSearchForm searchForm) {
        try {
            if (searchForm == null) {
                logger.error("search form is null");
                return null;
            }
            return purchaseOrderDao.searchPurchaseOrderHeader(sessionState.getOrgUnit().getId(), SearchClauseBuilder.buildSearchWhereCluase(searchForm, "PURCHASE_ORDER_HEADER"));
        } catch (Exception e) {
            logger.error("Exception in searching purchase order header list:", e);
            return null;
        }
    }

    /**
     * search purchase order header.
     * @param searchForm searchForm
     * @return List of PurchaseOrderHeader
     */
    public GeneralSearchForm searchPurchaseOrderHeadersPaging(GeneralSearchForm searchForm) {
        try {
            if (searchForm == null) {
                logger.error("search form is null");
                return null;
            }
            final long rowNoFrom = (searchForm.getPageNo() - 1) * searchForm.getPageSize() + 1;
            final long rowNoTo = rowNoFrom + searchForm.getPageSize() - 1;
            //final ProductSearchForm productSearchForm = new ProductSearchForm();
            final List<SearchClause> searchClauseList = SearchClauseBuilder.buildSearchWhereCluase(searchForm, "PURCHASE_ORDER_HEADER");
            searchForm.setResult(purchaseOrderDao.searchPurchaseOrderHeaderPaging(sessionState.getOrgUnit().getId(), searchClauseList, rowNoFrom, rowNoTo));
            searchForm.setTotalRecords(purchaseOrderDao.getPurchaseOrderQueryTotalRows(sessionState.getOrgUnit().getId(), searchClauseList));
            return searchForm;
        } catch (Exception e) {
            logger.error("Exception in searching purchase order header list:", e);
            return null;
        }
    }

    /**
     * get all purchase Order Header for specific product.
     * @param prodId prodId
     * @return List of PurchaseOrderHeader
     */
    public List<PurchaseLine> getAllPurchaseOrderOfProduct(long prodId) {
        try {
            return purchaseOrderDao.getPurchaseOrdersOfProductPerProdId(prodId, sessionState.getOrgUnit().getId());
        } catch (Exception e) {
            logger.error("Exception in getting purchase order header list per product:", e);
            return null;
        }
    }
}
