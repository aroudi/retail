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
     * COST_PRICE CODE.
     */
    String COST_PRICE_CODE = "COST_PRICE";
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
     * PRODUCT STATUS IN REVIEW.
     */
    String PRODUCT_STATUS_IN_REVIEW = "PROD_STATUS_IN_REVIEW";
    /**
     * PRODUCT STATUS IN FINALISED.
     */
    String PRODUCT_STATUS_FINALISED = "PROD_STATUS_FINALISED";
    /**
     * SUPPLIER STATUS IMPORTED.
     */
    String SUPPLIER_STATUS_IMPORTED = "IMPORTED";

    /**
     * SUPPLIER STATUS IMPORTED.
     */
    String SUPPLIER_STATUS_ACTIVE = "SUPPLIER_STATUS_ACTIVE";

    /**
     * SUPPLIER STATUS IMPORTED.
     */
    String SUPPLIER_STATUS_HELD = "SUPPLIER_STATUS_HELD";

    /**
     * PRODUCT STATE.
     */
    String TYPE_PRODUCT_TYPE = "PRODUCT_TYPE";


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
     * TRANSACTION STATUS.
     */
    String TYPE_TXN_STATUS = "TXN_STATUS";
    /**
     * TRANSACTION STATUS - REFUNDED.
     */
    String TXN_STATUS_REFUNDED = "TXN_STATUS_REFUNDED";
    /**
     * TRANSACTION TYPE.
     */
    String TYPE_TXN_TYPE = "TXN_TYPE";
    /**
     * TRANSACTION TYPE - SALE.
     */
    String TXN_TYPE_SALE = "TXN_TYPE_SALE";
    /**
     * TRANSACTION TYPE - INVOICE.
     */
    String TXN_TYPE_INVOICE = "TXN_TYPE_INVOICE";
    /**
     * TRANSACTION TYPE REFUND.
     */
    String TXN_TYPE_REFUND = "TXN_TYPE_REFUND";

    /**
     * TRANSACTION TYPE QUOTE.
     */
    String TXN_TYPE_QUOTE = "TXN_TYPE_QUOTE";

    /**
     * TRANSACTION TYPE BILL OF QUANTITY.
     */
    String TXN_TYPE_BOQ = "TXN_TYPE_BOQ";

    /**
     * TRANSACTION TYPE ACCOUNT PAYMENT.
     */
    String TXN_TYPE_ACCOUNT_PAYMENT = "TXN_TYPE_ACCOUNT_PAYMENT";

    /**
     * TRANSACTION TYPE REVERSAL ACCOUNT PAYMENT.
     */
    String TXN_TYPE_REVERSE_ACC_PAYMENT = "TXN_TYPE_REVERSE_ACC_PAYMENT";

    /**
     * TRANSACTION TYPE GOODS IN - USE FOR GOOD RECEIVED TRANSACTION.
     */
    String TXN_TYPE_GOODS_IN = "TXN_TYPE_GOODS_IN";

    /**
     * TRANSACTION TYPE GOOD RESERVE - USE FOR ASSIGNING PRODUCTS TO THE BOQ.
     */
    String TXN_TYPE_GOODS_RESERVE = "TXN_TYPE_GOODS_RESERVE";

    /**
     * TRANSACTION TYPE GOOD RESERVE FOR BOQ - USE FOR ASSIGNING PRODUCTS TO THE BOQ.
     */
    String TXN_TYPE_GOODS_RESERVE_BOQ = "TXN_TYPE_GOODS_RESERVE_BOQ";
    /**
     * TRANSACTION TYPE GOOD RESERVE FOR SO - USE FOR ASSIGNING PRODUCTS TO THE BOQ.
     */
    String TXN_TYPE_GOODS_RESERVE_SO = "TXN_TYPE_GOODS_RESERVE_SO";
    /**
     * TRANSACTION TYPE GOOD CANCEL RESERVE - USE FOR CANCEL ASSIGNING TO THE PRODUCTS.
     */
    String TXN_TYPE_GOODS_CANCEL_RESERVE = "TXN_TYPE_GOODS_CANCEL_RESERVE";

    /**
     * TRANSACTION TYPE GOODS IN TRANSIT: Used when products reserved to BOQ transfer to the site.
     */
    String TXN_TYPE_GOODS_IN_TRANSIT = "TXN_TYPE_GOODS_IN_TRANSIT";

    /**
     * TRANSACTION TYPE CHANGE IN COST.
     */
    String TXN_TYPE_CHANGE_IN_COST = "TXN_TYPE_CHANGE_IN_COST";

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
     * TRANSACTION MEDIA TYPE - INVOICE.
     */
    String TXN_MEDIA_TYPE_INVOICE = "TXN_MEDIA_INVOICE";
    /**
     * TRANSACTION MEDIA TYPE - DEPOSIT.
     */
    String TXN_MEDIA_TYPE_DEPOSIT = "TXN_MEDIA_DEPOSIT";
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
     * PURCHASE ORDER HEADER GOOD RECEIVED.
     */
    String POH_STATUS_GOOD_RECEIVED = "POH_STATUS_GOOD_RECEIVED";

    /**
     * PURCHASE ORDER HEADER PARTIALLY RECEIVED.
     */
    String POH_STATUS_PARTIAL_REC = "POH_STATUS_PARTIAL_REC";

    /**
     * PURCHASE ORDER HEADER CREATION TYPE.
     */
    String TYPE_POH_CREATION_TYPE = "POH_CREATION_TYPE";
    /**
     * PURCHASE ORDER HEADER CREATION TYPE POH_CREATION_TYPE_MANUAL.
     */
    String POH_CREATION_TYPE_MANUAL = "POH_CREATION_TYPE_MANUAL";
    /**
     * PURCHASE ORDER HEADER CREATION TYPE POH_CREATION_TYPE_AUTO - CREATED FROM BILL OF QUANTITY.
     */
    String POH_CREATION_TYPE_AUTO = "POH_CREATION_TYPE_AUTO";

    /**
     * PURCHASE ORDER HEADER CREATION TYPE POH_CREATION_TYPE_AUTO - CREATED FROM SALE ORDER.
     */
    String POH_CREATION_TYPE_AUTO_SO = "POH_CREATION_TYPE_AUTO_SO";

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

    /**
     * BILL OF QUANTITY HEADER STATUS.
     */
    String TYPE_BOQ_STATUS = "BOQ_STATUS";
    /**
     * BILL OF QUANTITY HEADER STATUS NEW.
     */
    String BOQ_STATUS_NEW = "BOQ_STATUS_NEW";
    /**
     * BILL OF QUANTITY HEADER STATUS ON ORDER.
     */
    String BOQ_STATUS_ON_ORDER = "BOQ_STATUS_ON_ORDER";

    /**
     * BILL OF QUANTITY HEADER STATUS PARTIAL RELEASED.
     */
    String BOQ_STATUS_PAR_RELEASED = "BOQ_STATUS_PAR_RELEASED";

    /**
     * BILL OF QUANTITY HEADER STATUS FINAL.
     */
    String BOQ_STATUS_FINAL = "BOQ_STATUS_FINAL";

    /**
     * BILL OF QUANTITY HEADER STATUS RECEIVED.
     */
    String BOQ_STATUS_RECEIVED = "BOQ_STATUS_RECEIVED";

    /**
     * BILL OF QUANTITY HEADER STATUS PARTIAL RECEIVED.
     */
    String BOQ_STATUS_PARTIAL_REC = "BOQ_STATUS_PARTIAL_REC";

    /**
     * BILL OF QUANTITY HEADER STATUS RECEIVED.
     */
    String BOQ_STATUS_CANCELLED = "BOQ_STATUS_CANCELLED";



    /**
     * SALE ORDER HEADER STATUS.
     */
    String TYPE_SO_STATUS = "SO_STATUS";
    /**
     * SALE ORDER STATUS OUTSTANDING.
     */
    String SO_STATUS_OUTSTANDING = "SO_STATUS_OUTSTANDING";
    /**
     * SALE ORDER HEADER STATUS ON ORDER.
     */
    String SO_STATUS_ON_ORDER = "SO_STATUS_ON_ORDER";
    /**
     * SALE ORDER HEADER STATUS FINAL.
     */
    String SO_STATUS_FINAL = "SO_STATUS_FINAL";

    /**
     * SALE ORDER HEADER STATUS RECEIVED.
     */
    String SO_STATUS_RECEIVED = "SO_STATUS_RECEIVED";

    /**
     * SALE ORDER HEADER STATUS PARTIAL RECEIVED.
     */
    String SO_STATUS_PARTIAL_REC = "SO_STATUS_PARTIAL_REC";

    /**
     * SALE ORDER HEADER STATUS RECEIVED.
     */
    String SO_STATUS_CANCELLED = "SO_STATUS_CANCELLED";



    /**
     * CUSTOMER STATUS.
     */
    String TYPE_CUSTOMER_STATUS = "CUSTOMER_STATUS";
    /**
     * CUSTOMER STATUS NEW.
     */
    String CUSTOMER_STATUS_NEW = "CUSTOMER_STATUS_NEW";
    /**
     * CUSTOMER STATUS CONFIRMED.
     */
    String CUSTOMER_STATUS_CONFIRMED = "CUSTOMER_STATUS_CONFIRMED";
    /**
     * CUSTOMER STATUS INACTIVE.
     */
    String CUSTOMER_STATUS_IN_ACTIVE = "CUSTOMER_STATUS_IN_ACTIVE";

    /**
     * CUSTOMER TYPE.
     */
    String TYPE_CUSTOMER_TYPE = "CUSTOMER_TYPE";
    /**
     * CUSTOMER TYPE COD.
     */
    String CUSTOMER_TYPE_CASH_ONLY = "CUSTOMER_TYPE_CASH_ONLY";
    /**
     * CUSTOMER TYPE ACCOUNT.
     */
    String CUSTOMER_TYPE_ACCOUNT = "CUSTOMER_TYPE_ACCOUNT";
    /**
     * Bill Of Quantity line status.
     */
    String TYPE_BOQ_LINE_STATUS = "BOQ_LINE_STATUS";
    /**
     * Bill Of Quantity line status PENDING.
     */
    String BOQ_LINE_STATUS_PENDING = "BOQ_LINE_STATUS_PENDING";
    /**
     * Bill Of Quantity line status Purchase Order Created.
     */
    String BOQ_LINE_STATUS_PO_CREATED = "BOQ_LINE_STATUS_PO_CREATED";
    /**
     * Bill Of Quantity line status partial received.
     */
    String BOQ_LINE_STATUS_PARTIAL_RECEIVED = "BOQ_LINE_STATUS_PARTIAL_RECEIVED";
    /**
     * Bill Of Quantity line status Good Received.
     */
    String BOQ_LINE_STATUS_GOOD_RECEIVED = "BOQ_LINE_STATUS_GOOD_RECEIVED";

    /**
     * Bill Of Quantity line status VOID.
     */
    String BOQ_LINE_STATUS_VOID = "BOQ_LINE_STATUS_VOID";

    /**
     * PurchaseOrderHeader Number AUTHO CREATED Prefix.
     */
    String POH_NUMBER_PREFIX_AUTO = "PO-A-";
    /**
     * PurchaseOrderHeader Number AUTHO CREATED Prefix.
     */
    String POH_NUMBER_PREFIX_MANUAL = "PO-M-";

    /**
     * Delivery Note Status.
     */
    String TYPE_DLV_NOTE_STATUS = "DLV_NOTE_STATUS";
    /**
     * DELIVERY NOTE STATUS STATUS IN PROGRESS.
     */
    String DLV_NOTE_STATUS_IN_PROGRESS = "DLV_NOTE_STATUS_IN_PROGRESS";
    /**
     * DELIVERY NOTE STATUS ON HOLD.
     */
    String DLV_NOTE_STATUS_ON_HOLD = "DLV_NOTE_STATUS_ON_HOLD";
    /**
     * DELIVERY NOTE STATUS COMPLETE.
     */
    String DLV_NOTE_STATUS_COMPLETE = "DLV_NOTE_STATUS_COMPLETE";
    /**
     * GRN number Prefix.
     */
    String GRN_PREFIX = "GRN-";

    /**
     * CONTACT TYPE.
     */
    String TYPE_CONTACT_TYPE = "CONTACT_TYPE";
    /**
     * CONTACT TYPE CONTACT PERSON.
     */
    String CONTACT_TYPE_CONTACT_PERSON = "CONTACT_TYPE_CONTACT_PERSON";

    /**
     * CONTACT TYPE account manager.
     */
    String CONTACT_TYPE_ACCOUNT = "CONTACT_TYPE_ACCOUNT";

    /**
     * CONTACT TYPE project manager.
     */
    String CONTACT_TYPE_PM = "CONTACT_TYPE_PM";
    /**
     * BAD_REQUEST.
     */
    String BAD_REQUEST = "BAD_REQUEST";

    /**
     * STOCK CATEGORY TYPE.
     */
    String TYPE_STOCK_CATEGORY = "STOCK_CATEGORY";
    /**
     * STOCK CATEGORIES.
     */
    String STOCK_CATEGORY_SALEABLE = "STOCK_CATEGORY_SALEABLE";
    /**
     * STOCK CATEGORIES.
     */
    String STOCK_CATEGORY_NON_SALEABLE = "STOCK_CATEGORY_NON_SALEABLE";
    /**
     * STOCK CATEGORIES.
     */
    String STOCK_CATEGORY_RESERVED = "STOCK_CATEGORY_RESERVED";
    /**
     * STOCK CATEGORIES.
     */
    String STOCK_CATEGORY_LAYAWAY = "STOCK_CATEGORY_LAYAWAY";
    /**
     * STOCK CATEGORIES.
     */
    String STOCK_CATEGORY_IN_DISPATCH = "STOCK_CATEGORY_IN_DISPATCH";
    /**
     * STOCK CATEGORIES.
     */
    String STOCK_CATEGORY_IN_TRANSIT = "STOCK_CATEGORY_IN_TRANSIT";
    /**
     * STOCK CATEGORIES.
     */
    String STOCK_CATEGORY_RETURN = "STOCK_CATEGORY_RETURN";


    /**
     * STOCK CONDITION TYPE.
     */
    String TYPE_STOCK_CONDITION = "STOCK_CONDITION";
    /**
     * STOCK CONDITIONS.
     */
    String STOCK_CONDITION_PRISTINE = "STOCK_CONDITION_PRISTINE";
    String STOCK_CONDITION_DAMAGED = "STOCK_CONDITION_DAMAGED";
    String STOCK_CONDITION_FAULTY = "STOCK_CONDITION_FAULTY";

    /**
     * PRODUCT CATEGORY TYPE.
     */
    String TYPE_PRODUCT_CATEGORY = "PROD_CAT_TYPE";
    /**
     * PRODUCT CATEGORY TYPES.
     */
    String PRODUCT_CATEGORY_1 = "PROD_CAT_1";
    String PRODUCT_CATEGORY_2 = "PROD_CAT_2";
    String PRODUCT_CATEGORY_3 = "PROD_CAT_3";

    /**
     * CLIENT STATUS.
     */
    String TYPE_CLIENT_STATUS = "CLIENT_STATUS";
    /**
     * CLIENT STATUSES.
     */
    String CLIENT_STATUS_ACTIVE = "CLIENT_STATUS_ACTIVE";
    String CLIENT_STATUS_IN_ACTIVE = "CLIENT_STATUS_IN_ACTIVE";
    String CLIENT_STATUS_ON_HOLD = "CLIENT_STATUS_ON_HOLD";

    /**
     * Transaction Number Prefix
     */
    String TXN_NUMBER_PREFIX = "TXN-";

    /**
     * Invoice Prefix
     */
    String INVOICE_PREFIX = "INVOICE-";

    /**
     * Refund Prefix
     */
    String REFUND_PREFIX = "REFUND-";

    /**
     * Transaction Number Prefix
     */
    String QUOTE_NUMBER_PREFIX = "QUOTE-";

    /**
     * Minimum value for rounding
     */
    double ROUNDING_VALUE_BASE = 0.06;

    /**
     *
     */
    String PAYMENT_MEDIA_ACCOUNT = "Account";
    String MEDIA_TYPE_ACCOUNT = "Account";
    String MEDIA_TYPE_CASH = "Cash";


    /**
     * SESSION STATE.
     */
    String TYPE_SESSION_STATE= "SESSION_STATE";
    /**
     * SESSION STATES.
     */
    String SESSION_STATE_OPEN = "SESSION_STATE_OPEN";
    String SESSION_STATE_CLOSED = "SESSION_STATE_CLOSED";
    String SESSION_STATE_RECONCILED = "SESSION_STATE_RECONCILED";
    String SESSION_STATE_ENDED = "SESSION_STATE_ENDED";


    /**
     * SESSION EVENT TYPE.
     */
    String TYPE_SESSION_EVENT= "SESSION_EVENT_TYPE";
    /**
     * SESSION EVENT TYPES.
     */
    String SESSION_EVENT_TYPE_OPEN = "SESSION_EVENT_TYPE_OPEN";
    String SESSION_EVENT_TYPE_CLOSE = "SESSION_EVENT_TYPE_CLOSE";
    String SESSION_EVENT_TYPE_END = "SESSION_EVENT_TYPE_END";
    String SESSION_EVENT_TYPE_RECONCILE = "SESSION_EVENT_TYPE_RECONCILE";
    String SESSION_EVENT_TYPE_FLOAT = "SESSION_EVENT_TYPE_FLOAT";
    String SESSION_EVENT_TYPE_PICKUP = "SESSION_EVENT_TYPE_PICKUP";
    String SESSION_EVENT_TYPE_TXN = "SESSION_EVENT_TYPE_TXN";
    String SESSION_EVENT_TYPE_PAY = "SESSION_EVENT_TYPE_PAY";
    String SESSION_EVENT_TYPE_ACC_PAY = "SESSION_EVENT_TYPE_ACC_PAY";
    String SESSION_EVENT_TYPE_ACC_REFUND = "SESSION_EVENT_TYPE_ACC_REFUND";

    //DEFAULT PROFIT MARGIN IF IT HAS NOT BEEN ASSIGNED TO THE CUSTOMER.
    double DEFAULT_PROFIT_MARGIN = 0.20;

    String PAY_MEDIA_CODE_CASH = "1";
    String PAY_MEDIA_CODE_ACCOUNT = "12";

    /**
     * JOURNAL ACTION TYPE
     */
    String TYPE_JOURNAL_ACTION= "TYPE_JOURNAL_ACTION";
    /**
     * JOURNAL ACTION CODES
     */
    String JOURNAL_ACTION_COST_OF_GOODS = "JA_COST_OF_GOODS";
    String JOURNAL_ACTION_SALE_INCOME = "JA_SALE_INCOME";
    String JOURNAL_ACTION_SALE_ORDER_PAYMENT = "JA_SALE_ORDER_PAYMENT";
    String JOURNAL_ACTION_CONVERT_SO_TO_SALE = "JA_CONVERT_SO_TO_SALE";
    String JOURNAL_ACTION_TILL_ADJUSTMENT = "JA_TILL_ADJUSTMENT";
    String JOURNAL_ACTION_ACTUAL_BANK_ACCOUNT = "JA_ACTUAL_BANK_ACCOUNT";
    String JOURNAL_ACTION_ACCOUNT_SALE = "JA_ACCOUNT_SALE";
    String JOURNAL_ACTION_INVOICE_PAYMENT = "JA_ACTION_INVOICE_PAYMENT";
    String JOURNAL_ACTION_CHANGE_IN_COST = "JA_CHANGE_IN_COST";


    /**
     * ACCOUNT TYPE
     */
    String TYPE_ACCOUNT_TYPE= "TYPE_ACCOUNT_TYPE";
    /**
     * ACCOUNT TYPES
     */
    String ACC_TYPE_ASSET = "ACC_TYPE_ASSET";
    String ACC_TYPE_INCOME = "ACC_TYPE_INCOME";
    String ACC_TYPE_COST_OF_SALE = "ACC_TYPE_COST_OF_SALE";
    String ACC_TYPE_EXPENSE = "ACC_TYPE_EXPENSE";
    String ACC_TYPE_LIABILITY = "ACC_TYPE_LIABILITY";
    String ACC_TYPE_CLEARING_ACCOUNT = "ACC_TYPE_CLEARING_ACCOUNT";

    String PAID_FROM_ACCOUNT = "PAID_FROM_ACCOUNT";
    String PAID_FROM_NON_ACCOUNT = "PAID_FROM_NON_ACCOUNT";
    String PAID_FROM_ACCOUNT_AND_NON_ACCOUNT = "PAID_FROM_BOTH";

    String ACCOUNT_SALES_INCOME = "SALES";
    String ACCOUNT_INVENTORY = "INVENTORY";
    String ACCOUNT_CLEARING_ACCOUNT = "CLEARING_ACCOUNT";
    String ACCOUNT_COST_OF_GOODS = "COST_OF_GOODS";

    /**
     * CUSTOMER GRADE CODE
     */
    String CUSTOMER_GRADE_DEFAULT= "Default";
    /**
     * CUSTOMER GRADE CODE
     */
    String CUSTOMER_GRADE_A= "A";
    /**
     * CUSTOMER GRADE CODE
     */
    String CUSTOMER_GRADE_B= "B";
    /**
     * CUSTOMER GRADE CODE
     */
    String CUSTOMER_GRADE_C= "C";
    /**
     * CUSTOMER GRADE CODE
     */
    String CUSTOMER_GRADE_D= "D";

    String DEFAULT_PRODUCT_TAX_CODE = "GST";
    /**
     * TYPE COUNTRY.
     */
    String TYPE_COUNTRY = "TYPE_COUNTRY";
    /**
     * PRODUCT GROUP LEVEL 1.
     */
    String PRGP_LEVEL_1 = "DEPARTMENT";
    /**
     * PRODUCT GROUP LEVEL 2.
     */
    String PRGP_LEVEL_2 = "CATEGORY_HEADING";
    /**
     * PRODUCT GROUP LEVEL 3.
     */
    String PRGP_LEVEL_3 = "CATEGORY_VALUE";

    /**
     * default department
     */
    String DEFAULT_DEPARTMENT = "Default";
    /**
     * Reports Param Date
     */
    String REPORTS_PARAM_DATE = "Date";
    /**
     * Reports Param Range
     */
    String REPORTS_PARAM_RANGE = "Range";
    /**
     * Reports Param Category
     */
    String REPORTS_PARAM_CATEGORY = "Category";
    /**
     * Reports Param Supplier
     */
    String REPORTS_PARAM_SUPPLIER = "Supplier";
    /**
     * Reports Param Customer
     */
    String REPORTS_PARAM_CUSTOMER = "Customer";
    /**
     * Reports Param Staff
     */
    String REPORTS_PARAM_STAFF = "Staff";
    /**
     * Reports Param GroupBy
     */
    String REPORTS_PARAM_GROUPBY = "GroupBy";
    /**
     * Reports Param SortBy
     */
    String REPORTS_PARAM_SORTBY = "SortBy";
    /**
     * Reports Param Value DateFrom
     */
    String REPORTS_PARAM_VAL_DATE_FROM = "DateFrom";
    /**
     * Reports Param Value DateTo
     */
    String REPORTS_PARAM_VAL_DATE_TO = "DateTo";
    /**
     * Reports Param Value RangeFrom
     */
    String REPORTS_PARAM_VAL_RANGE_FROM = "RangeFrom";
    /**
     * Reports Param Value RangeFrom
     */
    String REPORTS_PARAM_VAL_RANGE_TO = "RangeTo";

    /**
     * Reports Param Value None
     */
    String REPORTS_PARAM_VAL_NONE = "None";
    /**
     * Report Key ACCOUNT SALE.
     */
    String REPORT_KEY_ACCOUNT_SALES = "ACCOUNT_SALES";
    /**
     * Report Key Sale By Month
     */
    String REPORT_KEY_SALES_BY_MONTH = "SALES_BY_MONTH";
    /**
     * Report Key SALES_BY_TAX_CODE
     */
    String REPORT_KEY_SALES_BY_TAX_CODE = "SALES_BY_TAX_CODE";
    /**
     * Report Key SALES_BY_TAX_CODE_SUMMARY
     */
    String REPORT_KEY_SALES_BY_TAX_CODE_SUMMARY = "SALES_BY_TAX_CODE_SUMMARY";
    /**
     * Report Key SALES_DAILY
     */
    String REPORT_KEY_SALES_DAILY = "SALES_DAILY";
    /**
     * Report Key SALES_PERIOD
     */
    String REPORT_KEY_SALES_PERIOD = "SALES_PERIOD";
    /**
     * Report Key SALES_PERIOD_AVG
     */
    String REPORT_KEY_SALES_PERIOD_AVG = "SALES_PERIOD_AVG";
    /**
     * Report Key WHAT_IS_SELLING
     */
    String REPORT_KEY_WHAT_IS_SELLING = "WHAT_IS_SELLING";
    /**
     * Report Key PROFIT BY PRODUCT
     */
    String REPORT_KEY_PROFIT_BY_PRODUCT = "PROFIT_BY_PRODUCT";
    /**
     * Report Key WHAT_IS_ON_ORDER
     */
    String REPORT_KEY_WHAT_IS_ON_ORDER = "WHAT_IS_ON_ORDER";
    /**
     * Report Key PRICE_BY_GRADE
     */
    String REPORT_KEY_PRICE_BY_GRADE = "PRICE_BY_GRADE";
    /**
     * Report Key GOODS_RECEIVED
     */
    String REPORT_KEY_GOODS_RECEIVED = "GOODS_RECEIVED";
    /**
     * Report Key WHAT_IS_IN_STOCK
     */
    String REPORT_KEY_WHAT_IS_IN_STOCK = "WHAT_IS_IN_STOCK";
    /**
     * Report Key SUPPILER_LIST
     */
    String REPORT_KEY_SUPPLIER_LIST = "SUPPLIER_LIST";
    /**
     * Report Key CUSTOMER_LIST
     */
    String REPORT_KEY_CUSTOMER_LIST = "CUSTOMER_LIST";
    /**
     * Report Key REPORT_KEY_DEBTOR
     */
    String REPORT_KEY_DEBTOR = "DEBTORS";
    /**
     * CASH SESSION TYPE.
     */
    String CASH_SESSION_TYPE = "CASH_SESSION_TYPE";
    /**
     * CASH SESSION TYPE - SESSION PER USER.
     */
    String CASH_SESSION_TYPE_PER_USER = "SESSION_PER_USER";
    /**
     * CASH SESSION TYPE - SESSION PER COMPANY.
     */
    String CASH_SESSION_TYPE_PER_COMPANY = "SESSION_PER_COMPANY";

}
