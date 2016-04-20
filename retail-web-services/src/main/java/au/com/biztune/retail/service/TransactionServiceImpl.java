package au.com.biztune.retail.service;

import au.com.biztune.retail.dao.ConfigCategoryDao;
import au.com.biztune.retail.dao.TxnDao;
import au.com.biztune.retail.domain.ConfigCategory;
import au.com.biztune.retail.domain.TxnDetail;
import au.com.biztune.retail.domain.TxnHeader;
import au.com.biztune.retail.domain.TxnMedia;
import au.com.biztune.retail.form.TxnDetailForm;
import au.com.biztune.retail.form.TxnHeaderForm;
import au.com.biztune.retail.form.TxnMediaForm;
import au.com.biztune.retail.response.CommonResponse;
import au.com.biztune.retail.session.SessionState;
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
 * Created by akhoshraft on 16/03/2016.
 */

@Component
public class TransactionServiceImpl implements TransactionService {
    private final Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);

    @Autowired
    private SessionState sessionState;

    @Autowired
    private TxnDao txnDao;

    @Autowired
    private ConfigCategoryDao configCategoryDao;
    /**
     * submit a transaction and save it into database.
     * @param  txnHeaderForm txnHeaderForm
     * @return CommonResponse
     */
    public CommonResponse addTransaction(TxnHeaderForm txnHeaderForm) {
        final CommonResponse response = new CommonResponse();
        try {
            response.setStatus(IdBConstant.RESULT_SUCCESS);

            if (txnHeaderForm == null || txnHeaderForm.getTxnDetailFormList() == null || txnHeaderForm.getTxnMediaFormList() == null) {
                response.setStatus(IdBConstant.RESULT_FAILURE);
                response.setMessage("transaction object or its related objects are null");
                return response;
            }
            final boolean isNew = txnHeaderForm.getId() > 0 ? false : true;
            if (isNew) {
                final Timestamp currentDate = new Timestamp(new Date().getTime());
                final TxnHeader txnHeader = new TxnHeader();
                txnHeader.setOrgUnit(sessionState.getStore().getOrgUnit());
                txnHeader.setStore(sessionState.getStore());
                final ConfigCategory txnState = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_TXN_STATE, IdBConstant.TXN_STATE_DRAFT);
                if (txnState != null) {
                    txnHeader.setTxhdState(txnState);
                }
                txnHeader.setOrgUnitOriginal(sessionState.getStore().getOrgUnit());
                txnHeader.setTxhdTradingDate(currentDate);
                final ConfigCategory txntype = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_TXN_TYPE, IdBConstant.TXN_TYPE_SALE);
                if (txntype != null) {
                    txnHeader.setTxhdTxnType(txntype);
                }
                //todo: set txnHeader Operator
                txnHeader.setTxhdVoided(false);
                txnHeader.setTxhdValueGross(txnHeaderForm.getTxhdValueGross());
                txnHeader.setTxhdValueNett(txnHeaderForm.getTxhdValueNett());
                txnHeader.setTxhdValueDue(txnHeaderForm.getTxhdValueDue());
                txnHeader.setTxhdValueTax(txnHeaderForm.getTxhdValueTax());
                txnHeader.setCustomer(txnHeaderForm.getCustomer());

                //save it to database.
                txnDao.insertTxnHeader(txnHeader);
                TxnDetail txnDetail = null;
                for (TxnDetailForm txnDetailForm: txnHeaderForm.getTxnDetailFormList()) {
                    if (txnDetailForm == null) {
                        continue;
                    }
                    txnDetail = new TxnDetail();
                    txnDetail.setOrguId(sessionState.getStore().getOrgUnit().getId());
                    txnDetail.setStoreId(sessionState.getStore().getId());
                    txnDetail.setTxhdId(txnHeader.getId());
                    txnDetail.setProduct(txnDetailForm.getProduct());
                    txnDetail.setUnitOfMeasure(txnDetailForm.getUnitOfMeasure());
                    txnDetail.setTxdeValueLine(txnDetailForm.getTxdeValueLine());
                    txnDetail.setTxdeProfitMargin(txnDetailForm.getTxdeProfitMargin());
                    txnDetail.setTxdeValueProfit(txnDetailForm.getTxdeValueProfit());
                    txnDetail.setTxdeValueGross(txnDetailForm.getTxdeValueGross());
                    txnDetail.setTxdeTax(txnDetailForm.getTxdeTax());
                    txnDetail.setTxdeValueNet(txnDetailForm.getTxdeValueNet());
                    txnDetail.setTxdeQuantitySold(txnDetailForm.getTxdeQuantitySold());
                    txnDetail.setTxdePriceSold(txnDetailForm.getTxdePriceSold());
                    txnDetail.setTxdeLineRefund(txnDetailForm.isTxdeLineRefund());
                    txnDetail.setTxdeItemVoid(txnDetailForm.isTxdeItemVoid());
                    final ConfigCategory txntLineType = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_TXN_LINE_TYPE, IdBConstant.TXN_LINE_TYPE_SALE);
                    if (txntLineType != null) {
                        txnDetail.setTxdeDetailType(txntLineType);
                    }
                    txnDao.insertTxnDetail(txnDetail);
                }
                //save txn media
                TxnMedia txnMedia = null;
                for (TxnMediaForm txnMediaForm : txnHeaderForm.getTxnMediaFormList()) {
                    if (txnMediaForm == null) {
                        continue;
                    }
                    txnMedia = new TxnMedia();
                    txnMedia.setOrguId(sessionState.getStore().getOrgUnit().getId());
                    txnMedia.setStoreId(sessionState.getStore().getId());
                    txnMedia.setTxhdId(txnHeader.getId());
                    txnMedia.setMedtId(txnMediaForm.getPaymentMedia().getMediaType().getId());
                    txnMedia.setPaymentMedia(txnMediaForm.getPaymentMedia());
                    final ConfigCategory txntMediaType = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_TXN_MEDIA_TYPE, IdBConstant.TXN_MEDIA_TYPE_SALE);
                    if (txntMediaType != null) {
                        txnMedia.setTxmdType(txntMediaType);
                    }
                    txnMedia.setTxmdAmountLocal(txnMediaForm.getTxmdAmountLocal());
                    txnDao.insertTxnMedia(txnMedia);
                }
            }
            return response;
        } catch (Exception e) {
            logger.error("Exception in saving transaction: ", e);
            response.setStatus(IdBConstant.RESULT_FAILURE);
            response.setMessage("Exception in saving Transaction");
            return response;
        }
    }

    /**
     * get all transaction of store.
     * @return List of TxnHeader
     */
    public List<TxnHeader> getAllTxnHeadersForStore() {
        try {
            return txnDao.getTxnHeaderPerStoreId(sessionState.getStore().getId());
        } catch (Exception e) {
            logger.error("Exception in getting transaction sale: ", e);
            return null;
        }
    }

    /**
     * get Transaction Sale per Id.
     * @param  txhdId Transaction Header Id
     * @return TxnHeaderForm
     */
    public TxnHeaderForm getTxnHeaderPerId(long txhdId) {
        try {
            final TxnHeader txnHeader = txnDao.getTxnHeaderPerTxhdId(txhdId);
            if (txnHeader == null) {
                logger.error("Not able to fetch transaction id " + txhdId);
                return null;
            }
            final TxnHeaderForm txnHeaderForm = new TxnHeaderForm();
            txnHeaderForm.setTxhdTxnNr(txnHeader.getTxhdTxnNr());
            txnHeaderForm.setId(txnHeader.getId());
            txnHeaderForm.setTxhdOrigTxnNr(txnHeader.getTxhdOrigTxnNr());
            txnHeaderForm.setTxhdValueGross(txnHeader.getTxhdValueGross());
            txnHeaderForm.setTxhdValueNett(txnHeader.getTxhdValueNett());
            txnHeaderForm.setTxhdValueDue(txnHeader.getTxhdValueDue());
            txnHeaderForm.setTxhdValueChange(txnHeader.getTxhdValueChange());
            txnHeaderForm.setTxhdValRounding(txnHeader.getTxhdValRounding());
            txnHeaderForm.setTxhdValueTax(txnHeader.getTxhdValueTax());
            txnHeaderForm.setCustomer(txnHeader.getCustomer());
            if (txnHeader.getTxnDetails() != null) {
                TxnDetailForm txnDetailForm;
                final List<TxnDetailForm> txnDetailFormList = new ArrayList<TxnDetailForm>();
                for (TxnDetail txnDetail : txnHeader.getTxnDetails()) {
                    if (txnDetail == null) {
                        continue;
                    }
                    txnDetailForm = new TxnDetailForm();
                    txnDetailForm.setId(txnDetail.getId());
                    txnDetailForm.setTxdePriceOveriden(txnDetail.isTxdePriceOveriden());
                    txnDetailForm.setProduct(txnDetail.getProduct());
                    txnDetailForm.setUnitOfMeasure(txnDetail.getUnitOfMeasure());
                    txnDetailForm.setTxdeValueLine(txnDetail.getTxdeValueLine());
                    txnDetailForm.setTxdeProfitMargin(txnDetail.getTxdeProfitMargin());
                    txnDetailForm.setTxdeValueGross(txnDetail.getTxdeValueGross());
                    txnDetailForm.setTxdeValueProfit(txnDetail.getTxdeValueProfit());
                    txnDetailForm.setTxdeTax(txnDetail.getTxdeTax());
                    txnDetailForm.setTxdeValueNet(txnDetail.getTxdeValueNet());
                    txnDetailForm.setTxdeQuantitySold(txnDetail.getTxdeQuantitySold());
                    txnDetailForm.setTxdePriceSold(txnDetail.getTxdePriceSold());
                    txnDetailForm.setTxdeLineRefund(txnDetail.isTxdeLineRefund());
                    txnDetailForm.setTxdeItemVoid(txnDetail.isTxdeItemVoid());
                    txnDetailForm.setTxdeDetailType(txnDetail.getTxdeDetailType());
                    txnDetailFormList.add(txnDetailForm);
                }
                txnHeaderForm.setTxnDetailFormList(txnDetailFormList);
            }
            if (txnHeader.getTxnMedias() != null) {
                TxnMediaForm txnMediaForm;
                final List<TxnMediaForm> txnMediaFormList = new ArrayList<TxnMediaForm>();
                for (TxnMedia txnMedia: txnHeader.getTxnMedias()) {
                    if (txnMedia == null) {
                        continue;
                    }
                    txnMediaForm = new TxnMediaForm();
                    txnMediaForm.setId(txnMedia.getId());
                    txnMediaForm.setPaymentMedia(txnMedia.getPaymentMedia());
                    txnMediaForm.setTxmdType(txnMedia.getTxmdType());
                    txnMediaForm.setTxmdAmountLocal(txnMedia.getTxmdAmountLocal());
                    txnMediaForm.setTxmdVoided(txnMedia.isTxmdVoided());
                    txnMediaFormList.add(txnMediaForm);
                }
                txnHeaderForm.setTxnMediaFormList(txnMediaFormList);
            }
            return txnHeaderForm;
        } catch (Exception e) {
            logger.error("Exception in getting transaction per id: ", e);
            return null;
        }
    }
}
