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
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.core.SecurityContext;
import java.security.Principal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    private PoSoLinkDao poSoLinkDao;

    @Autowired
    private BoqDetailDao boqDetailDao;

    @Autowired
    private BillOfQuantityDao billOfQuantityDao;

    private SecurityContext securityContext;

    @Autowired
    private TxnDao txnDao;
    /**
     * save Purchase Order Header into database.
     * @param purchaseOrderHeader purchaseOrderHeader
     * @param  securityContext securityContext
     * @return response
     */
    public CommonResponse savePurchaseOrder(PurchaseOrderHeader purchaseOrderHeader, SecurityContext securityContext) {
        this.securityContext = securityContext;
        final CommonResponse response = new CommonResponse();
        //if we merged another po to this then we need to delete it at the end.
        long mergedPohId = -1;
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
                    //if line has been merged from another purchase order then update the order number and pohId on it
                    if (purchaseLine.isMerged()) {
                        mergedPohId = purchaseLine.getPohId();
                        purchaseLine.setPohOrderNumber(pohNumber);
                        purchaseLine.setPohId(purchaseOrderHeader.getId());
                        //change poh and number on linked object.
                        poSoLinkDao.changePohIdAndNumberPerPolId(purchaseOrderHeader.getId(), pohNumber, purchaseLine.getId());
                        poBoqLinkDao.changePohIdAndNumberPerPolId(purchaseOrderHeader.getId(), pohNumber, purchaseLine.getId());
                    }
                    purchaseOrderDao.updatePurchaseLine(purchaseLine);
                }
            }
            if (mergedPohId > 0) {
                //delete the merged purhcase order header
                deletePurchaseOrderPerPhoId(mergedPohId);
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
    /*
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
    */
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
     * create Purchase Order From Boq.
     * @param txnDetail sale order detail
     * @param appUser appUser
     * @return PurchaseOrderHeader
     */
    public PurchaseOrderHeader createPoFromSaleOrder(TxnDetail txnDetail, AppUser appUser) {
        try {
            if (txnDetail == null) {
                return  null;
            }
            final Timestamp currentDate = new Timestamp(new Date().getTime());
            final PurchaseOrderHeader purchaseOrderHeader = new PurchaseOrderHeader();
            purchaseOrderHeader.setOrgUnit(sessionState.getOrgUnit());
            final Supplier supplier = new Supplier();
            supplier.setId(txnDetail.getSupplierId());
            purchaseOrderHeader.setSupplier(supplier);
            purchaseOrderHeader.setPohCreatedDate(currentDate);
            purchaseOrderHeader.setPohLastModifiedDate(currentDate);
            //purchaseOrderHeader.setPohValueNett(boqDetail.getBillOfQuantity().getBoqValueNett());
            //purchaseOrderHeader.setPoh(boqDetail.getBillOfQuantity().getBoqValueNett());
            final ConfigCategory pohType = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_POH_TYPE, IdBConstant.POH_TYPE_PROJECT);
            final ConfigCategory pohCreationType = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_POH_CREATION_TYPE, IdBConstant.POH_CREATION_TYPE_AUTO_SO);
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

    /**
     * add lines to Purchase Order Header from txn detail.
     * @param purchaseOrderHeader purchaseOrderHeader
     * @param txnDetail txnDetail
     * @return true if successfull, otherwise return false;
     */
    public boolean addLineToPoFromTxnDetail(PurchaseOrderHeader purchaseOrderHeader, TxnDetail txnDetail) {
        try {
            if (purchaseOrderHeader == null || txnDetail == null) {
                return false;
            }
            //check if line is already exists. then we need to modify it
            final PurchaseLine purchaseLine = new PurchaseLine();
            purchaseLine.setPohId(purchaseOrderHeader.getId());
            purchaseLine.setPohOrderNumber(purchaseOrderHeader.getPohOrderNumber());
            //retreive Product Purchase Item from database
            final ProductPurchaseItem productPurchaseItem = suppProdPriceDao.getProductPurchaseItemByOrguIdAndProdIdAndSuppId(purchaseOrderHeader.getOrgUnit().getId(),
                    txnDetail.getProductId(), txnDetail.getSupplierId());
            purchaseLine.setPurchaseItem(productPurchaseItem);
            //todo: should it be the unit cost or the total cost????
            purchaseLine.setPolUnitCost(productPurchaseItem.getPrice());
            purchaseLine.setPolSpecialBuy(false);
            purchaseLine.setUnitOfMeasure(productPurchaseItem.getUnitOfMeasure());
            purchaseLine.setPolQtyOrdered(txnDetail.getTxdeQtyBackOrder() - txnDetail.getTxdeQtyOrdered());
            purchaseLine.setPolQtyReserved(txnDetail.getTxdeQtyBackOrder() - txnDetail.getTxdeQtyOrdered());
            purchaseLine.setPolValueOrdered(purchaseLine.getPolUnitCost() * purchaseLine.getPolQtyOrdered());
            if (productPurchaseItem.getUnitOfMeasureContent() != null) {
                purchaseLine.setUnomContents(productPurchaseItem.getUnitOfMeasureContent());
            }
            purchaseLine.setPolContents(productPurchaseItem.getUnomQty());
            final ConfigCategory polCreationType = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_POH_CREATION_TYPE, IdBConstant.POH_CREATION_TYPE_AUTO_SO);
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
                        createPoSoLink(pl, txnDetail);
                        lineFound = true;
                        break;
                    }
                }
            }
            if (!lineFound) {
                purchaseLine.setPolProdId(txnDetail.getProductId());
                purchaseLine.setPolSuppId(txnDetail.getSupplierId());
                purchaseOrderDao.insertPurchaseLine(purchaseLine);
                createPoSoLink(purchaseLine, txnDetail);
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

    private void createPoSoLink(PurchaseLine purchaseLine, TxnDetail txnDetail) {
        try {
            if (purchaseLine == null || txnDetail == null) {
                return;
            }
            final PoSoLink poSoLink = new PoSoLink();
            poSoLink.setTxdeId(txnDetail.getId());
            poSoLink.setTxhdId(txnDetail.getTxhdId());
            poSoLink.setSoLineQtyBalance(txnDetail.getTxdeQtyBackOrder() - txnDetail.getTxdeQtyOrdered());
            poSoLink.setSoLineQtyTotal(txnDetail.getTxdeQtyBackOrder() - txnDetail.getTxdeQtyOrdered());
            poSoLink.setPohId(purchaseLine.getPohId());
            if (txnDetail.getProjectCode() != null) {
                poSoLink.setProjectCode(txnDetail.getProjectCode());
            }
            poSoLink.setPohOrderNumber(purchaseLine.getPohOrderNumber());
            poSoLink.setPolId(purchaseLine.getId());
            poSoLink.setPoQtyReceived(0);
            //getTxdeQtyBalance.setProjectId(boqDetail.getBillOfQuantity().getProject().getId());
            //poBoqLink.setProjectCode(boqDetail.getBillOfQuantity().getProject().getProjectCode());
            final ConfigCategory status = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_BOQ_LINE_STATUS, IdBConstant.BOQ_LINE_STATUS_PENDING);
            poSoLink.setPoSoStatus(status);
            poSoLinkDao.insert(poSoLink);
            purchaseLine.addPoSoLink(poSoLink);
        } catch (Exception e) {
            logger.error("Exception in creating purchase order to so link:", e);
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
     * @param searchStr searchStr
     * @return List of PruductPurchaseItem
     */
    public List<ProductPurchaseItem> getAllSupplierProductPurchaseItems(long suppId, String searchStr) {
        try {
            String searchCr = searchStr;
            if ("@ALL@".equals(searchStr)) {
                searchCr = "";
            }
            searchCr = "%" + searchCr + "%";
            return suppProdPriceDao.getAllProductPurchaseItemsPerOrgUnitIdAndSuppId(sessionState.getOrgUnit().getId(), suppId, searchCr);
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
     * get product purchase item for specific supplier and catalog no.
     * @param sprcId sprcId
     * @return List of PruductPurchaseItem
     */
    public ProductPurchaseItem getProductPurchaseItemPerId(long sprcId) {
        try {
            return suppProdPriceDao.getProductPurchaseItemPerSprcId(sprcId);
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
     * get all IN-PROGRESS purchase Order Header.
     * @param supplierId supplierId
     * @return List of PurchaseOrderHeader
     */
    public List<PurchaseOrderHeader> getAllOutstandingAndConfirmedPurchaseOrderHeaderPerOrguIdAndSupplierId(long supplierId) {
        try {
            final List<Long> statusIds = new ArrayList<Long>();
            final ConfigCategory status = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_POH_STATUS, IdBConstant.POH_STATUS_IN_PROGRESS);
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
     * get all purchase order headers linked to specific sale order.
     * @param txhdId transaction header id.
     * @return List of purchase order linked to sale order
     */
    public List<PurchaseOrderHeader> getAllPurchaseOrderOfSaleOrder(long txhdId) {
        try {
            return purchaseOrderDao.getAllPurchaseOrderOfSaleOrder(sessionState.getOrgUnit().getId(), txhdId);
        } catch (Exception e) {
            logger.error("Exception in getting purchase order linked to sale order", e);
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

    /**
     * delete purchase order per poh id.
     * @param pohId pohId
     * @return CommonResponse.
     */
    @Transactional
    public CommonResponse deletePurchaseOrderPerPhoId(long pohId) {
        final CommonResponse response = new CommonResponse();
        try {
            //-------------------------------------begin of changing status of linked boq-----------------------------------------------------
            //check the status of pohId. we only delete the purchase order with status 'IN PROGRESS'
            final PurchaseOrderHeader purchaseOrderHeader = purchaseOrderDao.getPurchaseOrderWholePerPohId(pohId);
            if (purchaseOrderHeader == null || !purchaseOrderHeader.getPohStatus().getCategoryCode().equals(IdBConstant.POH_STATUS_IN_PROGRESS)) {
                response.setStatus(IdBConstant.RESULT_FAILURE);
                response.setMessage("Not allowed");
                return response;
            }
            //get all po_boq_links for this purchase order and update their status.
            final List<PoBoqLink> linkedBoqList = poBoqLinkDao.getAllPoBoqLinkPerPohId(pohId);
            final ConfigCategory pendingStatus = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_BOQ_LINE_STATUS, IdBConstant.BOQ_LINE_STATUS_PENDING);

            if (linkedBoqList != null) {
                linkedBoqList.forEach(poBoqLink -> {
                    //update the status and qtys on linked boq detail.
                    boqDetailDao.updateQtyPurchasedReceivedAndQty(0.00,
                            0.00, poBoqLink.getBoqQtyTotal(), pendingStatus.getId(), poBoqLink.getBoqDetailId());
                });
            }
            //update the BOQ satus.
            updatePurchaseOrderLinkedBoqStatus(pohId);
            //delete poBoqLink lines.
            poBoqLinkDao.deletePoBoqLinkPerPohId(pohId);

            //-------------------------------------begin of changing status of linked sale orders-----------------------------------------------------

            //get all po_so_links for this purchase order and update their status.
            final ConfigCategory outStandingStatus = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_SO_STATUS, IdBConstant.SO_STATUS_OUTSTANDING);
            final List<PoSoLink> linkedSoList = poSoLinkDao.getAllPoSoLinkPerPohId(pohId);
            if (linkedSoList != null) {
                linkedSoList.forEach(poSoLink -> {
                    //update the status and qtys on linked boq detail.
                    final TxnDetail txnDetail = txnDao.getTxnDetailPerId(poSoLink.getTxdeId());
                    txnDetail.setStatus(outStandingStatus);
                    txnDetail.setTxdeQtyOrdered(txnDetail.getTxdeQtyOrdered() - poSoLink.getSoLineQtyTotal());
                    txnDao.updateTxnDetailBackOrderAndStatus(txnDetail);
                });
            }
            //update Sale Order status
            updatePurchaseOrderLinkedSaleOrderStatus(pohId);
            poSoLinkDao.deletePoSoLinkPerPohId(pohId);
            //-------------------------------------delete purchase order itself-----------------------------------------------------
            //delete purchase order lines.
            purchaseOrderDao.deletePurchaseLinePerPohId(pohId);
            //delete purchase order header itself.
            purchaseOrderDao.deletePurchaseOrderPerId(pohId);


            return response;

        } catch (Exception e) {
            logger.error("Exception in deleting purchase order", e);
            response.setStatus(IdBConstant.RESULT_FAILURE);
            response.setMessage(e.getMessage());
            return response;
        }
    }

    /**
     * update status of linked BOQ.
     * @param pohId purchaes order header id.
     */
    public void updatePurchaseOrderLinkedBoqStatus(long pohId) {
        try {
            final List<PoBoqLink> linkedBoqList = poBoqLinkDao.getAllPoBoqLinkPerPohId(pohId);
            //update the boq status.
            //extract boqs from list: group linked boq per boqId
            final Map<Long, List<PoBoqLink>> extractedBoqIdList = linkedBoqList
                    .stream()
                    .collect(Collectors
                            .groupingBy(PoBoqLink::getBoqId));

            //for each boqId, extract the boq record and change the status
            extractedBoqIdList.forEach((extractedBoqId, linkedBoqs) -> {
                final List<BoqDetail> boqDetailList = boqDetailDao.getBoqDetailLightByBoqId(extractedBoqId);
                final long receivedCount = boqDetailList
                        .stream()
                        .map(BoqDetail::getBqdStatus)
                        .filter(status -> (status != null) && (status.getCategoryCode().equals(IdBConstant.BOQ_LINE_STATUS_GOOD_RECEIVED)))
                        .count();

                final long partialReceivedCount = boqDetailList
                        .stream()
                        .map(BoqDetail::getBqdStatus)
                        .filter(status -> (status != null) && (status.getCategoryCode().equals(IdBConstant.BOQ_LINE_STATUS_PARTIAL_RECEIVED)))
                        .count();

                final long pendingCount = boqDetailList
                        .stream()
                        .map(BoqDetail::getBqdStatus)
                        .filter(status -> (status != null) && (status.getCategoryCode().equals(IdBConstant.BOQ_LINE_STATUS_PENDING)))
                        .count();
                if (pendingCount > 0) {
                    final ConfigCategory newStatus = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_BOQ_STATUS, IdBConstant.BOQ_STATUS_NEW);
                    billOfQuantityDao.updateBoqStatusPerId(newStatus.getId(), extractedBoqId);
                    return;
                }
                //if all items received
                if (receivedCount == boqDetailList.size()) {
                    final ConfigCategory newStatus = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_BOQ_STATUS, IdBConstant.BOQ_STATUS_RECEIVED);
                    billOfQuantityDao.updateBoqStatusPerId(newStatus.getId(), extractedBoqId);
                } else if (partialReceivedCount > 0 || receivedCount > 0) {
                    final ConfigCategory newStatus = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_BOQ_STATUS, IdBConstant.BOQ_STATUS_PARTIAL_REC);
                    billOfQuantityDao.updateBoqStatusPerId(newStatus.getId(), extractedBoqId);
                }
            });

        } catch (Exception e) {
            logger.error("Exception in updating linked po sale order", e);
        }
    }

    /**
     * update order status of linked sales orders.
     * @param pohId purchaes order header id.
     */
    public void updatePurchaseOrderLinkedSaleOrderStatus(long pohId) {
        try {
            final List<PoSoLink> linkedSoList = poSoLinkDao.getAllPoSoLinkPerPohId(pohId);
            //update the sale order status.
            //extract sale orders from list: group linked sales order per txhdId
            final Map<Long, List<PoSoLink>> extractedtxhdIdList = linkedSoList
                    .stream()
                    .collect(Collectors
                            .groupingBy(PoSoLink::getTxhdId));

            //for each txhdId, extract the transactionHeader and change the status
            extractedtxhdIdList.forEach((extractedtxhdId, linkedSaleOrderList) -> {
                final ConfigCategory txnStatus = txnDao.getTxnHeaderStatusByTxhdId(extractedtxhdId);
                //if txn status is finalise then we should not change its status.
                if (txnStatus == null || !txnStatus.getCategoryCode().equals(IdBConstant.SO_STATUS_FINAL)) {
                    final List<TxnDetail> saleOrderLineList = txnDao.getTxnDetailStatusPerTxhdId(extractedtxhdId);
                    final long receivedCount = saleOrderLineList
                            .stream()
                            .map(TxnDetail::getStatus)
                            .filter(orderStatus -> (orderStatus != null) && (orderStatus.getCategoryCode().equals(IdBConstant.SO_STATUS_RECEIVED)))
                            .count();

                    final long outsTandingCount = saleOrderLineList
                            .stream()
                            .map(TxnDetail::getStatus)
                            .filter(orderStatus -> orderStatus == null || orderStatus.getCategoryCode().equals(IdBConstant.SO_STATUS_OUTSTANDING))
                            .count();
                    final long partialReceivedCount = saleOrderLineList
                            .stream()
                            .map(TxnDetail::getStatus)
                            .filter(orderStatus -> (orderStatus != null) && (orderStatus.getCategoryCode().equals(IdBConstant.SO_STATUS_PARTIAL_REC)))
                            .count();

                    if (outsTandingCount > 0) {
                        final ConfigCategory newStatus = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_SO_STATUS, IdBConstant.SO_STATUS_OUTSTANDING);
                        txnDao.updateTxnHeaderStatusPerTxhdId(extractedtxhdId, newStatus.getId());
                        return;
                    }
                    //if all items received
                    if (receivedCount == saleOrderLineList.size()) {
                        final ConfigCategory newStatus = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_SO_STATUS, IdBConstant.SO_STATUS_RECEIVED);
                        txnDao.updateTxnHeaderStatusPerTxhdId(extractedtxhdId, newStatus.getId());
                    } else if (partialReceivedCount > 0) {
                        final ConfigCategory newStatus = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_SO_STATUS, IdBConstant.SO_STATUS_PARTIAL_REC);
                        txnDao.updateTxnHeaderStatusPerTxhdId(extractedtxhdId, newStatus.getId());
                    }
                }
            });

        } catch (Exception e) {
            logger.error("Exception in updating linked po sale order", e);
        }
    }
}
