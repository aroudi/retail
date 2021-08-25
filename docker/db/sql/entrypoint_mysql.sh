#!/bin/bash
wait_time=15s
password=P@55w0rd

# wait for SQL Server to come up
echo importing data will start in $wait_time...
sleep $wait_time
echo importing data...

# run the init script to create the DB and the tables in /table
mysql -u root -p $password < ./createdb_mysql.sql

for entry in "table/*.sql"
do
  echo executing $entry
  mysql -u root -p $password < $entry
done

for entry in "data/*.sql"
do
  echo executing $entry
    mysql -u root -p $password < $entry
done
