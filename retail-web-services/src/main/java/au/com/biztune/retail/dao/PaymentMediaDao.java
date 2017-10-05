package au.com.biztune.retail.dao;

import au.com.biztune.retail.domain.MediaType;
import au.com.biztune.retail.domain.PaymentMedia;

import java.util.List;

/**
 * Created by arash on 14/04/2016.
 */
public interface PaymentMediaDao {
    /**
     * get all Media types.
     *
     * @return List of MediaType
     */
    List<MediaType> getAllMediaTypes();

    /**
     * get AllPaymentMedia Of Media Type with id.
     *
     * @param mediaTypeId mediaTypeId
     * @return List of PaymentMedia
     */
    List<PaymentMedia> getAllPaymentMediaOfTypePerId(long mediaTypeId);

    /**
     * get PaymentMedia Per Id.
     *
     * @param paymId paymId
     * @return PaymentMedia
     */
    PaymentMedia getPaymentMediaPerId(long paymId);

    /**
     * get payment media by code.
     *
     * @param paymentCode paymentCode
     * @return payment media.
     */
    PaymentMedia getPaymentMediaPerCode(String paymentCode);

    /**
     * get payment media by name.
     *
     * @param paymentName paymentName
     * @return payment media.
     */
    PaymentMedia getPaymentMediaPerName(String paymentName);

    /**
     * get all payment medias.
     *
     * @return List of Payment Media.
     */
    List<PaymentMedia> getAllPaymentMedias();

    /**
     * get all payment medias for reconciliation.
     *
     * @return List of Payment Media.
     */
    List<PaymentMedia> getPaymentMediasForReconciliation();

    /**
     * get media type by name.
     * @param mediaTypeName mediaTypeName
     * @return Media type by name
     */
    MediaType getMediaTypeByName(String mediaTypeName);
}