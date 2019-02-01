package au.com.biztune.retail.service;

import au.com.biztune.retail.dao.AccountingDao;
import au.com.biztune.retail.dao.ConfigCategoryDao;
import au.com.biztune.retail.dao.StockDao;
import au.com.biztune.retail.domain.*;
import au.com.biztune.retail.processor.StockProcessor;
import au.com.biztune.retail.processor.StockRequest;
import au.com.biztune.retail.session.SessionState;
import au.com.biztune.retail.util.IdBConstant;
import au.com.biztune.retail.util.queuemanager.QueueManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * Created by arash on 25/07/2016.
 */
@Component
public class StockServiceImpl implements StockService {
    private final Logger logger = LoggerFactory.getLogger(StockServiceImpl.class);
    @Autowired
    private StockDao stockDao;
    @Autowired
    private ConfigCategoryDao configCategoryDao;
    @Autowired
    private SessionState sessionState;
    @Autowired
    private StockProcessor stockProcessor;
    @Autowired
    private QueueManager stockProcessingQueue;
    @Autowired
    private AccountingDao accountingDao;
    @Autowired
    private CashSessionService cashSessionService;


    /**
     * update stock quantity.
     * @param txnHeader txnHeader
     */
    public void processTxnForStockUpdate(TxnHeader txnHeader) {
        try {
            //update stock only for txn_invoice and sale order.
            if (!(txnHeader.getTxhdTxnType().getCategoryCode().equals(IdBConstant.TXN_TYPE_SALE)
                    || txnHeader.getTxhdTxnType().getCategoryCode().equals(IdBConstant.TXN_TYPE_INVOICE)
                    || txnHeader.getTxhdTxnType().getCategoryCode().equals(IdBConstant.TXN_TYPE_REFUND)))
            {
                return;
            }

            String txnType = null;
            txnType = txnHeader.getTxhdTxnType().getCategoryCode();

            final Timestamp currentTime = new Timestamp(new Date().getTime());
            for (TxnDetail txnDetail:txnHeader.getTxnDetails()) {
                if (txnDetail == null) {
                    continue;
                }

                final StockEvent stockEvent = new StockEvent();
                stockEvent.setTxnTypeConst(txnType);
                if (txnType.equals(IdBConstant.TXN_TYPE_INVOICE)) {
                    stockEvent.setTxnTypeReservedFor(IdBConstant.TXN_TYPE_SALE);
                    //we need to refer to txn sale this invoice created from.
                    stockEvent.setTxnNrReservedFor(txnHeader.getTxhdOrigTxnNr());
                    stockEvent.setTxnIdReservedFor(txnHeader.getParentId());
                    //stockEvent.setTxnItemReservedFor(txnDetail.getId());
                    //for invoice we need to check if item original quantity has changed or not. if changed, we need to create 2 stock event. one for reseving goods and another for selling goods.
                    if (txnDetail.getOriginalQuantity() != txnDetail.getTxdeQuantitySold()) {
                        final StockEvent stockEvent2 = new StockEvent();
                        //we only need to reserve those available on stock. (available on stock = qtySold - qtyBackOrdered)
                        final double quantity = txnDetail.getTxdeQuantitySold() - txnDetail.getOriginalQuantity();
                        /*;
                        if ((txnDetail.getTxdeQuantitySold() - txnDetail.getOriginalQuantity()) >= txnDetail.getProduct().getStockQty()) {
                            quantity = txnDetail.getProduct().getStockQty();
                        } else {
                            quantity = txnDetail.getTxdeQuantitySold() - txnDetail.getOriginalQuantity();
                        }
                        */
                        stockEvent2.setStckQty(quantity);
                        //if item is voided then return the item to saleable stock.
                        if (txnDetail.isTxdeItemVoid()) {
                            stockEvent2.setStckQty(-txnDetail.getOriginalQuantity());
                        }
                        //stockEvent2.setUnomId(txnDetail.getUnitOfMeasure().getId());
                        //stockEvent.setSupplierId();
                        stockEvent2.setTxnTypeReservedFor(IdBConstant.TXN_TYPE_SALE);
                        //we need to refer to txn sale this invoice created from.
                        stockEvent2.setTxnNrReservedFor(txnHeader.getTxhdOrigTxnNr());
                        stockEvent2.setTxnIdReservedFor(txnHeader.getParentId());
                        stockEvent2.setCostPrice(txnDetail.getTxdeValueLine());
                        stockEvent2.setProdId(txnDetail.getProduct().getId());
                        stockEvent2.setSellPrice(txnDetail.getTxdeValueNet());
                        stockEvent2.setStckEvntDate(currentTime);
                        stockEvent2.setTxnDate(txnHeader.getTxhdTradingDate());
                        stockEvent2.setTxnHeader(txnHeader.getId());
                        stockEvent2.setTxnLine(txnDetail.getId());
                        stockEvent2.setUserId(txnHeader.getUser().getId());
                        stockEvent2.setTxnNumber(txnHeader.getTxhdTxnNr());
                        stockEvent2.setOrguId(sessionState.getOrgUnit().getId());
                        stockEvent2.setTxnTypeConst(IdBConstant.TXN_TYPE_SALE);
                        processStockEvent(stockEvent2);
                    }
                    stockEvent.setStckQty(txnDetail.getTxdeQtyInvoiced());

                } else if (txnType.equals(IdBConstant.TXN_TYPE_SALE)) {
                    final double quantity = txnDetail.getTxdeQuantitySold() - txnDetail.getOriginalQuantity();
                    /*
                    if ((txnDetail.getTxdeQuantitySold() - txnDetail.getOriginalQuantity()) >= txnDetail.getProduct().getStockQty()) {
                        quantity = txnDetail.getProduct().getStockQty();
                    } else {
                        quantity = txnDetail.getTxdeQuantitySold() - txnDetail.getOriginalQuantity();
                    }
                    */
                    stockEvent.setStckQty(quantity);
                    stockEvent.setTxnTypeReservedFor(IdBConstant.TXN_TYPE_SALE);
                    stockEvent.setTxnNrReservedFor(txnHeader.getTxhdTxnNr());
                    stockEvent.setTxnIdReservedFor(txnHeader.getId());
                    stockEvent.setTxnItemReservedFor(txnDetail.getId());
                    //if item is voided then return the item to saleable stock.
                    if (txnDetail.isTxdeItemVoid()) {
                        stockEvent.setStckQty(-txnDetail.getOriginalQuantity());
                    }
                } else if (txnType.equals(IdBConstant.TXN_TYPE_REFUND)) {
                    stockEvent.setStckQty(txnDetail.getTxdeQtyRefund());
                }
                //stockEvent.setUnomId(txnDetail.getUnitOfMeasure().getId());
                //stockEvent.setSupplierId();
                stockEvent.setCostPrice(txnDetail.getTxdeValueLine());
                stockEvent.setProdId(txnDetail.getProduct().getId());
                stockEvent.setSellPrice(txnDetail.getTxdeValueNet());
                stockEvent.setStckEvntDate(currentTime);
                stockEvent.setTxnDate(txnHeader.getTxhdTradingDate());
                stockEvent.setTxnHeader(txnHeader.getId());
                stockEvent.setTxnLine(txnDetail.getId());
                stockEvent.setUserId(txnHeader.getUser().getId());
                stockEvent.setTxnNumber(txnHeader.getTxhdTxnNr());
                stockEvent.setOrguId(sessionState.getOrgUnit().getId());
                processStockEvent(stockEvent);
            }
        } catch (Exception e) {
            logger.error("Exception in creating stock event:", e);
        }
    }


    /**
     * process stock event.
     * @param stockEvent stockEvent
     */
    public void processStockEvent(StockEvent stockEvent) {
        try {

            if (stockEvent == null) {
                logger.debug("StockEvent object is null");
                return;
            }
            if (stockEvent.getStckQty() == 0.00) {
                logger.debug("StockQty is 0");
                return;
            }
            //check if event is change price
            if (stockEvent.getTxnTypeConst().equals(IdBConstant.TXN_TYPE_CHANGE_IN_COST)) {
                processCostVarianceEvent(stockEvent);
                return;
            }

            //set stock location
            stockEvent.setStckLocId(sessionState.getOrgUnit().getStockLocation().getId());
            //indicate stock category from txnType.
            ConfigCategory stockCategory = null;
            ConfigCategory stockCondition = null;
            ConfigCategory txnType = null;
            txnType = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_TXN_TYPE, stockEvent.getTxnTypeConst());
            stockEvent.setTxnType(txnType.getId());
            //TODO: for timebeing we consider all goods as pristine. we need to decide the stock condition per reason code.
            stockCondition = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_STOCK_CONDITION, IdBConstant.STOCK_CONDITION_PRISTINE);
            stockEvent.setStckCond(stockCondition.getId());
            double quantity = 0.00;
            switch (stockEvent.getTxnTypeConst()) {
                case IdBConstant.TXN_TYPE_GOODS_IN :
                    //add products to SALEABLE category
                    stockCategory = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_STOCK_CATEGORY, IdBConstant.STOCK_CATEGORY_SALEABLE);
                    stockEvent.setStckCat(stockCategory.getId());
                    updateStockQty(stockEvent);
                    break;
                //todo: TXN_TYPE_GOODS_IN_TRANSIT in stock figures need more analysis
                case IdBConstant.TXN_TYPE_GOODS_IN_TRANSIT :
                    //add to TRANSIT category
                    stockCategory = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_STOCK_CATEGORY, IdBConstant.STOCK_CATEGORY_IN_TRANSIT);
                    stockEvent.setStckCat(stockCategory.getId());
                    updateStockQty(stockEvent);
                    break;
                case IdBConstant.TXN_TYPE_GOODS_RESERVE :
                    //add to reserve category
                    quantity = stockEvent.getStckQty();
                    stockCategory = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_STOCK_CATEGORY, IdBConstant.STOCK_CATEGORY_RESERVED);
                    stockEvent.setStckCat(stockCategory.getId());
                    updateStockQty(stockEvent);
                    //remove from saleable category
                    stockCategory = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_STOCK_CATEGORY, IdBConstant.STOCK_CATEGORY_SALEABLE);
                    stockEvent.setStckCat(stockCategory.getId());
                    stockEvent.setStckQty(-quantity);
                    updateStockQty(stockEvent);
                    break;
                case IdBConstant.TXN_TYPE_GOODS_CANCEL_RESERVE :
                    //return to saleable category
                    quantity = stockEvent.getStckQty();
                    stockCategory = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_STOCK_CATEGORY, IdBConstant.STOCK_CATEGORY_RESERVED);
                    stockEvent.setStckCat(stockCategory.getId());
                    stockEvent.setStckQty(-quantity);
                    updateStockQty(stockEvent);
                    //
                    stockCategory = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_STOCK_CATEGORY, IdBConstant.STOCK_CATEGORY_SALEABLE);
                    stockEvent.setStckCat(stockCategory.getId());
                    stockEvent.setStckQty(quantity);
                    updateStockQty(stockEvent);
                    break;
                case IdBConstant.TXN_TYPE_INVOICE :
                    quantity = stockEvent.getStckQty();
                    stockCategory = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_STOCK_CATEGORY, IdBConstant.STOCK_CATEGORY_RESERVED);
                    stockEvent.setStckCat(stockCategory.getId());
                    stockEvent.setStckQty(-quantity);
                    updateStockQty(stockEvent);

                    break;
                case IdBConstant.TXN_TYPE_SALE :
                    quantity = stockEvent.getStckQty();
                    stockCategory = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_STOCK_CATEGORY, IdBConstant.STOCK_CATEGORY_SALEABLE);
                    stockEvent.setStckQty(-quantity);
                    stockEvent.setStckCat(stockCategory.getId());
                    updateStockQty(stockEvent);

                    stockCategory = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_STOCK_CATEGORY, IdBConstant.STOCK_CATEGORY_RESERVED);
                    stockEvent.setStckCat(stockCategory.getId());
                    stockEvent.setStckQty(quantity);
                    updateStockQty(stockEvent);

                    break;
                case IdBConstant.TXN_TYPE_REFUND :
                    //TODO: for txn_refund we need to engage the reason to indicate which stock category should goes in.
                    stockCategory = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_STOCK_CATEGORY, IdBConstant.STOCK_CATEGORY_SALEABLE);
                    stockEvent.setStckCat(stockCategory.getId());
                    updateStockQty(stockEvent);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            logger.error("Exception in processing stock event:", e);
        }
    }
    /**
     * process stock event.
     * @param stockEvent stockEvent
     */
    public void pushStockEvent (StockEvent stockEvent) {
        try {
            final StockRequest stockRequest = new StockRequest();
            stockRequest.setProcessor(stockProcessor);
            stockRequest.setStockEvent(stockEvent);
            stockProcessingQueue.push(stockRequest);
        } catch (Exception e) {
            logger.error("Exception in processing stock event:", e);
        }
    }

    /**
     * check the saleable pristine stock quantity for specific product.
     * @param productId productId
     * @return Stock
     */
    public Stock checkStockForSaleableProduct(long productId) {
        try {
            final ConfigCategory stockCatSaleable = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_STOCK_CATEGORY, IdBConstant.STOCK_CATEGORY_SALEABLE);
            final ConfigCategory stockCondPristine = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_STOCK_CONDITION, IdBConstant.STOCK_CONDITION_PRISTINE);
            return stockDao.checkStockForProduct(productId, sessionState.getOrgUnit().getId(), stockCondPristine.getId()
                    , stockCatSaleable.getId(), sessionState.getOrgUnit().getStockLocation().getId());
        } catch (Exception e) {
            logger.error("Exception in checking stock for specific saleable product:", e);
            return null;
        }
    }

    /**
     * update stock qty per stock event.
     * @param stockEvent stockEvent
     */
    private void updateStockQty(StockEvent stockEvent) {
        try {
            //check if we can find product in stock. if found modify stock qty otherwise add it to the stock.
            Stock stock = stockDao.checkStockForProduct(stockEvent.getProdId(), stockEvent.getOrguId(), stockEvent.getStckCond(), stockEvent.getStckCat(), stockEvent.getStckLocId());
            if (stock == null) {
                stock = new Stock();
                stock.setOrguIdLocation(sessionState.getOrgUnit().getId());
                stock.setOrguIdRespbility(sessionState.getOrgUnit().getId());
                stock.setPrgpId(stockEvent.getPrgpId());
                stock.setProdId(stockEvent.getProdId());
                stock.setSelcId(stockEvent.getSelcId());
                stock.setStckCat(stockEvent.getStckCat());
                stock.setStckCond(stockEvent.getStckCond());
                stock.setStckLocnId(stockEvent.getStckLocId());
                stock.setSupplierId(stockEvent.getSupplierId());
                stock.setUnomId(stockEvent.getUnomId());
                stock.setStckQty(stockEvent.getStckQty());
                stockDao.insertStock(stock);
            } else {
                stock.setStckQty(stock.getStckQty() + stockEvent.getStckQty());
                stockDao.updateStockQty(stock);
            }
            //insert stock event
            stockEvent.setStckId(stock.getId());
            stockDao.insertStockEvent(stockEvent);
            //----------------- for category reserve, update the reserved figures -----------------------
            final ConfigCategory stockCategory = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_STOCK_CATEGORY, IdBConstant.STOCK_CATEGORY_RESERVED);
            if (stockEvent.getStckCat() != stockCategory.getId()) {
                return;
            }
            //insert stock reserve
            //first search for existing entry in reserve
            if (stockEvent.getTxnTypeReservedFor() != null
                    && (stockEvent.getTxnTypeReservedFor().equals(IdBConstant.TXN_TYPE_SALE)
                            || stockEvent.getTxnTypeReservedFor().equals(IdBConstant.TXN_TYPE_BOQ)))
            {
                StockReserve stockReserve = stockDao.getStockReservePerOrguIdAndProdIdAndTxnTypeAndTxnId(stockEvent.getOrguId()
                        , stockEvent.getProdId(), stockEvent.getTxnTypeReservedFor(), stockEvent.getTxnIdReservedFor());
                if (stockReserve == null) {
                    stockReserve = new StockReserve();
                    stockReserve.setOrguId(stockEvent.getOrguId());
                    stockReserve.setPrgpId(stockEvent.getPrgpId());
                    stockReserve.setProdId(stockEvent.getProdId());
                    stockReserve.setStckId(stockEvent.getStckId());
                    stockReserve.setStoreId(stockEvent.getStoreId());
                    stockReserve.setSupplierId(stockEvent.getSupplierId());
                    stockReserve.setTxnType(stockEvent.getTxnTypeReservedFor());
                    stockReserve.setTxnHeaderId(stockEvent.getTxnIdReservedFor());
                    stockReserve.setTxnLineId(stockEvent.getTxnItemReservedFor());
                    stockReserve.setTxnNumber(stockEvent.getTxnNrReservedFor());
                    stockReserve.setQtyReserved(stockEvent.getStckQty());
                    stockDao.insertStockReserve(stockReserve);
                } else {
                    stockDao.updateStockReservePerId(stockReserve.getId(), stockReserve.getQtyReserved() + stockEvent.getStckQty());
                }
            }
        } catch (Exception e) {
            logger.error("Exception in updating stock qty:", e);
        }

    }

    /**
     * process cost variance stock event.
     * the value for price change is set in stock event as below:
     * txnType = 'SESSION_EVENT_TYPE_CHANGE_IN_COST'
     * stckQty = qty of product being affected by change price
     * costPrice = product variance for qty 1 (new price - old price)
     * sellPrice = stock variance for whole qty ( qty * (new price - old price))
     * @param stockEvent stockEvent
     */
    private void processCostVarianceEvent(StockEvent stockEvent) {
        try {
            if (stockEvent == null) {
                return;
            }
            //add stock event for reference.
            final ConfigCategory sourceTxnType = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_TXN_TYPE, IdBConstant.TXN_TYPE_CHANGE_IN_COST);
            if (sourceTxnType != null) {
                stockEvent.setTxnType(sourceTxnType.getId());
            }
            stockDao.insertStockEvent(stockEvent);
            //insert journal entry.
            final CashSession activeCashSession = cashSessionService.getActiveCashSession(stockEvent.getUserId());
            final List<JournalRule> journalRuleList = accountingDao.getJournalRuleByOrguIdAndTxnTypeAndAction(sessionState.getOrgUnit().getId(),
                    "TXN_TYPE_CHANGE_IN_COST", "JA_CHANGE_IN_COST");
            JournalEntry journalEntry = null;
            if (journalRuleList != null && journalRuleList.size() > 0) {
                for (JournalRule journalRule : journalRuleList) {
                    if (journalRule == null) {
                        continue;
                    }
                    journalEntry = new JournalEntry();
                    if (sourceTxnType != null) {
                        journalEntry.setSrcTxnType(sourceTxnType.getId());
                    }
                    //journalEntry.setSrcTxnId(txnHeader.getId());
                    journalEntry.setAppUserId(stockEvent.getUserId());
                    journalEntry.setOrguId(sessionState.getOrgUnit().getId());
                    if (activeCashSession != null) {
                        journalEntry.setCssnSessionId(activeCashSession.getId());
                    }
                    journalEntry.setJrnActual(journalRule.isJrnrActual());
                    journalEntry.setJrnDate(new Timestamp(new Date().getTime()));
                    journalEntry.setAccId(journalRule.getAccount().getId());
                    journalEntry.setJrnAccCode(journalRule.getJrnrAccCode());
                    journalEntry.setJrnAccDesc(journalRule.getJrnrAccDesc());
                    //if (variance is positive)
                    if (stockEvent.getSellPrice() > 0) {
                        if (journalRule.isJrnrDebit()) {
                            journalEntry.setJrnDebit(stockEvent.getSellPrice());
                        } else if (journalRule.isJrnrCredit()) {
                            journalEntry.setJrnCredit(stockEvent.getSellPrice());
                        }
                    } else {
                        if (journalRule.isJrnrDebit()) {
                            journalEntry.setJrnCredit(stockEvent.getSellPrice());
                        } else if (journalRule.isJrnrCredit()) {
                            journalEntry.setJrnDebit(stockEvent.getSellPrice());
                        }
                    }
                    accountingDao.insertJournalEntry(journalEntry);

                }
            }

        } catch (Exception e) {
            logger.debug("Exception in processing cost variance event", e);
        }
    }

    /**
     * create cost variance event.
     * @param prodId prodId
     * @param oldPrice oldPrice
     * @param newPrice newPrice
     * @param qty qty
     * @param userId userId
     */
    public void createCostVarianceEvent(long prodId, double oldPrice, double newPrice, double qty, long userId) {
        //if there is no change in price then return.
        if ((newPrice == oldPrice) || qty == 0) {
            return;
        }
        final StockEvent stockEvent = new StockEvent();
        stockEvent.setTxnTypeConst(IdBConstant.TXN_TYPE_CHANGE_IN_COST);
        stockEvent.setCostPrice(newPrice - oldPrice);
        stockEvent.setStckQty(qty);
        stockEvent.setProdId(prodId);
        stockEvent.setSellPrice(qty * stockEvent.getCostPrice());
        stockEvent.setOrguId(sessionState.getOrgUnit().getId());
        stockEvent.setStoreId(sessionState.getStore().getId());
        stockEvent.setStckEvntDate(new Timestamp(new Date().getTime()));
        stockEvent.setUserId(userId);
        pushStockEvent(stockEvent);
    }

    /**
     * view product audit trail.
     * @param prodId prodId
     * @return List of StockEvent
     */
    public List<StockEvent> viewProductAuditTrail(long prodId) {
        try {
            return stockDao.getProductAuditTrail(prodId, sessionState.getOrgUnit().getId());
        } catch (Exception e) {
            logger.error("Exception in returning product audit trail:", e);
            return null;
        }
    }

}
