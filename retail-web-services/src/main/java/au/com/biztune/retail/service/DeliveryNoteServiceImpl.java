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
import java.util.Date;
import java.util.List;

/**
 * Created by arash on 14/05/2016.
 */

@Component
public class DeliveryNoteServiceImpl implements DeliveryNoteService {

    @Autowired
    private SessionState sessionState;
    private final Logger logger = LoggerFactory.getLogger(DeliveryNoteServiceImpl.class);
    @Autowired
    private ConfigCategoryDao configCategoryDao;

    @Autowired
    private DeliveryNoteDao deliveryNoteDao;
    @Autowired
    private PurchaseOrderDao purchaseOrderDao;
    @Autowired
    private PoDelNoteLinkDao poDelNoteLinkDao;

    @Autowired
    private PoBoqLinkDao poBoqLinkDao;
    @Autowired
    private StockService stockService;
    private SecurityContext securityContext;

    @Autowired
    private PoSoLinkDao poSoLinkDao;

    @Autowired
    private TxnDao txnDao;

    @Autowired
    private BoqDetailDao boqDetailDao;
    @Autowired
    private BillOfQuantityDao billOfQuantityDao;

    @Autowired
    private PurchaseOrderService purchaseOrderService;

    /**
     * save DeliveryNote into database.
     * @param deliveryNoteHeader deliveryNoteHeader
     * @param securityContext securityContext
     * @return response
     */
    public CommonResponse saveDeliveryNote(DeliveryNoteHeader deliveryNoteHeader, SecurityContext securityContext) {
        this.securityContext = securityContext;
        //get current user from security context.
        final Principal principal = securityContext.getUserPrincipal();
        AppUser appUser = null;
        if (principal instanceof AppUser) {
            appUser = (AppUser) principal;
        }

        final CommonResponse response = new CommonResponse();
        try {
            response.setStatus(IdBConstant.RESULT_SUCCESS);
            if (deliveryNoteHeader == null) {
                response.setStatus(IdBConstant.RESULT_FAILURE);
                response.setMessage("deliveryNote object is null");
                return response;
            }
            final boolean isNew = deliveryNoteHeader.getId() > 0 ? false : true;
            final Timestamp currentDate = new Timestamp(new Date().getTime());
            deliveryNoteHeader.setOrguId(sessionState.getOrgUnit().getId());
            String grnNumber = deliveryNoteHeader.getDelnGrn();
            deliveryNoteHeader.setDelnLastModifiedDate(currentDate);
            deliveryNoteHeader.setDelnLastModifiedBy(appUser.getId());
            deliveryNoteHeader.setDelnDeliveryDate(DateUtil.stringToDate(deliveryNoteHeader.getDeliveryDate(), "yyyy-MM-dd"));
            if (deliveryNoteHeader.getFreightTxrl() != null && deliveryNoteHeader.getFreightTxrl().getTaxRuleName() != null) {
                deliveryNoteHeader.setFreightTaxCode(deliveryNoteHeader.getFreightTxrl().getTxrlCode());
            }
            if (isNew) {
                //final ConfigCategory status = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_DLV_NOTE_STATUS, IdBConstant.DLV_NOTE_STATUS_IN_PROGRESS);
                //deliveryNoteHeader.setDelnStatus(status);
                deliveryNoteHeader.setDelnCreatedDate(currentDate);
                deliveryNoteDao.insertDeliveryNoteHeader(deliveryNoteHeader);
                //generate purchase order number
                grnNumber = generateGrnNumber(deliveryNoteHeader.getId());
                deliveryNoteHeader.setDelnGrn(grnNumber);
                deliveryNoteDao.updateDeliveryNoteGrnNo(deliveryNoteHeader);

            } else {
                deliveryNoteDao.updateDeliveryNoteHeader(deliveryNoteHeader);
            }
            for (DeliveryNoteLine deliveryNoteLine : deliveryNoteHeader.getLines()) {
                //if line id is less than 0; the line is new then insert it
                if (deliveryNoteLine.isDeleted()) {
                    if (deliveryNoteLine.getId() >= 0) {
                        deliveryNoteDao.deleteDeliveryNoteLinePerId(deliveryNoteLine.getId());
                    }
                    continue;
                }
                if (deliveryNoteLine.getId() < 0) {
                    deliveryNoteLine.setDelnGrn(grnNumber);
                    deliveryNoteLine.setDelnId(deliveryNoteHeader.getId());
                    deliveryNoteDao.insertDeliveryNoteLine(deliveryNoteLine);
                } else {
                    deliveryNoteDao.updateDeliveryNoteLine(deliveryNoteLine);
                }
                //update stock qty.
                updateStockQuantity(deliveryNoteHeader, deliveryNoteLine);
            }
            updateLinkedPurchaseOrder(deliveryNoteHeader);
            response.setInfo(deliveryNoteHeader.getDelnGrn());
            return response;
        } catch (Exception e) {
            logger.error("Exception in saving DeliveryNote:", e);
            response.setStatus(IdBConstant.RESULT_FAILURE);
            response.setMessage("Exception in saving DeliveryNote");
            return response;
        }
    }
    /**
     * generate GRN Number.
     * @param delnId delnId
     * @return PohNumber
     */
    public String generateGrnNumber(long delnId) {
        //final Timestamp currentDate = new Timestamp(new Date().getTime());
        //return IdBConstant.GRN_PREFIX + DateUtil.dateToString(currentDate, "yyyy-MM-dd") + "-" + delnId;
        return String.valueOf(delnId);
    }

    /**
     * get all delivery note headers.
     * @return List of DeliveryNoteHeader
     */
    public List<DeliveryNoteHeader> getAllDeliveryNoteHeaders() {
        try {
            return deliveryNoteDao.getAllDelNoteHeaderPerOrguId(sessionState.getOrgUnit().getId());
        } catch (Exception e) {
            logger.error("Exception in getting delivery note header list:", e);
            return null;
        }
    }

    /**
     * get DeliveryNote whole oblect per delnId.
     * @param delnId delnId.
     * @return DeliveryNoteHeader
     */
    public DeliveryNoteHeader getDeliveryNoteWhole(long delnId) {
        try {
            final DeliveryNoteHeader deliveryNoteHeader = deliveryNoteDao.getDelNoteWholePerDelnId(delnId);
            deliveryNoteHeader.setDeliveryDate(DateUtil.dateToString(deliveryNoteHeader.getDelnDeliveryDate(), "yyyy-MM-dd"));
            return deliveryNoteHeader;
        } catch (Exception e) {
            logger.error("Exception in getting delivery note whole per delnId:", e);
            return null;
        }
    }

    /**
     * pudate linked purchase order.
     * @param deliveryNoteHeader deliveryNoteHeader
     * @return true is ok; otherwise false
     */
    public boolean updateLinkedPurchaseOrder(DeliveryNoteHeader deliveryNoteHeader) {
        try {
            if (!deliveryNoteHeader.getDelnStatus().getCategoryCode().equals(IdBConstant.DLV_NOTE_STATUS_COMPLETE)) {
                logger.debug("DeliveryNote is note in complete state. so we don't update the purchase order");
                return true;
            }
            if (deliveryNoteHeader.getPohId() < 0) {
                logger.debug("there is no purchase order assigned to delivery note.");
                return true;
            }
                //create a link to purchase order header
            final PoDelNoteLink poDelNoteLink = new PoDelNoteLink();
            poDelNoteLink.setDelnId(deliveryNoteHeader.getId());
            poDelNoteLink.setDelnDeliveryDate(deliveryNoteHeader.getDelnDeliveryDate());
            poDelNoteLink.setDelnGrn(deliveryNoteHeader.getDelnGrn());
            poDelNoteLink.setDelnNoteNumber(deliveryNoteHeader.getDelnNoteNumber());
            poDelNoteLink.setPohId(deliveryNoteHeader.getPohId());
            poDelNoteLink.setPohOrderNumber(deliveryNoteHeader.getPohOrderNumber());
            poDelNoteLinkDao.insert(poDelNoteLink);

            //update qty received and status on purchase order header if delivery note is confirmed
            PurchaseLine purchaseLine = null;
            double qtyReceived = 0;
            ConfigCategory polStatus = null;
            for (DeliveryNoteLine deliveryNoteLine : deliveryNoteHeader.getLines()) {
                qtyReceived = 0;
                if (deliveryNoteLine == null || deliveryNoteLine.isDeleted()) {
                    continue;
                }
                purchaseLine = purchaseOrderDao.getPurchaseLinePerId(deliveryNoteLine.getPolId());
                //check if PurchaseOrderLine is already received. if so skip it
                if (purchaseLine == null || (purchaseLine.getPolStatus() != null
                        && purchaseLine.getPolStatus().getCategoryCode().equals(IdBConstant.POH_STATUS_GOOD_RECEIVED)))
                {
                   continue;
                }
                if (purchaseLine.getPolQtyReceived() >= 0) {
                    qtyReceived = purchaseLine.getPolQtyReceived() + deliveryNoteLine.getRcvdQty();
                } else {
                    qtyReceived = deliveryNoteLine.getRcvdQty();
                }
                purchaseLine.setPolQtyReceived(qtyReceived);
                purchaseLine.setPolValueReceived(purchaseLine.getPolUnitCost() * qtyReceived);
                purchaseLine.setPolDateReceived(deliveryNoteHeader.getDelnDeliveryDate());
                //put the location of received goods in comments(free_text)
                String location = "";
                if (purchaseLine.getPolFreeText() != null) {
                    location = purchaseLine.getPolFreeText();
                }
                purchaseLine.setPolFreeText(location + "Location: " + deliveryNoteLine.getDlnlComment() + " Qty: " + deliveryNoteLine.getRcvdQty() + System.lineSeparator());
                if (qtyReceived >= purchaseLine.getPolQtyOrdered()) {
                    polStatus = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_POH_STATUS, IdBConstant.POH_STATUS_GOOD_RECEIVED);

                } else {
                    polStatus = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_POH_STATUS, IdBConstant.POH_STATUS_PARTIAL_REC);
                }
                purchaseLine.setPolStatus(polStatus);
                purchaseOrderDao.updatePurchaseLine(purchaseLine);
                if (polStatus.getCategoryCode().equals(IdBConstant.POH_STATUS_GOOD_RECEIVED) || polStatus.getCategoryCode().equals(IdBConstant.POH_STATUS_PARTIAL_REC)) {
                    //only update related linked boq received when all or some of the ordered item has been received.
                    //we assigne the goods to lined boqs per their orders. when it is partial, user can go to the Purchase Order Form and change the assignement.
                    updatePurchaseLineLinkedBoq(deliveryNoteHeader, deliveryNoteLine, purchaseLine);
                    //update linked sale orders
                    updatePurchaseLineLinkedSaleOrder(deliveryNoteHeader, deliveryNoteLine, purchaseLine);
                }
            }
            //change the status of purchase order
            //retreive purchase order whole object from db.
            final PurchaseOrderHeader linkedPurchaseOrder = purchaseOrderDao.getPurchaseOrderWholePerPohId(deliveryNoteHeader.getPohId());
            boolean fullyReceived = true;
            for (PurchaseLine line : linkedPurchaseOrder.getLines()) {
                if (line.getPolStatus() == null || line.getPolStatus() != null && !line.getPolStatus().getCategoryCode().equals(IdBConstant.POH_STATUS_GOOD_RECEIVED)) {
                    fullyReceived = false;
                    break;
                }
            }
            final ConfigCategory pohStatus;
            if (fullyReceived) {
                pohStatus = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_POH_STATUS, IdBConstant.POH_STATUS_GOOD_RECEIVED);
            } else {
                pohStatus = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_POH_STATUS, IdBConstant.POH_STATUS_PARTIAL_REC);
            }
            if (pohStatus != null) {
                linkedPurchaseOrder.setPohStatus(pohStatus);
                linkedPurchaseOrder.setPohLastModifiedDate(deliveryNoteHeader.getDelnLastModifiedDate());
                linkedPurchaseOrder.setPohDelvDate(deliveryNoteHeader.getDelnDeliveryDate());
                linkedPurchaseOrder.setPohLastDelvDate(deliveryNoteHeader.getDelnDeliveryDate());
                purchaseOrderDao.updatePurchaseOrderHeader(linkedPurchaseOrder);
            }

            //update linked po sale order.
            purchaseOrderService.updatePurchaseOrderLinkedSaleOrderStatus(deliveryNoteHeader.getPohId());
            //update linked boq status.
            purchaseOrderService.updatePurchaseOrderLinkedBoqStatus(deliveryNoteHeader.getPohId());
            return true;

        } catch (Exception e) {
            logger.error("Exception in updating related purchase order", e);
            return false;
        }
    }

    private boolean updatePurchaseLineLinkedBoq(DeliveryNoteHeader deliveryNoteHeader, DeliveryNoteLine deliveryNoteLine, PurchaseLine purchaseLine) {
        try {
            if (purchaseLine == null) {
                return false;
            }
            final List<PoBoqLink> linkedBoq = poBoqLinkDao.getAllPoBoqLinkPerPolId(purchaseLine.getId());
            final double qtyReceived = purchaseLine.getPolQtyReceived();
            ConfigCategory status = null;
            double qtyRemain = qtyReceived;
            for (PoBoqLink poBoqLink:linkedBoq) {
                if (poBoqLink == null || poBoqLink.getBoqQtyBalance() == 0) {
                    continue;
                }
                if (qtyRemain >= poBoqLink.getBoqQtyBalance()) {
                    poBoqLink.setPoQtyReceived(poBoqLink.getPoQtyReceived() + poBoqLink.getBoqQtyBalance());
                    poBoqLink.setBoqQtyBalance(0);
                    qtyRemain = qtyRemain - poBoqLink.getBoqQtyBalance();
                    status = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_BOQ_LINE_STATUS, IdBConstant.BOQ_LINE_STATUS_GOOD_RECEIVED);
                } else {
                    poBoqLink.setPoQtyReceived(poBoqLink.getPoQtyReceived() + qtyRemain);
                    poBoqLink.setBoqQtyBalance(poBoqLink.getBoqQtyBalance() - qtyRemain);
                    status = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_BOQ_LINE_STATUS, IdBConstant.BOQ_LINE_STATUS_PARTIAL_RECEIVED);
                    qtyRemain = 0;
                }
                poBoqLink.setStatus(status);
                poBoqLink.setComment(purchaseLine.getPolFreeText());
                poBoqLinkDao.updateQtyReceived(poBoqLink);
                //update qty on linked boqdetail.
                boqDetailDao.updateQtyAndStatus(poBoqLink.getPoQtyReceived(), poBoqLink.getBoqQtyBalance(), poBoqLink.getStatus().getId(), poBoqLink.getBoqDetailId());
                //push stock event
                updateStockQuantityForLinkedBoq(deliveryNoteHeader, deliveryNoteLine, poBoqLink);
                if (qtyRemain == 0) {
                    break;
                }
            }
            return true;

        } catch (Exception e) {
            logger.error("Error in updating purchase order line linked BOQ", e);
            return false;
        }
    }

    private boolean updatePurchaseLineLinkedSaleOrder(DeliveryNoteHeader deliveryNoteHeader, DeliveryNoteLine deliveryNoteLine, PurchaseLine purchaseLine) {
        try {
            if (purchaseLine == null) {
                return false;
            }
            final List<PoSoLink> linkedSoList = poSoLinkDao.getAllPoSoLinkPerPolId(purchaseLine.getId());
            final double qtyReceived = deliveryNoteLine.getRcvdQty();
            //purchaseLine.getPolQtyReceived();
            ConfigCategory status = null;
            double qtyRemain = qtyReceived;
            double qtyAssigned = 0.00;
            for (PoSoLink poSoLink:linkedSoList) {
                if (poSoLink == null || poSoLink.getSoLineQtyBalance() == 0) {
                    continue;
                }
                if (qtyRemain >= poSoLink.getSoLineQtyBalance()) {
                    qtyAssigned = poSoLink.getSoLineQtyBalance();
                    qtyRemain = qtyRemain - qtyAssigned;
                    poSoLink.setPoQtyReceived(poSoLink.getPoQtyReceived() + qtyAssigned);
                    poSoLink.setSoLineQtyBalance(0);
                    status = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_SO_STATUS, IdBConstant.SO_STATUS_RECEIVED);
                } else {
                    poSoLink.setPoQtyReceived(poSoLink.getPoQtyReceived() + qtyRemain);
                    poSoLink.setSoLineQtyBalance(poSoLink.getSoLineQtyBalance() - qtyRemain);
                    status = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_SO_STATUS, IdBConstant.SO_STATUS_PARTIAL_REC);
                    qtyAssigned = qtyRemain;
                    qtyRemain = 0;
                }
                poSoLink.setPoSoStatus(status);
                //update po_so_link table
                poSoLinkDao.updateQtyReceived(poSoLink);
                //update sale order itself.
                //check if sale order status is not finalised. if finalised then we should not change its status and also
                //reserve the incoming goods that sale order
                final ConfigCategory txnStatus = txnDao.getTxnHeaderStatusByTxhdId(poSoLink.getTxhdId());
                if (txnStatus != null && txnStatus.getCategoryCode().equals(IdBConstant.SO_STATUS_FINAL)) {
                    continue;
                }
                final TxnDetail txnDetail = txnDao.getTxnDetailLightPerId(poSoLink.getTxdeId());
                if (txnDetail != null) {
                    //check status on txn detail
                    if ((txnDetail.getTxdeQtyReceived() + qtyAssigned) < txnDetail.getTxdeQtyOrdered()) {
                        status = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_SO_STATUS, IdBConstant.SO_STATUS_PARTIAL_REC);
                    } else {
                        status = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_SO_STATUS, IdBConstant.SO_STATUS_RECEIVED);
                    }
                    txnDao.updateTxnDetailRcvdQtyAndStatus(status.getId(), qtyAssigned + txnDetail.getTxdeQtyReceived(), poSoLink.getTxdeId());
                }
                //todo: we have reserved the qty in first place(on sale order). no need to reserve it again.
                //create stock event for linked sale order
                //updateStockQuantityForLinkedSaleOrder(deliveryNoteHeader, deliveryNoteLine, poSoLink);
                if (qtyRemain == 0) {
                    break;
                }
            }
            return true;

        } catch (Exception e) {
            logger.error("Error in updating purchase order line linked BOQ", e);
            return false;
        }
    }

    /**     * update stockQuantity.
     * @param deliveryNoteHeader deliveryNoteHeader
     * @param deliveryNoteLine deliveryNoteLine
     */
    private void updateStockQuantity(DeliveryNoteHeader deliveryNoteHeader, DeliveryNoteLine deliveryNoteLine) {
        try {
            //get current user from security context.
            final Principal principal = securityContext.getUserPrincipal();
            AppUser appUser = null;
            if (principal instanceof AppUser) {
                appUser = (AppUser) principal;
            }

            if (deliveryNoteHeader == null || deliveryNoteLine == null || !deliveryNoteHeader.getDelnStatus().getCategoryCode().equals(IdBConstant.DLV_NOTE_STATUS_COMPLETE)) {
                return;
            }
            String txnType = null;
            double quantity = 0;

            //First of all, goods need to transfer to the saleable stocks.
            txnType = IdBConstant.TXN_TYPE_GOODS_IN;
            quantity = deliveryNoteLine.getRcvdQty();
            final Timestamp currentTime = new Timestamp(new Date().getTime());
            final StockEvent stockEvent = new StockEvent();
            stockEvent.setTxnTypeConst(txnType);
            stockEvent.setStckQty(quantity);
            stockEvent.setUnomId(deliveryNoteLine.getRcvdCaseSize().getId());
            //stockEvent.setSupplierId(deliveryNoteLine.getPurchaseItem());
            stockEvent.setCostPrice(deliveryNoteLine.getDlnlUnitCost());
            stockEvent.setProdId(deliveryNoteLine.getPurchaseItem().getProdId());
            stockEvent.setSellPrice(deliveryNoteLine.getPurchaseItem().getPrice());
            stockEvent.setStckEvntDate(currentTime);
            stockEvent.setTxnDate(deliveryNoteHeader.getDelnCreatedDate());
            stockEvent.setTxnHeader(deliveryNoteHeader.getId());
            stockEvent.setTxnLine(deliveryNoteLine.getId());
            stockEvent.setUserId(appUser.getId());
            stockEvent.setTxnNumber(deliveryNoteHeader.getDelnGrn());
            stockEvent.setOrguId(sessionState.getOrgUnit().getId());
            logger.info("stock event type =" + stockEvent.getTxnTypeConst() + "qty =" + stockEvent.getStckQty() + " pushed to the queue");
            stockService.pushStockEvent(stockEvent);
        } catch (Exception e) {
            logger.error("Exception in creating stock event:", e);
        }
    }


    private void updateStockQuantityForLinkedBoq(DeliveryNoteHeader deliveryNoteHeader, DeliveryNoteLine deliveryNoteLine, PoBoqLink poBoqLink) {
        try {

            //get current user from security context.
            final Principal principal = securityContext.getUserPrincipal();
            AppUser appUser = null;
            if (principal instanceof AppUser) {
                appUser = (AppUser) principal;
            }

            if (deliveryNoteHeader == null || poBoqLink == null || deliveryNoteLine == null) {
                return;
            }
            String txnType = null;

            //now move from saleable stock to reserve stock for items has been reserved by BOQs.
            txnType = IdBConstant.TXN_TYPE_GOODS_RESERVE;
            final Timestamp currentTime = new Timestamp(new Date().getTime());
            final StockEvent stockEvent = new StockEvent();
            stockEvent.setTxnTypeReservedFor(IdBConstant.TXN_TYPE_BOQ);
            stockEvent.setTxnIdReservedFor(poBoqLink.getBoqId());
            stockEvent.setTxnNrReservedFor(poBoqLink.getBoqName());
            stockEvent.setTxnItemReservedFor(poBoqLink.getBoqDetailId());
            stockEvent.setTxnTypeConst(txnType);
            stockEvent.setStckQty(poBoqLink.getPoQtyReceived());
            stockEvent.setUnomId(deliveryNoteLine.getRcvdCaseSize().getId());
            //stockEvent.setSupplierId(deliveryNoteLine.getPurchaseItem());
            stockEvent.setCostPrice(deliveryNoteLine.getDlnlUnitCost());
            stockEvent.setProdId(deliveryNoteLine.getPurchaseItem().getProdId());
            stockEvent.setSellPrice(deliveryNoteLine.getPurchaseItem().getPrice());
            stockEvent.setStckEvntDate(currentTime);
            stockEvent.setTxnDate(deliveryNoteHeader.getDelnCreatedDate());
            stockEvent.setTxnHeader(deliveryNoteHeader.getId());
            stockEvent.setTxnLine(deliveryNoteLine.getId());
            stockEvent.setUserId(appUser.getId());
            stockEvent.setTxnNumber(deliveryNoteHeader.getDelnGrn());
            stockEvent.setOrguId(sessionState.getOrgUnit().getId());
            logger.info("stock event type =" + stockEvent.getTxnTypeConst() + "qty =" + stockEvent.getStckQty() + " pushed to the queue");
            stockService.pushStockEvent(stockEvent);
            logger.info("after pushed to the queue");
        } catch (Exception e) {
            logger.error("Exception in creating stock event:", e);
        }
    }

    private void updateStockQuantityForLinkedSaleOrder(DeliveryNoteHeader deliveryNoteHeader, DeliveryNoteLine deliveryNoteLine, PoSoLink poSoLink) {
        try {

            //get current user from security context.
            final Principal principal = securityContext.getUserPrincipal();
            AppUser appUser = null;
            if (principal instanceof AppUser) {
                appUser = (AppUser) principal;
            }

            if (deliveryNoteHeader == null || poSoLink == null || deliveryNoteLine == null) {
                return;
            }
            String txnType = null;

            //now move from saleable stock to reserve stock for items has been reserved by BOQs.
            txnType = IdBConstant.TXN_TYPE_GOODS_RESERVE;
            final Timestamp currentTime = new Timestamp(new Date().getTime());
            final StockEvent stockEvent = new StockEvent();
            stockEvent.setTxnTypeReservedFor(IdBConstant.TXN_TYPE_SALE);
            stockEvent.setTxnIdReservedFor(poSoLink.getTxhdId());
            //TODO: we need to store txn number
            stockEvent.setTxnNrReservedFor(String.valueOf(poSoLink.getTxhdId()));
            stockEvent.setTxnItemReservedFor(poSoLink.getTxdeId());
            stockEvent.setTxnTypeConst(txnType);
            stockEvent.setStckQty(poSoLink.getPoQtyReceived());
            stockEvent.setUnomId(deliveryNoteLine.getRcvdCaseSize().getId());
            //stockEvent.setSupplierId(deliveryNoteLine.getPurchaseItem());
            stockEvent.setCostPrice(deliveryNoteLine.getDlnlUnitCost());
            stockEvent.setProdId(deliveryNoteLine.getPurchaseItem().getProdId());
            stockEvent.setSellPrice(deliveryNoteLine.getPurchaseItem().getPrice());
            stockEvent.setStckEvntDate(currentTime);
            stockEvent.setTxnDate(deliveryNoteHeader.getDelnCreatedDate());
            stockEvent.setTxnHeader(deliveryNoteHeader.getId());
            stockEvent.setTxnLine(deliveryNoteLine.getId());
            stockEvent.setUserId(appUser.getId());
            stockEvent.setTxnNumber(deliveryNoteHeader.getDelnGrn());
            stockEvent.setOrguId(sessionState.getOrgUnit().getId());
            logger.info("stock event type =" + stockEvent.getTxnTypeConst() + "qty =" + stockEvent.getStckQty() + " pushed to the queue");
            stockService.pushStockEvent(stockEvent);
            logger.info("after pushed to the queue");
        } catch (Exception e) {
            logger.error("Exception in creating stock event:", e);
        }
    }
    /**
     * get all delivery note headers per supplier.
     * @param supplierId supplierId.
     * @return List of DeliveryNoteHeader
     */
    public List<DeliveryNoteHeader> getAllSuppliersDeliveryNotes(long supplierId) {
        try {
            return deliveryNoteDao.getAllDelNoteHeaderPerOrguIdAndSupplierId(sessionState.getOrgUnit().getId(), supplierId);
        } catch (Exception e) {
            logger.error("Exception in getting supplier's delivery note list:", e);
            return null;
        }
    }
    /**
     * search delivery note.
     * @param searchForm searchForm
     * @return List of delivery note
     */
    public List<DeliveryNoteHeader> searchDeliveryNote(GeneralSearchForm searchForm) {
        try {
            if (searchForm == null) {
                logger.error("search form is null");
                return null;
            }
            return deliveryNoteDao.searchDelNoteHeader(sessionState.getOrgUnit().getId(), SearchClauseBuilder.buildSearchWhereCluase(searchForm, "DELIVERY_NOTE_HEADER"));
        } catch (Exception e) {
            logger.error("Exception in searching delivery note:", e);
            return null;
        }
    }
    /**
     * search delivery note paging.
     * @param searchForm searchForm
     * @return List of Delivery Note
     */
    public GeneralSearchForm searchDeliveryNotePaging(GeneralSearchForm searchForm) {
        try {
            if (searchForm == null) {
                logger.error("search form is null");
                return null;
            }
            final long rowNoFrom = (searchForm.getPageNo() - 1) * searchForm.getPageSize() + 1;
            final long rowNoTo = rowNoFrom + searchForm.getPageSize() - 1;
            //final ProductSearchForm productSearchForm = new ProductSearchForm();
            final List<SearchClause> searchClauseList = SearchClauseBuilder.buildSearchWhereCluase(searchForm, "DELIVERY_NOTE_HEADER");
            searchForm.setResult(deliveryNoteDao.searchDelNoteHeaderPaging(sessionState.getOrgUnit().getId(), searchClauseList, rowNoFrom, rowNoTo));
            searchForm.setTotalRecords(deliveryNoteDao.getDelNoteHeaderQueryTotalRows(sessionState.getOrgUnit().getId(), searchClauseList));
            return searchForm;
        } catch (Exception e) {
            logger.error("Exception in searching delivery note:", e);
            return null;
        }
    }


}
