USE [Sales]
GO

CREATE TABLE [dbo].[Product]
(
 [ProductId] [uniqueidentifier] DEFAULT NEWID() NOT NULL,
 [ProductName] [nchar](50) NULL,
 [ProductDescription] [nchar](3000) NULL,
 [ProductPrice] MONEY NULL
) ON [PRIMARY]
GO