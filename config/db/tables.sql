USE [RETAIL]
GO

CREATE TABLE [dbo].[CONFIG_TYPE](
	[CONFIG_TYPE_ID] [int] IDENTITY (1,1) NOT NULL,
	[CONFIG_CODE] [varchar](100) NOT NULL,
	[DISPLAY_NAME] [varchar](200) NOT NULL,
	[DESCRIPTION] [varchar](500) NULL,
	[CATEGORY_ORDER] [int] NULL
) ON [PRIMARY]

CREATE TABLE [dbo].[CONFIG_CATEGORY](
	[CATEGORY_ID] DECIMAL(31,0) IDENTITY (1,1) NOT NULL,
	[CONFIG_TYPE_ID] [int] NOT NULL,
	[CATEGORY_CODE] [varchar](100) NULL,
	[DISPLAY_NAME] [varchar](200) NOT NULL,
	[CATEGORY_ORDER] [int] ,
	[DESCRIPTION] [varchar](500) NULL
) ON [PRIMARY]


CREATE TABLE [dbo].[Customer](
	[CUSTOMER_ID] DECIMAL(31,0) IDENTITY (1,1) NOT NULL,
	[Customer_Type] [varchar](100) NULL,
	[First_Name] [varchar](100) NULL,
	[Sur_Name] [varchar](100) NULL,
	[Middle_Name] [varchar](100) NULL,
	[Company_Name] [nchar](100) NULL,
	[Date_Of_Birth] [datetime] NULL,
	[Address] [varchar](500) NULL,
	[E_Mail] [varchar](100) NULL,
	[Mobile] [varchar](20) NULL,
	[Phone] [varchar](20) NULL,
	[Web_Site] [nchar](100) NULL,
	[Code] [varchar](100) NULL,
	[Title] [varchar](10) NULL,
	[Fax] [varchar](20) NULL,
	[Grade_Id] [int] NULL
) ON [PRIMARY]

CREATE TABLE [dbo].[CUSTOMER_GRADE](
	[GRADE_ID] [int] IDENTITY (1,1) NOT NULL,
	[GRADE_CODE] [varchar](20) NULL,
	[GRADE_NAME] [varchar](200) NULL,
	[DESCRIPTION] [varchar](500) NULL,
	[RATE] [NUMERIC ] NULL
) ON [PRIMARY]

CREATE TABLE [dbo].[CONTACT](
	[CONTACT_ID] DECIMAL(31,0) IDENTITY (1,1) NOT NULL,
	[COUNTRY] [varchar](100) NULL,
	[STATE] [varchar](100) NULL,
	[POST_CODE] [varchar](20) NULL,
	[ADDRESS1] [varchar](200) NULL,
	[ADDRESS2] [varchar](200) NULL,
	[E_MAIL] [varchar](100) NULL,
	[MOBILE] [varchar](20) NULL,
	[PHONE] [varchar](20) NULL,
	[FAX] [varchar](20) NULL,
	[WEB_SITE] [nchar](100) NULL
) ON [PRIMARY]

CREATE TABLE [dbo].[SUPPLIER](
	[SUPPLIER_ID] DECIMAL(31,0) IDENTITY (1,1) NOT NULL,
	[SUPPLIER_CODE] [varchar](50) NOT NULL,
	[SUPPLIER_NAME] [varchar](100) NOT NULL,
	[SUPPLIER_TYPE_ID] DECIMAL(31,0) NULL,
	[SUPPLIER_STATUS_ID] DECIMAL(31,0) NULL,
	[CONTACT_ID] DECIMAL(31,0) NULL,
	[LEAD_TIME] [int] NULL,
	[STOCK_COVER] [int] NULL,
	[MIN_ORDER_VALUE] [decimal](19,4) NULL,
	[CREDIT_LIMIT] [decimal](19,4) NULL,
	[MAX_ADV_ORDER] [int] NULL,
	[LAST_MODIFIED_DATE] [datetime] NULL,
	[CREATE_DATE] [datetime] NULL,
	[LAST_MODIFIED_BY] DECIMAL(31,0) NULL,
	[CONTACT_FIRST_NAME] [varchar](100) NULL,
	[CONTACT_SUR_NAME] [varchar](100) NULL,
	[CONTACT_TITLE] [varchar](20) NULL,
	[CONTACT_KNOWN_AS] [varchar](100) NULL
) ON [PRIMARY]

CREATE TABLE PROJECT(
    PROJECT_ID DECIMAL(31,0) IDENTITY (1,1) NOT NULL,
    PROJECT_NAME VARCHAR(500) NOT NULL,
    PROJECT_CODE VARCHAR(100) NOT NULL,
    PROJECT_DESCRIPTION VARCHAR(1000) ,
    LCOUNT DECIMAL(31,0) DEFAULT 0 NOT NULL
) ON [PRIMARY]
go

CREATE TABLE BILL_OF_QUANTITY(
    BOQ_ID DECIMAL(31,0) IDENTITY (1,1) NOT NULL,
    PROJECT_ID DECIMAL(31,0) NOT NULL,
    REFERENCE_CODE VARCHAR(100),
    DATE_CREATED datetime NOT NULL,
    LCOUNT DECIMAL(31,0) DEFAULT 0 NOT NULL
) ON [PRIMARY]
go

CREATE TABLE BOQ_DETAIL(
    BOQ_DETAIL_ID DECIMAL(31,0) IDENTITY (1,1) NOT NULL,
    BOQ_ID DECIMAL(31,0) NOT NULL,
    PRIB_ID DECIMAL(31,0),
    UNOM_ID DECIMAL(31,0) NOT NULL,
    PROU_ID DECIMAL(31,0) NOT NULL,
    QUANTITY DECIMAL(31,4) NOT NULL,
    COST DECIMAL(31,6) NOT NULL,
    MARGIN DECIMAL(31,6),
    SELL_PRICE DECIMAL(31,6),
    LCOUNT DECIMAL(31,0) DEFAULT 0 NOT NULL
) ON [PRIMARY]
go


CREATE TABLE LEGAL_TENDER(
    LEGT_ID DECIMAL(31,0) IDENTITY (1,1) NOT NULL,
    LEGT_CODE VARCHAR(10) NOT NULL,
    LEGT_NAME VARCHAR(60) NOT NULL,
    LEGT_URL_IMAGE VARCHAR(2048),
    LEGT_URL_THUMB VARCHAR(2048),
    LEGT_FROM_DATE datetime,
    LEGT_TO_DATE datetime,
    LEGT_NO_DECS DECIMAL(31,0) NOT NULL,
    LEGT_MASK VARCHAR(2048),
    LEGT_IS_EURO BIT DEFAULT 0 NOT NULL,
    LEGT_EURO_MEMBER DECIMAL(31,0) DEFAULT 0 NOT NULL,
    LEGT_SYMBOL VARCHAR(6),
    LEGT_NUMERIC_CODE VARCHAR(10),
    LCOUNT DECIMAL(31,0) DEFAULT 0 NOT NULL
) ON [PRIMARY]
go

CREATE TABLE PRICE_BAND(
    PRIB_ACTIVE BIT DEFAULT 1 NOT NULL,
    PRIB_ID DECIMAL(31,0) IDENTITY (1,1) NOT NULL,
    PRIB_CODE VARCHAR(10) NOT NULL,
    PRIB_NAME VARCHAR(60) NOT NULL,
    ORGU_ID DECIMAL(31,0) NOT NULL,
    LEGT_ID DECIMAL(31,0) NOT NULL,
    PRIB_TAX_INCLUDED BIT DEFAULT 0 NOT NULL,
    VALC_ID_CATEGORY DECIMAL(31,0),
    LCOUNT DECIMAL(31,0) DEFAULT 0 NOT NULL
)ON [PRIMARY]
go

CREATE TABLE COMPANY(
    COMP_ID DECIMAL(31,0) IDENTITY (1,1) NOT NULL,
    COMP_IS_TRADING BIT,
    COMP_OPEN_DATE datetime,
    COMP_CLOSE_DATE datetime,
    COMP_CHQ_PAYEE VARCHAR(60),
    COMP_STK_AUTH_VAL DECIMAL(31,6),
    COMP_TRADE_CUTOFF datetime,
    COMP_PWRD_VALIDITY DECIMAL(31,0),
    COMP_LOGOFF_PERIOD DECIMAL(31,0) DEFAULT 0 NOT NULL,
    COMP_FORCECRDSWIPE BIT DEFAULT 0 NOT NULL,
    COMP_SECURE_LOGOFF BIT DEFAULT 0 NOT NULL,
    COMP_PWD_RETRY DECIMAL(31,0) DEFAULT 3 NOT NULL,
    COMP_REF_DUP_RCPT BIT,
    COMP_RECON_ATTEMPT DECIMAL(31,0),
    COMP_REFUND_DAYS DECIMAL(31,0),
    COMP_REFUND_START datetime,
    COMP_REFUND_EXPIRY datetime,
    COMP_REFUND_DAYSGR DECIMAL(31,0),
    COMP_REFUND_POLICY VARCHAR(255),
    COMP_DISC_TAX_RULE BIT DEFAULT 0 NOT NULL,
    COMP_CVOUCH_DAYS DECIMAL(31,0),
    COMP_CVOUCH_POLICY VARCHAR(2048),
    COMP_GRACE_LIMIT DECIMAL(31,0) DEFAULT 0 NOT NULL,
    LCOUNT DECIMAL(31,0) DEFAULT 0 NOT NULL
) ON [PRIMARY]
go

CREATE TABLE ORG_TYPE(
    ORGT_TYPE_ID DECIMAL(31,0) IDENTITY (1,1) NOT NULL,
    ORGT_TYPE_NAME VARCHAR(60) NOT NULL,
    ORGT_TYPE_CODE VARCHAR(20) NOT NULL,
    LCOUNT DECIMAL(31,0) DEFAULT 0 NOT NULL
) ON [PRIMARY]
go


CREATE TABLE ORG_UNIT(
    ORGU_ID DECIMAL(31,0)IDENTITY (1,1) NOT NULL,
    ORGU_NAME VARCHAR(60) NOT NULL,
    ORGU_DESC VARCHAR(2048),
    ORGU_CODE VARCHAR(10) NOT NULL,
    ORGT_TYPE_ID DECIMAL(31,0) NOT NULL,
    ORGU_SHORT_NAME VARCHAR(15),
    PRIB_ID DECIMAL(31,0),
    WARH_ID DECIMAL(31,0),
    OUTL_ID DECIMAL(31,0),
    COMP_ID DECIMAL(31,0),
    ORGU_DEF_LEGT_ID DECIMAL(31,0),
    ORGU_CREATED datetime,
    ORGU_MODIFIED datetime,
    ORGU_NEXT_PO FLOAT,
    ORGU_AUDIT_LEVEL DECIMAL(31,0),
    ORGU_SOFTWARE VARCHAR(10),
    ORGU_VAT_REG_NO VARCHAR(100),
    AIRP_ID DECIMAL(31,0),
    PRIB_ID_COST DECIMAL(31,0),
    ORGU_WEEE_REG_NO VARCHAR(100),
    ORGU_DEF_LANG_ID DECIMAL(31,0),
    ORGU_TIME_ZONE VARCHAR(60),
    LCOUNT DECIMAL(31,0) DEFAULT 0 NOT NULL
) ON [PRIMARY]
go


--product group type
CREATE TABLE PRGP_TYPE(
    PRGT_ID DECIMAL(31,0) IDENTITY (1,1) NOT NULL,
    PRGT_CODE VARCHAR(10) NOT NULL,
    PRGT_NAME VARCHAR(60) NOT NULL,
    PRGT_TYPE_CONST DECIMAL(31,0),
    LCOUNT DECIMAL(31,0) DEFAULT 0 NOT NULL
) ON [PRIMARY]
go

--product group structure
CREATE TABLE PRODGP_STRUCTURE(
    PSTR_ID DECIMAL(31,0) IDENTITY (1,1) NOT NULL,
    PSTR_NAME VARCHAR(60) NOT NULL,
    PSTR_DESC VARCHAR(2048),
    PSTR_DATE_FROM datetime ,
    PSTR_DATE_TO datetime ,
    ORGU_ID DECIMAL(31,0) NOT NULL,
    PSTR_IS_LIVE BIT DEFAULT 0 NOT NULL,
    PSTR_MAX_LEVELS DECIMAL(31,0),
    PSTR_CONSTANT DECIMAL(31,0),
    LOCN_ID DECIMAL(31,0),
    LCOUNT DECIMAL(31,0) DEFAULT 0 NOT NULL
) ON [PRIMARY]
go
-- define hierarchy of product group structure
CREATE TABLE PRODGP_STRUCTURE_DETAIL(
    PGSD_ID DECIMAL(31,0) IDENTITY (1,1) NOT NULL,
    PGSD_LEVEL_NAME VARCHAR(60) NOT NULL,
    PGSD_LEVEL_DESC VARCHAR(2048),
    PGSD_ID_PARENT DECIMAL(31,0),
    PGSD_LEVEL DECIMAL(31,0) NOT NULL,
    PGSD_MANDATORY BIT DEFAULT 1 NOT NULL,
    PSTR_ID DECIMAL(31,0) NOT NULL,
    PRGT_ID DECIMAL(31,0) NOT NULL,
    LCOUNT DECIMAL(31,0) DEFAULT 0 NOT NULL
) ON [PRIMARY]
go

CREATE TABLE PRODUCT_GROUP(
    PRGP_ID DECIMAL(31,0) IDENTITY(1,1) NOT NULL,
    ORGU_ID DECIMAL(31,0) NOT NULL,
    PRGP_CODE VARCHAR(60) NOT NULL,
    PRGT_ID DECIMAL(31,0) NOT NULL,
    PRGP_DESC VARCHAR(2048),
    PRGP_NAME VARCHAR(60) NOT NULL,
    PRGP_CREATED datetime,
    PRGP_MODIFIED datetime,
    PRGP_CAN_BE_MERCH BIT DEFAULT 0 NOT NULL,
    PRGP_MAIN_PRGP_ID DECIMAL(31,0),
    PRGP_TRADE_START datetime,
    PRGP_TRADE_END datetime,
    LCOUNT DECIMAL(31,0) DEFAULT 0 NOT NULL
) ON [PRIMARY]
go


CREATE TABLE PRODGP_STR_LINK(
    PGSL_ID DECIMAL(31,0)IDENTITY(1,1) NOT NULL,
    PGSL_CAN_BE_RANGED BIT DEFAULT 1 NOT NULL,
    PRGP_ID DECIMAL(31,0) NOT NULL,
    PSTR_ID DECIMAL(31,0) NOT NULL,
    PRGP_PARENT_ID DECIMAL(31,0),
    PRGP_CHILD_ID DECIMAL(31,0),
    PGSL_DISPLAY_SEQ DECIMAL(31,0),
    PGSD_ID DECIMAL(31,0),
    LCOUNT DECIMAL(31,0) DEFAULT 0 NOT NULL
) ON [PRIMARY]
go

CREATE TABLE TAX_RULE_NAME(
    TXRN_ID DECIMAL(31,0) IDENTITY(1,1) NOT NULL,
    TXRN_NAME VARCHAR(60) NOT NULL,
    TXRN_CODE VARCHAR(20) NOT NULL,
    LCOUNT DECIMAL(31,0) DEFAULT 0 NOT NULL
) ON [PRIMARY]
go

CREATE TABLE TAX_RULE(
    TXRL_ID DECIMAL(31,0) IDENTITY(1,1) NOT NULL,
    TXRL_DATE_FROM datetime NOT NULL,
    TXRL_DATE_TO datetime NOT NULL,
    TXRL_APPLY_SEQ DECIMAL(31,0) NOT NULL,
    TXRL_CUM_TAX BIT DEFAULT 0 NOT NULL,
    TXRL_UPPER_VAL DECIMAL(31,6),
    TXRN_ID DECIMAL(31,0) NOT NULL,
    TXRL_CODE VARCHAR(10) NOT NULL,
    TXRL_DESC VARCHAR(30),
    TXRL_CALC_MTHD DECIMAL(31,0) DEFAULT 1 NOT NULL,
    TXRL_LEGAL_DESC VARCHAR(2048),
    TXRL_ROUNDING_MTHD DECIMAL(31,0) DEFAULT 1,
    TXRL_STEPPED BIT DEFAULT 0 NOT NULL,
    TXRL_ACC_CODE VARCHAR(60),
    LCOUNT DECIMAL(31,0) DEFAULT 0 NOT NULL
) ON [PRIMARY]
go

CREATE TABLE TAX_LEG_VARIANCE(
    TXLV_ID DECIMAL(31,0) IDENTITY(1,1) NOT NULL,
    TXRL_ID DECIMAL(31,0) NOT NULL,
    TXLV_RATE DECIMAL(31,6),
    TXLV_DATETIME_FROM datetime NOT NULL,
    TXLV_DATETIME_TO datetime NOT NULL,
    TXLV_CODE VARCHAR(10),
    TXLV_DESC VARCHAR(30),
    TXLV_AMOUNT DECIMAL(31,6),
    TXLV_IS_ACTIVE BIT DEFAULT 0,
    LCOUNT DECIMAL(31,0) DEFAULT 0 NOT NULL
) ON [PRIMARY]
go

CREATE TABLE UNIT_OF_MEASURE(
    UNOM_ID DECIMAL(31,0) IDENTITY(1,1) NOT NULL,
    UNOM_CODE VARCHAR(10) NOT NULL,
    UNOM_DESC VARCHAR(2048) NOT NULL,
    UNOM_FACTOR DECIMAL(31,0) NOT NULL,
    UNOM_CREATED datetime NOT NULL,
    UNOM_MODIFIED datetime NOT NULL,
    UNOM_CONSTANT DECIMAL(31,0),
    UNOM_ID_PARENT DECIMAL(31,0),
    LCOUNT DECIMAL(31,0) DEFAULT 0 NOT NULL
) ON [PRIMARY]
go

CREATE TABLE PRODUCT(
    PROD_ID DECIMAL(31,0) IDENTITY(1,1) NOT NULL,
    PROD_SKU VARCHAR(60) NOT NULL,
    REFERENCE VARCHAR(60) NOT NULL,
    ORGU_ID_OWNING DECIMAL(31,0) NOT NULL,
    PROD_NAME VARCHAR(60) NOT NULL,
    CAT_ID_TYPE DECIMAL(31,0) NOT NULL,
    PROD_DESC VARCHAR(2048),
    PROD_OWN_BRAND BIT DEFAULT 0 NOT NULL,
    PROD_URL VARCHAR(2048),
    PROD_CREATED datetime,
    PROD_MODIFIED datetime,
    PROD_RECEIPT_DESC VARCHAR(30),
    PROD_WARRANTY_TERM DECIMAL(31,0),
    PROD_BRAND VARCHAR(60),
    PROD_IMAGE_DESC VARCHAR(30),
    PROD_URL_THUMB VARCHAR(2048),
    PROD_CLASS VARCHAR(30),
    PROD_WARRANTY_TEXT VARCHAR(255),
    LCOUNT DECIMAL(31,0) DEFAULT 0 NOT NULL
) ON [PRIMARY]
go

CREATE TABLE PROD_ORGU_LINK(
    PROU_ID DECIMAL(31,0) IDENTITY(1,1) NOT NULL,
    PROD_ID DECIMAL(31,0) NOT NULL,
    ORGU_ID DECIMAL(31,0) NOT NULL,
    PROU_REFUNDABLE DECIMAL(31,0) DEFAULT 0 NOT NULL,
    PROU_DISCOUNTABLE DECIMAL(31,0) DEFAULT 0 NOT NULL,
    PROU_CAN_QTY_SALE BIT DEFAULT 1 NOT NULL,
    PROU_FORCE_QTY DECIMAL(31,0) DEFAULT 0 NOT NULL,
    CAT_ID_STATUS DECIMAL(31,0) NOT NULL,
    PROU_REFUND_DAYS DECIMAL(31,0),
    LCOUNT DECIMAL(31,0) DEFAULT 0 NOT NULL
) ON [PRIMARY]
go

CREATE TABLE PROD_PGRP_LINK(
    PPGL_ID DECIMAL(31,0)IDENTITY(1,1) NOT NULL,
    PROD_ID DECIMAL(31,0) NOT NULL,
    PRGP_ID DECIMAL(31,0) NOT NULL,
    PPGL_DISPLAY_SEQ DECIMAL(31,0),
    PPGL_REL_CONST DECIMAL(31,0) DEFAULT 0 NOT NULL,
    PPGL_PRIORITY DECIMAL(31,0) DEFAULT 0 NOT NULL,
    LCOUNT DECIMAL(31,0) DEFAULT 0 NOT NULL
) ON [PRIMARY]
go

CREATE TABLE SUPP_ORGU_LINK(
    SOL_ID DECIMAL(31,0) IDENTITY(1,1) NOT NULL,
    SUPP_ID DECIMAL(31,0) NOT NULL,
    ORGU_ID DECIMAL(31,0) NOT NULL,
    CAT_ID_STATUS DECIMAL(31,0),
    SOL_CRED_LIMIT DECIMAL(31,6),
    SOL_MIN_ORD_VAL DECIMAL(31,6),
    SOL_LEAD_TIME DECIMAL(31,0),
    SOL_ORDERING_CODE VARCHAR(10),
    SOL_MIN_PO_QTY DECIMAL(31,4),
    SOL_PAYMENT_DAYS DECIMAL(31,0),
    SOL_ACC_CODE VARCHAR(60),
    LCOUNT DECIMAL(31,0) DEFAULT 0 NOT NULL
) ON [PRIMARY]
go

CREATE TABLE SUPP_PROD_PRICE(
    SPRC_ID DECIMAL(31,0) IDENTITY(1,1) NOT NULL,
    SOL_ID DECIMAL(31,0) NOT NULL,
    PROD_ID DECIMAL(31,0) NOT NULL,
    CATALOGUE_NO VARCHAR(60),
    PART_NO VARCHAR(60),
    UNOM_ID DECIMAL(31,0) NOT NULL,
    UNOM_QTY DECIMAL(31,4) NOT NULL,
    PRICE DECIMAL(31,6),
    BULK_PRICE DECIMAL(31,6),
    BULK_QTY DECIMAL(31,4),
    LEGT_ID DECIMAL(31,0),
    SPRC_MIN_ORD_QTY DECIMAL(31,4),
    SPRC_LEAD_TIME DECIMAL(31,0),
    SPRC_CREATED DATETIME NOT NULL,
    SPRC_MODIFIED DATETIME NOT NULL,
    SPRC_PREFER_BUY BIT DEFAULT 0 NOT NULL,
    SPRC_MIN_ORD_VAL DECIMAL(31,6),
    LCOUNT DECIMAL(31,0) DEFAULT 0 NOT NULL
) ON [PRIMARY]
go

CREATE TABLE PRODUCT_METRIC(
    PRDM_ID DECIMAL(31,0) IDENTITY(1,1) NOT NULL,
    PROD_ID DECIMAL(31,0) NOT NULL,
    PRDM_DRY_WEIGHT DECIMAL(31,4),
    UNOM_ID_DRY_WEIGHT DECIMAL(31,0),
    PRDM_WEIGHT DECIMAL(31,4),
    UNOM_ID_WEIGHT DECIMAL(31,0),
    PRDM_VOLUME DECIMAL(31,4),
    UNOM_ID_VOLUME DECIMAL(31,0),
    PRDM_QUANTITY DECIMAL(31,4),
    PRDM_PER_UNIT DECIMAL(31,4),
    UNOM_ID_PER_UNIT DECIMAL(31,0),
    PRDM_CASE_SIZE DECIMAL(31,0),
    PRDM_NUM_LABELS DECIMAL(31,0),
    PRDM_BARCODE_LOCAL DECIMAL(31,0) DEFAULT 0 NOT NULL,
    PRDM_LENGTH DECIMAL(31,4),
    UNOM_ID_LENGTH DECIMAL(31,0),
    PRDM_DIM_HEIGHT DECIMAL(31,4),
    PRDM_DIM_WIDTH DECIMAL(31,4),
    PRDM_DIM_LENGTH DECIMAL(31,4),
    UNOM_ID_DIM DECIMAL(31,0),
    PRDM_USAGE DECIMAL(31,4),
    UNOM_ID_USAGE DECIMAL(31,0),
    UNOM_ID_UNIT DECIMAL(31,0),
    LCOUNT DECIMAL(31,0) DEFAULT 0
) ON [PRIMARY]
go

CREATE TABLE PRICE_CODE(
    PRCC_ID DECIMAL(31,0) IDENTITY(1,1) NOT NULL,
    PRCC_NAME VARCHAR(60) NOT NULL,
    PRCC_CODE VARCHAR(10) NOT NULL,
    PRCC_CREATED datetime NOT NULL,
    LCOUNT DECIMAL(31,0) DEFAULT 0 NOT NULL
) ON [PRIMARY]
go

CREATE TABLE PRICE(
    PRCE_ID DECIMAL(31,0) IDENTITY(1,1) NOT NULL,
    PROD_ID DECIMAL(31,0) NOT NULL,
    PRIB_ID DECIMAL(31,0) NOT NULL,
    PRCC_ID DECIMAL(31,0) NOT NULL,
    MARGIN DECIMAL(31,6),
    PRCE_FROM_DATE datetime NOT NULL,
    PRCE_TO_DATE datetime NOT NULL,
    PRCE_PRICE DECIMAL(31,6) NOT NULL,
    PRCE_CREATED datetime NOT NULL,
    PRCE_MODIFIED datetime,
    PRCE_TAX_INCLUDED BIT DEFAULT 0 NOT NULL,
    UNOM_ID DECIMAL(31,0),
    UNOM_QTY DECIMAL(31,4) NOT NULL,
    PRCE_SET_CENTRAL BIT DEFAULT 1 NOT NULL,
    PRCE_REDUCED BIT DEFAULT 0 NOT NULL,
    LCOUNT DECIMAL(31,0) DEFAULT 0 NOT NULL
) ON [PRIMARY]
go

CREATE TABLE PROD_TYPE(
    PRTY_ID DECIMAL(31,0) IDENTITY(1,1) NOT NULL,
    PRTY_NAME VARCHAR(60) NOT NULL,
    PRTY_CONST DECIMAL(31,0) NOT NULL,
    PRTY_PRODUCT_TYPE DECIMAL(31,0) NOT NULL,
    LCOUNT DECIMAL(31,0) DEFAULT 0 NOT NULL
) ON [PRIMARY]
go


CREATE TABLE PROD_TO_PROD_LINK(
    PRTY_ID DECIMAL(31,0) NOT NULL,
    PROD_ID_FROM DECIMAL(31,0) NOT NULL,
    PROD_ID_TO DECIMAL(31,0) NOT NULL,
    PTPL_COMPONENT_QTY DECIMAL(31,4) DEFAULT 1,
    PTPL_KIT_ONLY BIT DEFAULT 0,
    PTPL_COMP_MERCH BIT DEFAULT 0 NOT NULL,
    PTPL_SEQUENCE_NO DECIMAL(31,0) NOT NULL,
    UNOM_ID DECIMAL(31,0) NOT NULL,
    PTPL_DESC VARCHAR(2048),
    LCOUNT DECIMAL(31,0) DEFAULT 0 NOT NULL
) ON [PRIMARY]
go

CREATE TABLE PROU_TXRL_LINK(
    PROU_ID DECIMAL(31,0) NOT NULL,
    TXRL_ID DECIMAL(31,0) NOT NULL,
    LCOUNT DECIMAL(31,0) DEFAULT 0
) ON [PRIMARY]
go

ALTER TABLE CUSTOMER ADD CONSTRAINT CUSTOMER_PK PRIMARY KEY(CUSTOMER_ID)
go

ALTER TABLE CUSTOMER_GRADE ADD CONSTRAINT GRADE_PK PRIMARY KEY(GRADE_ID)
go

ALTER TABLE SUPPLIER ADD CONSTRAINT SUPPLIER_PK PRIMARY KEY(SUPPLIER_ID)
go

ALTER TABLE CONFIG_TYPE ADD CONSTRAINT CONFIG_TYPE_PK PRIMARY KEY(CONFIG_TYPE_ID)
go

ALTER TABLE CONFIG_CATEGORY ADD CONSTRAINT CATEGORY_PK PRIMARY KEY(CATEGORY_ID)
go

ALTER TABLE CONTACT ADD CONSTRAINT CONTACT_PK PRIMARY KEY(CONTACT_ID)
go

ALTER TABLE LEGAL_TENDER ADD CONSTRAINT LEGAL_TENDER_PK PRIMARY KEY(LEGT_ID)
go

ALTER TABLE PRICE_BAND ADD CONSTRAINT PRIB_PK PRIMARY KEY (PRIB_ID)
go

ALTER TABLE COMPANY ADD CONSTRAINT COMPANY_PK PRIMARY KEY(COMP_ID)
go

ALTER TABLE ORG_UNIT ADD CONSTRAINT ORG_UNIT_PK PRIMARY KEY(ORGU_ID)
go

ALTER TABLE PRGP_TYPE ADD CONSTRAINT PRGP_TYPE_PK PRIMARY KEY(PRGT_ID)
go

ALTER TABLE PRODGP_STRUCTURE ADD CONSTRAINT PRODGP_STRUCTURE_PK PRIMARY KEY(PSTR_ID)
go

ALTER TABLE PRODGP_STRUCTURE_DETAIL ADD CONSTRAINT PRODGP_STRUCTURE_DETAIL_PK PRIMARY KEY(PGSD_ID)
go

ALTER TABLE PRODUCT_GROUP ADD CONSTRAINT PRODUCT_GROUP_PK PRIMARY KEY(PRGP_ID)
go

ALTER TABLE ORG_TYPE ADD CONSTRAINT ORG_TYPE_PK PRIMARY KEY(ORGT_TYPE_ID)
go

ALTER TABLE PRODGP_STR_LINK ADD CONSTRAINT PRODGP_STR_LINK_PK PRIMARY KEY(PGSL_ID)
go

ALTER TABLE TAX_RULE_NAME ADD CONSTRAINT TXRN_PK PRIMARY KEY (TXRN_ID)
go

ALTER TABLE TAX_RULE ADD CONSTRAINT TXRL_PK PRIMARY KEY (TXRL_ID)
go

ALTER TABLE TAX_LEG_VARIANCE ADD CONSTRAINT TXLV_PK PRIMARY KEY (TXLV_ID)
go

ALTER TABLE UNIT_OF_MEASURE ADD CONSTRAINT UNOM_PK PRIMARY KEY (UNOM_ID)
go

ALTER TABLE PRODUCT ADD CONSTRAINT PROD_PK PRIMARY KEY (PROD_ID)
go

ALTER TABLE PROD_ORGU_LINK ADD CONSTRAINT PROU_PK PRIMARY KEY (PROU_ID)
go

ALTER TABLE PROD_PGRP_LINK
 ADD  CONSTRAINT PPGL_PK PRIMARY KEY(PPGL_ID);

ALTER TABLE SUPP_ORGU_LINK ADD CONSTRAINT SOL_PK PRIMARY KEY (SOL_ID)
go

ALTER TABLE SUPP_PROD_PRICE ADD CONSTRAINT SPRC_PK PRIMARY KEY (SPRC_ID)
go


ALTER TABLE PRODUCT_METRIC ADD CONSTRAINT PRDM_PK PRIMARY KEY (PRDM_ID)
go

ALTER TABLE PRICE_CODE ADD CONSTRAINT PRCC_PK PRIMARY KEY (PRCC_ID)
go

ALTER TABLE PRICE ADD CONSTRAINT PRCE_PK PRIMARY KEY (PRCE_ID)
go

ALTER TABLE PROD_TYPE ADD CONSTRAINT PRLT_PK PRIMARY KEY (PRTY_ID)
go

ALTER TABLE PROU_TXRL_LINK
 ADD  CONSTRAINT POTL_PK PRIMARY KEY(PROU_ID,TXRL_ID);
GO


ALTER TABLE PROJECT
 ADD  CONSTRAINT PROJECT_PK PRIMARY KEY(PROJECT_ID);
GO

ALTER TABLE BILL_OF_QUANTITY
 ADD  CONSTRAINT BILL_OF_QUANTITY_PK PRIMARY KEY(BOQ_ID);
GO

ALTER TABLE BOQ_DETAIL
 ADD  CONSTRAINT BOQ_DETAIL_PK PRIMARY KEY(BOQ_DETAIL_ID);
GO


ALTER TABLE CUSTOMER ADD CONSTRAINT CUSTOMER_2GRADE_FK FOREIGN KEY (GRADE_ID) REFERENCES CUSTOMER_GRADE(GRADE_ID)
go

ALTER TABLE CONFIG_CATEGORY ADD CONSTRAINT CATEGORY_2TYPE_FK FOREIGN KEY (CONFIG_TYPE_ID) REFERENCES CONFIG_TYPE(CONFIG_TYPE_ID)
go



ALTER TABLE SUPPLIER ADD CONSTRAINT SUPPLIER_TYEP_ID_2CATEGORY_FK FOREIGN KEY (SUPPLIER_TYPE_ID) REFERENCES CONFIG_CATEGORY(CATEGORY_ID)
go

ALTER TABLE SUPPLIER ADD CONSTRAINT SUPPLIER_STATUS_ID_2CATEGORY_FK FOREIGN KEY (SUPPLIER_STATUS_ID) REFERENCES CONFIG_CATEGORY(CATEGORY_ID)
go

ALTER TABLE SUPPLIER ADD CONSTRAINT SUPPLIER_CONTACT_ID_2CONTACT_FK FOREIGN KEY (CONTACT_ID) REFERENCES CONTACT(CONTACT_ID)
go


ALTER TABLE LEGAL_TENDER ADD CONSTRAINT LEGT_UK1 UNIQUE(LEGT_CODE)
go


ALTER TABLE PRICE_BAND ADD CONSTRAINT PRIB_UK1 UNIQUE(PRIB_CODE)
go

ALTER TABLE PRICE_BAND ADD CONSTRAINT PRIB_FK1 FOREIGN KEY (ORGU_ID) REFERENCES ORG_UNIT(ORGU_ID)
go

ALTER TABLE PRICE_BAND ADD CONSTRAINT PRIB_FK3 FOREIGN KEY (LEGT_ID) REFERENCES LEGAL_TENDER(LEGT_ID)
go


ALTER TABLE ORG_TYPE ADD CONSTRAINT ORGT_UK1 UNIQUE(ORGT_TYPE_NAME)
go



ALTER TABLE PRODUCT_GROUP ADD CONSTRAINT PRODUCT_GROUP_ORGU_ID_2ORG_UNIT_FK FOREIGN KEY (ORGU_ID) REFERENCES ORG_UNIT(ORGU_ID)
go

ALTER TABLE PRODUCT_GROUP ADD CONSTRAINT PRODUCT_GROUP_PRGT_ID_2PRGP_TYPE_FK FOREIGN KEY (PRGT_ID) REFERENCES PRGP_TYPE(PRGT_ID)
go

ALTER TABLE PRODUCT_GROUP ADD CONSTRAINT PRGP_FK4 FOREIGN KEY (PRGP_MAIN_PRGP_ID) REFERENCES PRODUCT_GROUP(PRGP_ID)
go

ALTER TABLE PRODUCT_GROUP ADD CONSTRAINT PRGP_UK1 UNIQUE(PRGP_CODE,
   ORGU_ID,
   PRGT_ID)
go


ALTER TABLE ORG_UNIT ADD CONSTRAINT ORG_UNIT_COMP_ID_2COMPANY_FK FOREIGN KEY (COMP_ID) REFERENCES COMPANY(COMP_ID)
go

ALTER TABLE ORG_UNIT ADD CONSTRAINT ORG_UNIT_ORGT_TYPE_ID_2ORG_TYPE_FK FOREIGN KEY (ORGT_TYPE_ID) REFERENCES ORG_TYPE(ORGT_TYPE_ID)
go

ALTER TABLE ORG_UNIT ADD CONSTRAINT ORGU_FK8 FOREIGN KEY (PRIB_ID_COST) REFERENCES PRICE_BAND(PRIB_ID)
go

ALTER TABLE ORG_UNIT ADD CONSTRAINT ORGU_FK5 FOREIGN KEY (PRIB_ID) REFERENCES PRICE_BAND(PRIB_ID)
go

ALTER TABLE ORG_UNIT ADD CONSTRAINT ORGU_FK2 FOREIGN KEY (ORGU_DEF_LEGT_ID) REFERENCES LEGAL_TENDER(LEGT_ID)
go

ALTER TABLE PRODGP_STRUCTURE ADD CONSTRAINT PRODGP_STRUCTURE_ORGU_ID_2ORG_UNIT_FK FOREIGN KEY (ORGU_ID) REFERENCES ORG_UNIT(ORGU_ID)
go

ALTER TABLE PRODGP_STRUCTURE_DETAIL ADD CONSTRAINT PRODGP_STRUCTURE_DETAIL_PSTR_ID_2PRODGP_STRUCTURE_FK FOREIGN KEY (PSTR_ID) REFERENCES PRODGP_STRUCTURE(PSTR_ID)
go

ALTER TABLE PRODGP_STRUCTURE_DETAIL ADD CONSTRAINT PRODGP_STRUCTURE_DETAIL_PRGT_ID_2PRGP_TYPE_FK FOREIGN KEY (PRGT_ID) REFERENCES PRGP_TYPE(PRGT_ID)
go

ALTER TABLE PRODGP_STR_LINK ADD CONSTRAINT PRODGP_STR_LINK_PRGP_ID_2PRODUCT_GROUP_FK FOREIGN KEY (PRGP_ID) REFERENCES PRODUCT_GROUP(PRGP_ID)
go

ALTER TABLE PRODGP_STR_LINK ADD CONSTRAINT PRODGP_STR_LINK_PSTR_ID_2PRODGP_STRUCTURE_FK FOREIGN KEY (PSTR_ID) REFERENCES PRODGP_STRUCTURE(PSTR_ID)
go

ALTER TABLE PRODGP_STR_LINK ADD CONSTRAINT PRODGP_STR_LINK_PRGP_PARENT_ID_2PRODUCT_GROUP_FK FOREIGN KEY (PRGP_PARENT_ID) REFERENCES PRODUCT_GROUP(PRGP_ID)
go

ALTER TABLE PRODGP_STR_LINK ADD CONSTRAINT PRODGP_STR_LINK_PRGP_CHILD_ID_2PRODUCT_GROUP_FK FOREIGN KEY (PRGP_CHILD_ID) REFERENCES PRODUCT_GROUP(PRGP_ID)
go

ALTER TABLE PRODGP_STR_LINK ADD CONSTRAINT PRODGP_STR_LINK_PGSD_ID_2PRODGP_STRUCTURE_DETAIL_FK FOREIGN KEY (PGSD_ID) REFERENCES PRODGP_STRUCTURE_DETAIL(PGSD_ID)
go

ALTER TABLE TAX_RULE_NAME ADD CONSTRAINT TXRN_UK1 UNIQUE(TXRN_NAME)
go

ALTER TABLE TAX_RULE ADD CONSTRAINT TXRL_FK1 FOREIGN KEY (TXRN_ID) REFERENCES TAX_RULE_NAME(TXRN_ID)
go

ALTER TABLE TAX_LEG_VARIANCE ADD CONSTRAINT TXLV_FK1 FOREIGN KEY (TXRL_ID) REFERENCES TAX_RULE(TXRL_ID)
go

ALTER TABLE UNIT_OF_MEASURE ADD CONSTRAINT UNOM_UK1 UNIQUE(UNOM_CODE)
go

ALTER TABLE UNIT_OF_MEASURE ADD CONSTRAINT UNOM_FK1 FOREIGN KEY (UNOM_ID_PARENT) REFERENCES UNIT_OF_MEASURE(UNOM_ID)
go


ALTER TABLE PRODUCT ADD CONSTRAINT PROD_UK1 UNIQUE(PROD_SKU,
   ORGU_ID_OWNING)
go

ALTER TABLE PRODUCT ADD CONSTRAINT PROD_FK6 FOREIGN KEY (ORGU_ID_OWNING) REFERENCES ORG_UNIT(ORGU_ID)
go

ALTER TABLE PRODUCT ADD CONSTRAINT PRODUCT_FK1 FOREIGN KEY (CAT_ID_TYPE) REFERENCES CONFIG_CATEGORY(CATEGORY_ID)
go



ALTER TABLE PROD_ORGU_LINK ADD CONSTRAINT PROU_FK3 FOREIGN KEY (CAT_ID_STATUS) REFERENCES CONFIG_CATEGORY(CATEGORY_ID)
go

ALTER TABLE PROD_ORGU_LINK ADD CONSTRAINT PROU_FK1 FOREIGN KEY (PROD_ID) REFERENCES PRODUCT(PROD_ID)
go

ALTER TABLE PROD_ORGU_LINK ADD CONSTRAINT PROU_FK2 FOREIGN KEY (ORGU_ID) REFERENCES ORG_UNIT(ORGU_ID)
go

ALTER TABLE PROD_PGRP_LINK
 ADD  CONSTRAINT PPGL_FK2 FOREIGN KEY(PRGP_ID) REFERENCES PRODUCT_GROUP(PRGP_ID);

ALTER TABLE PROD_PGRP_LINK
 ADD  CONSTRAINT PPGL_FK1 FOREIGN KEY(PROD_ID) REFERENCES PRODUCT(PROD_ID);

ALTER TABLE SUPP_ORGU_LINK ADD CONSTRAINT SOL_UK1 UNIQUE(SUPP_ID,
   ORGU_ID)
go

ALTER TABLE SUPP_ORGU_LINK ADD CONSTRAINT SOL_FK1 FOREIGN KEY (SUPP_ID) REFERENCES SUPPLIER(SUPPLIER_ID)
go

ALTER TABLE SUPP_ORGU_LINK ADD CONSTRAINT SOL_FK2 FOREIGN KEY (ORGU_ID) REFERENCES ORG_UNIT(ORGU_ID)
go

ALTER TABLE SUPP_ORGU_LINK ADD CONSTRAINT SOL_FK3 FOREIGN KEY (CAT_ID_STATUS) REFERENCES CONFIG_CATEGORY(CATEGORY_ID)
go


ALTER TABLE SUPP_PROD_PRICE ADD CONSTRAINT SPRC_UK1 UNIQUE(SOL_ID,
   PROD_ID,
   UNOM_ID,
   UNOM_QTY)
go

ALTER TABLE SUPP_PROD_PRICE ADD CONSTRAINT SPRC_FK5 FOREIGN KEY (LEGT_ID) REFERENCES LEGAL_TENDER(LEGT_ID)
go

ALTER TABLE SUPP_PROD_PRICE ADD CONSTRAINT SPRC_FK2 FOREIGN KEY (SOL_ID) REFERENCES SUPP_ORGU_LINK(SOL_ID)
go

ALTER TABLE SUPP_PROD_PRICE ADD CONSTRAINT SPRC_FK3 FOREIGN KEY (UNOM_ID) REFERENCES UNIT_OF_MEASURE(UNOM_ID)
go

ALTER TABLE SUPP_PROD_PRICE ADD CONSTRAINT SPRC_FK1 FOREIGN KEY (PROD_ID) REFERENCES PRODUCT(PROD_ID)
go

ALTER TABLE PRODUCT_METRIC ADD CONSTRAINT PRDM_FK2 FOREIGN KEY (PROD_ID) REFERENCES PRODUCT(PROD_ID)
go

ALTER TABLE PRICE_CODE ADD CONSTRAINT PRCC_UK1 UNIQUE(PRCC_CODE)
go

ALTER TABLE PRICE ADD CONSTRAINT PRCE_FK2 FOREIGN KEY (PRCC_ID) REFERENCES PRICE_CODE(PRCC_ID)
go

ALTER TABLE PRICE ADD CONSTRAINT PRCE_FK3 FOREIGN KEY (PROD_ID) REFERENCES PRODUCT(PROD_ID)
go

ALTER TABLE PRICE ADD CONSTRAINT PRCE_FK1 FOREIGN KEY (PRIB_ID) REFERENCES PRICE_BAND(PRIB_ID)
go

ALTER TABLE PRICE ADD CONSTRAINT PRCE_FK4 FOREIGN KEY (UNOM_ID) REFERENCES unit_of_measure(UNOM_ID)
go

ALTER TABLE PROD_TO_PROD_LINK ADD CONSTRAINT PTPL_PK PRIMARY KEY (PROD_ID_FROM,
  PRTY_ID,
  PROD_ID_TO)
go

ALTER TABLE PROD_TO_PROD_LINK ADD CONSTRAINT PTPL_FK4 FOREIGN KEY (UNOM_ID) REFERENCES UNIT_OF_MEASURE(UNOM_ID)
go

ALTER TABLE PROD_TO_PROD_LINK ADD CONSTRAINT PTPL_FK1 FOREIGN KEY (PROD_ID_TO) REFERENCES PRODUCT(PROD_ID)
go

ALTER TABLE PROD_TO_PROD_LINK ADD CONSTRAINT PTPL_FK2 FOREIGN KEY (PROD_ID_FROM) REFERENCES PRODUCT(PROD_ID)
go

ALTER TABLE PROD_TO_PROD_LINK ADD CONSTRAINT PTPL_FK3 FOREIGN KEY (PRTY_ID) REFERENCES PROD_TYPE(PRTY_ID)
go

ALTER TABLE PROU_TXRL_LINK
 ADD  CONSTRAINT POTL_FK1 FOREIGN KEY(PROU_ID) REFERENCES PROD_ORGU_LINK(PROU_ID);
GO

ALTER TABLE PROU_TXRL_LINK
 ADD  CONSTRAINT POTL_FK2 FOREIGN KEY(TXRL_ID) REFERENCES TAX_RULE(TXRL_ID);
GO

ALTER TABLE BILL_OF_QUANTITY
 ADD  CONSTRAINT BILL_OF_QUANTITY_FK1 FOREIGN KEY(PROJECT_ID) REFERENCES PROJECT(PROJECT_ID);
GO

ALTER TABLE BOQ_DETAIL
 ADD  CONSTRAINT BOQ_DETAIL_FK1 FOREIGN KEY(BOQ_ID) REFERENCES BILL_OF_QUANTITY(BOQ_ID);
GO

ALTER TABLE BOQ_DETAIL
 ADD  CONSTRAINT BOQ_DETAIL_FK2 FOREIGN KEY(UNOM_ID) REFERENCES UNIT_OF_MEASURE(UNOM_ID);
GO

ALTER TABLE BOQ_DETAIL
 ADD  CONSTRAINT BOQ_DETAIL_FK3 FOREIGN KEY(PROU_ID) REFERENCES PROD_ORGU_LINK(PROU_ID);
GO

--INDEXES
CREATE INDEX SUPP_IX1 ON SUPPLIER
(SUPPLIER_CODE)
go

CREATE INDEX SUPP_IX2 ON SUPPLIER
(SUPPLIER_CODE)
go


CREATE INDEX LEGT_IX1 ON LEGAL_TENDER
(LEGT_FROM_DATE)
go

CREATE INDEX LEGT_IX2 ON LEGAL_TENDER
(LEGT_TO_DATE)
go

CREATE INDEX PRIB_IX1 ON PRICE_BAND
(ORGU_ID)
go

CREATE INDEX PRIB_IX3 ON PRICE_BAND
(LEGT_ID)
go

CREATE INDEX ORGU_IX3 ON ORG_UNIT
(COMP_ID)
go

CREATE INDEX ORGU_IX8 ON ORG_UNIT
(ORGU_CODE)
go

CREATE INDEX ORGU_IX7 ON ORG_UNIT
(PRIB_ID)
go

CREATE INDEX ORGU_IX5 ON ORG_UNIT
(ORGT_TYPE_ID)
go

CREATE INDEX ORGU_IX2 ON ORG_UNIT
(WARH_ID)
go

CREATE INDEX ORGU_IX9 ON ORG_UNIT
(PRIB_ID_COST)
go

CREATE INDEX ORGU_IX6 ON ORG_UNIT
(ORGU_DEF_LEGT_ID)
go

CREATE INDEX ORGUIX10 ON ORG_UNIT
(ORGU_DEF_LANG_ID)
go

CREATE INDEX PSTR_IX1 ON PRODGP_STRUCTURE
(ORGU_ID)
go

CREATE INDEX PSTR_IX5 ON PRODGP_STRUCTURE
(PSTR_IS_LIVE)
go

CREATE INDEX PSTR_IX3 ON PRODGP_STRUCTURE
(PSTR_DATE_FROM)
go

CREATE INDEX PSTR_IX6 ON PRODGP_STRUCTURE
(LOCN_ID)
go

CREATE INDEX PSTR_IX4 ON PRODGP_STRUCTURE
(PSTR_DATE_TO)
go

CREATE INDEX PSTR_IX2 ON PRODGP_STRUCTURE
(PSTR_CONSTANT)
go

CREATE INDEX PGPT_IX5 ON PRODGP_STRUCTURE_DETAIL
(PRGT_ID)
go

CREATE INDEX PGPT_IX4 ON PRODGP_STRUCTURE_DETAIL
(PSTR_ID)
go

CREATE INDEX PGPT_IX3 ON PRODGP_STRUCTURE_DETAIL
(PGSD_ID_PARENT)
go

CREATE INDEX PRGP_IX4 ON PRODUCT_GROUP
(PRGP_MAIN_PRGP_ID)
go

CREATE INDEX PGSL_IX4 ON PRODGP_STR_LINK
(PRGP_CHILD_ID)
go

CREATE INDEX PGSL_IX3 ON PRODGP_STR_LINK
(PRGP_PARENT_ID)
go

CREATE INDEX PGSL_IX1 ON PRODGP_STR_LINK
(PRGP_ID)
go

CREATE INDEX PGSL_IX5 ON PRODGP_STR_LINK
(PGSD_ID)
go

CREATE INDEX TXRN_IX1 ON TAX_RULE_NAME
(TXRN_NAME)
go


CREATE INDEX TXRL_IX3 ON TAX_RULE
(TXRN_ID)
go

CREATE INDEX TXRL_IX4 ON TAX_RULE
(TXRL_CODE)
go

CREATE INDEX TXLV_IX4 ON TAX_LEG_VARIANCE
(TXLV_DATETIME_FROM)
go

CREATE INDEX TXLV_IX1 ON TAX_LEG_VARIANCE
(TXRL_ID)
go

CREATE INDEX TXLV_IX5 ON TAX_LEG_VARIANCE
(TXLV_DATETIME_TO)
go

CREATE INDEX UNOM_IX1 ON UNIT_OF_MEASURE
(UNOM_CODE)
go

CREATE INDEX PROD_IX1 ON PRODUCT
(ORGU_ID_OWNING)
go

CREATE INDEX PROD_IX7 ON PRODUCT
(PROD_SKU)
go

CREATE INDEX PROD_IX2 ON PRODUCT
(PROD_NAME)
go

CREATE INDEX PROU_IX3 ON PROD_ORGU_LINK
(PROD_ID)
go


CREATE INDEX PROU_IX2 ON PROD_ORGU_LINK
(ORGU_ID)
go
