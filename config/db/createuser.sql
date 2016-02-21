-- Create user for SQL Authentication
CREATE LOGIN sale WITH PASSWORD = 'Kchsdk10';
GO
-- Add User to first database
USE Sales;

IF NOT EXISTS (SELECT * FROM sys.database_principals WHERE name = N'sale')
BEGIN
    CREATE USER [sale] FOR LOGIN [sale]
    EXEC sp_addrolemember N'db_owner', N'sale'
    EXEC sp_addrolemember 'db_datareader', 'sale'
    EXEC sp_addrolemember 'db_datawriter', 'sale'
END;
GO

