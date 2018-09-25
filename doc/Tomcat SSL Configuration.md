# Tomcat SSL Configuration

- 证书从腾讯云申请
- 基本配置参考
  - https://cloud.tencent.com/document/product/400/4143
  - https://tomcat.apache.org/tomcat-8.5-doc/ssl-howto.html

## server.xml的配置

- 腾讯云给出了以下的参考

  ```xml
  <Connector port="443" protocol="HTTP/1.1" SSLEnabled="true"
      maxThreads="150" scheme="https" secure="true"
      keystoreFile="conf/www.domain.com.jks"
      keystorePass="changeit"
      clientAuth="false" sslProtocol="TLS" />
  ```

  [查看一下各个tag的含义](https://tomcat.apache.org/tomcat-8.5-doc/config/http.html)

  |                |                                                              |
  | -------------- | ------------------------------------------------------------ |
  | maxThreads     | **Connector**创建的最大请求处理线程数, 即最大并发数          |
  | scheme         | 设置request.getScheme()返回的协议的名称, 默认值是“http”      |
  | secure         | 调用request.isSecure()的返回值, 默认值是false。              |
  | keystoreFile | 指定keystore文件的存放位置.  可以使用URL, 绝对路径或相对于路径. 默认情况下, Tomcat将从当前操作系统用户的用户目录下读取名为 “.keystore”的文件. |
  | keystorePass | 密钥库密码，指定keystore的密码                               |
  | SSLEnabled   | 设置为true使connector启用SSL通信(握手/加密/解密). 设置scheme和secure传递正确的值到servlet |
  | clientAuth   | 如果设为true，表示Tomcat要求所有的SSL客户出示安全证书，对SSL客户进行身份验证<br\>Set to true if you want the SSL stack to require a valid certificate chain from the client before accepting a connection. Set to want if you want the SSL stack to request a client Certificate, but not fail if one isn't presented. A false value (which is the default) will not require a certificate chain unless the client requests a resource protected by a security constraint that uses CLIENT-CERT authentication. |
  | sslProtocol  | 仅限JSSE。要使用的SSL协议（单个值可以启用多个协议 - 有关详细信息，请参阅JVM文档）。 如果未指定，则默认为TLS。 在创建SSLContext实例时，可以从JVM文档获得允许的值以获得算法的允许值，例如， Oracle Java 7.注意：此属性和协议之间有重叠。 |

## APP的配置

- [简单配置](https://dzone.com/articles/setting-ssl-tomcat-5-minutes):

```xml
<security-constraint>
    <web-resource-collection>
        <web-resource-name>securedapp</web-resource-name>
        <url-pattern>/*</url-pattern>
    </web-resource-collection>
    <user-data-constraint>
        <transport-guarantee>CONFIDENTIAL</transport-guarantee>
    </user-data-constraint>
</security-constraint>
```

- [tag的含义](http://wiki.metawerx.net/wiki/ForcingSSLForSectionsOfYourWebsite)

|                         |                                                              |
| ----------------------- | ------------------------------------------------------------ |
| security-constraint     | 定义应用程序的安全区域，可能是整个站点/应用程序，或仅限于特定区域 |
| web-resource-collection | 定义要保护的资源，需要配置名称和URL                          |
| web-resource-name       | web-resource-collection的名称                                |
| url-pattern             | web-resource-collection的URL, /*匹配所有                     |
| user-data-constraint    | 定义用于此受保护资源的数据保护的元素                         |
| transport-guarantee     | 指定了所需的数据保护的确切级别 (NONE, INTEGRAL or CONFIDENTIAL) |

### transport-guarantee的值

|              |                                                              |
| ------------ | ------------------------------------------------------------ |
| NONE         | 不指定安全传输的方式(在不设置user-data-constraint时为默认值) |
| INTEGRAL     | 数据必须以确保传输过程中无法更改的方式发送(即数据会验证校验和, SSL有实现) |
| CONFIDENTIAL | 数据必须以确保在传输过程中无法观察（或更改）的方式发送(数据被加密, SSL实现) |

## 例子

ref:

http://wiki.metawerx.net/wiki/ForcingSSLForSectionsOfYourWebsite

### 整个站点

```xml
<!-- Force SSL for entire site -->
<security-constraint>
	<web-resource-collection>
		<web-resource-name>Entire Application</web-resource-name>
		<url-pattern>/*</url-pattern>
	</web-resource-collection>
	<user-data-constraint>
		<transport-guarantee>CONFIDENTIAL</transport-guarantee>
	</user-data-constraint>
</security-constraint>
```

### 某个目录

```xml
<!-- Force SSL for entire site -->
<security-constraint>
	<web-resource-collection>
		<web-resource-name>Members Folder</web-resource-name>
		<url-pattern>/members/*</url-pattern>
	</web-resource-collection>
	<user-data-constraint>
		<transport-guarantee>CONFIDENTIAL</transport-guarantee>
	</user-data-constraint>
</security-constraint>
```

### 整个站点, 但是忽略一部分

```xml
<!-- Don't force SSL on notify.jsp -->
<security-constraint>
	<web-resource-collection>
		<web-resource-name>Notify page, accessed internally by application</web-resource-name>
		<url-pattern>/notify.jsp</url-pattern>
	</web-resource-collection>
	<user-data-constraint>
		<transport-guarantee>NONE</transport-guarantee>
	</user-data-constraint>
</security-constraint>

<!-- Force SSL for entire site -->
<security-constraint>
	<web-resource-collection>
		<web-resource-name>Entire Site</web-resource-name>
		<url-pattern>/*</url-pattern>
	</web-resource-collection>
	<user-data-constraint>
		<transport-guarantee>CONFIDENTIAL</transport-guarantee>
	</user-data-constraint>
</security-constraint>
```

### 强制使用301重定向

- 在默认情况下security-constraint会让容器通过302状态码(临时)重定向
- 301状态码([Moved Permanently](https://zh.wikipedia.org/wiki/HTTP_301))可以更好地适应SEO优化. 这时客户端将被告知该页面被永久转移, 且不要重复检查http版本(?)
- 从Tomcat 7.0.70, 8.0.37, 8.5.4之后，添加了新的属性transportGuaranteeRedirectStatus可用于更改状态码。 在大多数情况下, 您的顶级Realm应该是LockOutRealm。 如果嵌套了Realms，则需要将其添加到相关的Realm声明中。 如果没有声明现有的Realm，则默认使用NullRealm。 在这种情况下，需要显式声明NullRealm以添加属性，或者使用默认的LockOutRealm，如下例所示。

#### 在server.xml里配置

```xml
<Realm className="org.apache.catalina.realm.LockOutRealm" transportGuaranteeRedirectStatus="301">
    <!-- This Realm uses the UserDatabase configured in the global JNDI
         resources under the key "UserDatabase".  Any edits
         that are performed against this UserDatabase are immediately
         available for use by the Realm.  -->
    <Realm className="org.apache.catalina.realm.UserDatabaseRealm"
           resourceName="UserDatabase" />
</Realm>
```
## Other Reference

[Specifying Security Constraints](https://docs.oracle.com/cd/E19226-01/820-7627/bncbk/index.html)

[Tomcat处理HTTP请求 详解Connector模块](http://tech.it168.com/a2011/1220/1291/000001291047_all.shtml)

[tomcat中server.xml配置详解](https://blog.csdn.net/zcyhappy1314/article/details/10356909)