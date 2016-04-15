package au.com.biztune.retail.service;

import au.com.biztune.retail.dao.PaymentMediaDao;
import au.com.biztune.retail.domain.MediaType;
import au.com.biztune.retail.domain.PaymentMedia;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by akhoshraft on 16/03/2016.
 */

@Component
public class PaymentMediaServiceImpl implements PaymentMediaService {
    private final Logger logger = LoggerFactory.getLogger(PaymentMediaServiceImpl.class);

    @Autowired
    private PaymentMediaDao paymentMediaDao;

    /**
     * get all payment medias of specific mediatype.
     * @param  mediaTypeId mediaTypeId
     * @return UnitOfMeasure
     */
    public List<PaymentMedia> getAllPaymentMediaOfType(long mediaTypeId) {
        try {
            return paymentMediaDao.getAllPaymentMediaOfTypePerId(mediaTypeId);
        } catch (Exception e) {
            logger.error("Error in getting payment medias: ", e);
            return null;
        }
    }
    /**
     * get all payment medias.
     * @return LIst of PaymentMedia
     */
    public List<MediaType> getAllMediaTypes() {
        try {
            return paymentMediaDao.getAllMediaTypes();
        } catch (Exception e) {
            logger.error("Error in getting list of media types: ", e);
            return null;
        }
    }
}
