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
	[CATEGORY_ID] [bigint] IDENTITY (1,1) NOT NULL,
	[CONFIG_TYPE_ID] [int] NOT NULL,
	[CATEGORY_CODE] [varchar](100) NULL,
	[DISPLAY_NAME] [varchar](200) NOT NULL,
	[DESCRIPTION] [varchar](500) NULL
) ON [PRIMARY]


CREATE TABLE [dbo].[Customer](
	[CUSTOMER_ID] [bigint] IDENTITY (1,1) NOT NULL,
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
	[CONTACT_ID] [bigint] IDENTITY (1,1) NOT NULL,
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
	[SUPPLIER_ID] [bigint] IDENTITY (1,1) NOT NULL,
	[SUPPLIER_CODE] [varchar](50) NOT NULL,
	[SUPPLIER_NAME] [varchar](100) NOT NULL,
	[SUPPLIER_TYPE_ID] [bigint] NULL,
	[SUPPLIER_STATUS_ID] [bigint] NULL,
	[CONTACT_ID] [bigint] NULL,
	[LEAD_TIME] [int] NULL,
	[STOCK_COVER] [int] NULL,
	[MIN_ORDER_VALUE] [decimal](19,4) NULL,
	[CREDIT_LIMIT] [decimal](19,4) NULL,
	[MAX_ADV_ORDER] [int] NULL,
	[LAST_MODIFIED_DATE] [datetime] NULL,
	[CREATE_DATE] [datetime] NULL,
	[LAST_MODIFIED_BY] [bigint] NULL,
	[CONTACT_FIRST_NAME] [varchar](100) NULL,
	[CONTACT_SUR_NAME] [varchar](100) NULL,
	[CONTACT_TITLE] [varchar](20) NULL,
	[CONTACT_KNOWN_AS] [varchar](100) NULL
) ON [PRIMARY]


ALTER TABLE CUSTOMER ADD CONSTRAINT CUSTOMER_PK PRIMARY KEY(CUSTOMER_ID)
go

ALTER TABLE CUSTOMER_GRADE ADD CONSTRAINT GRADE_PK PRIMARY KEY(GRADE_ID)
go

ALTER TABLE CUSTOMER ADD CONSTRAINT CUSTOMER_2GRADE_FK FOREIGN KEY (GRADE_ID) REFERENCES CUSTOMER_GRADE(GRADE_ID)
go
ALTER TABLE CONFIG_TYPE ADD CONSTRAINT CONFIG_TYPE_PK PRIMARY KEY(CONFIG_TYPE_ID)
go

ALTER TABLE CONFIG_CATEGORY ADD CONSTRAINT CATEGORY_PK PRIMARY KEY(CATEGORY_ID)
go

ALTER TABLE CONFIG_CATEGORY ADD CONSTRAINT CATEGORY_2TYPE_FK FOREIGN KEY (CONFIG_TYPE_ID) REFERENCES CONFIG_TYPE(CONFIG_TYPE_ID)
go

ALTER TABLE SUPPLIER ADD CONSTRAINT SUPPLIER_PK PRIMARY KEY(SUPPLIER_ID)
go

ALTER TABLE CONTACT ADD CONSTRAINT CONTACT_PK PRIMARY KEY(CONTACT_ID)
go

ALTER TABLE SUPPLIER ADD CONSTRAINT SUPPLIER_TYEP_ID_2CATEGORY_FK FOREIGN KEY (SUPPLIER_TYPE_ID) REFERENCES CONFIG_CATEGORY(CATEGORY_ID)
go

ALTER TABLE SUPPLIER ADD CONSTRAINT SUPPLIER_STATUS_ID_2CATEGORY_FK FOREIGN KEY (SUPPLIER_STATUS_ID) REFERENCES CONFIG_CATEGORY(CATEGORY_ID)
go

ALTER TABLE SUPPLIER ADD CONSTRAINT SUPPLIER_CONTACT_ID_2CONTACT_FK FOREIGN KEY (CONTACT_ID) REFERENCES CONTACT(CONTACT_ID)
go

