USE RETAIL
GO

INSERT INTO CUSTOMER_GRADE(GRADE_CODE, GRADE_NAME, DESCRIPTION, RATE) VALUES ('A', 'COST + 20.00%', 'COST + 20.00%','20.00')
GO

INSERT INTO CUSTOMER_GRADE(GRADE_CODE, GRADE_NAME, DESCRIPTION, RATE) VALUES ('B', 'COST + 23.00%', 'COST + 23.00%','23.00')
GO

INSERT INTO CUSTOMER_GRADE(GRADE_CODE, GRADE_NAME, DESCRIPTION, RATE) VALUES ('C', 'COST + 28.00%', 'COST + 28.00%','28.00')
GO

INSERT INTO CUSTOMER_GRADE(GRADE_CODE, GRADE_NAME, DESCRIPTION, RATE) VALUES ('D', 'COST + 38.00%', 'COST + 38.00%','38.00')
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

INSERT INTO TAX_RULE (TXRL_APPLY_SEQ, TXRL_CUM_TAX, TXRL_CODE,TXRL_DESC, TXRL_ROUNDING_MTHD, TXRN_ID, TXRL_DATE_FROM, TXRL_DATEa_TO ) VALUES (0, 0, 'E', '10% GST Rate', 2, (SELECT TXRN_ID FROM TAX_RULE_NAME WHERE TXRN_CODE='GST'), '1900-01-01 12:00:00', '2100-01-01 12:00:00')
GO

INSERT INTO TAX_RULE (TXRL_APPLY_SEQ, TXRL_CUM_TAX, TXRL_CODE,TXRL_DESC, TXRL_ROUNDING_MTHD, TXRN_ID, TXRL_DATE_FROM, TXRL_DATE_TO ) VALUES (0, 0, 'A', '0% GST Rate', 2, (SELECT TXRN_ID FROM TAX_RULE_NAME WHERE TXRN_CODE='GST'), '1900-01-01 12:00:00', '2100-01-01 12:00:00')
GO


INSERT INTO TAX_LEG_VARIANCE(TXLV_RATE, TXLV_DATETIME_FROM, TXLV_DATETIME_TO, TXLV_CODE, TXLV_DESC, TXLV_IS_ACTIVE,TXRL_ID) VALUES (10, '2000-01-01 12:00:00','2100-01-01 12:00:00', 'GST_10','10% GST Rate',1, (SELECT TXRL_ID FROM TAX_RULE WHERE TXRL_CODE='E')  );
GO

INSERT INTO TAX_LEG_VARIANCE(TXLV_RATE, TXLV_DATETIME_FROM, TXLV_DATETIME_TO, TXLV_CODE, TXLV_DESC, TXLV_IS_ACTIVE,TXRL_ID) VALUES (0, '2000-01-01 12:00:00','2100-01-01 12:00:00', 'GST_0','0% GST Rate',0, (SELECT TXRL_ID FROM TAX_RULE WHERE TXRL_CODE='A')  );
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


--TRANSACTION STATE
INSERT INTO CONFIG_TYPE(CONFIG_CODE, DISPLAY_NAME, DESCRIPTION) VALUES ('TXN_STATE', 'TXN_STATE', 'TRANSACTION STATE');
GO

INSERT INTO CONFIG_CATEGORY(CONFIG_TYPE_ID, CATEGORY_CODE, DISPLAY_NAME, DESCRIPTION) VALUES ((SELECT CONFIG_TYPE_ID FROM CONFIG_TYPE WHERE CONFIG_CODE='TXN_STATE'),'TXN_STATE_DRAFT', 'DRAFT', 'DRAFT');
GO
INSERT INTO CONFIG_CATEGORY(CONFIG_TYPE_ID, CATEGORY_CODE, DISPLAY_NAME, DESCRIPTION) VALUES ((SELECT CONFIG_TYPE_ID FROM CONFIG_TYPE WHERE CONFIG_CODE='TXN_STATE'),'TXN_STATE_VOID', 'VOID', 'VOID');
GO
INSERT INTO CONFIG_CATEGORY(CONFIG_TYPE_ID, CATEGORY_CODE, DISPLAY_NAME, DESCRIPTION) VALUES ((SELECT CONFIG_TYPE_ID FROM CONFIG_TYPE WHERE CONFIG_CODE='TXN_STATE'),'TXN_STATE_SUSPEND', 'SUSPEND', 'VOID');
GO
INSERT INTO CONFIG_CATEGORY(CONFIG_TYPE_ID, CATEGORY_CODE, DISPLAY_NAME, DESCRIPTION) VALUES ((SELECT CONFIG_TYPE_ID FROM CONFIG_TYPE WHERE CONFIG_CODE='TXN_STATE'),'TXN_STATE_FINAL', 'FINAL', 'VOID');
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

