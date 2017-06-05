// Sydney Trains 2016
package au.com.biztune.retail.service;

import au.com.biztune.retail.domain.MediaType;
import au.com.biztune.retail.domain.PaymentMedia;

import java.util.List;

/**
 * Created by arash on 15/04/2016.
 */
public interface PaymentMediaService {
    /**
     * get all payment medias of specific mediatype.
     *
     * @param mediaTypeId mediaTypeId
     * @return UnitOfMeasure
     */
    List<PaymentMedia> getAllPaymentMediaOfType(long mediaTypeId);

    /**
     * get all payment medias.
     *
     * @return LIst of PaymentMedia
     */
    List<MediaType> getAllMediaTypes();

    /**
     * get mediatype by name.
     *
     * @param mediaTypeName mediaTypeName
     * @return media type.
     */
    MediaType getMediaTypeByName(String mediaTypeName);

    /**
     * get all payment medias.
     *
     * @return UnitOfMeasure
     */
    List<PaymentMedia> getAllPaymentMedias();
}
