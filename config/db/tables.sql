USE [RETAIL]
GO

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

CREATE TABLE [dbo].[Sequence](
	[SEQ_NAME] [varchar](100) ,
	[NEXT_VAL] [decimal](20,0)
) ON [PRIMARY]


ALTER TABLE CUSTOMER ADD CONSTRAINT CUSTOMER_PK PRIMARY KEY(CUSTOMER_ID)
go

ALTER TABLE CUSTOMER_GRADE ADD CONSTRAINT GRADE_PK PRIMARY KEY(GRADE_ID)
go

ALTER TABLE CUSTOMER ADD CONSTRAINT CUSTOMER_2GRADE_FK FOREIGN KEY (GRADE_ID) REFERENCES CUSTOMER_GRADE(GRADE_ID)
go
