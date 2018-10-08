# Tomcat Architecture

## 组件

via [Apache Tomcat 9 Architecture Version 9.0.12, Sep 4 2018](http://tomcat.apache.org/tomcat-9.0-doc/architecture/overview.html)

### Server

Server代表了整个container，tomcat提供了Server接口的默认实现（StandardServer？）

http://tomcat.apache.org/tomcat-9.0-doc/api/org/apache/catalina/Server.html

### Service

Service作为一个中间件存在于Server内，连接一个或多个Connector到一个Engine上。

http://tomcat.apache.org/tomcat-9.0-doc/api/org/apache/catalina/Service.html

### Engine

Engine为Service提供Request的处理管道。由于一个Service可能有多个Connector，Engine接收并处理所有来自这些Connector的请求，同时将Response返回给合适的Connector以传输给客户端。

Engine可以通过jvmRoute参数被Tomcat服务器集群使用。

### Host

Host关联起一个Server和一个网络名称（比如www.yourcompany.com）。一个Engine可以有多个Host。Host支持诸如yourcompany.com 和 abc.yourcompany.com等网络别名。[StandardHost](http://tomcat.apache.org/tomcat-9.0-doc/api/org/apache/catalina/core/StandardHost.html)类提供了大多数的功能。

### Connector

Connector提供与客户端的通讯。Tomcat提供了多种connector的实现：

- [HTTP connector](http://tomcat.apache.org/tomcat-9.0-doc/config/http.html)提供了大多数情况下的HTTP流量支持，尤其是在Tomcat运行在独立服务器的情况下
-  [AJP connector](http://tomcat.apache.org/tomcat-9.0-doc/config/ajp.html)实现了AJP协议，可以用在Tomcat连接到一台web服务器的时候（比如Apache HTTPD server）。

### Context

Context表示一个web应用。一个Host可以有多个context，每个context配置有一个独立的路径。

http://tomcat.apache.org/tomcat-9.0-doc/api/org/apache/catalina/core/StandardContext.html
