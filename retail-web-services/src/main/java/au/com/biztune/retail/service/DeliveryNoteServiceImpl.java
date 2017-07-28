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
import java.util.Map;
import java.util.stream.Collectors;

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
                if (deliveryNoteLine == null) {
                    continue;
                }
                purchaseLine = purchaseOrderDao.getPurchaseLinePerId(deliveryNoteLine.getPolId());
                //check if PurchaseOrderLine is already received. if so skip it
                if (purchaseLine.getPolStatus() != null && purchaseLine.getPolStatus().getCategoryCode().equals(IdBConstant.POH_STATUS_GOOD_RECEIVED)) {
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
                    updatePurchaseLineLinkedBoq(purchaseLine);
                    //update linked sale orders
                    updatePurchaseLineLinkedSaleOrder(purchaseLine);
                }
                //update stock qty.
                updateStockQuantity(deliveryNoteHeader, deliveryNoteLine, purchaseLine);
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
            updatePurchaseOrderLinkedSaleOrderStatus(deliveryNoteHeader.getPohId());
            //update linked boq status.
            updatePurchaseOrderLinkedBoqStatus(deliveryNoteHeader.getPohId());
            return true;

        } catch (Exception e) {
            logger.error("Exception in updating related purchase order", e);
            return false;
        }
    }

    private boolean updatePurchaseLineLinkedBoq(PurchaseLine purchaseLine) {
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
                boqDetailDao.updateQtyAndStatus(poBoqLink.getPoQtyReceived(), poBoqLink.getBoqQtyBalance(), poBoqLink.getStatus().getId(), poBoqLink.getBoqId());
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

    private boolean updatePurchaseLineLinkedSaleOrder(PurchaseLine purchaseLine) {
        try {
            if (purchaseLine == null) {
                return false;
            }
            final List<PoSoLink> linkedSoList = poSoLinkDao.getAllPoSoLinkPerPolId(purchaseLine.getId());
            final double qtyReceived = purchaseLine.getPolQtyReceived();
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
                txnDao.updateTxnDetailRcvdQtyAndStatus(status.getId(), qtyAssigned, poSoLink.getTxdeId());
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

    /**
     * update order status of linked sales orders.
     * @param pohId purchaes order header id.
     */
    private void updatePurchaseOrderLinkedSaleOrderStatus(long pohId) {
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

                //if all items received
                if (receivedCount == saleOrderLineList.size()) {
                    final ConfigCategory newStatus = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_SO_STATUS, IdBConstant.SO_STATUS_RECEIVED);
                    txnDao.updateTxnHeaderStatusPerTxhdId(extractedtxhdId, newStatus.getId());
                } else if (partialReceivedCount > 0) {
                    final ConfigCategory newStatus = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_SO_STATUS, IdBConstant.SO_STATUS_PARTIAL_REC);
                    txnDao.updateTxnHeaderStatusPerTxhdId(extractedtxhdId, newStatus.getId());
                } else if (outsTandingCount > 0) {
                    final ConfigCategory newStatus = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_SO_STATUS, IdBConstant.SO_STATUS_OUTSTANDING);
                    txnDao.updateTxnHeaderStatusPerTxhdId(extractedtxhdId, newStatus.getId());
                }
            });

        } catch (Exception e) {
            logger.error("Exception in updating linked po sale order", e);
        }
    }


    /**
     * update status of linked BOQ.
     * @param pohId purchaes order header id.
     */
    private void updatePurchaseOrderLinkedBoqStatus(long pohId) {
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

                //if all items received
                if (receivedCount == boqDetailList.size()) {
                    final ConfigCategory newStatus = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_BOQ_STATUS, IdBConstant.BOQ_STATUS_RECEIVED);
                    billOfQuantityDao.updateBoqStatusPerId(extractedBoqId, newStatus.getId());
                } else if (partialReceivedCount > 0 || receivedCount > 0) {
                    final ConfigCategory newStatus = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_BOQ_STATUS, IdBConstant.BOQ_STATUS_PARTIAL_REC);
                    billOfQuantityDao.updateBoqStatusPerId(extractedBoqId, newStatus.getId());
                }
            });

        } catch (Exception e) {
            logger.error("Exception in updating linked po sale order", e);
        }
    }

    /**
     * update stockQuantity.
     * @param deliveryNoteHeader deliveryNoteHeader
     * @param deliveryNoteLine deliveryNoteLine
     * @param linkedPurchaseLine linkedPurchaseLine
     */
    private void updateStockQuantity(DeliveryNoteHeader deliveryNoteHeader, DeliveryNoteLine deliveryNoteLine, PurchaseLine linkedPurchaseLine) {
        try {
            //get current user from security context.
            final Principal principal = securityContext.getUserPrincipal();
            AppUser appUser = null;
            if (principal instanceof AppUser) {
                appUser = (AppUser) principal;
            }

            if (deliveryNoteHeader == null || linkedPurchaseLine == null || deliveryNoteLine == null) {
                return;
            }
            String txnType = null;
            double quantity = 0;

            //First of all, goods need to transfer to the saleable stocks.
            txnType = IdBConstant.TXN_TYPE_GOODS_IN;
            quantity = deliveryNoteLine.getRcvdQty();
            Timestamp currentTime = new Timestamp(new Date().getTime());
            StockEvent stockEvent = new StockEvent();
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

            //now move from saleable stock to reserve stock for items has been reserved by BOQs.
            txnType = IdBConstant.TXN_TYPE_GOODS_RESERVE;
            if (deliveryNoteLine.getRcvdQty() <= linkedPurchaseLine.getPolQtyReserved()) {
                quantity = deliveryNoteLine.getRcvdQty();
            } else if (deliveryNoteLine.getRcvdQty() > linkedPurchaseLine.getPolQtyReserved()) {
                quantity = linkedPurchaseLine.getPolQtyReserved();
            }
            currentTime = new Timestamp(new Date().getTime());
            stockEvent = new StockEvent();
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
            logger.info("after pushed to the queue");
            //update reserved qty on purchase order
            linkedPurchaseLine.setPolQtyReserved(linkedPurchaseLine.getPolQtyReserved() - quantity);
            purchaseOrderDao.updatePurchaseLineReserveQty(linkedPurchaseLine);
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
