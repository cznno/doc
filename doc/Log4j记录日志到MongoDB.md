# Log4j记录日志到MongoDB

##从Log4j 1.2迁移到2.x

### Why

https://stackoverflow.com/questions/16386975/are-there-any-major-differences-between-log4j-1-2-and-log4j-2-0

https://www.infoq.com/news/2014/07/apache-log4j2

### How

- use maven

#### 原来的pom.xml

```xml
<dependency>
    <groupId>log4j</groupId>
    <artifactId>log4j</artifactId>
    <version>1.2.17</version>
</dependency>
<dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>slf4j-api</artifactId>
    <version>1.7.7</version>
</dependency>
<dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>slf4j-log4j12</artifactId>
    <version>1.7.7</version>
</dependency>
```

#### 新的pom.xml

```xml
<dependency>
    <groupId>org.apache.logging.log4j</groupId>
    <artifactId>log4j-api</artifactId>
    <version>2.9.1</version>
</dependency>
<dependency>
    <groupId>org.apache.logging.log4j</groupId>
    <artifactId>log4j-core</artifactId>
    <version>2.9.1</version>
</dependency>
<dependency>
    <groupId>org.apache.logging.log4j</groupId>
    <artifactId>log4j-web</artifactId>
    <version>2.9.1</version>
</dependency>
<dependency>
    <groupId>org.apache.logging.log4j</groupId>
    <artifactId>log4j-slf4j-impl</artifactId>
    <version>2.9.1</version>
</dependency>
<dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>jcl-over-slf4j</artifactId>
    <version>1.7.13</version>
</dependency>
<dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>slf4j-api</artifactId>
    <version>1.7.25</version>
</dependency>
```

#### 一些说明

- 移除了slf4j-log4j12, 这个是SLF4J用来绑定Log4j 1.2的 [来源:stackoverflow](https://stackoverflow.com/questions/31044619/difference-between-slf4j-log4j12-and-log4j-over-slf4j)

```xml
 <groupId>org.slf4j</groupId>
 <artifactId>slf4j-log4j12</artifactId>
```

- log4j-web是用来支持web servlet container的
- log4j-slf4j-impl: SLF4J API binding to Log4j 2 Core
- jcl-over-slf4j: JCL(Jakarta Commons Logging) 1.2 implemented over SLF4J. 连接commons-logging和slf4j (似乎没什么用) [来源1](https://blog.csdn.net/fenglixiong123/article/details/79162484) [来源2](https://www.slf4j.org/legacy.html)

#### 自定义配置文件路径

在web.xml

```xml
1 <context-param>  
2   <param-name>log4jConfiguration</param-name>  
3   <param-value>classpath:conf/log4j2.xml</param-value>  
4 </context-param>
```

注意: 在log4j 1.x里是这样的,参数名不同.

```xml
<context-param>
  <param-name>log4jConfigLocation</param-name>
  <param-value>classpath:conf/log4j.properties</param-value>
</context-param>
```

servlet 2.5的配置有些不同

https://logging.apache.org/log4j/2.x/manual/webapp.html

配置文件的内容到处都有

https://logging.apache.org/log4j/2.x/manual/configuration.html

####网上有些奇怪的lib(不知道为什么要用这个, 似乎没什么用)

```xml
<!-- SLF4J Simple binding -->
<dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>slf4j-simple</artifactId>
    <version>1.7.7</version>
</dependency>
```

### 日志输出到MongoDB

省去mongodb的配置

####依赖

```xml
<dependency>
    <groupId>org.apache.logging.log4j</groupId>
    <artifactId>log4j-nosql</artifactId>
    <version>2.9.1</version>
</dependency>
<dependency>
    <groupId>org.mongodb</groupId>
    <artifactId>mongo-java-driver</artifactId>
    <version>3.7.1</version>
</dependency>
<dependency>
    <groupId>org.mongodb</groupId>
    <artifactId>mongodb-driver</artifactId>
    <version>3.7.1</version>
</dependency>
<!--<dependency>-->
<!--<groupId>org.springframework.data</groupId>-->
<!--<artifactId>spring-data-mongodb-log4j</artifactId>-->
<!--<version>1.10.13.RELEASE</version>-->
<!--</dependency>-->
```

log4j2.xml

```xml
<Appenders>
    <NoSql name="databaseAppender">
   <MongoDb databaseName="logs" collectionName="log4j2"
            server="127.0.0.1" port="27017" username = "username" password =              "password" />
</NoSql>
</Appenders>
<!-- 省略其他 -->
```

虽然官网用了

```xml
<NoSql name="databaseAppender">
    <MongoDb3 databaseName="applicationDb" collectionName="applicationLog" server="mongo.example.org"
               username="loggingUser" password="abc123" />
</NoSql>
```

但是会提示element错误

这样基本上就可以了

# Ref

[Log4j 2 configuration depending on environment](https://blog.oio.de/2015/04/27/log4j-2-configuration-depending-environment/)

[使用Slf4j集成Log4j2构建项目日志系统的完美解决方案](https://www.cnblogs.com/hafiz/p/6160298.html)

###misc

[SLF4J简介与使用(整合log4j)](https://blog.csdn.net/jiapengcs/article/details/73359918)

[log4j2 实际使用详解](https://blog.csdn.net/vbirdbest/article/details/71751835)

[详解log4j2(上) - 从基础到实战](https://www.cnblogs.com/sa-dan/p/6837225.html)

[使用Log4j2打印Mybatis SQL语句以及结果集](https://blog.csdn.net/z69183787/article/details/52925567)

[Log4j自定义Appender(二)](http://lucien-zzy.iteye.com/blog/2285967)

[log4j2 config file is not being recognized](https://stackoverflow.com/questions/22107849/log4j2-config-file-is-not-being-recognized)

[Log4j2 configuration - No log4j2 configuration file found](https://stackoverflow.com/questions/25487116/log4j2-configuration-no-log4j2-configuration-file-found)

[Spring MVC using Log4j Example](https://examples.javacodegeeks.com/enterprise-java/spring/mvc/spring-mvc-using-log4j-example/)

[[log4j2 的几种简单配置,集成配置MongoDB](https://www.cnblogs.com/demon-kingdom/articles/4898073.html)](http://www.cnblogs.com/demon-kingdom/p/4898073.html)

[玩转mongodb（九）：通过log4jmongo来实现分布式系统的日志统一管理](https://www.cnblogs.com/zhouqinxiong/p/6822260.html)

[SLF4J with Log4j2 example](https://www.logicbig.com/tutorials/misc/java-logging/slf4j-with-log4j2.html)