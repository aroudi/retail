package au.com.biztune.retail.service;

import au.com.biztune.retail.dao.TxnDao;
import au.com.biztune.retail.domain.TxnHeader;
import au.com.biztune.retail.response.CommonResponse;
import au.com.biztune.retail.util.IdBConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by akhoshraft on 16/03/2016.
 */

@Component
public class TransactionServiceImpl {
    private final Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);

    @Autowired
    private TxnDao txnDao;

    /**
     * submit a transaction and save it into database.
     * @param  txnHeader txnHeader
     * @return CommonResponse
     */
    public CommonResponse submitTransaction(TxnHeader txnHeader) {
        final CommonResponse response = new CommonResponse();
        try {
            response.setStatus(IdBConstant.RESULT_SUCCESS);

            if (txnHeader == null) {
                response.setStatus(IdBConstant.RESULT_FAILURE);
                response.setMessage("transaction object is null");
                return response;
            }
            final boolean isNew = txnHeader.getId() > 0 ? false : true;
            return response;
        } catch (Exception e) {
            logger.error("Exception in saving transaction: ", e);
            response.setStatus(IdBConstant.RESULT_FAILURE);
            response.setMessage("Exception in saving Transaction");
            return response;
        }
    }
}
