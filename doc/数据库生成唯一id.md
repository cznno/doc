# 数据库生成唯一id

source: https://forums.mysql.com/read.php?24,425424,425491

## UUID/GUID

可读性高, 保证唯一

过长

## 随机数

```sql
SELECT FLOOR(rand() * 90000 + 10000); 
```

自定义长度

容易重复 (可以检查是否存在)

## MD5

- 完整MD5

```sql
SELECT MD5(NOW()); 
```

不太容易重复

过长

- 截取部分

```sql
SELECT LEFT(MD5(NOW()), 4); 
```

长度较短

容易重复

## 加密

AUTO_INCREMENT

```sql
SELECT HEX(AES_ENCRYPT(123, 'foo'));
```

保证唯一性

过长