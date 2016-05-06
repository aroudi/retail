package au.com.biztune.retail.util;

/**
 * Created by arash on 22/02/2016.
 */
public interface IdBConstant {
    /**
     * RESULT_SUCCESS.
     */
    int RESULT_SUCCESS = 1;
    /**
     * RESULT_FAILURE.
     */
    int RESULT_FAILURE = -1;

    /**
     * LEGAL TENDER AU.
     */
    String LEGAL_TENDER_AU = "AUD";

    /**
     * SELL_PRICE CODE.
     */
    String SELL_PRICE_CODE = "SELL_PRICE";
    /**
     * SELL_PRICE CODE.
     */
    String PRICE_BAND_CODE = "A";

    /**
     * Customer Type.
     */
    String CUSTOMER_TYPE_COMPANY = "Company";
    /**
     * SUPPLIER STATUS CONFIG.
     */
    String CONFIG_SUPLIER_STATUS = "SUPLIER_STATUS";
    /**
     * PRODUCT STATUS CONFIG.
     */
    String CONFIG_PRODUCT_STATUS = "PRODUCT_STATUS";
    /**
     * SUPPLIER STATUS IMPORTED.
     */
    String PRODUCT_STATUS_IMPORTED = "IMPORTED";
    /**
     * SUPPLIER STATUS IMPORTED.
     */
    String SUPPLIER_STATUS_IMPORTED = "IMPORTED";


    /**
     * TRANSACTION STATE.
     */
    String TYPE_TXN_STATE = "TXN_STATE";
    /**
     * TRANSACTION STATE - DRAFT.
     */
    String TXN_STATE_DRAFT = "TXN_STATE_DRAFT";
    /**
     * TRANSACTION STATE VOID.
     */
    String TXN_STATE_VOID = "TXN_STATE_VOID";

    /**
     * TRANSACTION STATE SUSPEND.
     */
    String TXN_STATE_SUSPEND = "TXN_STATE_SUSPEND";

    /**
     * TRANSACTION STATE SUSPEND.
     */
    String TXN_STATE_FINAL = "TXN_STATE_FINAL";

    /**
     * TRANSACTION TYPE.
     */
    String TYPE_TXN_TYPE = "TXN_TYPE";
    /**
     * TRANSACTION TYPE - SALE.
     */
    String TXN_TYPE_SALE = "TXN_TYPE_SALE";
    /**
     * TRANSACTION TYPE REFUND.
     */
    String TXN_TYPE_REFUND = "TXN_TYPE_REFUND";

    /**
     * TRANSACTION TYPE QUOTE.
     */
    String TXN_TYPE_QUOTE = "TXN_TYPE_QUOTE";

    /**
     * TRANSACTION LINE TYPE.
     */
    String TYPE_TXN_LINE_TYPE = "TXN_LINE_TYPE";
    /**
     * TRANSACTION LINE TYPE - SALE.
     */
    String TXN_LINE_TYPE_SALE = "TXN_LINE_SALE";
    /**
     * TRANSACTION LINE TYPE REFUND.
     */
    String TXN_LINE_TYPE_REFUND = "TXN_LINE_REFUND";


    /**
     * TRANSACTION MEDIA TYPE.
     */
    String TYPE_TXN_MEDIA_TYPE = "TXN_MEDIA_TYPE";
    /**
     * TRANSACTION MEDIA TYPE - SALE.
     */
    String TXN_MEDIA_TYPE_SALE = "TXN_MEDIA_SALE";
    /**
     * TRANSACTION MEDIA TYPE REFUND.
     */
    String TXN_MEDIA_TYPE_REFUND = "TXN_MEDIA_REFUND";


    /**
     * PURCHASE ORDER HEADER STATUS.
     */
    String TYPE_POH_STATUS = "POH_STATUS";
    /**
     * PURCHASE ORDER HEADER STATUS IN PROGRESS.
     */
    String POH_STATUS_IN_PROGRESS = "POH_STATUS_IN_PROGRESS";
    /**
     * PURCHASE ORDER HEADER STATUS CONFIRMED.
     */
    String POH_STATUS_CONFIRMED = "POH_STATUS_CONFIRMED";

    /**
     * PURCHASE ORDER HEADER STATUS CANCELLED.
     */
    String POH_STATUS_CANCELLED = "POH_STATUS_CANCELLED";


    /**
     * PURCHASE ORDER HEADER CREATION TYPE.
     */
    String TYPE_POH_CREATION_TYPE = "POH_CREATION_TYPE";
    /**
     * PURCHASE ORDER HEADER CREATION TYPE POH_CREATION_TYPE_MANUAL.
     */
    String POH_CREATION_TYPE_MANUAL = "POH_CREATION_TYPE_MANUAL";
    /**
     * PURCHASE ORDER HEADER CREATION TYPE POH_CREATION_TYPE_AUTO.
     */
    String POH_CREATION_TYPE_AUTO = "POH_CREATION_TYPE_AUTO";


    /**
     * PURCHASE ORDER HEADER TYPE.
     */
    String TYPE_POH_TYPE = "POH_TYPE";
    /**
     * PURCHASE ORDER HEADER  TYPE STOCK.
     */
    String POH_TYPE_STOCK = "POH_TYPE_STOCK";
    /**
     * PURCHASE ORDER HEADER TYPE PROJECT.
     */
    String POH_TYPE_PROJECT = "POH_TYPE_PROJECT";
    /**
     * PURCHASE ORDER HEADER TYPE SPECIAL.
     */
    String POH_TYPE_SPECIAL_ORDER = "POH_TYPE_SPECIAL_ORDER";
}
