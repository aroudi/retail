package au.com.biztune.retail.service;

import au.com.biztune.retail.dao.*;
import au.com.biztune.retail.domain.*;
import au.com.biztune.retail.response.CommonResponse;
import au.com.biztune.retail.session.SessionState;
import au.com.biztune.retail.util.DateUtil;
import au.com.biztune.retail.util.IdBConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
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

    /**
     * save Purchase Order Header into database.
     * @param purchaseOrderHeader purchaseOrderHeader
     * @return response
     */
    public CommonResponse savePurchaseOrder(PurchaseOrderHeader purchaseOrderHeader) {
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
            //check if status is confirmed
            if (purchaseOrderHeader.getPohStatus() != null && purchaseOrderHeader.getPohStatus().getCategoryCode().equals(IdBConstant.POH_STATUS_CONFIRMED)) {
                purchaseOrderHeader.setPohConfirmDate(currentDate);
                purchaseOrderHeader.setPohApproved(true);
            }
            if (isNew) {
                final ConfigCategory creationType = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_POH_CREATION_TYPE, IdBConstant.POH_CREATION_TYPE_MANUAL);
                purchaseOrderHeader.setPohCreationType(creationType);
                purchaseOrderHeader.setPohCreatedDate(currentDate);
                purchaseOrderHeader.setPohLastModifiedDate(currentDate);
                //final ConfigCategory status = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_POH_STATUS, IdBConstant.POH_STATUS_IN_PROGRESS);
                //purchaseOrderHeader.setPohStatus(status);
                purchaseOrderDao.insertPurchaseOrderHeader(purchaseOrderHeader);
                //generate purchase order number
                pohNumber = generatePohNumber(purchaseOrderHeader.getId(), IdBConstant.POH_NUMBER_PREFIX_MANUAL);
                purchaseOrderHeader.setPohOrderNumber(pohNumber);
                purchaseOrderDao.updatePurchaseOrderHeader(purchaseOrderHeader);

            } else {
                purchaseOrderDao.updatePurchaseOrderHeader(purchaseOrderHeader);
            }
            final List<Long> updatedLines = new ArrayList<Long>();
            for (PurchaseLine purchaseLine : purchaseOrderHeader.getLines()) {
                //if line id is less than 0; the line is new then insert it
                if (purchaseLine.getId() < 0) {
                    purchaseLine.setPohOrderNumber(pohNumber);
                    purchaseLine.setPohId(purchaseOrderHeader.getId());
                    purchaseOrderDao.insertPurchaseLine(purchaseLine);
                } else {
                    purchaseOrderDao.updatePurchaseLine(purchaseLine);
                }
                updatedLines.add(purchaseLine.getId());
            }
           //delete from db, removed lines.
            purchaseOrderDao.deletePurchaseLineWhereIdNotIn(purchaseOrderHeader.getId(), updatedLines);
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
        ConfigCategory status = null;
        try {
            response.setStatus(IdBConstant.RESULT_SUCCESS);
            if (purchaseOrderHeader == null) {
                response.setStatus(IdBConstant.RESULT_FAILURE);
                response.setMessage("purchase order object or its related objects are null");
                return response;
            }
            if (!purchaseOrderHeader.getPohStatus().getCategoryCode().equals(IdBConstant.POH_STATUS_PARTIAL_REC) || !purchaseOrderHeader.getPohStatus().getCategoryCode().equals(IdBConstant.POH_STATUS_GOOD_RECEIVED)) {
                response.setStatus(IdBConstant.RESULT_FAILURE);
                response.setMessage("goods still yet to be received!!!");
                return response;
            }
            for (PurchaseLine purchaseLine: purchaseOrderHeader.getLines()) {
                //WE CHANGE THE BOQ LINKED ONLY IF GOOD PARTIALLY OR TOTALLY HAD BEEN RECEIVED
                if (purchaseLine == null || !purchaseLine.getPolStatus().getCategoryCode().equals(IdBConstant.POH_STATUS_GOOD_RECEIVED)
                        || !purchaseLine.getPolStatus().getCategoryCode().equals(IdBConstant.POH_STATUS_PARTIAL_REC))
                {
                    continue;
                }
                for (PoBoqLink linkedBoq: purchaseLine.getPoBoqLinks()) {
                    if (linkedBoq == null) {
                        continue;
                    }
                    if (linkedBoq.getBoqQtyBalance() == 0) {
                        status = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_BOQ_LINE_STATUS, IdBConstant.BOQ_LINE_STATUS_GOOD_RECEIVED);
                    } else if (linkedBoq.getBoqQtyBalance() < linkedBoq.getBoqQtyTotal()) {
                        status = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_BOQ_LINE_STATUS, IdBConstant.BOQ_LINE_STATUS_PARTIAL_RECEIVED);
                    }
                    linkedBoq.setStatus(status);
                    poBoqLinkDao.updateQtyReceived(linkedBoq);
                    //get the related BOQ line and update its figure and status as well.
                    final BoqDetail linkedBoqLine = boqDetailDao.getBoqDetailById(linkedBoq.getBoqId());
                    if (linkedBoqLine != null) {
                        linkedBoqLine.setBqdStatus(status);
                        linkedBoqLine.setQtyReceived(linkedBoq.getPoQtyReceived());
                        linkedBoqLine.setQtyBalance(linkedBoqLine.getQtyPurchased() - linkedBoqLine.getQtyReceived());
                        boqDetailDao.updateQtyReceived(linkedBoqLine);
                    }
                }
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
     * @return PurchaseOrderHeader
     */
    public PurchaseOrderHeader createPoFromBoq(BoqDetail boqDetail) {
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
            purchaseLine.setPolValueOrdered(purchaseLine.getPolUnitCost() * purchaseLine.getPolQtyOrdered());
            if (productPurchaseItem.getUnitOfMeasureContent() != null) {
                purchaseLine.setUnomContents(productPurchaseItem.getUnitOfMeasureContent());
            }
            purchaseLine.setPolContents(productPurchaseItem.getUnomQty());
            final ConfigCategory polCreationType = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_POH_CREATION_TYPE, IdBConstant.POH_CREATION_TYPE_AUTO);
            if (polCreationType != null) {
                purchaseLine.setPolCreationType(polCreationType);
            }
            //check if line already exists in purchase order header. if so update existing line; otherwise create new line
            boolean lineFound = false;
            if (purchaseOrderHeader.getLines() != null) {
                for (PurchaseLine pl: purchaseOrderHeader.getLines()) {
                    if (pl.getPurchaseItem().getProdId() == purchaseLine.getPurchaseItem().getProdId()) {
                        pl.setPolQtyOrdered(pl.getPolQtyOrdered() + purchaseLine.getPolQtyOrdered());
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
        final Timestamp currentDate = new Timestamp(new Date().getTime());
        return preFix + DateUtil.dateToString(currentDate, "yyyy-MM-dd") + "-" + pohId;
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
            return purchaseOrderDao.getPurchaseOrderWholePerPohId(pohId);
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
}
