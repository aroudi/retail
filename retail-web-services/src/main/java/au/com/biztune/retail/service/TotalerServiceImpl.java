package au.com.biztune.retail.service;

import au.com.biztune.retail.dao.TotalerDao;
import au.com.biztune.retail.domain.*;
import au.com.biztune.retail.util.IdBConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by arash on 27/09/2016.
 */
@Component
public class TotalerServiceImpl implements TotalerService {
    @Autowired
    private TotalerDao totalerDao;
    private final Logger logger = LoggerFactory.getLogger(TotalerServiceImpl.class);

    /**
     * process transaction and extract total figures from it.
     * @param txnHeader txnHeader
     */
    public void extractTotalFiguresFromTxn(TxnHeader txnHeader) {
        try {
            if (txnHeader == null || txnHeader.getTxnDetails() == null) {
                logger.debug("Totaller : transaction is empty");
                return;
            }
            if (txnHeader.getTxhdTxnType().getCategoryCode().equals(IdBConstant.TXN_TYPE_INVOICE)) {
                processTotalSale(txnHeader);
            }
            //TOTAL MEDIA PROCESS
            processtotalMedia(txnHeader);

        } catch (Exception e) {
            logger.error("Error in extracting total figures from transaction", e);
        }
    }

    private void processTotalSale(TxnHeader txnHeader) {
        try {
            double totalSoldQty = 0.00;
            double totalItemsValue = 0.00;
            double totalProfitValue = 0.00;
            double totalTaxedValue = 0.00;
            double totalTaxdPaid = 0.00;
            double totalSaleValue = 0.00;

            final Timestamp currentDate = new Timestamp(new Date().getTime());


            final Map<String, TotalTaxGroup> totalTaxGroupMap = new HashMap<String , TotalTaxGroup>();

            double profitMargin = IdBConstant.DEFAULT_PROFIT_MARGIN;
            if (txnHeader.getCustomer() != null && txnHeader.getCustomer().getGrade() != null) {
                profitMargin = txnHeader.getCustomer().getGrade().getRate();
            }
            for (TxnDetail txnDetail : txnHeader.getTxnDetails()) {
                //skip voided items.
                //process only invoiced items.
                if (txnDetail == null || txnDetail.isTxdeItemVoid() || !txnDetail.isInvoiced()) {
                    continue;
                }
                //set profit margin on item
                txnDetail.setProfitMargin(profitMargin);

                if (txnDetail.getTxdeDetailType().getCategoryCode().equals(IdBConstant.TXN_LINE_TYPE_SALE)) {
                    totalSoldQty = totalSoldQty + txnDetail.getTxdeQtyInvoiced();
                    totalItemsValue = totalItemsValue + txnDetail.getItemCost() * txnDetail.getTxdeQtyInvoiced();
                    totalProfitValue = totalProfitValue + txnDetail.getItemCost() * profitMargin * txnDetail.getTxdeQtyInvoiced();
                    totalTaxedValue = totalTaxedValue + txnDetail.getItemGrossValue() * txnDetail.getTxdeQtyInvoiced();
                    totalTaxdPaid = totalTaxdPaid + txnDetail.getItemTaxPaid() * txnDetail.getTxdeQtyInvoiced();
                    totalSaleValue = totalSaleValue + (txnDetail.getItemGrossValue() + txnDetail.getItemTaxPaid()) * txnDetail.getTxdeQtyInvoiced();
                    extractTaxFiguresFromItem(txnDetail, totalTaxGroupMap);
                }
            }
            //save TotalSaleOperator.

            final TotalSaleOperator totalSaleOperator = new TotalSaleOperator();
            totalSaleOperator.setOrguId(txnHeader.getOrgUnit().getId());
            totalSaleOperator.setStoreId(txnHeader.getStore().getId());
            totalSaleOperator.setToopOperator(txnHeader.getUser());
            totalSaleOperator.setToopItemsValue(totalItemsValue);
            totalSaleOperator.setToopTradingDate(currentDate);
            totalSaleOperator.setToopProfitValue(totalProfitValue);
            totalSaleOperator.setToopSaleQty(totalSoldQty);
            totalSaleOperator.setToopSaleValue(totalSaleValue);
            totalSaleOperator.setToopTaxedValue(totalTaxedValue);
            totalSaleOperator.setToopTaxPaid(totalTaxdPaid);
            totalSaleOperator.setToopTxnType(txnHeader.getTxhdTxnType());

            totalerDao.insertTotalSaleOperator(totalSaleOperator);

            //save TotalTaxGroup

            for (TotalTaxGroup totalTaxGroup : totalTaxGroupMap.values()) {
                totalTaxGroup.setOrguId(txnHeader.getOrgUnit().getId());
                totalTaxGroup.setStoreId(txnHeader.getStore().getId());
                totalTaxGroup.setTotgTradingDate(currentDate);
                totalerDao.insertTotalTaxGroup(totalTaxGroup);
            }

        } catch (Exception e) {
            logger.error("Error in extracting process total sale", e);
        }
    }

    private void extractTaxFiguresFromItem(TxnDetail txnDetail, Map<String, TotalTaxGroup> itemTaxGroupMap) {

        TotalTaxGroup totalTaxGroup = null;
        for (TaxRule taxRule : txnDetail.getProduct().getProdOrguLink().getTaxRules()) {
            if (taxRule == null) {
                continue;
            }
            if (itemTaxGroupMap.containsKey(taxRule.getTaxLegVariance().getTxlvCode())) {
                totalTaxGroup = itemTaxGroupMap.get(taxRule.getTaxLegVariance().getTxlvCode());
                if (totalTaxGroup != null) {
                    totalTaxGroup.setTotgTaxedValue(totalTaxGroup.getTotgTaxedValue() + txnDetail.getItemGrossValue() * txnDetail.getTxdeQtyInvoiced());
                    totalTaxGroup.setTotgTax(totalTaxGroup.getTotgTax() + txnDetail.getItemGrossValue() * txnDetail.getTxdeQtyInvoiced() * taxRule.getTaxLegVariance().getTxlvRate());
                }
            } else {
                totalTaxGroup = new TotalTaxGroup();
                totalTaxGroup.setTotgTaxedValue(txnDetail.getItemGrossValue() * txnDetail.getTxdeQtyInvoiced());
                totalTaxGroup.setTotgTax(txnDetail.getItemGrossValue() * txnDetail.getTxdeQtyInvoiced() * taxRule.getTaxLegVariance().getTxlvRate());
                totalTaxGroup.setTotgTotSalesQty(txnDetail.getTxdeQtyInvoiced());
                totalTaxGroup.setTaxLegVariance(taxRule.getTaxLegVariance());
                totalTaxGroup.setTotgTxgpCode(taxRule.getTaxLegVariance().getTxlvCode());
                itemTaxGroupMap.put(taxRule.getTaxLegVariance().getTxlvCode(), totalTaxGroup);
            }
        }
    }

    private void processtotalMedia(TxnHeader txnHeader) {
        try {
            //extract media values used for this event
            final Map<PaymentMedia, Double[]> totalMediaVals = new HashMap<PaymentMedia, Double[]>();
            Double[] values = null;
            for (TxnMedia txnMedia : txnHeader.getTxnMedias()) {
                //only new added media should be count.
                if (txnMedia == null || !txnMedia.isNewAdded() || txnMedia.isTxmdVoided()) {
                    continue;
                }
                if (totalMediaVals.containsKey(txnMedia.getPaymentMedia())) {
                    values = totalMediaVals.get(txnMedia.getPaymentMedia());
                    values[0] = values[0] + txnMedia.getCount();
                    values[1] = values[1] + txnMedia.getValue();
                } else {
                    values = new Double[2];
                    values[0] = (double) txnMedia.getCount();
                    values[1] = txnMedia.getValue();
                    totalMediaVals.put(txnMedia.getPaymentMedia(), values);
                }
            }

            TotalMediaOperator totalMediaOperator;
            final Timestamp currentDate = new Timestamp(new Date().getTime());
            for (PaymentMedia paymentMedia : totalMediaVals.keySet()) {
                values = totalMediaVals.get(paymentMedia);
                totalMediaOperator = new TotalMediaOperator();
                totalMediaOperator.setStoreId(txnHeader.getStore().getId());
                totalMediaOperator.setOrguId(txnHeader.getOrgUnit().getId());
                totalMediaOperator.setMedtId(paymentMedia.getMediaType().getId());
                totalMediaOperator.setPaymentMedia(paymentMedia);
                totalMediaOperator.setTomoOperator(txnHeader.getUser());
                totalMediaOperator.setTomoSaleQty(values[0]);
                totalMediaOperator.setTomoSaleValue(values[1]);
                totalMediaOperator.setTomoTradingDate(currentDate);
                totalerDao.insertTotalMediaOperator(totalMediaOperator);
            }

        } catch (Exception e) {
            logger.error("Exception in processing total media", e);
        }
    }

}
