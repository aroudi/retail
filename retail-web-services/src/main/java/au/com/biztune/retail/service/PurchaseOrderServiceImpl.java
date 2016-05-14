package au.com.biztune.retail.service;

import au.com.biztune.retail.dao.ConfigCategoryDao;
import au.com.biztune.retail.dao.PurchaseOrderDao;
import au.com.biztune.retail.dao.SuppProdPriceDao;
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
import java.util.Date;

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
            if (isNew) {
                purchaseOrderDao.insertPurchaseOrderHeader(purchaseOrderHeader);
            } else {
                purchaseOrderDao.updatePurchaseOrderHeader(purchaseOrderHeader);
            }
            for (PurchaseLine purchaseLine : purchaseOrderHeader.getLines()) {
                if (isNew) {
                    purchaseOrderDao.insertPurchaseLine(purchaseLine);
                }
                    //todo: the business process should be clarified.
            }
            return response;
        } catch (Exception e) {
            logger.error("Exception in saving Purchase Order Header:", e);
            response.setStatus(IdBConstant.RESULT_FAILURE);
            response.setMessage("Exception in saving Transaction");
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
            final String pohNumber = generatePohNumber(purchaseOrderHeader.getId());
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
     */
    public void addLineToPoFromBoqDetail(PurchaseOrderHeader purchaseOrderHeader, BoqDetail boqDetail) {
        try {
            if (purchaseOrderHeader == null || boqDetail == null) {
                return;
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
            purchaseLine.setPolUnitCost(productPurchaseItem.getPrice());
            purchaseLine.setPolSpecialBuy(false);
            purchaseLine.setUnitOfMeasure(productPurchaseItem.getUnitOfMeasure());
            purchaseLine.setPolQtyOrdered(boqDetail.getQtyPurchased());
            purchaseLine.setPolValueOrdered(productPurchaseItem.getPrice() * boqDetail.getQtyPurchased());
            if (productPurchaseItem.getUnitOfMeasureContent() != null) {
                purchaseLine.setUnomContents(productPurchaseItem.getUnitOfMeasureContent());
            }
            purchaseLine.setPolContents(productPurchaseItem.getUnomQty());
            createPoBoqLink(purchaseLine, boqDetail);
            purchaseOrderHeader.addLine(purchaseLine);
            //purchaseOrderDao.insertPurchaseLine(purchaseLine);

        } catch (Exception e) {
            logger.error("Exception in adding line to Purchase Order Header:", e);
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
            poBoqLink.setProjectCode(boqDetail.getBillOfQuantity().getProject().getProjectCode());
            final ConfigCategory status = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_BOQ_LINE_STATUS, IdBConstant.BOQ_LINE_STATUS_PENDING);
            poBoqLink.setStatus(status);
            purchaseLine.addPoBoqLink(poBoqLink);
        } catch (Exception e) {
            logger.error("Exception in creating purchase order to boq link:", e);
            return;
        }
    }
    /**
     * generate Purchase Order Header Number.
     * @param pohId pohId
     * @return PohNumber
     */
    public String generatePohNumber(long pohId) {
        final Timestamp currentDate = new Timestamp(new Date().getTime());
        return IdBConstant.POH_NUMBER_PREFIX_AUTO + DateUtil.dateToString(currentDate, "yyyy-MM-dd") + pohId;
    }

}
