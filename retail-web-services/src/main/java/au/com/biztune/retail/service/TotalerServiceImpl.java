package au.com.biztune.retail.service;

import au.com.biztune.retail.dao.CustomerGradeDao;
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
    @Autowired
    private CustomerGradeDao customerGradeDao;
    private final Logger logger = LoggerFactory.getLogger(TotalerServiceImpl.class);

    /**
     * process transaction and extract total figures from it.
     * @param txnHeader txnHeader
     */
    public void extractTotalFiguresFromTxn(TxnHeader txnHeader) {
        try {
            if (txnHeader == null) {
                logger.debug("Totaller : transaction is empty");
                return;
            }
            processTotalSale(txnHeader);
            //TOTAL MEDIA PROCESS
            processtotalMedia(txnHeader);

        } catch (Exception e) {
            logger.error("Error in extracting total figures from transaction", e);
        }
    }

    private void processTotalSale(TxnHeader txnHeader) {
        try {
            if (txnHeader.getTxnDetails() == null) {
                return;
            }
            double totalSoldQty = 0.00;
            double totalItemsValue = 0.00;
            double totalProfitValue = 0.00;
            double totalTaxedValue = 0.00;
            double totalTaxdPaid = 0.00;
            double totalSaleValue = 0.00;
            double totalRefundValue = 0.00;
            double totalRefundQty = 0.00;

            final Timestamp currentDate = new Timestamp(new Date().getTime());


            final Map<String, TotalTaxGroup> totalTaxGroupMap = new HashMap<String , TotalTaxGroup>();

            for (TxnDetail txnDetail : txnHeader.getTxnDetails()) {
                //skip voided items.
                //process only invoiced items.
                if (txnDetail == null || txnDetail.isTxdeItemVoid() || !txnDetail.isSelected()) {
                    continue;
                }

                if (txnDetail.getTxdeDetailType().getCategoryCode().equals(IdBConstant.TXN_LINE_TYPE_SALE)
                         || txnDetail.getTxdeDetailType().getCategoryCode().equals(IdBConstant.TXN_LINE_TYPE_REFUND))
                {
                    if (txnDetail.getTxdeDetailType().getCategoryCode().equals(IdBConstant.TXN_LINE_TYPE_SALE)) {
                        totalSoldQty = totalSoldQty + txnDetail.getQuantity();
                    }
                    if (txnDetail.getTxdeDetailType().getCategoryCode().equals(IdBConstant.TXN_LINE_TYPE_REFUND)) {
                        totalRefundQty = totalRefundQty + txnDetail.getQuantity();
                    }
                    totalItemsValue = totalItemsValue + txnDetail.getTxdePriceSold();
                    totalProfitValue = totalProfitValue + ((txnDetail.getTxdeValueGross() - txnDetail.getItemCost()) * txnDetail.getQuantity());
                    totalTaxedValue = totalTaxedValue + txnDetail.getTxdePriceSold();
                    totalTaxdPaid = totalTaxdPaid + ((txnDetail.getTxdeValueNet() - txnDetail.getTxdeValueGross()) * txnDetail.getQuantity());
                    if (txnDetail.getTxdeDetailType().getCategoryCode().equals(IdBConstant.TXN_LINE_TYPE_SALE)) {
                        totalSaleValue = totalSaleValue + txnDetail.getTxdePriceSold();
                    }
                    if (txnDetail.getTxdeDetailType().getCategoryCode().equals(IdBConstant.TXN_LINE_TYPE_REFUND)) {
                        totalRefundValue = totalRefundValue + txnDetail.getTxdePriceSold() * txnDetail.getQuantity();
                    }

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
            totalSaleOperator.setToopRefundQty(totalRefundQty);
            totalSaleOperator.setToopSaleValue(totalSaleValue);
            totalSaleOperator.setToopRefundValue(totalRefundValue);
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
                    totalTaxGroup.setTotgTaxedValue(totalTaxGroup.getTotgTaxedValue() + txnDetail.getTxdePriceSold());
                    totalTaxGroup.setTotgTax(totalTaxGroup.getTotgTax() + (txnDetail.getTxdeValueGross() * taxRule.getTaxLegVariance().getTxlvRate()));
                }
            } else {
                totalTaxGroup = new TotalTaxGroup();
                totalTaxGroup.setTotgTaxedValue(txnDetail.getTxdePriceSold());
                totalTaxGroup.setTotgTax(txnDetail.getTxdeValueGross() * taxRule.getTaxLegVariance().getTxlvRate());
                totalTaxGroup.setTotgTotSalesQty(txnDetail.getQuantity());
                totalTaxGroup.setTaxLegVariance(taxRule.getTaxLegVariance());
                totalTaxGroup.setTotgTxgpCode(taxRule.getTaxLegVariance().getTxlvCode());
                itemTaxGroupMap.put(taxRule.getTaxLegVariance().getTxlvCode(), totalTaxGroup);
            }
        }
    }

    private void processtotalMedia(TxnHeader txnHeader) {
        try {
            if (txnHeader.getTxnMedias() == null) {
                return;
            }
            //extract media values used for this event
            final Map<PaymentMedia, Double[]> totalMediaVals = new HashMap<PaymentMedia, Double[]>();
            /**
             * double[0] : sale count  double[1]: sale amount double[2]: refund cound  double[3]: refund value
             */
            Double[] values = null;
            for (TxnMedia txnMedia : txnHeader.getTxnMedias()) {
                //only new added media should be count.
                if (txnMedia == null || !txnMedia.isNewAdded() || txnMedia.isTxmdVoided()) {
                    continue;
                }
                if (totalMediaVals.containsKey(txnMedia.getPaymentMedia())) {
                    values = totalMediaVals.get(txnMedia.getPaymentMedia());
                    if (txnMedia.getTxmdType().getCategoryCode().equals(IdBConstant.TXN_MEDIA_TYPE_SALE)) {
                        values[0] = values[0] + txnMedia.getCount();
                        values[1] = values[1] + txnMedia.getValue();
                    } else if (txnMedia.getTxmdType().getCategoryCode().equals(IdBConstant.TXN_MEDIA_TYPE_REFUND)) {
                        values[2] = values[2] + txnMedia.getCount();
                        values[3] = values[3] + txnMedia.getValue();
                    }
                } else {
                    values = new Double[4];
                    if (txnMedia.getTxmdType().getCategoryCode().equals(IdBConstant.TXN_MEDIA_TYPE_SALE)) {
                        values[0] = (double) txnMedia.getCount();
                        values[1] = txnMedia.getValue();
                    } else if (txnMedia.getTxmdType().getCategoryCode().equals(IdBConstant.TXN_MEDIA_TYPE_REFUND)) {
                        values[2] = (double) txnMedia.getCount();
                        values[3] = txnMedia.getValue();
                    }
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
                if (values.length >= 1 && values[0] != null) {
                    totalMediaOperator.setTomoSaleQty(values[0]);
                }
                if (values.length >= 2 && values[1] != null) {
                    totalMediaOperator.setTomoSaleValue(values[1]);
                }
                if (values.length >= 3 && values[2] != null) {
                    totalMediaOperator.setTomoRefundQty(values[2]);
                }
                if (values.length >= 4 && values[3] != null) {
                    totalMediaOperator.setTomoRefundValue(values[3]);
                }
                totalMediaOperator.setTomoTradingDate(currentDate);
                totalerDao.insertTotalMediaOperator(totalMediaOperator);
            }

        } catch (Exception e) {
            logger.error("Exception in processing total media", e);
        }
    }

}
