CREATE DATABASE [Sales] ON  PRIMARY
( NAME = N'Sales', FILENAME = N'c:\saledb\SQLDBSales.mdf' ,
  SIZE = 2GB , MAXSIZE = 8GB, FILEGROWTH = 1GB )
LOG ON
( NAME = N'Sales_log', FILENAME = N'c:\saledb\SQLDBSales_log.ldf' ,
  SIZE = 1GB , MAXSIZE = 2GB , FILEGROWTH = 10%)
GO

