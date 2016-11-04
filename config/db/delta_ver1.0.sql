use [retail]
go

alter table purchase_line add POL_QTY_RESERVED DECIMAL(31,4)
go

--PHYSICAL LOCATION OF STOCK.
CREATE TABLE STOCK_LOCATION(
    STCK_LOCN_ID DECIMAL(31,0)IDENTITY (1,1) NOT NULL,
    STCK_LOCN_CODE VARCHAR(100) NULL,
    STCK_LOCN_NAME VARCHAR(100) NULL,
    STCK_LOCN_ADDRESS VARCHAR(500) NULL,
    ORGU_ID DECIMAL(31,0),
    STORE_ID DECIMAL(31,0)
);

CREATE TABLE STOCK(
    STCK_ID DECIMAL(31,0) IDENTITY (1,1) NOT NULL,
    SUPPLIER_ID DECIMAL(31,0) NULL,
    PROD_ID DECIMAL(31,0) NOT NULL,
    PRGP_ID DECIMAL(31,0),
    SELC_ID DECIMAL(31,0),
    ORGU_ID_LOCATION DECIMAL(31,0) NOT NULL,
    STCK_QTY DECIMAL(31,4) NOT NULL,
    UNOM_ID DECIMAL(31,0),
    STCK_COND DECIMAL(31,0) NOT NULL,
    STCK_CAT DECIMAL(31,0) NOT NULL,
    STCK_LOCN_ID DECIMAL(31,0),
    ORGU_ID_RESPBILITY DECIMAL(31,0),
    STCK_VALUE DECIMAL(31,6),
    STCK_LAST_VERIFIED datetime,
    LEGT_ID DECIMAL(31,0)
);

CREATE TABLE STOCK_EVENT(
    STCK_EVNT_ID DECIMAL(31,0) IDENTITY (1,1) NOT NULL,
    STCK_ID DECIMAL(31,0) NOT NULL,
    ORGU_ID DECIMAL(31,0),
    STORE_ID DECIMAL(31,0),
    USR_ID DECIMAL(31,0),
    SUPPLIER_ID DECIMAL(31,0) NULL,
    UNOM_ID DECIMAL(31,0),
    PROD_ID DECIMAL(31,0) NOT NULL,
    PRGP_ID DECIMAL(31,0),
    SELC_ID DECIMAL(31,0),
    STCK_EVNT_DATE datetime,
    TXN_DATE datetime,
    TXN_TYPE DECIMAL(31,0),
    TXN_HEADER DECIMAL(31,0),
    TXN_LINE DECIMAL(31,0),
    TXN_NUMBER VARCHAR(100),
    STCK_LOCN_ID DECIMAL(31,0),
    STCK_QTY DECIMAL(31,4) NOT NULL,
    SELL_PRICE DECIMAL(31,6),
    COST_PRICE DECIMAL(31,6),
    STCK_COND DECIMAL(31,0),
    STCK_CAT DECIMAL(31,0)
);


ALTER TABLE STOCK_LOCATION
 ADD  CONSTRAINT STOCK_LOCATION_PK PRIMARY KEY(STCK_LOCN_ID);
GO

ALTER TABLE STOCK
 ADD  CONSTRAINT STOCK_PK PRIMARY KEY(STCK_ID);
GO

ALTER TABLE STOCK_EVENT
 ADD  CONSTRAINT STOCK_EVENT_PK PRIMARY KEY(STCK_EVNT_ID);
GO

ALTER TABLE STOCK_LOCATION ADD CONSTRAINT STOCK_LOCATION_FK1 FOREIGN KEY (ORGU_ID) REFERENCES ORG_UNIT(ORGU_ID);
go

ALTER TABLE STOCK_LOCATION ADD CONSTRAINT STOCK_LOCATION_FK2 FOREIGN KEY (STORE_ID) REFERENCES STORE(STORE_ID);
go

ALTER TABLE STOCK ADD CONSTRAINT STOCK_FK2 FOREIGN KEY (PROD_ID) REFERENCES PRODUCT(PROD_ID);
go

ALTER TABLE STOCK ADD CONSTRAINT STOCK_FK4 FOREIGN KEY (ORGU_ID_LOCATION) REFERENCES ORG_UNIT(ORGU_ID);
go

ALTER TABLE STOCK ADD CONSTRAINT STOCK_FK5 FOREIGN KEY (UNOM_ID) REFERENCES UNIT_OF_MEASURE(UNOM_ID);
go

ALTER TABLE STOCK ADD CONSTRAINT STOCK_FK6 FOREIGN KEY (STCK_COND) REFERENCES CONFIG_CATEGORY(CATEGORY_ID);
go

ALTER TABLE STOCK ADD CONSTRAINT STOCK_FK7 FOREIGN KEY (STCK_CAT) REFERENCES CONFIG_CATEGORY(CATEGORY_ID);
go

ALTER TABLE STOCK ADD CONSTRAINT STOCK_FK8 FOREIGN KEY (STCK_LOCN_ID) REFERENCES STOCK_LOCATION(STCK_LOCN_ID);
go

ALTER TABLE STOCK ADD CONSTRAINT STOCK_FK9 FOREIGN KEY (ORGU_ID_RESPBILITY) REFERENCES ORG_UNIT(ORGU_ID);
go

ALTER TABLE STOCK_EVENT ADD CONSTRAINT STOCK_EVENT_FK1 FOREIGN KEY (STCK_ID) REFERENCES STOCK(STCK_ID);
go

ALTER TABLE STOCK_EVENT ADD CONSTRAINT STOCK_EVENT_FK2 FOREIGN KEY (ORGU_ID) REFERENCES ORG_UNIT(ORGU_ID);
go

ALTER TABLE STOCK_EVENT ADD CONSTRAINT STOCK_EVENT_FK4 FOREIGN KEY (USR_ID) REFERENCES APP_USER(USR_ID);
go

ALTER TABLE STOCK_EVENT ADD CONSTRAINT STOCK_EVENT_FK5 FOREIGN KEY (PROD_ID) REFERENCES PRODUCT(PROD_ID);
go

ALTER TABLE STOCK_EVENT ADD CONSTRAINT STOCK_EVENT_FK8 FOREIGN KEY (TXN_TYPE) REFERENCES CONFIG_CATEGORY(CATEGORY_ID);
go

ALTER TABLE STOCK_EVENT ADD CONSTRAINT STOCK_EVENT_FK9 FOREIGN KEY (STCK_COND) REFERENCES CONFIG_CATEGORY(CATEGORY_ID);
go

ALTER TABLE STOCK_EVENT ADD CONSTRAINT STOCK_EVENT_FK10 FOREIGN KEY (STCK_CAT) REFERENCES CONFIG_CATEGORY(CATEGORY_ID);
go

ALTER TABLE STOCK_EVENT ADD CONSTRAINT STOCK_EVENT_FK12 FOREIGN KEY (UNOM_ID) REFERENCES UNIT_OF_MEASURE(UNOM_ID);
go

ALTER TABLE STOCK_EVENT ADD CONSTRAINT STOCK_EVENT_FK14 FOREIGN KEY (STCK_LOCN_ID) REFERENCES STOCK_LOCATION(STCK_LOCN_ID);
go

CREATE INDEX STOCK_LOCATION_IDX1 ON STOCK_LOCATION
(STCK_LOCN_NAME)
go

CREATE INDEX STOCK_IDX1 ON STOCK
(PROD_ID)
go

CREATE INDEX STOCK_IDX2 ON STOCK
(ORGU_ID_LOCATION)
go


CREATE INDEX STOCK_IDX3 ON STOCK
(STCK_COND)
go

CREATE INDEX STOCK_IDX4 ON STOCK
(STCK_CAT)
go

CREATE INDEX STOCK_IDX5 ON STOCK
(STCK_LOCN_ID)
go


CREATE INDEX STOCK_EVENT_IDX1 ON STOCK_EVENT
(STCK_ID)
go

CREATE INDEX STOCK_EVENT_IDX2 ON STOCK_EVENT
(ORGU_ID)
go

CREATE INDEX STOCK_EVENT_IDX3 ON STOCK_EVENT
(STORE_ID)
go

CREATE INDEX STOCK_EVENT_IDX4 ON STOCK_EVENT
(USR_ID)
go

CREATE INDEX STOCK_EVENT_IDX5 ON STOCK_EVENT
(PROD_ID)
go

CREATE INDEX STOCK_EVENT_IDX6 ON STOCK_EVENT
(STCK_EVNT_DATE)
go

CREATE INDEX STOCK_EVENT_IDX7 ON STOCK_EVENT
(TXN_DATE)
go

CREATE INDEX STOCK_EVENT_IDX8 ON STOCK_EVENT
(TXN_TYPE)
go

CREATE INDEX STOCK_EVENT_IDX9 ON STOCK_EVENT
(TXN_NUMBER)
go

CREATE INDEX STOCK_EVENT_IDX10 ON STOCK_EVENT
(STCK_COND)
go

CREATE INDEX STOCK_EVENT_IDX11 ON STOCK_EVENT
(STCK_CAT)
go


-------------------------------------- data -------------------------------------------
----- data ----

INSERT INTO CONFIG_CATEGORY(CONFIG_TYPE_ID, CATEGORY_CODE, DISPLAY_NAME, DESCRIPTION) VALUES ((SELECT CONFIG_TYPE_ID FROM CONFIG_TYPE WHERE CONFIG_CODE='TXN_TYPE'),'TXN_TYPE_GOODS_IN', 'GOODS IN', 'GOODS IN');
GO
INSERT INTO CONFIG_CATEGORY(CONFIG_TYPE_ID, CATEGORY_CODE, DISPLAY_NAME, DESCRIPTION) VALUES ((SELECT CONFIG_TYPE_ID FROM CONFIG_TYPE WHERE CONFIG_CODE='TXN_TYPE'),'TXN_TYPE_GOODS_RESERVE', 'GOODS RESERVED', 'GOODS RESERVED');
GO
INSERT INTO CONFIG_CATEGORY(CONFIG_TYPE_ID, CATEGORY_CODE, DISPLAY_NAME, DESCRIPTION) VALUES ((SELECT CONFIG_TYPE_ID FROM CONFIG_TYPE WHERE CONFIG_CODE='TXN_TYPE'),'TXN_TYPE_GOODS_CANCEL_RESERVE', 'RESERVED CANCELED', 'GOODS RESERVED CANCELED');
GO
INSERT INTO CONFIG_CATEGORY(CONFIG_TYPE_ID, CATEGORY_CODE, DISPLAY_NAME, DESCRIPTION) VALUES ((SELECT CONFIG_TYPE_ID FROM CONFIG_TYPE WHERE CONFIG_CODE='TXN_TYPE'),'TXN_TYPE_GOODS_IN_TRANSIT', 'IN TRANSIT', 'GOODS IN TRANSIT');
GO


------------------------ STOCK TYPE -------------------
INSERT INTO CONFIG_TYPE(CONFIG_CODE, DISPLAY_NAME, DESCRIPTION) VALUES ('STOCK_TYPE', 'STOCK TYPE', 'STOCK TYPE');
GO

INSERT INTO CONFIG_CATEGORY(CONFIG_TYPE_ID, CATEGORY_CODE, DISPLAY_NAME, DESCRIPTION) VALUES ((SELECT CONFIG_TYPE_ID FROM CONFIG_TYPE WHERE CONFIG_CODE='STOCK_TYPE'),'STOCK_TYPE_STOCKABLE', 'STOCKABLE', 'Physical products where a stock count/inventory is kept');
GO
INSERT INTO CONFIG_CATEGORY(CONFIG_TYPE_ID, CATEGORY_CODE, DISPLAY_NAME, DESCRIPTION) VALUES ((SELECT CONFIG_TYPE_ID FROM CONFIG_TYPE WHERE CONFIG_CODE='STOCK_TYPE'),'STOCK_TYPE_NON_STOCKABLE', 'NON-STOCKABLE', 'Physical products where a stock count/inventory is not kept, for examplewhere the product is of such a little valuethat the cost of keeping an inventory of the stock outweights the value of the stock');
GO
INSERT INTO CONFIG_CATEGORY(CONFIG_TYPE_ID, CATEGORY_CODE, DISPLAY_NAME, DESCRIPTION) VALUES ((SELECT CONFIG_TYPE_ID FROM CONFIG_TYPE WHERE CONFIG_CODE='STOCK_TYPE'),'STOCK_TYPE_INTANGIBLE', 'INTANGIBLE', 'non physical product where a stock count/inventory can not be kept such as a service, delivery charge etc.');
GO

  ------------------------ STOCK CATEGORY -------------------
INSERT INTO CONFIG_TYPE(CONFIG_CODE, DISPLAY_NAME, DESCRIPTION) VALUES ('STOCK_CATEGORY', 'STOCK CATEGORY', 'STOCK CATEGORY');
GO

INSERT INTO CONFIG_CATEGORY(CONFIG_TYPE_ID, CATEGORY_CODE, DISPLAY_NAME, DESCRIPTION) VALUES ((SELECT CONFIG_TYPE_ID FROM CONFIG_TYPE WHERE CONFIG_CODE='STOCK_CATEGORY'),'STOCK_CATEGORY_SALEABLE', 'SALEABLE', 'Stock that is available for sale');
GO
INSERT INTO CONFIG_CATEGORY(CONFIG_TYPE_ID, CATEGORY_CODE, DISPLAY_NAME, DESCRIPTION) VALUES ((SELECT CONFIG_TYPE_ID FROM CONFIG_TYPE WHERE CONFIG_CODE='STOCK_CATEGORY'),'STOCK_CATEGORY_NON_SALEABLE', 'NON SALEABLE', 'Stock that is not available for sale e.g. Held');
GO
INSERT INTO CONFIG_CATEGORY(CONFIG_TYPE_ID, CATEGORY_CODE, DISPLAY_NAME, DESCRIPTION) VALUES ((SELECT CONFIG_TYPE_ID FROM CONFIG_TYPE WHERE CONFIG_CODE='STOCK_CATEGORY'),'STOCK_CATEGORY_RESERVED', 'RESERVED', 'Stock that is not available for sale because it is reserved');
GO
INSERT INTO CONFIG_CATEGORY(CONFIG_TYPE_ID, CATEGORY_CODE, DISPLAY_NAME, DESCRIPTION) VALUES ((SELECT CONFIG_TYPE_ID FROM CONFIG_TYPE WHERE CONFIG_CODE='STOCK_CATEGORY'),'STOCK_CATEGORY_LAYAWAY', 'LAYAWAY', 'Stock that is not available for sale because it is held for LAYAWAY transaction');
GO
INSERT INTO CONFIG_CATEGORY(CONFIG_TYPE_ID, CATEGORY_CODE, DISPLAY_NAME, DESCRIPTION) VALUES ((SELECT CONFIG_TYPE_ID FROM CONFIG_TYPE WHERE CONFIG_CODE='STOCK_CATEGORY'),'STOCK_CATEGORY_IN_DISPATCH', 'IN DISPATCH', 'Stock that is not available for sale because it is about to be dispatched in a Stock Transfer transaction');
GO
INSERT INTO CONFIG_CATEGORY(CONFIG_TYPE_ID, CATEGORY_CODE, DISPLAY_NAME, DESCRIPTION) VALUES ((SELECT CONFIG_TYPE_ID FROM CONFIG_TYPE WHERE CONFIG_CODE='STOCK_CATEGORY'),'STOCK_CATEGORY_IN_TRANSIT', 'IN TRANSIT', 'Stock that is not available for sale because it is being transfered to site - we can consider this one for projects');
GO
INSERT INTO CONFIG_CATEGORY(CONFIG_TYPE_ID, CATEGORY_CODE, DISPLAY_NAME, DESCRIPTION) VALUES ((SELECT CONFIG_TYPE_ID FROM CONFIG_TYPE WHERE CONFIG_CODE='STOCK_CATEGORY'),'STOCK_CATEGORY_RETURN', 'RETURN', 'Stock that is not available for sale because it is being prepared for return to warehouse, returns location or supplier');
GO

  ------------------------ STOCK CONDITION -------------------
INSERT INTO CONFIG_TYPE(CONFIG_CODE, DISPLAY_NAME, DESCRIPTION) VALUES ('STOCK_CONDITION', 'STOCK CONDITION', 'STOCK CONDITION');
GO

INSERT INTO CONFIG_CATEGORY(CONFIG_TYPE_ID, CATEGORY_CODE, DISPLAY_NAME, DESCRIPTION) VALUES ((SELECT CONFIG_TYPE_ID FROM CONFIG_TYPE WHERE CONFIG_CODE='STOCK_CONDITION'),'STOCK_CONDITION_PRISTINE', 'PRISTINE', 'Stock that is perfect and can be sold');
GO
INSERT INTO CONFIG_CATEGORY(CONFIG_TYPE_ID, CATEGORY_CODE, DISPLAY_NAME, DESCRIPTION) VALUES ((SELECT CONFIG_TYPE_ID FROM CONFIG_TYPE WHERE CONFIG_CODE='STOCK_CONDITION'),'STOCK_CONDITION_DAMAGED', 'DAMAGED', 'Stock that has been classified as damaged and can be written off, returned, repaired or sold(e.g. at a discount');
GO
INSERT INTO CONFIG_CATEGORY(CONFIG_TYPE_ID, CATEGORY_CODE, DISPLAY_NAME, DESCRIPTION) VALUES ((SELECT CONFIG_TYPE_ID FROM CONFIG_TYPE WHERE CONFIG_CODE='STOCK_CONDITION'),'STOCK_CONDITION_FAULTY', 'FAULTY', 'Stock that has been classified as faulty and can be written off, returned, repaired or sold(e.g. at a discount');
GO


------ ASSIGNE STOCK_LOCATION TO ORG_UNIT AND STORE

INSERT INTO STOCK_LOCATION(ORGU_ID, STORE_ID, STCK_LOCN_CODE, STCK_LOCN_NAME, STCK_LOCN_ADDRESS) VALUES ((SELECT ORGU_ID FROM ORG_UNIT WHERE ORGU_CODE='JOMON'),(SELECT STORE_ID FROM STORE WHERE STORE_CODE='JOMON_SYD'),'STOCK_SYD', 'SYDNEY MAIN STOCK', '32 SPENCER STREET, FIVE DOCK, NSW 2046')
GO

-- customer comment

alter table customer add 	[COMMENT] [varchar](1000) NULL
go

INSERT INTO CONFIG_CATEGORY(CONFIG_TYPE_ID, CATEGORY_CODE, DISPLAY_NAME, DESCRIPTION, COLOR) VALUES ((SELECT CONFIG_TYPE_ID FROM CONFIG_TYPE WHERE CONFIG_CODE='CUSTOMER_STATUS'),'CUSTOMER_STATUS_ON_HOLD', 'ON HOLD', 'ON HOLD','amber');
GO

INSERT INTO CONFIG_CATEGORY(CONFIG_TYPE_ID, CATEGORY_CODE, DISPLAY_NAME, DESCRIPTION, COLOR) VALUES ((SELECT CONFIG_TYPE_ID FROM CONFIG_TYPE WHERE CONFIG_CODE='CUSTOMER_STATUS'),'CUSTOMER_STATUS_IN_ACTIVE', 'IN-ACTIVE', 'IN-ACTIVE','yellow');
GO

-- not applied to the jomon

drop index TXHDIX02 on TXN_HEADER
go

alter table txn_header alter column TXHD_TXN_NR varchar(200)
go

alter table txn_header alter column TXHD_ORIG_TXN_NR varchar(200)
go

CREATE INDEX TXHDIX02 ON TXN_HEADER
(TXHD_TXN_NR)
go

INSERT INTO ACCESS_POINT (ACPT_NAME, ACPT_TOKEN,ACPT_DESC) VALUES ('PDF Viewer', 'pdfViewer', 'PDF Viewer');
go
INSERT INTO USER_ACCESS(USR_ID, ACPT_ID) VALUES ( (select USR_ID from APP_USER WHERE USR_NAME = 'Admin'), (select ACPT_ID FROM ACCESS_POINT WHERE ACPT_TOKEN = 'pdfViewer' ));
go
-- up to this point applied to jomon on 17/08/16 11:00

INSERT INTO CONFIG_CATEGORY(CONFIG_TYPE_ID, CATEGORY_CODE, DISPLAY_NAME, DESCRIPTION) VALUES ((SELECT CONFIG_TYPE_ID FROM CONFIG_TYPE WHERE CONFIG_CODE='TXN_TYPE'),'TXN_TYPE_INVOICE', 'INVOICE', 'INVOICE');
GO

alter table TXN_DETAIL add TXDE_QTY_INVOICED DECIMAL(31,4)
go

alter table TXN_DETAIL add TXDE_QTY_BALANCE DECIMAL(31,4)
go

alter table TXN_DETAIL add TXDE_QTY_TOTAL_INVOICED DECIMAL(31,4)
go
--INVOICE

CREATE TABLE INVOICE (
    TXIV_ID DECIMAL(31,0)IDENTITY (1,1) NOT NULL,
    TXHD_ID DECIMAL(31,0),
    ORGU_ID DECIMAL(31,0) NOT NULL,
    STORE_ID DECIMAL(31,0) NOT NULL,
    TXIV_TXN_NR varchar(200),
    TXHD_TXN_NR varchar(200),
    TXIV_TRADING_DATE datetime,
    TXIV_OPERATOR DECIMAL(31,0),
    TXIV_VALUE_GROSS DECIMAL(31,6),
    TXIV_VALUE_NETT DECIMAL(31,6),
    TXIV_VALUE_DUE DECIMAL(31,6),
    TXIV_VALUE_CHANGE DECIMAL(31,6),
    TXIV_VAL_ROUNDING DECIMAL(31,6),
    TXIV_VALUE_TAXABLE DECIMAL(31,6),
    TXIV_VALUE_TAX DECIMAL(31,6),
    TXIV_RECEIPT_ID VARCHAR(50),
    CUSTOMER_ID DECIMAL(31,0),
    TXIV_REFUND_EXPIRY datetime,
    TXIV_COLLECT_DATE datetime
)
GO

CREATE TABLE INVOICE_DETAIL (
    TXID_ID DECIMAL(31,0)IDENTITY (1,1) NOT NULL,
    TXIV_ID DECIMAL(31,0),
    TXDE_ID DECIMAL(31,0),
    ORGU_ID DECIMAL(31,0) NOT NULL,
    STORE_ID DECIMAL(31,0) NOT NULL,
    TXID_QTY_INVOICED DECIMAL(31,4),
    TXID_PRICE_SOLD DECIMAL(31,6)
)
GO

CREATE TABLE INVOICE_MEDIA (
    TXIM_ID DECIMAL(31,0)IDENTITY (1,1) NOT NULL,
    TXIV_ID DECIMAL(31,0),
    TXMD_ID DECIMAL(31,0),
    ORGU_ID DECIMAL(31,0) NOT NULL,
    STORE_ID DECIMAL(31,0) NOT NULL
)
GO

ALTER TABLE INVOICE
 ADD  CONSTRAINT INVOICE_PK PRIMARY KEY(TXIV_ID);
GO

ALTER TABLE INVOICE_DETAIL
 ADD  CONSTRAINT INVOICE_DETAIL_PK PRIMARY KEY(TXID_ID);
GO

ALTER TABLE INVOICE_MEDIA
 ADD  CONSTRAINT INVOICE_MEDIA_PK PRIMARY KEY(TXIM_ID);
GO


ALTER TABLE INVOICE ADD CONSTRAINT INVOICE_FK1 FOREIGN KEY (TXHD_ID) REFERENCES TXN_HEADER(TXHD_ID);
go

ALTER TABLE INVOICE ADD CONSTRAINT INVOICE_FK2 FOREIGN KEY (ORGU_ID) REFERENCES ORG_UNIT(ORGU_ID);
go

ALTER TABLE INVOICE ADD CONSTRAINT INVOICE_FK3 FOREIGN KEY (STORE_ID) REFERENCES STORE(STORE_ID);
go

ALTER TABLE INVOICE ADD CONSTRAINT INVOICE_FK4 FOREIGN KEY (CUSTOMER_ID) REFERENCES CUSTOMER(CUSTOMER_ID);
go

ALTER TABLE INVOICE_DETAIL ADD CONSTRAINT INVOICE_DETAIL_FK1 FOREIGN KEY (TXIV_ID) REFERENCES INVOICE(TXIV_ID);
go

ALTER TABLE INVOICE_DETAIL ADD CONSTRAINT INVOICE_DETAIL_FK2 FOREIGN KEY (TXDE_ID) REFERENCES TXN_DETAIL(TXDE_ID);
go

ALTER TABLE INVOICE_DETAIL ADD CONSTRAINT INVOICE_DETAIL_FK3 FOREIGN KEY (ORGU_ID) REFERENCES ORG_UNIT(ORGU_ID);
go

ALTER TABLE INVOICE_DETAIL ADD CONSTRAINT INVOICE_DETAIL_FK4 FOREIGN KEY (STORE_ID) REFERENCES STORE(STORE_ID);
go


ALTER TABLE INVOICE_MEDIA ADD CONSTRAINT INVOICE_MEDIA_FK1 FOREIGN KEY (TXIV_ID) REFERENCES INVOICE(TXIV_ID);
go

ALTER TABLE INVOICE_MEDIA ADD CONSTRAINT INVOICE_MEDIA_FK2 FOREIGN KEY (TXMD_ID) REFERENCES TXN_MEDIA(TXMD_ID);
go

ALTER TABLE INVOICE_MEDIA ADD CONSTRAINT INVOICE_MEDIA_FK3 FOREIGN KEY (ORGU_ID) REFERENCES ORG_UNIT(ORGU_ID);
go

ALTER TABLE INVOICE_MEDIA ADD CONSTRAINT INVOICE_MEDIA_FK4 FOREIGN KEY (STORE_ID) REFERENCES STORE(STORE_ID);
go

INSERT INTO CONFIG_CATEGORY(CONFIG_TYPE_ID, CATEGORY_CODE, DISPLAY_NAME, DESCRIPTION) VALUES ((SELECT CONFIG_TYPE_ID FROM CONFIG_TYPE WHERE CONFIG_CODE='TXN_MEDIA_TYPE'),'TXN_MEDIA_DEPOSIT', 'DEPOSIT', 'TRANSACTION MEDIA DEPOSIT');
GO

INSERT INTO ACCESS_POINT (ACPT_NAME, ACPT_TOKEN,ACPT_DESC) VALUES ('List Invoice', 'listInvoice', 'View invoice list');
GO

INSERT INTO USER_ACCESS(USR_ID, ACPT_ID) VALUES ( (select USR_ID from APP_USER WHERE USR_NAME = 'Admin'), (select ACPT_ID FROM ACCESS_POINT WHERE ACPT_TOKEN = 'listInvoice' ));
GO



alter table PO_BOQ_LINK add COMMENT VARCHAR(1000)
go

--ACCOUNT PAYMENT

alter table Customer
  add CREDIT_LIMIT DECIMAL(31,6) DEFAULT 0 NOT NULL
go
alter table Customer
  add OWING DECIMAL(31,6) DEFAULT 0 NOT NULL
go
alter table Customer
  add REMAIN_CREDIT DECIMAL(31,6) DEFAULT 0 NOT NULL
go
alter table Customer
  add CREDIT_START_DATE datetime
go
alter table Customer
  add CREDIT_START_EOM BIT DEFAULT 0 NOT NULL
go
alter table Customer
  add   CREDIT_DURATION DECIMAL(31,0) DEFAULT 0 NOT NULL
go


CREATE TABLE CUSTOMER_ACCOUNT_DEBT(
    CAD_ID DECIMAL(31,0) IDENTITY(1,1) NOT NULL,
    CUSTOMER_ID DECIMAL(31,0)  NOT NULL,
    TXHD_ID DECIMAL(31,0)  NOT NULL,
    TXHD_TXN_NR varchar(200),
    CAD_AMOUNT_DEBT DECIMAL(31,6) DEFAULT 0 NOT NULL,
    BALANCE DECIMAL(31,6) DEFAULT 0 NOT NULL,
    CAD_START_DATE datetime,
    CAD_DUE_DATE datetime,
    CAD_PAYMENT_DATE datetime,
    CAD_PAIED BIT DEFAULT 0 NOT NULL
) ON [PRIMARY]
go

ALTER TABLE CUSTOMER_ACCOUNT_DEBT
 ADD  CONSTRAINT CUSTOMER_ACCOUNT_DEBT_PK PRIMARY KEY(CAD_ID);
GO


ALTER TABLE CUSTOMER_ACCOUNT_DEBT ADD CONSTRAINT CUSTOMER_ACCOUNT_DEBT_FK1 FOREIGN KEY (CUSTOMER_ID) REFERENCES CUSTOMER(CUSTOMER_ID);
go

ALTER TABLE CUSTOMER_ACCOUNT_DEBT ADD CONSTRAINT CUSTOMER_ACCOUNT_DEBT_FK2 FOREIGN KEY (TXHD_ID) REFERENCES INVOICE(TXIV_ID);
go



--CUSTOMER ACCOUNT DEBT PAYMENT
CREATE TABLE TXN_ACC_PAYMENT (
    TAP_ID DECIMAL(31,0)IDENTITY (1,1) NOT NULL,
    TXHD_ID DECIMAL(31,0),
    CAD_ID DECIMAL(31,0),
    ORGU_ID DECIMAL(31,0) ,
    STORE_ID DECIMAL(31,0),
    TAP_PAYMENT_DATE datetime,
    TAP_AMOUNT_PAID DECIMAL(31,6)
)
GO

ALTER TABLE TXN_ACC_PAYMENT
 ADD  CONSTRAINT TXN_ACC_PAYMENT_PK PRIMARY KEY(TAP_ID);
GO

ALTER TABLE TXN_ACC_PAYMENT ADD CONSTRAINT TXN_ACC_PAYMENT_FK1 FOREIGN KEY (TXHD_ID) REFERENCES TXN_HEADER(TXHD_ID);
go

ALTER TABLE TXN_ACC_PAYMENT ADD CONSTRAINT TXN_ACC_PAYMENT_FK2 FOREIGN KEY (CAD_ID) REFERENCES CUSTOMER_ACCOUNT_DEBT(CAD_ID);
go


INSERT INTO CONFIG_CATEGORY(CONFIG_TYPE_ID, CATEGORY_CODE, DISPLAY_NAME, DESCRIPTION) VALUES ((SELECT CONFIG_TYPE_ID FROM CONFIG_TYPE WHERE CONFIG_CODE='TXN_TYPE'),'TXN_TYPE_ACCOUNT_PAYMENT', 'ACCOUNT PAY', 'ACCOUNT PAYMENT'); --FOR RECORDING DEBTOR PAYMENT
GO


INSERT INTO ACCESS_POINT (ACPT_NAME, ACPT_TOKEN,ACPT_DESC) VALUES ('Debtor Payment', 'debtorPayment', 'Debtor Payment');
go

INSERT INTO USER_ACCESS(USR_ID, ACPT_ID) VALUES ( (select USR_ID from APP_USER WHERE USR_NAME = 'Admin'), (select ACPT_ID FROM ACCESS_POINT WHERE ACPT_TOKEN = 'debtorPayment' ));
go

alter table TXN_HEADER
  add   TXHD_DLV_ADDRESS VARCHAR(500)
go

--UP TO THIS POINT applied to jomon on 13/09/16 11:35 pm

UPDATE CONFIG_CATEGORY SET DISPLAY_NAME = 'SALE ORDER' where CATEGORY_CODE = 'TXN_TYPE_SALE';
go

UPDATE CONFIG_CATEGORY SET DISPLAY_NAME = 'PENDING' , COLOR='amber' where CATEGORY_CODE = 'TXN_STATE_DRAFT';
go

UPDATE CONFIG_CATEGORY SET DISPLAY_NAME = 'ACCEPTED' , COLOR='green' where CATEGORY_CODE = 'TXN_STATE_FINAL';
go

alter table PURCHASE_ORDER_HEADER
  add POH_COMMENT VARCHAR(1000)
go


alter table PRODUCT
 add PROD_LOCATION VARCHAR(1000)
go


--up to this point applied to JOMON

--CASH FLOW MANAGEMENT
CREATE TABLE CASH_SESSION  (
   CSSN_SESSION_ID DECIMAL(31,0)IDENTITY (1,1) NOT NULL,
   ORGU_ID DECIMAL(31,0) NOT NULL,
   STORE_ID DECIMAL(31,0) NOT NULL,
   TILL_SHORT_DESC VARCHAR(200),
   CSSN_OPERATOR DECIMAL(31,0),
   CSSN_TRADING_DATE datetime,
   CSSN_STATUS DECIMAL(31,0) NOT NULL,
   CSSN_START_DATE datetime,
   CSSN_IMBALANCE_RSN VARCHAR(500),
   CSSN_TXN_NR VARCHAR(100),
   CSSN_FORCED BIT DEFAULT 0 NOT NULL,
   CSSN_IS_OP_ACC BIT DEFAULT 1 NOT NULL,
   CSSN_SAP_CTRL_NR VARCHAR(100),
   CSSN_EXP_END_DATE datetime,
   CSSN_ACT_END_DATE datetime,
   CSSN_RECOCILE_DATE datetime,
   CSSN_METHOD_OPEN DECIMAL(31,0),
   CSSN_METHOD_CLOSE DECIMAL(31,0),
   CSSN_CYCLE_PERIOD DECIMAL(31,0),
   CSSN_CURRENT_CASH DECIMAL(31,6),
   CSSN_TOTAL_FLOAT DECIMAL(31,6) NOT NULL DEFAULT 0,
   CSSN_TOTAL_PICKUP DECIMAL(31,6) NOT NULL DEFAULT 0
   )
go

ALTER TABLE CASH_SESSION
 ADD  CONSTRAINT CASH_SESSION_PK PRIMARY KEY(CSSN_SESSION_ID);
GO


ALTER TABLE CASH_SESSION ADD CONSTRAINT CASH_SESSION_FK1 FOREIGN KEY (ORGU_ID) REFERENCES ORG_UNIT(ORGU_ID);
go

ALTER TABLE CASH_SESSION ADD CONSTRAINT CASH_SESSION_FK2 FOREIGN KEY (STORE_ID) REFERENCES STORE(STORE_ID);
go

ALTER TABLE CASH_SESSION ADD CONSTRAINT CASH_SESSION_FK3 FOREIGN KEY (CSSN_OPERATOR) REFERENCES APP_USER(USR_ID);
go


ALTER TABLE CASH_SESSION ADD CONSTRAINT CASH_SESSION_FK4 FOREIGN KEY (CSSN_STATUS) REFERENCES CONFIG_CATEGORY(CATEGORY_ID);
go

ALTER TABLE CASH_SESSION ADD CONSTRAINT CASH_SESSION_FK5 FOREIGN KEY (CSSN_METHOD_OPEN) REFERENCES CONFIG_CATEGORY(CATEGORY_ID);
go

ALTER TABLE CASH_SESSION ADD CONSTRAINT CASH_SESSION_FK6 FOREIGN KEY (CSSN_METHOD_CLOSE) REFERENCES CONFIG_CATEGORY(CATEGORY_ID);
go

ALTER TABLE CASH_SESSION ADD CONSTRAINT CASH_SESSION_FK7 FOREIGN KEY (CSSN_CYCLE_PERIOD) REFERENCES CONFIG_CATEGORY(CATEGORY_ID);
go



CREATE TABLE SESSION_EVENT  (
   SEEV_ID DECIMAL(31,0)IDENTITY (1,1) NOT NULL,
   CSSN_SESSION_ID DECIMAL(31,0) NOT NULL,
   ORGU_ID DECIMAL(31,0) NOT NULL,
   STORE_ID DECIMAL(31,0) NOT NULL,
   SEEV_EVENT_TYPE DECIMAL(31,0) NOT NULL,
   SEEV_EVENT_DATE datetime,
   SEEV_OPERATOR DECIMAL(31,0),
   SEEV_REFERENCE VARCHAR(500),
   SEEV_REASON VARCHAR(500),
   SEEV_COLLECT_DATE datetime,
   SEEV_COLLECT_REF VARCHAR(500),
   SEEV_PICKUP_NR VARCHAR(500),
   SEEV_PAY_IN_SLIP VARCHAR(500),
   SEEV_COMMENT VARCHAR(500),
   )
go


ALTER TABLE SESSION_EVENT
 ADD  CONSTRAINT SESSION_EVENT_PK PRIMARY KEY(SEEV_ID);
GO



ALTER TABLE SESSION_EVENT ADD CONSTRAINT SESSION_EVENT_FK1 FOREIGN KEY (CSSN_SESSION_ID) REFERENCES CASH_SESSION(CSSN_SESSION_ID);
go

ALTER TABLE SESSION_EVENT ADD CONSTRAINT SESSION_EVENT_FK2 FOREIGN KEY (ORGU_ID) REFERENCES ORG_UNIT(ORGU_ID);
go

ALTER TABLE SESSION_EVENT ADD CONSTRAINT SESSION_EVENT_FK3 FOREIGN KEY (STORE_ID) REFERENCES STORE(STORE_ID);
go

ALTER TABLE SESSION_EVENT ADD CONSTRAINT SESSION_EVENT_FK4 FOREIGN KEY (SEEV_EVENT_TYPE) REFERENCES CONFIG_CATEGORY(CATEGORY_ID);
go

ALTER TABLE SESSION_EVENT ADD CONSTRAINT SESSION_EVENT_FK5 FOREIGN KEY (SEEV_OPERATOR) REFERENCES APP_USER(USR_ID);
go



CREATE TABLE SESSION_MEDIA  (
   SEME_ID DECIMAL(31,0)IDENTITY (1,1) NOT NULL,
   CSSN_SESSION_ID DECIMAL(31,0) NOT NULL,
   SEEV_ID DECIMAL(31,0) NOT NULL,
   ORGU_ID DECIMAL(31,0) NOT NULL,
   STORE_ID DECIMAL(31,0) NOT NULL,
   MEDT_ID DECIMAL(31,0),
   PAYM_ID DECIMAL(31,0),
   SEME_MEDIA_COUNT DECIMAL(31,4),
   SEME_MEDIA_VALUE DECIMAL(38,6) NOT NULL DEFAULT 0
   )
go


ALTER TABLE SESSION_MEDIA
 ADD  CONSTRAINT SESSION_MEDIA_PK PRIMARY KEY(SEME_ID);
GO

ALTER TABLE SESSION_MEDIA ADD CONSTRAINT SESSION_MEDIA_FK1 FOREIGN KEY (CSSN_SESSION_ID) REFERENCES CASH_SESSION(CSSN_SESSION_ID);
go

ALTER TABLE SESSION_MEDIA ADD CONSTRAINT SESSION_MEDIA_FK2 FOREIGN KEY (SEEV_ID) REFERENCES SESSION_EVENT(SEEV_ID);
go

ALTER TABLE SESSION_MEDIA ADD CONSTRAINT SESSION_MEDIA_FK3 FOREIGN KEY (ORGU_ID) REFERENCES ORG_UNIT(ORGU_ID);
go

ALTER TABLE SESSION_MEDIA ADD CONSTRAINT SESSION_MEDIA_FK4 FOREIGN KEY (STORE_ID) REFERENCES STORE(STORE_ID);
go

ALTER TABLE SESSION_MEDIA ADD CONSTRAINT SESSION_MEDIA_FK5 FOREIGN KEY (MEDT_ID) REFERENCES MEDIA_TYPE(MEDT_ID);
go

ALTER TABLE SESSION_MEDIA ADD CONSTRAINT SESSION_MEDIA_FK6 FOREIGN KEY (PAYM_ID) REFERENCES PAYMENT_MEDIA(PAYM_ID);
go


--DETAIL FOR SOME SESSION EVENT LIKE RECONCILIATION.
CREATE TABLE SESSION_EVENT_DETAIL  (
   SEDE_ID DECIMAL(31,0)IDENTITY (1,1) NOT NULL,
   CSSN_SESSION_ID DECIMAL(31,0) NOT NULL,
   SEEV_ID DECIMAL(31,0),
   ORGU_ID DECIMAL(31,0) NOT NULL,
   STORE_ID DECIMAL(31,0) NOT NULL,
   MEDT_ID DECIMAL(31,0),
   PAYM_ID DECIMAL(31,0),
   MEDIA_COUNT_ACTUAL DECIMAL(31,4),
   MEDIA_COUNT_EXPECTED DECIMAL(31,4),
   MEDIA_COUNT_DIFF DECIMAL(31,4),
   MEDIA_VALUE_ACTUAL DECIMAL(38,6) NOT NULL DEFAULT 0,
   MEDIA_VALUE_EXPECTED DECIMAL(38,6) NOT NULL DEFAULT 0,
   MEDIA_VALUE_DIFF DECIMAL(38,6) NOT NULL DEFAULT 0,
   SEDE_COMMENT VARCHAR(500)
   )
go

ALTER TABLE SESSION_EVENT_DETAIL
 ADD  CONSTRAINT SESSION_EVENT_DETAIL_PK PRIMARY KEY(SEDE_ID);
GO

ALTER TABLE SESSION_EVENT_DETAIL ADD CONSTRAINT SESSION_EVENT_DETAIL_FK1 FOREIGN KEY (CSSN_SESSION_ID) REFERENCES CASH_SESSION(CSSN_SESSION_ID);
go

ALTER TABLE SESSION_EVENT_DETAIL ADD CONSTRAINT SESSION_EVENT_DETAIL_FK2 FOREIGN KEY (SEEV_ID) REFERENCES SESSION_EVENT(SEEV_ID);
go

ALTER TABLE SESSION_EVENT_DETAIL ADD CONSTRAINT SESSION_EVENT_DETAIL_FK3 FOREIGN KEY (ORGU_ID) REFERENCES ORG_UNIT(ORGU_ID);
go

ALTER TABLE SESSION_EVENT_DETAIL ADD CONSTRAINT SESSION_EVENT_DETAIL_FK4 FOREIGN KEY (STORE_ID) REFERENCES STORE(STORE_ID);
go

ALTER TABLE SESSION_EVENT_DETAIL ADD CONSTRAINT SESSION_EVENT_DETAIL_FK5 FOREIGN KEY (MEDT_ID) REFERENCES MEDIA_TYPE(MEDT_ID);
go

ALTER TABLE SESSION_EVENT_DETAIL ADD CONSTRAINT SESSION_EVENT_DETAIL_FK6 FOREIGN KEY (PAYM_ID) REFERENCES PAYMENT_MEDIA(PAYM_ID);
go

CREATE TABLE SESSION_TOTAL  (
   SETL_ID DECIMAL(31,0)IDENTITY (1,1) NOT NULL,
   CSSN_SESSION_ID DECIMAL(31,0) NOT NULL,
   MEDT_ID DECIMAL(31,0),
   PAYM_ID DECIMAL(31,0),
   MEDIA_TOTAL_COUNT DECIMAL(31,4),
   MEDIA_TOTAL_VALUE DECIMAL(38,6) NOT NULL DEFAULT 0
   )
go

ALTER TABLE SESSION_TOTAL ADD CONSTRAINT SESSION_TOTAL_UK1 UNIQUE(CSSN_SESSION_ID,
   MEDT_ID,
   PAYM_ID)
go

ALTER TABLE SESSION_TOTAL
 ADD  CONSTRAINT SESSION_TOTAL_PK PRIMARY KEY(SETL_ID);
GO

ALTER TABLE SESSION_TOTAL ADD CONSTRAINT SESSION_TOTAL_FK1 FOREIGN KEY (CSSN_SESSION_ID) REFERENCES CASH_SESSION(CSSN_SESSION_ID);
go

ALTER TABLE SESSION_TOTAL ADD CONSTRAINT SESSION_TOTAL_FK2 FOREIGN KEY (MEDT_ID) REFERENCES MEDIA_TYPE(MEDT_ID);
go

ALTER TABLE SESSION_TOTAL ADD CONSTRAINT SESSION_TOTAL_FK3 FOREIGN KEY (PAYM_ID) REFERENCES PAYMENT_MEDIA(PAYM_ID);
go

CREATE TABLE TXN_SESSION_LINK  (
   TSL_ID DECIMAL(31,0)IDENTITY (1,1) NOT NULL,
   CSSN_SESSION_ID DECIMAL(31,0) NOT NULL,
   TXN_ID DECIMAL(31,0),
   TXN_TYPE DECIMAL(31,0),
   ORGU_ID DECIMAL(31,0) NOT NULL,
   STORE_ID DECIMAL(31,0) NOT NULL,
   TILL_SHORT_DESC VARCHAR(100),
   TXHD_TXN_NR VARCHAR(100),
   TSL_DATE_CREATED datetime
   )
go

ALTER TABLE TXN_SESSION_LINK
 ADD  CONSTRAINT TXN_SESSION_LINK_PK PRIMARY KEY(TSL_ID);
GO

ALTER TABLE TXN_SESSION_LINK ADD CONSTRAINT TXN_SESSION_LINK_FK1 FOREIGN KEY (CSSN_SESSION_ID) REFERENCES CASH_SESSION(CSSN_SESSION_ID);
go

ALTER TABLE TXN_SESSION_LINK ADD CONSTRAINT TXN_SESSION_LINK_FK2 FOREIGN KEY (TXN_TYPE) REFERENCES CONFIG_CATEGORY(CATEGORY_ID);
go

ALTER TABLE TXN_SESSION_LINK ADD CONSTRAINT TXN_SESSION_LINK_FK3 FOREIGN KEY (ORGU_ID) REFERENCES ORG_UNIT(ORGU_ID);
go

ALTER TABLE TXN_SESSION_LINK ADD CONSTRAINT TXN_SESSION_LINK_FK4 FOREIGN KEY (STORE_ID) REFERENCES STORE(STORE_ID);
go

-- BASE DATA
------------------------ SESSION EVENT TYPE -------------------
INSERT INTO CONFIG_TYPE(CONFIG_CODE, DISPLAY_NAME, DESCRIPTION) VALUES ('SESSION_EVENT_TYPE', 'SESSION EVENT TYPE', 'SESSION EVENT TYPE');
GO

INSERT INTO CONFIG_CATEGORY(CONFIG_TYPE_ID, CATEGORY_CODE, DISPLAY_NAME, DESCRIPTION) VALUES ((SELECT CONFIG_TYPE_ID FROM CONFIG_TYPE WHERE CONFIG_CODE='SESSION_EVENT_TYPE'),'SESSION_EVENT_TYPE_OPEN', 'OPEN', 'open session event');
GO
INSERT INTO CONFIG_CATEGORY(CONFIG_TYPE_ID, CATEGORY_CODE, DISPLAY_NAME, DESCRIPTION) VALUES ((SELECT CONFIG_TYPE_ID FROM CONFIG_TYPE WHERE CONFIG_CODE='SESSION_EVENT_TYPE'),'SESSION_EVENT_TYPE_CLOSE', 'CLOSE', 'CLOSE SESSION');
GO
INSERT INTO CONFIG_CATEGORY(CONFIG_TYPE_ID, CATEGORY_CODE, DISPLAY_NAME, DESCRIPTION) VALUES ((SELECT CONFIG_TYPE_ID FROM CONFIG_TYPE WHERE CONFIG_CODE='SESSION_EVENT_TYPE'),'SESSION_EVENT_TYPE_END', 'END', 'END SESSION');
GO
INSERT INTO CONFIG_CATEGORY(CONFIG_TYPE_ID, CATEGORY_CODE, DISPLAY_NAME, DESCRIPTION) VALUES ((SELECT CONFIG_TYPE_ID FROM CONFIG_TYPE WHERE CONFIG_CODE='SESSION_EVENT_TYPE'),'SESSION_EVENT_TYPE_RECONCILE', 'RECONCILE', 'RECONCILE SESSION');
GO
INSERT INTO CONFIG_CATEGORY(CONFIG_TYPE_ID, CATEGORY_CODE, DISPLAY_NAME, DESCRIPTION) VALUES ((SELECT CONFIG_TYPE_ID FROM CONFIG_TYPE WHERE CONFIG_CODE='SESSION_EVENT_TYPE'),'SESSION_EVENT_TYPE_FLOAT', 'ADD FLOAT', 'ADD FLOAT');
GO
INSERT INTO CONFIG_CATEGORY(CONFIG_TYPE_ID, CATEGORY_CODE, DISPLAY_NAME, DESCRIPTION) VALUES ((SELECT CONFIG_TYPE_ID FROM CONFIG_TYPE WHERE CONFIG_CODE='SESSION_EVENT_TYPE'),'SESSION_EVENT_TYPE_PICKUP', 'PICKUP', 'PICKUP');
GO
INSERT INTO CONFIG_CATEGORY(CONFIG_TYPE_ID, CATEGORY_CODE, DISPLAY_NAME, DESCRIPTION) VALUES ((SELECT CONFIG_TYPE_ID FROM CONFIG_TYPE WHERE CONFIG_CODE='SESSION_EVENT_TYPE'),'SESSION_EVENT_TYPE_TXN', 'TRANSACTION', 'DO TRANSACTION AGAINST SESSION');
GO
INSERT INTO CONFIG_CATEGORY(CONFIG_TYPE_ID, CATEGORY_CODE, DISPLAY_NAME, DESCRIPTION) VALUES ((SELECT CONFIG_TYPE_ID FROM CONFIG_TYPE WHERE CONFIG_CODE='SESSION_EVENT_TYPE'),'SESSION_EVENT_TYPE_PAY', 'ADD PAYMENT', 'ADD PAYMENT TO TRANSACTION');
GO
INSERT INTO CONFIG_CATEGORY(CONFIG_TYPE_ID, CATEGORY_CODE, DISPLAY_NAME, DESCRIPTION) VALUES ((SELECT CONFIG_TYPE_ID FROM CONFIG_TYPE WHERE CONFIG_CODE='SESSION_EVENT_TYPE'),'SESSION_EVENT_TYPE_ACC_PAY', 'ACCOUNT PAYMENT', 'ACCOUNT PAYMENT');
GO


------------------------ SESSION STATE -------------------
INSERT INTO CONFIG_TYPE(CONFIG_CODE, DISPLAY_NAME, DESCRIPTION) VALUES ('SESSION_STATE', 'SESSION STATE', 'SESSION STATE');
GO

INSERT INTO CONFIG_CATEGORY(CONFIG_TYPE_ID, CATEGORY_CODE, DISPLAY_NAME, DESCRIPTION, COLOR) VALUES ((SELECT CONFIG_TYPE_ID FROM CONFIG_TYPE WHERE CONFIG_CODE='SESSION_STATE'),'SESSION_STATE_OPEN', 'OPEN', 'open session event','amber');
GO
INSERT INTO CONFIG_CATEGORY(CONFIG_TYPE_ID, CATEGORY_CODE, DISPLAY_NAME, DESCRIPTION, COLOR) VALUES ((SELECT CONFIG_TYPE_ID FROM CONFIG_TYPE WHERE CONFIG_CODE='SESSION_STATE'),'SESSION_STATE_CLOSED', 'CLOSED', 'CLOSED SESSION','blue');
GO
INSERT INTO CONFIG_CATEGORY(CONFIG_TYPE_ID, CATEGORY_CODE, DISPLAY_NAME, DESCRIPTION, COLOR) VALUES ((SELECT CONFIG_TYPE_ID FROM CONFIG_TYPE WHERE CONFIG_CODE='SESSION_STATE'),'SESSION_STATE_ENDED', 'ENDED', 'ENDED SESSION','brown');
GO
INSERT INTO CONFIG_CATEGORY(CONFIG_TYPE_ID, CATEGORY_CODE, DISPLAY_NAME, DESCRIPTION, COLOR) VALUES ((SELECT CONFIG_TYPE_ID FROM CONFIG_TYPE WHERE CONFIG_CODE='SESSION_STATE'),'SESSION_STATE_RECONCILED', 'RECONCILED', 'RECONCILED SESSION','green');
GO


CREATE TABLE TOTAL_SALE_OPERATOR  (
   TOOP_ID DECIMAL(31,0)IDENTITY (1,1) NOT NULL,
   TOOP_OPERATOR DECIMAL(31,0),
   TOOP_TXN_TYPE DECIMAL(31,0),
   ORGU_ID DECIMAL(31,0) NOT NULL,
   STORE_ID DECIMAL(31,0) NOT NULL,
   TOOP_TRADING_DATE datetime,
   TOOP_SALE_QTY DECIMAL(31,4) NOT NULL DEFAULT 0, --TOTAL SALE ITEMS
   TOOP_ITEMS_VALUE DECIMAL(38,6) NOT NULL DEFAULT 0, -- ITEM COST
   TOOP_PROFIT_VALUE DECIMAL(38,6) NOT NULL DEFAULT 0, -- PROFIT GAINED ON SELLING ITEMS
   TOOP_TAXED_VALUE DECIMAL(38,6) NOT NULL DEFAULT 0, -- VALUE ELIGIBLE FOR TAX
   TOOP_TAX_PAID DECIMAL(38,6) NOT NULL DEFAULT 0, -- AMOUNT PAID FOR TAX
   TOOP_SALE_VALUE DECIMAL(38,6) NOT NULL DEFAULT 0, -- ITEM VALUE + PROFIT VALUE + TAXED PAID
   TOOP_REFUND_QTY DECIMAL(31,4) NOT NULL DEFAULT 0, -- TOTAL REFUND ITEMS
   TOOP_REFUND_VALUE DECIMAL(38,6) NOT NULL DEFAULT 0,
   TOOP_DISCOUNT_VALUE DECIMAL(38,6) NOT NULL DEFAULT 0, -- DISCOUNT APPLIED OT ITEMS
   TOOP_VOID_QTY DECIMAL(31,4) NOT NULL DEFAULT 0, -- TOTAL REFUND ITEMS
   TOOP_VOID_VALUE DECIMAL(38,6) NOT NULL DEFAULT 0
   )
go

ALTER TABLE TOTAL_SALE_OPERATOR
 ADD  CONSTRAINT TOTAL_SALE_OPERATOR_PK PRIMARY KEY(TOOP_ID);
GO

ALTER TABLE TOTAL_SALE_OPERATOR ADD CONSTRAINT TOTAL_SALE_OPERATOR_FK1 FOREIGN KEY (TOOP_OPERATOR) REFERENCES app_user(USR_ID);
go

ALTER TABLE TOTAL_SALE_OPERATOR ADD CONSTRAINT TOTAL_SALE_OPERATOR_FK2 FOREIGN KEY (TOOP_TXN_TYPE) REFERENCES CONFIG_CATEGORY(CATEGORY_ID);
go

ALTER TABLE TOTAL_SALE_OPERATOR ADD CONSTRAINT TOTAL_SALE_OPERATOR_FK3 FOREIGN KEY (ORGU_ID) REFERENCES ORG_UNIT(ORGU_ID);
go

ALTER TABLE TOTAL_SALE_OPERATOR ADD CONSTRAINT TOTAL_SALE_OPERATOR_FK4 FOREIGN KEY (STORE_ID) REFERENCES STORE(STORE_ID);
go


CREATE INDEX TOTAL_SALE_OPERATOR_IDX1 ON TOTAL_SALE_OPERATOR
(TOOP_OPERATOR)
go

CREATE INDEX TOTAL_SALE_OPERATOR_IDX2 ON TOTAL_SALE_OPERATOR
(TOOP_TXN_TYPE)
go

CREATE INDEX TOTAL_SALE_OPERATOR_IDX3 ON TOTAL_SALE_OPERATOR
(ORGU_ID)
go

CREATE INDEX TOTAL_SALE_OPERATOR_IDX4 ON TOTAL_SALE_OPERATOR
(STORE_ID)
go

CREATE INDEX TOTAL_SALE_OPERATOR_IDX5 ON TOTAL_SALE_OPERATOR
(TOOP_TRADING_DATE)
go


CREATE TABLE TOTAL_TAX_GROUP  (
   ORGU_ID DECIMAL(31,0) NOT NULL,
   STORE_ID DECIMAL(31,0) NOT NULL,
   TOTG_TXRL_ID DECIMAL(31,0) NOT NULL,
   TOTG_TRADING_DATE datetime NOT NULL,
   TOTG_TXGP_CODE VARCHAR(10) NOT NULL,
   TOTG_TAXED_VALUE DECIMAL(38,6) NOT NULL DEFAULT 0,
   TOTG_TAX DECIMAL(38,6) NOT NULL DEFAULT 0,
   TOTG_TOT_SALES_QTY DECIMAL(31,4)
   )
go

ALTER TABLE TOTAL_TAX_GROUP ADD CONSTRAINT TOTAL_TAX_GROUP_FK1 FOREIGN KEY (STORE_ID) REFERENCES STORE(STORE_ID);
go

ALTER TABLE TOTAL_TAX_GROUP ADD CONSTRAINT TOTAL_TAX_GROUP_FK2 FOREIGN KEY (ORGU_ID) REFERENCES ORG_UNIT(ORGU_ID);
go

ALTER TABLE TOTAL_TAX_GROUP ADD CONSTRAINT TOTAL_TAX_GROUP_FK3 FOREIGN KEY (TOTG_TXRL_ID) REFERENCES TAX_LEG_VARIANCE(TXLV_ID);
go

CREATE INDEX TOTAL_TAX_GROUP_IDX1 ON TOTAL_TAX_GROUP
(ORGU_ID)
go

CREATE INDEX TOTAL_TAX_GROUP_IDX2 ON TOTAL_TAX_GROUP
(STORE_ID)
go

CREATE INDEX TOTAL_TAX_GROUP_IDX3 ON TOTAL_TAX_GROUP
(TOTG_TXRL_ID)
go

CREATE INDEX TOTAL_TAX_GROUP_IDX4 ON TOTAL_TAX_GROUP
(TOTG_TRADING_DATE)
go

CREATE INDEX TOTAL_TAX_GROUP_IDX5 ON TOTAL_TAX_GROUP
(TOTG_TXGP_CODE)
go


CREATE TABLE TOTAL_MEDIA_OPERATOR  (
   TOMO_ID DECIMAL(31,0)IDENTITY (1,1) NOT NULL,
   TOMO_OPERATOR DECIMAL(31,0) NOT NULL,
   ORGU_ID DECIMAL(31,0) NOT NULL,
   STORE_ID DECIMAL(31,0) NOT NULL,
   TOMO_TRADING_DATE datetime NOT NULL,
   MEDT_ID DECIMAL(31,0),
   PAYM_ID DECIMAL(31,0),
   TOMO_SALE_QTY DECIMAL(31,4) NOT NULL DEFAULT 0,
   TOMO_SALE_VALUE DECIMAL(38,6) NOT NULL DEFAULT 0,
   TOMO_REFUND_QTY DECIMAL(31,4) NOT NULL DEFAULT 0,
   TOMO_REFUMND_VALUE DECIMAL(38,6) NOT NULL DEFAULT 0,
   TOMO_CASHBACK_QTY DECIMAL(31,4) NOT NULL DEFAULT 0,
   TOMO_CASHBACK_VALUE DECIMAL(38,6) NOT NULL DEFAULT 0
   )
go


ALTER TABLE TOTAL_MEDIA_OPERATOR
 ADD  CONSTRAINT TOTAL_MEDIA_OPERATOR_PK PRIMARY KEY(TOMO_ID);
GO

ALTER TABLE TOTAL_MEDIA_OPERATOR ADD CONSTRAINT TOTAL_MEDIA_OPERATOR_FK1 FOREIGN KEY (TOMO_OPERATOR) REFERENCES app_user(USR_ID);
go

ALTER TABLE TOTAL_MEDIA_OPERATOR ADD CONSTRAINT TOTAL_MEDIA_OPERATOR_FK3 FOREIGN KEY (ORGU_ID) REFERENCES ORG_UNIT(ORGU_ID);
go

ALTER TABLE TOTAL_MEDIA_OPERATOR ADD CONSTRAINT TOTAL_MEDIA_OPERATOR_FK4 FOREIGN KEY (STORE_ID) REFERENCES STORE(STORE_ID);
go


CREATE INDEX TOTAL_MEDIA_OPERATOR_IDX1 ON TOTAL_MEDIA_OPERATOR
(TOMO_OPERATOR)
go

CREATE INDEX TOTAL_MEDIA_OPERATOR_IDX2 ON TOTAL_MEDIA_OPERATOR
(ORGU_ID)
go

CREATE INDEX TOTAL_MEDIA_OPERATOR_IDX3 ON TOTAL_MEDIA_OPERATOR
(STORE_ID)
go

CREATE INDEX TOTAL_MEDIA_OPERATOR_IDX4 ON TOTAL_MEDIA_OPERATOR
(TOMO_TRADING_DATE)
go

CREATE INDEX TOTAL_MEDIA_OPERATOR_IDX5 ON TOTAL_MEDIA_OPERATOR
(PAYM_ID)
go

-- CUSTOMER GRADE
ALTER TABLE CUSTOMER_GRADE DROP COLUMN RATE
GO

ALTER TABLE CUSTOMER_GRADE ADD RATE DECIMAL(31,6)
GO

UPDATE CUSTOMER_GRADE SET RATE = 0.20 WHERE GRADE_CODE = 'A'
GO

UPDATE CUSTOMER_GRADE SET RATE = 0.23 WHERE GRADE_CODE = 'B'
GO

UPDATE CUSTOMER_GRADE SET RATE = 0.28 WHERE GRADE_CODE = 'C'
GO

UPDATE CUSTOMER_GRADE SET RATE = 0.38 WHERE GRADE_CODE = 'D'
GO


INSERT INTO ACCESS_POINT (ACPT_NAME, ACPT_TOKEN,ACPT_DESC) VALUES ('List Cash Session', 'listCurrentCashSession', 'List Cash Session');
go

INSERT INTO ACCESS_POINT (ACPT_NAME, ACPT_TOKEN,ACPT_DESC) VALUES ('Add Float', 'addFloat', 'add float');
go

INSERT INTO USER_ACCESS(USR_ID, ACPT_ID) VALUES ( (select USR_ID from APP_USER WHERE USR_NAME = 'Admin'), (select ACPT_ID FROM ACCESS_POINT WHERE ACPT_TOKEN = 'listCurrentCashSession' ));
go

INSERT INTO USER_ACCESS(USR_ID, ACPT_ID) VALUES ( (select USR_ID from APP_USER WHERE USR_NAME = 'Admin'), (select ACPT_ID FROM ACCESS_POINT WHERE ACPT_TOKEN = 'addFloat' ));
go



INSERT INTO ACCESS_POINT (ACPT_NAME, ACPT_TOKEN,ACPT_DESC) VALUES ('Reconcile Session', 'reconcileSession', 'reconcile session');
go

INSERT INTO USER_ACCESS(USR_ID, ACPT_ID) VALUES ( (select USR_ID from APP_USER WHERE USR_NAME = 'Admin'), (select ACPT_ID FROM ACCESS_POINT WHERE ACPT_TOKEN = 'reconcileSession' ));
go

INSERT INTO ACCESS_POINT (ACPT_NAME, ACPT_TOKEN,ACPT_DESC) VALUES ('Sale Summary Report', 'saleSummaryReport', 'sale summary report');
go

INSERT INTO USER_ACCESS(USR_ID, ACPT_ID) VALUES ( (select USR_ID from APP_USER WHERE USR_NAME = 'Admin'), (select ACPT_ID FROM ACCESS_POINT WHERE ACPT_TOKEN = 'saleSummaryReport' ));
go

------------------------- REFUND ----------------------------

alter table TXN_DETAIL
  add       TXDE_QTY_REFUND DECIMAL(31,4)
go

alter table TXN_DETAIL
  add       TXDE_QTY_TOTAL_REFUND DECIMAL(31,4)
go


alter table INVOICE
  add   TXIV_TXN_TYPE DECIMAL(31,0)
go

ALTER TABLE INVOICE ADD CONSTRAINT INVOICE_FK5 FOREIGN KEY (TXIV_TXN_TYPE) REFERENCES CONFIG_CATEGORY(CATEGORY_ID);
go


alter table INVOICE_DETAIL
  add       TXID_LINE_TYPE DECIMAL(31,0)
go

alter table INVOICE_DETAIL
  add       TXID_QTY_REFUND DECIMAL(31,4)
go

ALTER TABLE INVOICE_DETAIL ADD CONSTRAINT INVOICE_DETAIL_FK5 FOREIGN KEY (TXID_LINE_TYPE) REFERENCES CONFIG_CATEGORY(CATEGORY_ID);
go

alter table INVOICE_MEDIA
  add TXIM_LINE_TYPE DECIMAL(31,0)
go

ALTER TABLE INVOICE_MEDIA ADD CONSTRAINT INVOICE_MEDIA_FK5 FOREIGN KEY (TXIM_LINE_TYPE) REFERENCES CONFIG_CATEGORY(CATEGORY_ID);
go

INSERT INTO ACCESS_POINT (ACPT_NAME, ACPT_TOKEN,ACPT_DESC) VALUES ('Refund', 'refundTxn', 'Refund Transaction');
go
INSERT INTO USER_ACCESS(USR_ID, ACPT_ID) VALUES ( (select USR_ID from APP_USER WHERE USR_NAME = 'Admin'), (select ACPT_ID FROM ACCESS_POINT WHERE ACPT_TOKEN = 'refundTxn' ));
go

INSERT INTO ACCESS_POINT (ACPT_NAME, ACPT_TOKEN,ACPT_DESC) VALUES ('CashUp Reports', 'cashupReport', 'Print CashUp Reports');
go

INSERT INTO USER_ACCESS(USR_ID, ACPT_ID) VALUES ( (select USR_ID from APP_USER WHERE USR_NAME = 'Admin'), (select ACPT_ID FROM ACCESS_POINT WHERE ACPT_TOKEN = 'cashupReport' ));
go


-- up to this point applied on jomon on 30/10/16
INSERT INTO ACCESS_POINT (ACPT_NAME, ACPT_TOKEN,ACPT_DESC) VALUES ('Create Invoice', 'createInvoice', 'Create Invoice');
go

INSERT INTO USER_ACCESS(USR_ID, ACPT_ID) VALUES ( (select USR_ID from APP_USER WHERE USR_NAME = 'Admin'), (select ACPT_ID FROM ACCESS_POINT WHERE ACPT_TOKEN = 'createInvoice' ));
go

alter table INVOICE_DETAIL
  add  TXID_SURCHARGE DECIMAL(31,6) DEFAULT 0.00
go

alter table INVOICE
  add  TXIV_DLV_ADDRESS varchar(2000)
go

INSERT INTO ACCESS_POINT (ACPT_NAME, ACPT_TOKEN,ACPT_DESC) VALUES ('Import Product', 'importProduct', 'import product');
go
INSERT INTO USER_ACCESS(USR_ID, ACPT_ID) VALUES ( (select USR_ID from APP_USER WHERE USR_NAME = 'Admin'), (select ACPT_ID FROM ACCESS_POINT WHERE ACPT_TOKEN = 'importProduct' ));
go