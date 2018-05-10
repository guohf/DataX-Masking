# DataX-Masking

DataX-Masking 是在 [DataX 3.0](https://github.com/alibaba/DataX/) 基础上二次开发得到的**大数据脱敏平台**，可以快速地在数据传输过程中对指定的单个或多个字段用可选的脱敏方法进行处理。

一般主要运行在linux系统。

# Features

DataX本身作为数据同步框架，将不同数据源的同步抽象为从源头数据源读取数据的Reader插件，以及向目标端写入数据的Writer插件，理论上DataX框架可以支持任意数据源类型的数据同步工作。同时DataX插件体系作为一套生态系统, 每接入一套新数据源该新加入的数据源即可实现和现有的数据源互通。

DataX-Masking 通过扩展DataX的transformer中间件，集成了多种脱敏算法。

#### DataX使用手册：[DataX-Introduction](datax-masking-user-guide.md)

## 支持的脱敏方法

平台中的脱敏方法可以分为两类，一种是常用的脱敏方法，这种方法计算开销比较小；另一种是加密方法，这种方法计算开销较大，一般而言用时较久。

|脱敏方法名称|描述|示例|
|---|---|---|
|Hiding|将数据置为常量，一般用于处理不需要的敏感字段。|500 ->0<br>false->true|
|Floor|对整数或浮点数或者日期向下取整。|-12.68->-12<br>12580->12000<br>2018-05-10 10:17->2018-05-01 6:00|
|Enumerate|将数字映射为新值，同时保持数据的大小顺序。|500->1500 600->1860 700->2000|
|Prefix Preserve|保持前n位不变，混淆其余部分。可针对字母和数字字符在同为字母或数字范围内进行混淆，特殊符号将保留。|10.199.90.105->10.199.38.154<br>18965432100->18985214789|
|MD5|不可逆的hash摘要方法。将不定长的数据映射成定长的数据(长度为32的字符串)。|你好世界！->4f025928d787aa7b73beb58c1a85b11d|
|EDP|Epsilon Differential Privacy | 17.5 -> 17.962 |
|AES|AES-128-CBC 对称加密|你好世界！-> 12da3fedd5f0992447b1c7b4af0d7133|
| FPE | format Preserving Encryption | abcdefg -> iskejtl |
| RSA | RSA 非对称密钥加密算法 | 加密：明文->长度为256字串(1024位二进制整数的16进制表示法)<br>解密：加密后的字串->明文 | 


## Support Data Channels 

DataX目前已经有了比较全面的插件体系，主流的RDBMS数据库、NOSQL、大数据计算系统都已经接入，目前支持数据如下图，详情请点击：[DataX数据源参考指南](https://github.com/alibaba/DataX/wiki/DataX-all-data-channels)

| 类型           | 数据源        | Reader(读) | Writer(写) |文档|
| ------------ | ---------- | :-------: | :-------: |:-------: |
| RDBMS 关系型数据库 | MySQL      |     √     |     √     |[读](https://github.com/alibaba/DataX/blob/master/mysqlreader/doc/mysqlreader.md) 、[写](https://github.com/alibaba/DataX/blob/master/mysqlwriter/doc/mysqlwriter.md)|
|              | Oracle     |     √     |     √     |[读](https://github.com/alibaba/DataX/blob/master/oraclereader/doc/oraclereader.md) 、[写](https://github.com/alibaba/DataX/blob/master/oraclewriter/doc/oraclewriter.md)|
|              | SQLServer  |     √     |     √     |[读](https://github.com/alibaba/DataX/blob/master/sqlserverreader/doc/sqlserverreader.md) 、[写](https://github.com/alibaba/DataX/blob/master/sqlserverwriter/doc/sqlserverwriter.md)|
|              | PostgreSQL |     √     |     √     |[读](https://github.com/alibaba/DataX/blob/master/postgresqlreader/doc/postgresqlreader.md) 、[写](https://github.com/alibaba/DataX/blob/master/postgresqlwriter/doc/postgresqlwriter.md)|
|              | DRDS |     √     |     √     |[读](https://github.com/alibaba/DataX/blob/master/drdsreader/doc/drdsreader.md) 、[写](https://github.com/alibaba/DataX/blob/master/drdswriter/doc/drdswriter.md)|
|              | 达梦         |     √     |     √     |[读]() 、[写]()|
|              | 通用RDBMS(支持所有关系型数据库)         |     √     |     √     |[读]() 、[写]()|
| 阿里云数仓数据存储    | ODPS       |     √     |     √     |[读](https://github.com/alibaba/DataX/blob/master/odpsreader/doc/odpsreader.md) 、[写](https://github.com/alibaba/DataX/blob/master/odpswriter/doc/odpswriter.md)|
|              | ADS        |           |     √     |[写](https://github.com/alibaba/DataX/blob/master/adswriter/doc/adswriter.md)|
|              | OSS        |     √     |     √     |[读](https://github.com/alibaba/DataX/blob/master/ossreader/doc/ossreader.md) 、[写](https://github.com/alibaba/DataX/blob/master/osswriter/doc/osswriter.md)|
|              | OCS        |     √     |     √     |[读](https://github.com/alibaba/DataX/blob/master/ocsreader/doc/ocsreader.md) 、[写](https://github.com/alibaba/DataX/blob/master/ocswriter/doc/ocswriter.md)|
| NoSQL数据存储    | OTS        |     √     |     √     |[读](https://github.com/alibaba/DataX/blob/master/otsreader/doc/otsreader.md) 、[写](https://github.com/alibaba/DataX/blob/master/otswriter/doc/otswriter.md)|
|              | Hbase0.94  |     √     |     √     |[读](https://github.com/alibaba/DataX/blob/master/hbase094xreader/doc/hbase094xreader.md) 、[写](https://github.com/alibaba/DataX/blob/master/hbase094xwriter/doc/hbase094xwriter.md)|
|              | Hbase1.1   |     √     |     √     |[读](https://github.com/alibaba/DataX/blob/master/hbase11xreader/doc/hbase11xreader.md) 、[写](https://github.com/alibaba/DataX/blob/master/hbase11xwriter/doc/hbase11xwriter.md)|
|              | MongoDB    |     √     |     √     |[读](https://github.com/alibaba/DataX/blob/master/mongoreader/doc/mongoreader.md) 、[写](https://github.com/alibaba/DataX/blob/master/mongowriter/doc/mongowriter.md)|
|              | Hive       |     √     |     √     |[读](https://github.com/alibaba/DataX/blob/master/hdfsreader/doc/hdfsreader.md) 、[写](https://github.com/alibaba/DataX/blob/master/hdfswriter/doc/hdfswriter.md)|
| 无结构化数据存储     | TxtFile    |     √     |     √     |[读](https://github.com/alibaba/DataX/blob/master/txtfilereader/doc/txtfilereader.md) 、[写](https://github.com/alibaba/DataX/blob/master/txtfilewriter/doc/txtfilewriter.md)|
|              | FTP        |     √     |     √     |[读](https://github.com/alibaba/DataX/blob/master/ftpreader/doc/ftpreader.md) 、[写](https://github.com/alibaba/DataX/blob/master/ftpwriter/doc/ftpwriter.md)|
|              | HDFS       |     √     |     √     |[读](https://github.com/alibaba/DataX/blob/master/hdfsreader/doc/hdfsreader.md) 、[写](https://github.com/alibaba/DataX/blob/master/hdfswriter/doc/hdfswriter.md)|
|              | Elasticsearch       |         |     √     |[写](https://github.com/alibaba/DataX/blob/master/elasticsearchwriter/doc/elasticsearchwriter.md)|

## DataX-Masking Contributor
* Liu Kun
* Liu Wenyan
* Wang Hao
* Liu Jiaye

## 我要开发新的插件
请点击：[DataX transformer插件开发](https://blog.csdn.net/landstream/article/details/79933800)

## License

This software is free to use under the Apache License [Apache license](https://github.com/alibaba/DataX/blob/master/license.txt).
