USE RETAIL
GO

INSERT INTO CUSTOMER_GRADE(GRADE_CODE, GRADE_NAME, DESCRIPTION, RATE) VALUES ('A', 'COST + 20.00%', 'COST + 20.00%','0.20')
GO

INSERT INTO CUSTOMER_GRADE(GRADE_CODE, GRADE_NAME, DESCRIPTION, RATE) VALUES ('B', 'COST + 23.00%', 'COST + 23.00%','0.23')
GO

INSERT INTO CUSTOMER_GRADE(GRADE_CODE, GRADE_NAME, DESCRIPTION, RATE) VALUES ('C', 'COST + 28.00%', 'COST + 28.00%','0.28')
GO

INSERT INTO CUSTOMER_GRADE(GRADE_CODE, GRADE_NAME, DESCRIPTION, RATE) VALUES ('D', 'COST + 38.00%', 'COST + 38.00%','0.38')
GO

INSERT INTO CONFIG_TYPE(CONFIG_CODE, DISPLAY_NAME, DESCRIPTION) VALUES ('SUPPLIER_TYPE', 'SUPPLIER TYPE', 'SUPPLIER TYPE');
GO

INSERT INTO CONFIG_TYPE(CONFIG_CODE, DISPLAY_NAME, DESCRIPTION) VALUES ('SUPPLIER_STATUS', 'SUPPLIER STATUS', 'SUPPLIER STATUS');
GO



INSERT INTO CONFIG_CATEGORY(CONFIG_TYPE_ID, CATEGORY_CODE, DISPLAY_NAME, DESCRIPTION) VALUES ((SELECT CONFIG_TYPE_ID FROM CONFIG_TYPE WHERE CONFIG_CODE='SUPPLIER_TYPE'),'SUPPLIER_TYPE_DIRECT', 'DIRECT', 'DIRECT');
GO


INSERT INTO CONFIG_CATEGORY(CONFIG_TYPE_ID, CATEGORY_CODE, DISPLAY_NAME, DESCRIPTION) VALUES ((SELECT CONFIG_TYPE_ID FROM CONFIG_TYPE WHERE CONFIG_CODE='SUPPLIER_TYPE'),'SUPPLIER_TYPE_WAREHOUSET', 'WareHouse', 'WareHouse');
GO


INSERT INTO CONFIG_CATEGORY(CONFIG_TYPE_ID, CATEGORY_CODE, DISPLAY_NAME, DESCRIPTION) VALUES ((SELECT CONFIG_TYPE_ID FROM CONFIG_TYPE WHERE CONFIG_CODE='SUPPLIER_STATUS'),'SUPPLIER_STATUS_ACTIVE', 'Active', 'Active');
GO


INSERT INTO CONFIG_CATEGORY(CONFIG_TYPE_ID, CATEGORY_CODE, DISPLAY_NAME, DESCRIPTION) VALUES ((SELECT CONFIG_TYPE_ID FROM CONFIG_TYPE WHERE CONFIG_CODE='SUPPLIER_STATUS'),'SUPPLIER_STATUS_HELD', 'Held', 'Held');
GO

INSERT INTO CONFIG_CATEGORY(CONFIG_TYPE_ID, CATEGORY_CODE, DISPLAY_NAME, DESCRIPTION) VALUES ((SELECT CONFIG_TYPE_ID FROM CONFIG_TYPE WHERE CONFIG_CODE='SUPPLIER_STATUS'),'SUPPLIER_STATUS_DELETED', 'Deleted', 'Deleted');
GO

INSERT INTO CONFIG_CATEGORY(CONFIG_TYPE_ID, CATEGORY_CODE, DISPLAY_NAME, DESCRIPTION) VALUES ((SELECT CONFIG_TYPE_ID FROM CONFIG_TYPE WHERE CONFIG_CODE='SUPPLIER_STATUS'),'SUPPLIER_STATUS_IMPORTED', 'Impoted', 'Impoted');
GO

INSERT INTO CONFIG_TYPE(CONFIG_CODE, DISPLAY_NAME, DESCRIPTION) VALUES ('PRODUCT_TYPE', 'PRODUCT_TYPE', 'PRODUCT TYPE');
GO


INSERT INTO CONFIG_CATEGORY(CONFIG_TYPE_ID, CATEGORY_CODE, DISPLAY_NAME, DESCRIPTION) VALUES ((SELECT CONFIG_TYPE_ID FROM CONFIG_TYPE WHERE CONFIG_CODE='PRODUCT_TYPE'),'Bolts', 'Bolts', 'Bolts');
GO

INSERT INTO CONFIG_CATEGORY(CONFIG_TYPE_ID, CATEGORY_CODE, DISPLAY_NAME, DESCRIPTION) VALUES ((SELECT CONFIG_TYPE_ID FROM CONFIG_TYPE WHERE CONFIG_CODE='PRODUCT_TYPE'),'Door Closer', 'Door Closer', 'Door Closer');
GO

INSERT INTO CONFIG_CATEGORY(CONFIG_TYPE_ID, CATEGORY_CODE, DISPLAY_NAME, DESCRIPTION) VALUES ((SELECT CONFIG_TYPE_ID FROM CONFIG_TYPE WHERE CONFIG_CODE='PRODUCT_TYPE'),'Door Stop', 'Door Stop', 'Door Stop');
GO

INSERT INTO CONFIG_CATEGORY(CONFIG_TYPE_ID, CATEGORY_CODE, DISPLAY_NAME, DESCRIPTION) VALUES ((SELECT CONFIG_TYPE_ID FROM CONFIG_TYPE WHERE CONFIG_CODE='PRODUCT_TYPE'),'Furniture', 'Furniture', 'Furniture');
GO

INSERT INTO CONFIG_CATEGORY(CONFIG_TYPE_ID, CATEGORY_CODE, DISPLAY_NAME, DESCRIPTION) VALUES ((SELECT CONFIG_TYPE_ID FROM CONFIG_TYPE WHERE CONFIG_CODE='PRODUCT_TYPE'),'Kick Plate', 'Kick Plate', 'Kick Plate');
GO

INSERT INTO CONFIG_CATEGORY(CONFIG_TYPE_ID, CATEGORY_CODE, DISPLAY_NAME, DESCRIPTION) VALUES ((SELECT CONFIG_TYPE_ID FROM CONFIG_TYPE WHERE CONFIG_CODE='PRODUCT_TYPE'),'Lock', 'Lock', 'Lock');
GO

INSERT INTO CONFIG_CATEGORY(CONFIG_TYPE_ID, CATEGORY_CODE, DISPLAY_NAME, DESCRIPTION) VALUES ((SELECT CONFIG_TYPE_ID FROM CONFIG_TYPE WHERE CONFIG_CODE='PRODUCT_TYPE'),'Pull/Push handles', 'Pull/Push handles', 'Pull/Push handles');
GO

INSERT INTO CONFIG_CATEGORY(CONFIG_TYPE_ID, CATEGORY_CODE, DISPLAY_NAME, DESCRIPTION) VALUES ((SELECT CONFIG_TYPE_ID FROM CONFIG_TYPE WHERE CONFIG_CODE='PRODUCT_TYPE'),'Other', 'Other', 'Other');
GO

INSERT INTO CONFIG_TYPE(CONFIG_CODE, DISPLAY_NAME, DESCRIPTION) VALUES ('PRODUCT_STATUS', 'PRODUCT_STATUS', 'PRODUCT STATUS');
GO


INSERT INTO CONFIG_CATEGORY(CONFIG_TYPE_ID, CATEGORY_CODE, DISPLAY_NAME, DESCRIPTION) VALUES ((SELECT CONFIG_TYPE_ID FROM CONFIG_TYPE WHERE CONFIG_CODE='PRODUCT_STATUS'),'LIVE', 'LIVE', 'LIVE');
GO

INSERT INTO CONFIG_CATEGORY(CONFIG_TYPE_ID, CATEGORY_CODE, DISPLAY_NAME, DESCRIPTION) VALUES ((SELECT CONFIG_TYPE_ID FROM CONFIG_TYPE WHERE CONFIG_CODE='PRODUCT_STATUS'),'IMPORTED', 'IMPORTED', 'IMPORTED');
GO

--- company
INSERT INTO ORG_TYPE(ORGT_TYPE_NAME, ORGT_TYPE_CODE ) VALUES ('COMPANY','COMPANY')
GO

INSERT INTO ORG_TYPE(ORGT_TYPE_NAME, ORGT_TYPE_CODE ) VALUES ('ZONE','ZONE')
GO

-- ORGANISATION UNIT
INSERT INTO ORG_UNIT(COMP_ID, ORGU_NAME, ORGU_DESC, ORGU_CODE, ORGU_SHORT_NAME, ORGT_TYPE_ID) VALUES ((SELECT MAX(COMP_ID) FROM COMPANY) ,'JOMON PTY LTD', 'JOMON PTY LTD', 'JOMON', 'JOMON', (SELECT ORGT_TYPE_ID FROM ORG_TYPE WHERE ORGT_TYPE_CODE='COMPANY') )
GO

-- product group type
INSERT INTO PRGP_TYPE(PRGT_CODE, PRGT_NAME, PRGT_TYPE_CONST) VALUES ('MAJORGRP','Major Group','999003')
GO

INSERT INTO PRGP_TYPE(PRGT_CODE, PRGT_NAME, PRGT_TYPE_CONST) VALUES ('GROUP','Group','999002')
GO

INSERT INTO PRGP_TYPE(PRGT_CODE, PRGT_NAME, PRGT_TYPE_CONST) VALUES ('DEPT','Department','3')
GO

--product structure
INSERT INTO PRODGP_STRUCTURE(PSTR_NAME, PSTR_DESC, ORGU_ID, PSTR_IS_LIVE, PSTR_MAX_LEVELS ) VALUES ('MERCH_DEPTS','JOMON MAIN STRUCTURE',(SELECT ORGU_ID FROM ORG_UNIT WHERE ORGU_CODE='JOMON'),1,3)
GO

--product structure DETAIL
INSERT INTO PRODGP_STRUCTURE_DETAIL(PGSD_LEVEL_NAME, PGSD_LEVEL_DESC, PGSD_ID_PARENT, PGSD_LEVEL, PGSD_MANDATORY,PSTR_ID, PRGT_ID ) VALUES ('lEVEL1','LEVEL 1',0,1,1,(SELECT PSTR_ID FROM PRODGP_STRUCTURE WHERE PSTR_NAME='MERCH_DEPTS'),(SELECT PRGT_ID FROM PRGP_TYPE WHERE PRGT_CODE='MAJORGRP'))
GO

INSERT INTO PRODGP_STRUCTURE_DETAIL(PGSD_LEVEL_NAME, PGSD_LEVEL_DESC, PGSD_ID_PARENT, PGSD_LEVEL, PGSD_MANDATORY,PSTR_ID, PRGT_ID ) VALUES ('lEVEL2','LEVEL 2',1,2,1,(SELECT PSTR_ID FROM PRODGP_STRUCTURE WHERE PSTR_NAME='MERCH_DEPTS'),(SELECT PRGT_ID FROM PRGP_TYPE WHERE PRGT_CODE='GROUP'))
GO

INSERT INTO PRODGP_STRUCTURE_DETAIL(PGSD_LEVEL_NAME, PGSD_LEVEL_DESC, PGSD_ID_PARENT, PGSD_LEVEL, PGSD_MANDATORY,PSTR_ID, PRGT_ID ) VALUES ('lEVEL3','LEVEL 3',2,3,1,(SELECT PSTR_ID FROM PRODGP_STRUCTURE WHERE PSTR_NAME='MERCH_DEPTS'),(SELECT PRGT_ID FROM PRGP_TYPE WHERE PRGT_CODE='DEPT'))
GO

INSERT INTO TAX_RULE_NAME(TXRN_NAME, TXRN_CODE) VALUES ('GST', 'GST');
GO

INSERT INTO TAX_RULE (TXRL_APPLY_SEQ, TXRL_CUM_TAX, TXRL_CODE,TXRL_DESC, TXRL_ROUNDING_MTHD, TXRN_ID, TXRL_DATE_FROM, TXRL_DATE_TO ) VALUES (0, 0, 'E', '10% GST Rate', 2, (SELECT TXRN_ID FROM TAX_RULE_NAME WHERE TXRN_CODE='GST'), '1900-01-01 12:00:00', '2100-01-01 12:00:00')
GO

INSERT INTO TAX_RULE (TXRL_APPLY_SEQ, TXRL_CUM_TAX, TXRL_CODE,TXRL_DESC, TXRL_ROUNDING_MTHD, TXRN_ID, TXRL_DATE_FROM, TXRL_DATE_TO ) VALUES (0, 0, 'A', '0% GST Rate', 2, (SELECT TXRN_ID FROM TAX_RULE_NAME WHERE TXRN_CODE='GST'), '1900-01-01 12:00:00', '2100-01-01 12:00:00')
GO


INSERT INTO TAX_LEG_VARIANCE(TXLV_RATE, TXLV_DATETIME_FROM, TXLV_DATETIME_TO, TXLV_CODE, TXLV_DESC, TXLV_IS_ACTIVE,TXRL_ID) VALUES (0.10, '2000-01-01 12:00:00','2100-01-01 12:00:00', 'GST_10','10% GST Rate',1, (SELECT TXRL_ID FROM TAX_RULE WHERE TXRL_CODE='E')  );
GO

INSERT INTO TAX_LEG_VARIANCE(TXLV_RATE, TXLV_DATETIME_FROM, TXLV_DATETIME_TO, TXLV_CODE, TXLV_DESC, TXLV_IS_ACTIVE,TXRL_ID) VALUES (0.00, '2000-01-01 12:00:00','2100-01-01 12:00:00', 'GST_0','0% GST Rate',0, (SELECT TXRL_ID FROM TAX_RULE WHERE TXRL_CODE='A')  );
GO


INSERT INTO UNIT_OF_MEASURE(UNOM_CODE, UNOM_DESC, UNOM_FACTOR, UNOM_ID_PARENT, UNOM_CREATED, UNOM_MODIFIED) VALUES ('EA', 'Each', 1, null, GETDATE(), GETDATE());
go

INSERT INTO UNIT_OF_MEASURE(UNOM_CODE, UNOM_DESC, UNOM_FACTOR, UNOM_ID_PARENT,UNOM_CREATED, UNOM_MODIFIED) VALUES ('kg', 'kiloo', 1, null,GETDATE(),GETDATE());
GO


INSERT INTO UNIT_OF_MEASURE(UNOM_CODE, UNOM_DESC, UNOM_FACTOR, UNOM_ID_PARENT,UNOM_CREATED, UNOM_MODIFIED) VALUES ('gram', 'gram', 1000, (select UNOM_ID FROM UNIT_OF_MEASURE WHERE UNOM_CODE='kg'), GETDATE(),GETDATE());
GO

INSERT INTO UNIT_OF_MEASURE(UNOM_CODE, UNOM_DESC, UNOM_FACTOR, UNOM_ID_PARENT,UNOM_CREATED, UNOM_MODIFIED) VALUES ('mg', 'miligram', 1000, (select UNOM_ID FROM UNIT_OF_MEASURE WHERE UNOM_CODE='gram'),GETDATE(),GETDATE() );
GO

INSERT INTO UNIT_OF_MEASURE(UNOM_CODE, UNOM_DESC, UNOM_FACTOR, UNOM_ID_PARENT,UNOM_CREATED, UNOM_MODIFIED) VALUES ('liter', 'liter', 1, null,GETDATE(),GETDATE());
GO


INSERT INTO LEGAL_TENDER(LEGT_CODE, LEGT_NAME ,LEGT_NO_DECS, LEGT_IS_EURO, LEGT_EURO_MEMBER, LEGT_SYMBOL, LEGT_NUMERIC_CODE) VALUES('AUD', 'Australian Dollar',2,0, 0,'$','036');
GO

INSERT INTO PRICE_BAND (PRIB_ACTIVE, PRIB_CODE, PRIB_NAME, ORGU_ID, LEGT_ID, PRIB_TAX_INCLUDED) VALUES (1, 'A', 'AU DOLLAR',(SELECT ORGU_ID FROM ORG_UNIT WHERE ORGU_CODE='JOMON'), (SELECT LEGT_ID FROM LEGAL_TENDER WHERE LEGT_CODE='AUD'), 1)
GO


INSERT INTO PRICE_CODE (PRCC_NAME, PRCC_CODE, PRCC_CREATED) VALUES ('SELL PRICE', 'SELL_PRICE', GETDATE());
go

INSERT INTO STORE(ORGU_ID, STORE_CODE,STORE_NAME) values ((SELECT ORGU_ID FROM ORG_UNIT WHERE ORGU_CODE='JOMON'), 'JOMON_SYD','JOMON SYDNEY STORE');
go


--INSERT TRANSACTION TYPE

INSERT INTO CONFIG_TYPE(CONFIG_CODE, DISPLAY_NAME, DESCRIPTION) VALUES ('TXN_TYPE', 'TXN_TYPE', 'TRANSACTION TYPE');
GO


INSERT INTO CONFIG_CATEGORY(CONFIG_TYPE_ID, CATEGORY_CODE, DISPLAY_NAME, DESCRIPTION) VALUES ((SELECT CONFIG_TYPE_ID FROM CONFIG_TYPE WHERE CONFIG_CODE='TXN_TYPE'),'TXN_TYPE_SALE', 'SALE', 'TRANSACTION SALE');
GO
INSERT INTO CONFIG_CATEGORY(CONFIG_TYPE_ID, CATEGORY_CODE, DISPLAY_NAME, DESCRIPTION) VALUES ((SELECT CONFIG_TYPE_ID FROM CONFIG_TYPE WHERE CONFIG_CODE='TXN_TYPE'),'TXN_TYPE_REFUND', 'REFUND', 'TRANSACTION REFUND');
GO
INSERT INTO CONFIG_CATEGORY(CONFIG_TYPE_ID, CATEGORY_CODE, DISPLAY_NAME, DESCRIPTION) VALUES ((SELECT CONFIG_TYPE_ID FROM CONFIG_TYPE WHERE CONFIG_CODE='TXN_TYPE'),'TXN_TYPE_QUOTE', 'QUOTE', 'QUOTE');
GO
INSERT INTO CONFIG_CATEGORY(CONFIG_TYPE_ID, CATEGORY_CODE, DISPLAY_NAME, DESCRIPTION) VALUES ((SELECT CONFIG_TYPE_ID FROM CONFIG_TYPE WHERE CONFIG_CODE='TXN_TYPE'),'TXN_TYPE_GOODS_IN', 'GOODS IN', 'GOODS IN');
GO
INSERT INTO CONFIG_CATEGORY(CONFIG_TYPE_ID, CATEGORY_CODE, DISPLAY_NAME, DESCRIPTION) VALUES ((SELECT CONFIG_TYPE_ID FROM CONFIG_TYPE WHERE CONFIG_CODE='TXN_TYPE'),'TXN_TYPE_GOODS_RESERVE', 'GOODS RESERVED', 'GOODS RESERVED');
GO
INSERT INTO CONFIG_CATEGORY(CONFIG_TYPE_ID, CATEGORY_CODE, DISPLAY_NAME, DESCRIPTION) VALUES ((SELECT CONFIG_TYPE_ID FROM CONFIG_TYPE WHERE CONFIG_CODE='TXN_TYPE'),'TXN_TYPE_GOODS_CANCEL_RESERVE', 'RESERVED CANCELED', 'GOODS RESERVED CANCELED');
GO
INSERT INTO CONFIG_CATEGORY(CONFIG_TYPE_ID, CATEGORY_CODE, DISPLAY_NAME, DESCRIPTION) VALUES ((SELECT CONFIG_TYPE_ID FROM CONFIG_TYPE WHERE CONFIG_CODE='TXN_TYPE'),'TXN_TYPE_GOODS_IN_TRANSIT', 'IN TRANSIT', 'GOODS IN TRANSIT');
GO

--INSERT TRANSACTION item type

INSERT INTO CONFIG_TYPE(CONFIG_CODE, DISPLAY_NAME, DESCRIPTION) VALUES ('TXN_MEDIA_TYPE', 'TXN_MEDIA_TYPE', 'TRANSACTION MEDIA TYPE');
GO


INSERT INTO CONFIG_CATEGORY(CONFIG_TYPE_ID, CATEGORY_CODE, DISPLAY_NAME, DESCRIPTION) VALUES ((SELECT CONFIG_TYPE_ID FROM CONFIG_TYPE WHERE CONFIG_CODE='TXN_MEDIA_TYPE'),'TXN_MEDIA_SALE', 'SALE', 'TRANSACTION MEDIA SALE');
GO
INSERT INTO CONFIG_CATEGORY(CONFIG_TYPE_ID, CATEGORY_CODE, DISPLAY_NAME, DESCRIPTION) VALUES ((SELECT CONFIG_TYPE_ID FROM CONFIG_TYPE WHERE CONFIG_CODE='TXN_MEDIA_TYPE'),'TXN_MEDIA_REFUND', 'REFUND', 'TRANSACTION MEDIA REFUND');
GO


--INSERT TRANSACTION item type

INSERT INTO CONFIG_TYPE(CONFIG_CODE, DISPLAY_NAME, DESCRIPTION) VALUES ('TXN_LINE_TYPE', 'TXN_LINE_TYPE', 'TRANSACTION LINE TYPE');
GO


INSERT INTO CONFIG_CATEGORY(CONFIG_TYPE_ID, CATEGORY_CODE, DISPLAY_NAME, DESCRIPTION) VALUES ((SELECT CONFIG_TYPE_ID FROM CONFIG_TYPE WHERE CONFIG_CODE='TXN_LINE_TYPE'),'TXN_LINE_SALE', 'SALE', 'TRANSACTION LINE SALE');
GO
INSERT INTO CONFIG_CATEGORY(CONFIG_TYPE_ID, CATEGORY_CODE, DISPLAY_NAME, DESCRIPTION) VALUES ((SELECT CONFIG_TYPE_ID FROM CONFIG_TYPE WHERE CONFIG_CODE='TXN_LINE_TYPE'),'TXN_LINE_REFUND', 'REFUND', 'TRANSACTION LINE REFUND');
GO


--TRANSACTION STATE
INSERT INTO CONFIG_TYPE(CONFIG_CODE, DISPLAY_NAME, DESCRIPTION) VALUES ('TXN_STATE', 'TXN_STATE', 'TRANSACTION STATE');
GO

INSERT INTO CONFIG_CATEGORY(CONFIG_TYPE_ID, CATEGORY_CODE, DISPLAY_NAME, DESCRIPTION) VALUES ((SELECT CONFIG_TYPE_ID FROM CONFIG_TYPE WHERE CONFIG_CODE='TXN_STATE'),'TXN_STATE_DRAFT', 'DRAFT', 'DRAFT');
GO
INSERT INTO CONFIG_CATEGORY(CONFIG_TYPE_ID, CATEGORY_CODE, DISPLAY_NAME, DESCRIPTION) VALUES ((SELECT CONFIG_TYPE_ID FROM CONFIG_TYPE WHERE CONFIG_CODE='TXN_STATE'),'TXN_STATE_VOID', 'VOID', 'VOID');
GO
INSERT INTO CONFIG_CATEGORY(CONFIG_TYPE_ID, CATEGORY_CODE, DISPLAY_NAME, DESCRIPTION) VALUES ((SELECT CONFIG_TYPE_ID FROM CONFIG_TYPE WHERE CONFIG_CODE='TXN_STATE'),'TXN_STATE_SUSPEND', 'SUSPEND', 'SUSPEND');
GO
INSERT INTO CONFIG_CATEGORY(CONFIG_TYPE_ID, CATEGORY_CODE, DISPLAY_NAME, DESCRIPTION) VALUES ((SELECT CONFIG_TYPE_ID FROM CONFIG_TYPE WHERE CONFIG_CODE='TXN_STATE'),'TXN_STATE_FINAL', 'FINAL', 'FINAL');
GO

INSERT INTO MEDIA_TYPE(MEDT_NAME, MEDT_DESC, MEDT_CODE, MEDT_REFUNDABLE) VALUES ('Cash', 'Cash', '1', 1);
GO
INSERT INTO MEDIA_TYPE(MEDT_NAME, MEDT_DESC, MEDT_CODE, MEDT_REFUNDABLE) VALUES ('Cheque', 'Bank Cheque', '2', 1);
GO
INSERT INTO MEDIA_TYPE(MEDT_NAME, MEDT_DESC, MEDT_CODE, MEDT_REFUNDABLE) VALUES ('Card', 'Debit/Credit Card', '3', 1);
GO
INSERT INTO MEDIA_TYPE(MEDT_NAME, MEDT_DESC, MEDT_CODE, MEDT_REFUNDABLE) VALUES ('Account', 'Charge Account', '4', 0);
GO

INSERT INTO PAYMENT_MEDIA(PAYM_NAME, PAYM_CODE, MEDT_ID, LEGT_ID) VALUES ('Cash', '1',(SELECT MEDT_ID FROM MEDIA_TYPE WHERE MEDT_NAME='Cash'), (SELECT LEGT_ID FROM LEGAL_TENDER WHERE LEGT_CODE='AUD'));
GO
INSERT INTO PAYMENT_MEDIA(PAYM_NAME, PAYM_CODE, MEDT_ID, LEGT_ID) VALUES ('Cheque', '2',(SELECT MEDT_ID FROM MEDIA_TYPE WHERE MEDT_NAME='Cheque'), (SELECT LEGT_ID FROM LEGAL_TENDER WHERE LEGT_CODE='AUD'));
GO
INSERT INTO PAYMENT_MEDIA(PAYM_NAME, PAYM_CODE, MEDT_ID, LEGT_ID) VALUES ('Business Cheque', '3',(SELECT MEDT_ID FROM MEDIA_TYPE WHERE MEDT_NAME='Cheque'), (SELECT LEGT_ID FROM LEGAL_TENDER WHERE LEGT_CODE='AUD'));
GO
INSERT INTO PAYMENT_MEDIA(PAYM_NAME, PAYM_CODE, MEDT_ID, LEGT_ID) VALUES ('MasterCard', '4',(SELECT MEDT_ID FROM MEDIA_TYPE WHERE MEDT_NAME='Card'), (SELECT LEGT_ID FROM LEGAL_TENDER WHERE LEGT_CODE='AUD'));
GO
INSERT INTO PAYMENT_MEDIA(PAYM_NAME, PAYM_CODE, MEDT_ID, LEGT_ID) VALUES ('VISA', '5',(SELECT MEDT_ID FROM MEDIA_TYPE WHERE MEDT_NAME='Card'), (SELECT LEGT_ID FROM LEGAL_TENDER WHERE LEGT_CODE='AUD'));
GO
INSERT INTO PAYMENT_MEDIA(PAYM_NAME, PAYM_CODE, MEDT_ID, LEGT_ID) VALUES ('Diners', '6',(SELECT MEDT_ID FROM MEDIA_TYPE WHERE MEDT_NAME='Card'), (SELECT LEGT_ID FROM LEGAL_TENDER WHERE LEGT_CODE='AUD'));
GO
INSERT INTO PAYMENT_MEDIA(PAYM_NAME, PAYM_CODE, MEDT_ID, LEGT_ID) VALUES ('AMEX', '7',(SELECT MEDT_ID FROM MEDIA_TYPE WHERE MEDT_NAME='Card'), (SELECT LEGT_ID FROM LEGAL_TENDER WHERE LEGT_CODE='AUD'));
GO
INSERT INTO PAYMENT_MEDIA(PAYM_NAME, PAYM_CODE, MEDT_ID, LEGT_ID) VALUES ('Discover', '8',(SELECT MEDT_ID FROM MEDIA_TYPE WHERE MEDT_NAME='Card'), (SELECT LEGT_ID FROM LEGAL_TENDER WHERE LEGT_CODE='AUD'));
GO
INSERT INTO PAYMENT_MEDIA(PAYM_NAME, PAYM_CODE, MEDT_ID, LEGT_ID) VALUES ('EFTPOS', '9',(SELECT MEDT_ID FROM MEDIA_TYPE WHERE MEDT_NAME='Card'), (SELECT LEGT_ID FROM LEGAL_TENDER WHERE LEGT_CODE='AUD'));
GO
INSERT INTO PAYMENT_MEDIA(PAYM_NAME, PAYM_CODE, MEDT_ID, LEGT_ID) VALUES ('Maestro', '10',(SELECT MEDT_ID FROM MEDIA_TYPE WHERE MEDT_NAME='Card'), (SELECT LEGT_ID FROM LEGAL_TENDER WHERE LEGT_CODE='AUD'));
GO
INSERT INTO PAYMENT_MEDIA(PAYM_NAME, PAYM_CODE, MEDT_ID, LEGT_ID) VALUES ('Unknown Card', '11',(SELECT MEDT_ID FROM MEDIA_TYPE WHERE MEDT_NAME='Card'), (SELECT LEGT_ID FROM LEGAL_TENDER WHERE LEGT_CODE='AUD'));
GO
INSERT INTO PAYMENT_MEDIA(PAYM_NAME, PAYM_CODE, MEDT_ID, LEGT_ID) VALUES ('Account', '12',(SELECT MEDT_ID FROM MEDIA_TYPE WHERE MEDT_NAME='Account'), (SELECT LEGT_ID FROM LEGAL_TENDER WHERE LEGT_CODE='AUD'));
GO

-- PURCHASE ORDER HEADER STATUS

INSERT INTO CONFIG_TYPE(CONFIG_CODE, DISPLAY_NAME, DESCRIPTION) VALUES ('POH_STATUS', 'POH_STATUS', 'PURCHASE ORDER HEADER STATUS');
GO

INSERT INTO CONFIG_CATEGORY(CONFIG_TYPE_ID, CATEGORY_CODE, DISPLAY_NAME, DESCRIPTION, COLOR) VALUES ((SELECT CONFIG_TYPE_ID FROM CONFIG_TYPE WHERE CONFIG_CODE='POH_STATUS'),'POH_STATUS_IN_PROGRESS', 'IN PROGRESS', 'IN PROGRESS', 'amber');
GO

INSERT INTO CONFIG_CATEGORY(CONFIG_TYPE_ID, CATEGORY_CODE, DISPLAY_NAME, DESCRIPTION, COLOR) VALUES ((SELECT CONFIG_TYPE_ID FROM CONFIG_TYPE WHERE CONFIG_CODE='POH_STATUS'),'POH_STATUS_CONFIRMED', 'CONFIRMED', 'CONFIRMED', 'green');
GO

INSERT INTO CONFIG_CATEGORY(CONFIG_TYPE_ID, CATEGORY_CODE, DISPLAY_NAME, DESCRIPTION, COLOR) VALUES ((SELECT CONFIG_TYPE_ID FROM CONFIG_TYPE WHERE CONFIG_CODE='POH_STATUS'),'POH_STATUS_CANCELLED', 'CANCELLED', 'CANCELLED', 'red');
GO

INSERT INTO CONFIG_CATEGORY(CONFIG_TYPE_ID, CATEGORY_CODE, DISPLAY_NAME, DESCRIPTION, COLOR) VALUES ((SELECT CONFIG_TYPE_ID FROM CONFIG_TYPE WHERE CONFIG_CODE='POH_STATUS'),'POH_STATUS_PARTIAL_REC', 'PARTIAL REC', 'PARTIAL REC', 'yellow');
GO

INSERT INTO CONFIG_CATEGORY(CONFIG_TYPE_ID, CATEGORY_CODE, DISPLAY_NAME, DESCRIPTION, COLOR) VALUES ((SELECT CONFIG_TYPE_ID FROM CONFIG_TYPE WHERE CONFIG_CODE='POH_STATUS'),'POH_STATUS_GOOD_RECEIVED', 'GOOD RECEIVED', 'GOOD RECEIVED', 'brown');
GO

--PURCHASE ORDER HEADER TYPE
INSERT INTO CONFIG_TYPE(CONFIG_CODE, DISPLAY_NAME, DESCRIPTION) VALUES ('POH_TYPE', 'POH_TYPE', 'PURCHASE ORDER HEADER TYPE');
GO

INSERT INTO CONFIG_CATEGORY(CONFIG_TYPE_ID, CATEGORY_CODE, DISPLAY_NAME, DESCRIPTION) VALUES ((SELECT CONFIG_TYPE_ID FROM CONFIG_TYPE WHERE CONFIG_CODE='POH_TYPE'),'POH_TYPE_STOCK', 'STOCK', 'STOCK');
GO

INSERT INTO CONFIG_CATEGORY(CONFIG_TYPE_ID, CATEGORY_CODE, DISPLAY_NAME, DESCRIPTION) VALUES ((SELECT CONFIG_TYPE_ID FROM CONFIG_TYPE WHERE CONFIG_CODE='POH_TYPE'),'POH_TYPE_PROJECT', 'PROJECT', 'PROJECT');
GO

INSERT INTO CONFIG_CATEGORY(CONFIG_TYPE_ID, CATEGORY_CODE, DISPLAY_NAME, DESCRIPTION) VALUES ((SELECT CONFIG_TYPE_ID FROM CONFIG_TYPE WHERE CONFIG_CODE='POH_TYPE'),'POH_TYPE_SPECIAL_ORDER', 'SPECIAL', 'SPECIAL');
GO


--PURCHASE ORDER HEADER CREATION TYPE
INSERT INTO CONFIG_TYPE(CONFIG_CODE, DISPLAY_NAME, DESCRIPTION) VALUES ('POH_CREATION_TYPE', 'POH_CREATION_TYPE', 'PURCHASE ORDER HEADER CREATION TYPE');
GO

INSERT INTO CONFIG_CATEGORY(CONFIG_TYPE_ID, CATEGORY_CODE, DISPLAY_NAME, DESCRIPTION, COLOR) VALUES ((SELECT CONFIG_TYPE_ID FROM CONFIG_TYPE WHERE CONFIG_CODE='POH_CREATION_TYPE'),'POH_CREATION_TYPE_MANUAL', 'MANUAL', 'MANUAL','blue');
GO

INSERT INTO CONFIG_CATEGORY(CONFIG_TYPE_ID, CATEGORY_CODE, DISPLAY_NAME, DESCRIPTION, COLOR) VALUES ((SELECT CONFIG_TYPE_ID FROM CONFIG_TYPE WHERE CONFIG_CODE='POH_CREATION_TYPE'),'POH_CREATION_TYPE_AUTO', 'AUTO', 'AUTO','amber');
GO


-- BILL OF QUANTITY STATUS

INSERT INTO CONFIG_TYPE(CONFIG_CODE, DISPLAY_NAME, DESCRIPTION) VALUES ('BOQ_STATUS', 'BOQ_STATUS', 'BILL OF QUANTITY HEADER STATUS');
GO

INSERT INTO CONFIG_CATEGORY(CONFIG_TYPE_ID, CATEGORY_CODE, DISPLAY_NAME, DESCRIPTION, COLOR) VALUES ((SELECT CONFIG_TYPE_ID FROM CONFIG_TYPE WHERE CONFIG_CODE='BOQ_STATUS'),'BOQ_STATUS_NEW', 'NEW', 'NEW','amber');
GO

INSERT INTO CONFIG_CATEGORY(CONFIG_TYPE_ID, CATEGORY_CODE, DISPLAY_NAME, DESCRIPTION, COLOR) VALUES ((SELECT CONFIG_TYPE_ID FROM CONFIG_TYPE WHERE CONFIG_CODE='BOQ_STATUS'),'BOQ_STATUS_ON_ORDER', 'ON ORDER', 'ON ORDER','blue');
GO

INSERT INTO CONFIG_CATEGORY(CONFIG_TYPE_ID, CATEGORY_CODE, DISPLAY_NAME, DESCRIPTION, COLOR) VALUES ((SELECT CONFIG_TYPE_ID FROM CONFIG_TYPE WHERE CONFIG_CODE='BOQ_STATUS'),'BOQ_STATUS_FINAL', 'FINALISED', 'FINALISED','green');
GO

INSERT INTO CONFIG_CATEGORY(CONFIG_TYPE_ID, CATEGORY_CODE, DISPLAY_NAME, DESCRIPTION, COLOR) VALUES ((SELECT CONFIG_TYPE_ID FROM CONFIG_TYPE WHERE CONFIG_CODE='BOQ_STATUS'),'BOQ_STATUS_PARTIAL_REC', 'PARTIAL REC', 'PARTIAL REC','yellow');
GO

INSERT INTO CONFIG_CATEGORY(CONFIG_TYPE_ID, CATEGORY_CODE, DISPLAY_NAME, DESCRIPTION, COLOR) VALUES ((SELECT CONFIG_TYPE_ID FROM CONFIG_TYPE WHERE CONFIG_CODE='BOQ_STATUS'),'BOQ_STATUS_RECEIVED', 'RECEIVED', 'RECEIVED','brown');
GO

INSERT INTO CONFIG_CATEGORY(CONFIG_TYPE_ID, CATEGORY_CODE, DISPLAY_NAME, DESCRIPTION, COLOR) VALUES ((SELECT CONFIG_TYPE_ID FROM CONFIG_TYPE WHERE CONFIG_CODE='BOQ_STATUS'),'BOQ_STATUS_CANCELLED', 'CANCELLED', 'CANCELLED','red');
GO


-- customer type

INSERT INTO CONFIG_TYPE(CONFIG_CODE, DISPLAY_NAME, DESCRIPTION) VALUES ('CUSTOMER_TYPE', 'CUSTOMER_TYPE', 'CUSTOMER TYPES');
GO

INSERT INTO CONFIG_CATEGORY(CONFIG_TYPE_ID, CATEGORY_CODE, DISPLAY_NAME, DESCRIPTION, COLOR) VALUES ((SELECT CONFIG_TYPE_ID FROM CONFIG_TYPE WHERE CONFIG_CODE='CUSTOMER_TYPE'),'CUSTOMER_TYPE_CASH_ONLY', 'COD', 'COD','amber');
GO

INSERT INTO CONFIG_CATEGORY(CONFIG_TYPE_ID, CATEGORY_CODE, DISPLAY_NAME, DESCRIPTION, COLOR) VALUES ((SELECT CONFIG_TYPE_ID FROM CONFIG_TYPE WHERE CONFIG_CODE='CUSTOMER_TYPE'),'CUSTOMER_TYPE_ACCOUNT', 'ACC', 'ACC','green');
GO

-- customer status

INSERT INTO CONFIG_TYPE(CONFIG_CODE, DISPLAY_NAME, DESCRIPTION) VALUES ('CUSTOMER_STATUS', 'CUSTOMER_STATUS', 'CUSTOMER STATUS');
GO

INSERT INTO CONFIG_CATEGORY(CONFIG_TYPE_ID, CATEGORY_CODE, DISPLAY_NAME, DESCRIPTION, COLOR) VALUES ((SELECT CONFIG_TYPE_ID FROM CONFIG_TYPE WHERE CONFIG_CODE='CUSTOMER_STATUS'),'CUSTOMER_STATUS_NEW', 'NEW', 'NEW','red');
GO

INSERT INTO CONFIG_CATEGORY(CONFIG_TYPE_ID, CATEGORY_CODE, DISPLAY_NAME, DESCRIPTION, COLOR) VALUES ((SELECT CONFIG_TYPE_ID FROM CONFIG_TYPE WHERE CONFIG_CODE='CUSTOMER_STATUS'),'CUSTOMER_STATUS_CONFIRMED', 'CONFIRMED', 'CONFIRMED','green');
GO


INSERT INTO CONFIG_TYPE(CONFIG_CODE, DISPLAY_NAME, DESCRIPTION) VALUES ('BOQ_LINE_STATUS', 'BOQ_LINE_STATUS', 'BOQ LINE STATUS');
GO

INSERT INTO CONFIG_CATEGORY(CONFIG_TYPE_ID, CATEGORY_CODE, DISPLAY_NAME, DESCRIPTION, COLOR) VALUES ((SELECT CONFIG_TYPE_ID FROM CONFIG_TYPE WHERE CONFIG_CODE='BOQ_LINE_STATUS'),'BOQ_LINE_STATUS_PENDING', 'PENDING', 'PENDING', 'amber');
GO

INSERT INTO CONFIG_CATEGORY(CONFIG_TYPE_ID, CATEGORY_CODE, DISPLAY_NAME, DESCRIPTION, COLOR) VALUES ((SELECT CONFIG_TYPE_ID FROM CONFIG_TYPE WHERE CONFIG_CODE='BOQ_LINE_STATUS'),'BOQ_LINE_STATUS_PO_CREATED', 'ON ORDER', 'ON ORDER', 'green');
GO

INSERT INTO CONFIG_CATEGORY(CONFIG_TYPE_ID, CATEGORY_CODE, DISPLAY_NAME, DESCRIPTION, COLOR) VALUES ((SELECT CONFIG_TYPE_ID FROM CONFIG_TYPE WHERE CONFIG_CODE='BOQ_LINE_STATUS'),'BOQ_LINE_STATUS_PARTIAL_RECEIVED', 'PARTIAL REC', 'PARTIAL REC', 'yellow');
GO

INSERT INTO CONFIG_CATEGORY(CONFIG_TYPE_ID, CATEGORY_CODE, DISPLAY_NAME, DESCRIPTION, COLOR) VALUES ((SELECT CONFIG_TYPE_ID FROM CONFIG_TYPE WHERE CONFIG_CODE='BOQ_LINE_STATUS'),'BOQ_LINE_STATUS_GOOD_RECEIVED', 'RECEIVED', 'RECEIVED', 'brown');
GO

INSERT INTO CONFIG_CATEGORY(CONFIG_TYPE_ID, CATEGORY_CODE, DISPLAY_NAME, DESCRIPTION, COLOR) VALUES ((SELECT CONFIG_TYPE_ID FROM CONFIG_TYPE WHERE CONFIG_CODE='BOQ_LINE_STATUS'),'BOQ_LINE_STATUS_VOID', 'VOID', 'VOID', 'red');
GO

-- DELIVERY NOTE STATUS

INSERT INTO CONFIG_TYPE(CONFIG_CODE, DISPLAY_NAME, DESCRIPTION) VALUES ('DLV_NOTE_STATUS', 'DLV_NOTE_STATUS', 'DELIVERY NOTE STATUS');
GO

INSERT INTO CONFIG_CATEGORY(CONFIG_TYPE_ID, CATEGORY_CODE, DISPLAY_NAME, DESCRIPTION, COLOR) VALUES ((SELECT CONFIG_TYPE_ID FROM CONFIG_TYPE WHERE CONFIG_CODE='DLV_NOTE_STATUS'),'DLV_NOTE_STATUS_IN_PROGRESS', 'IN PROGRESS', 'IN PROGRESS', 'amber');
GO

INSERT INTO CONFIG_CATEGORY(CONFIG_TYPE_ID, CATEGORY_CODE, DISPLAY_NAME, DESCRIPTION, COLOR) VALUES ((SELECT CONFIG_TYPE_ID FROM CONFIG_TYPE WHERE CONFIG_CODE='DLV_NOTE_STATUS'),'DLV_NOTE_STATUS_ON_HOLD', 'ON HOLD', 'ON HOLD', 'red');
GO

INSERT INTO CONFIG_CATEGORY(CONFIG_TYPE_ID, CATEGORY_CODE, DISPLAY_NAME, DESCRIPTION, COLOR) VALUES ((SELECT CONFIG_TYPE_ID FROM CONFIG_TYPE WHERE CONFIG_CODE='DLV_NOTE_STATUS'),'DLV_NOTE_STATUS_COMPLETE', 'COMPLETE', 'COMPLETE', 'green');
GO

-- CONTACT TYPE


INSERT INTO CONFIG_TYPE(CONFIG_CODE, DISPLAY_NAME, DESCRIPTION) VALUES ('CONTACT_TYPE', 'CONTACT_TYPE', 'CONTACT TYPES');
GO

INSERT INTO CONFIG_CATEGORY(CONFIG_TYPE_ID, CATEGORY_CODE, DISPLAY_NAME, DESCRIPTION, COLOR) VALUES ((SELECT CONFIG_TYPE_ID FROM CONFIG_TYPE WHERE CONFIG_CODE='CONTACT_TYPE'),'CONTACT_TYPE_CONTACT_PERSON', 'CONTACT PERSON', 'CONTACT PERSON','amber');
GO

INSERT INTO CONFIG_CATEGORY(CONFIG_TYPE_ID, CATEGORY_CODE, DISPLAY_NAME, DESCRIPTION, COLOR) VALUES ((SELECT CONFIG_TYPE_ID FROM CONFIG_TYPE WHERE CONFIG_CODE='CONTACT_TYPE'),'CONTACT_TYPE_ACCOUNT', 'ACCOUNT MANAGER', 'ACCOUNT MANAGER','green');
GO

INSERT INTO CONFIG_CATEGORY(CONFIG_TYPE_ID, CATEGORY_CODE, DISPLAY_NAME, DESCRIPTION, COLOR) VALUES ((SELECT CONFIG_TYPE_ID FROM CONFIG_TYPE WHERE CONFIG_CODE='CONTACT_TYPE'),'CONTACT_TYPE_PM', 'PROJECT MANAGER', 'PROJECT MANAGER','blue');
GO


--user access

INSERT INTO ACCESS_POINT (ACPT_NAME, ACPT_TOKEN,ACPT_DESC) VALUES ('Add Customer', 'createCustomer', 'Add new customer');
INSERT INTO ACCESS_POINT (ACPT_NAME, ACPT_TOKEN,ACPT_DESC) VALUES ('List Customers', 'listCustomer', 'View customer list');
INSERT INTO ACCESS_POINT (ACPT_NAME, ACPT_TOKEN,ACPT_DESC) VALUES ('Add Supplier', 'createSupplier', 'Add new supplier');
INSERT INTO ACCESS_POINT (ACPT_NAME, ACPT_TOKEN,ACPT_DESC) VALUES ('List Suppliers', 'listSupplier', 'View supplier list');
INSERT INTO ACCESS_POINT (ACPT_NAME, ACPT_TOKEN,ACPT_DESC) VALUES ('Add Product', 'createProduct', 'Add new product');
INSERT INTO ACCESS_POINT (ACPT_NAME, ACPT_TOKEN,ACPT_DESC) VALUES ('List products', 'listProduct', 'View product list');
INSERT INTO ACCESS_POINT (ACPT_NAME, ACPT_TOKEN,ACPT_DESC) VALUES ('Import BOQ', 'uploadFile', 'Import BOQ');
INSERT INTO ACCESS_POINT (ACPT_NAME, ACPT_TOKEN,ACPT_DESC) VALUES ('List BOQs', 'boqList', 'View BOQ list');
INSERT INTO ACCESS_POINT (ACPT_NAME, ACPT_TOKEN,ACPT_DESC) VALUES ('View BOQ Detail', 'viewBoqDetail', 'View BOQ Detail');
INSERT INTO ACCESS_POINT (ACPT_NAME, ACPT_TOKEN,ACPT_DESC) VALUES ('Create Purchase Order', 'purchaseOrderDetail', 'Create Purchase Order');
INSERT INTO ACCESS_POINT (ACPT_NAME, ACPT_TOKEN,ACPT_DESC) VALUES ('List Purchase Orders', 'purchaseOrderList', 'View purchase order list');
INSERT INTO ACCESS_POINT (ACPT_NAME, ACPT_TOKEN,ACPT_DESC) VALUES ('Create Good Received', 'deliveryNote', 'Create Good Received');
INSERT INTO ACCESS_POINT (ACPT_NAME, ACPT_TOKEN,ACPT_DESC) VALUES ('List Good Received', 'deliveryNoteList', 'View Good Received list');
INSERT INTO ACCESS_POINT (ACPT_NAME, ACPT_TOKEN,ACPT_DESC) VALUES ('Create Sale Transaction', 'createSaleTransaction', 'Create Sale Transaction');
INSERT INTO ACCESS_POINT (ACPT_NAME, ACPT_TOKEN,ACPT_DESC) VALUES ('List Sale Transaction', 'listSaleTransaction', 'View sale transaction list');
INSERT INTO ACCESS_POINT (ACPT_NAME, ACPT_TOKEN,ACPT_DESC) VALUES ('Add User', 'addUser', 'Add new user');
INSERT INTO ACCESS_POINT (ACPT_NAME, ACPT_TOKEN,ACPT_DESC) VALUES ('List Users', 'listUser', 'View user list');
INSERT INTO ACCESS_POINT (ACPT_NAME, ACPT_TOKEN,ACPT_DESC) VALUES ('Add Role', 'addRole', 'Add new role');
INSERT INTO ACCESS_POINT (ACPT_NAME, ACPT_TOKEN,ACPT_DESC) VALUES ('List Roles', 'listRole', 'View role list');

INSERT INTO APP_USER (USR_NAME, USR_PASS, USR_FIRST_NAME, USR_SUR_NAME, USR_ACTIVE, USR_DELETED) VALUES ('Admin', 'admin', 'Administrator', 'Administrator', 1, 0 );

INSERT INTO USER_ACCESS(USR_ID, ACPT_ID) VALUES ( (select USR_ID from APP_USER WHERE USR_NAME = 'Admin'), (select ACPT_ID FROM ACCESS_POINT WHERE ACPT_TOKEN = 'createCustomer' ));
INSERT INTO USER_ACCESS(USR_ID, ACPT_ID) VALUES ( (select USR_ID from APP_USER WHERE USR_NAME = 'Admin'), (select ACPT_ID FROM ACCESS_POINT WHERE ACPT_TOKEN = 'listCustomer' ));
INSERT INTO USER_ACCESS(USR_ID, ACPT_ID) VALUES ( (select USR_ID from APP_USER WHERE USR_NAME = 'Admin'), (select ACPT_ID FROM ACCESS_POINT WHERE ACPT_TOKEN = 'createSupplier' ));
INSERT INTO USER_ACCESS(USR_ID, ACPT_ID) VALUES ( (select USR_ID from APP_USER WHERE USR_NAME = 'Admin'), (select ACPT_ID FROM ACCESS_POINT WHERE ACPT_TOKEN = 'listSupplier' ));
INSERT INTO USER_ACCESS(USR_ID, ACPT_ID) VALUES ( (select USR_ID from APP_USER WHERE USR_NAME = 'Admin'), (select ACPT_ID FROM ACCESS_POINT WHERE ACPT_TOKEN = 'uploadFile' ));
INSERT INTO USER_ACCESS(USR_ID, ACPT_ID) VALUES ( (select USR_ID from APP_USER WHERE USR_NAME = 'Admin'), (select ACPT_ID FROM ACCESS_POINT WHERE ACPT_TOKEN = 'boqList' ));
INSERT INTO USER_ACCESS(USR_ID, ACPT_ID) VALUES ( (select USR_ID from APP_USER WHERE USR_NAME = 'Admin'), (select ACPT_ID FROM ACCESS_POINT WHERE ACPT_TOKEN = 'viewBoqDetail' ));
INSERT INTO USER_ACCESS(USR_ID, ACPT_ID) VALUES ( (select USR_ID from APP_USER WHERE USR_NAME = 'Admin'), (select ACPT_ID FROM ACCESS_POINT WHERE ACPT_TOKEN = 'purchaseOrderDetail' ));
INSERT INTO USER_ACCESS(USR_ID, ACPT_ID) VALUES ( (select USR_ID from APP_USER WHERE USR_NAME = 'Admin'), (select ACPT_ID FROM ACCESS_POINT WHERE ACPT_TOKEN = 'purchaseOrderList' ));
INSERT INTO USER_ACCESS(USR_ID, ACPT_ID) VALUES ( (select USR_ID from APP_USER WHERE USR_NAME = 'Admin'), (select ACPT_ID FROM ACCESS_POINT WHERE ACPT_TOKEN = 'deliveryNote' ));
INSERT INTO USER_ACCESS(USR_ID, ACPT_ID) VALUES ( (select USR_ID from APP_USER WHERE USR_NAME = 'Admin'), (select ACPT_ID FROM ACCESS_POINT WHERE ACPT_TOKEN = 'deliveryNoteList' ));
INSERT INTO USER_ACCESS(USR_ID, ACPT_ID) VALUES ( (select USR_ID from APP_USER WHERE USR_NAME = 'Admin'), (select ACPT_ID FROM ACCESS_POINT WHERE ACPT_TOKEN = 'createSaleTransaction' ));
INSERT INTO USER_ACCESS(USR_ID, ACPT_ID) VALUES ( (select USR_ID from APP_USER WHERE USR_NAME = 'Admin'), (select ACPT_ID FROM ACCESS_POINT WHERE ACPT_TOKEN = 'listSaleTransaction' ));
INSERT INTO USER_ACCESS(USR_ID, ACPT_ID) VALUES ( (select USR_ID from APP_USER WHERE USR_NAME = 'Admin'), (select ACPT_ID FROM ACCESS_POINT WHERE ACPT_TOKEN = 'addUser' ));
INSERT INTO USER_ACCESS(USR_ID, ACPT_ID) VALUES ( (select USR_ID from APP_USER WHERE USR_NAME = 'Admin'), (select ACPT_ID FROM ACCESS_POINT WHERE ACPT_TOKEN = 'listUser' ));
INSERT INTO USER_ACCESS(USR_ID, ACPT_ID) VALUES ( (select USR_ID from APP_USER WHERE USR_NAME = 'Admin'), (select ACPT_ID FROM ACCESS_POINT WHERE ACPT_TOKEN = 'addRole' ));
INSERT INTO USER_ACCESS(USR_ID, ACPT_ID) VALUES ( (select USR_ID from APP_USER WHERE USR_NAME = 'Admin'), (select ACPT_ID FROM ACCESS_POINT WHERE ACPT_TOKEN = 'listRole' ));
INSERT INTO USER_ACCESS(USR_ID, ACPT_ID) VALUES ( (select USR_ID from APP_USER WHERE USR_NAME = 'Admin'), (select ACPT_ID FROM ACCESS_POINT WHERE ACPT_TOKEN = 'createProduct' ));
INSERT INTO USER_ACCESS(USR_ID, ACPT_ID) VALUES ( (select USR_ID from APP_USER WHERE USR_NAME = 'Admin'), (select ACPT_ID FROM ACCESS_POINT WHERE ACPT_TOKEN = 'listProduct' ));

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