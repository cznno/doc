## 查看count all line in schema
https://stackoverflow.com/questions/286039/get-record-counts-for-all-tables-in-mysql-database
``` sql
SELECT SUM(TABLE_ROWS)
FROM INFORMATION_SCHEMA.TABLES
WHERE TABLE_SCHEMA = 'tablename';
```
