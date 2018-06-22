# 原有的单实例Tomcat项目迁移为集群

### TODO

- [x] [spring session + redis 实现分布式session](#Session)
- [x] [nginx做负载均衡](#nginx)
- [x] [业务缓存](#缓存)
- [ ] [分布式日志](#分布式日志)
- [x] [Nginx配置SSL](#Nginx配置SSL)

###FUCK

- 原有的项目不是无状态的, 需要中间件做缓存(redis应该可以满足)

## Done

### Session

quite simple

ref: [Spring Session + Redis实现分布式Session共享](https://blog.csdn.net/xiao__gui/article/details/52706243)

- 首先需要一个redis, ```summon -S redis``` and `obey my command and wake with daemonize redis-server` done! 

- add some condiments

  ```xml
  <dependency>
      <groupId>redis.clients</groupId>
      <artifactId>jedis</artifactId>
      <version>2.9.0</version>  <!--低于2.9.0会报NoSuchMethod的错-->
  </dependency>
  <dependency>
      <groupId>org.springframework.session</groupId>
      <artifactId>spring-session</artifactId>
      <version>1.3.1.RELEASE</version>
  </dependency>
  <dependency>
      <groupId>org.springframework.data</groupId>
      <artifactId>spring-data-redis</artifactId>
      <version>1.8.8.RELEASE</version>
  </dependency>
  ```

  关于报错:

  https://github.com/alibaba/jetcache/wiki/RedisWithJedis

- make the config

  ```xml
  <bean class="org.springframework.session.data.redis.config.annotation.web.http.RedisHttpSessionConfiguration"/>
      <bean id="connectionFactory" class="org.springframework.data.redis.connection.jedis.JedisConnectionFactory">
          <property name="hostName" value="${redis.host}" />
          <property name="port" value="${redis.port}" />
          <property name="poolConfig"  ref="poolConfig" />
      </bean>
  ```

  相当于, 保持和其他配置风格一致

  ```java
  @Configuration
  @EnableRedisHttpSession
  public class SessionConfig extends AbstractHttpSessionApplicationInitializer {
      @Bean
      public JedisConnectionFactory connectionFactory() {
          JedisConnectionFactory jcf = new JedisConnectionFactory();
          //....
          return jcf;
      }
  }
  ```

- Filter

  ```xml
  <filter>
      <filter-name>springSessionRepositoryFilter</filter-name>
      <filter-class>org.springframework.web.filter.DelegatingFilterProxy</filter-class>
  </filter>
  <filter-mapping>
      <filter-name>springSessionRepositoryFilter</filter-name>
      <url-pattern>/*</url-pattern>
      <dispatcher>REQUEST</dispatcher>
      <dispatcher>ERROR</dispatcher>
  </filter-mapping>
  ```

#### Tip

- 在session中存储的类要实现Serializable
- idea可以在settings -> editor -> inspections 里找到选项 `Serializable class without 'serialVersionUID'` , 打开的话可以用补全功能生成serialVersionUID
- 貌似还有一些其他关于序列化的类需要实现的方法(坑)

现在运行两个实例应该可以看到cookies里的jsessionid变成了session, 而且两个实例直接的session实现共享了

### nginx

教程一大堆..

配置`/etc/nginx/nginx.conf`

```nginx
http{
    # misc...
    upstream app{
		server      127.0.0.1:8080;
		server      127.0.0.1:8081;
	}
	server {
		listen       80;
		server_name  localhost 127.0.0.1;
		location / {
			root   /usr/share/nginx/html;
			index  index.html index.htm;
            proxy_pass http://app/;
		}
		# misc...
    }
}
```

具体还是按需求的路径配

#### 一些值得注意的

- 反代地址(proxy_pass)和upstream的配置 (注意对应关系)

#### 部署的时候发生的问题

- 配置了`listen 80;`外部无法访问

  加上外部ip `listen 192.168.1.111:80`这样就可以

  疑问:80不是应该监听所有ip吗?

- 静态文件无法访问

  在nginx中配置了`user root;`, 同时修改/www目录的权限为 777 root:root. 但仍然没有权限

  原因是**SELinux**限制了访问, 配置一下

  ```shell
  chcon -Rt httpd_sys_content_t /path/to/www
  ```

  ref: https://stackoverflow.com/questions/6795350/nginx-403-forbidden-for-all-files

- 经过代理的服务接收不到正确的请求ip

  发现`ServletRequest.getRemoteAddr()`拿到的是127.0.0.1

  - 配置nginx

    ```nginx
    location / {
        #...
        proxy_redirect     off;
        proxy_set_header   Host             $host;
        proxy_set_header   X-Real-IP        $remote_addr;
        proxy_set_header   X-Forwarded-For  $proxy_add_x_forwarded_for;
        #...
    }
    ```

  - 配置tomcat

    ```xml
    <Engine defaultHost="localhost" name="Catalina">
    	...
    	<Valve className="org.apache.catalina.valves.RemoteIpValve"
                   remoteIpHeader="X-Forwarded-For"
                   requestAttributesEnabled="true"
                   internalProxies="127\.0\.0\.1"  />
    </Engine>
    ```

  - 或者也可以通过

    `HttpServletRequest.getHeader("X-Real-IP")`获得

  - ref

    - https://medium.com/@apuravchauhan/securing-tomcat-running-behind-nginx-as-reverse-proxy-load-balancer-c2a313602669
    - https://blog.csdn.net/saindy5828/article/details/51375588
    - http://blog.51cto.com/lee90/1768284
    - https://serverfault.com/questions/514551/make-tomcat-use-x-real-ip/692604
    - https://stackoverflow.com/questions/42661710/remote-ip-in-tomcat-webapp-running-behind-nginx-proxy
    - https://www.cnblogs.com/qvennnnn/p/6245676.html

- 监听不了端口

  SELinux的问题

  ```shell
  semanage port -a -t http_port_t  -p tcp 8090
  ```

  ref: https://serverfault.com/questions/566317/nginx-no-permission-to-bind-port-8090-but-it-binds-to-80-and-8080

- 下载大文件出错

  下载服务器生成的文件打开出错, 大小和正常结果相差很大

  日志:

  `5399 open() "/var/lib/nginx/tmp/proxy/9/02/0000000029" failed (13: Permission denied) while reading upstream`

  需要修改一下文件夹的权限

  [解决nginx下载大文件出现文件损坏，文件大小不一致](https://blog.csdn.net/u010257920/article/details/50563646)

  [[Permission denied while reading upstream]](https://serverfault.com/questions/235154/permission-denied-while-reading-upstream)

  [Nginx Proxy Stops After 38 kB](https://serverfault.com/questions/771148/nginx-proxy-stops-after-38-kb)

####ref

- http://www.ttlsa.com/nginx/use-nginx-proxy/
- https://www.jianshu.com/p/bed000e1830b
- https://www.jianshu.com/p/ac8956f79206
- https://my.oschina.net/bgq365/blog/870569
- https://www.jianshu.com/p/47a94a3bff34

### 缓存

*这是非无状态应用的恶果*

1. 配置一下

   ```xml
   <bean id="redisTemplate" class="org.springframework.data.redis.core.StringRedisTemplate">
           <property name="connectionFactory"   ref="connectionFactory" />
   </bean>
   ```

   or Java

   ```java
   @Bean
   public RedisTemplate<String, Object> redisTemplate() {
       RedisTemplate<String, Object> template = new RedisTemplate<>();
       template.setConnectionFactory(jedisConnectionFactory());
       return template;
   }
   ```

2. 那么问题来了

   继承了`CrudRepository<?, ?>`的dao在调用的时候会报错

   ```
   org.apache.ibatis.binding.BindingException: Invalid bound 
   statement (not found)
   ```

   可能和`@EnableRedisRepositories`这个注解有关, 但是不知道如何在xml中配置

3. 使用`StringRedisTemplate`来实现redis操作, 替代内嵌在web应用里的存储

4. 问题:spring-data-redis 1.8.8 的delete方法没有返回值

   1. 升级成2.0.7 => 失败

   2. 原因: 

      `spring-data Kay-RELEASE (aka 2.0.0) doesn't work with Spring v4.3.X <. It only works with springframework v 5.0.0 >.`

   3. 解决:

      1. 降级
      2. 利用RedisTemplate的hasKey()方法多查询一次

5. 直接替换, 经测试基本没有问题

####备忘

- 配置文件 `/etc/redis.conf`

  ```
  daemonize yes
  logfile "/var/log/redis/redis-server.log"
  ```

- 指定配置文件启动(没有指定则所有配置不生效)

  `redis-server /etc/redis.conf`

- spring-data-redis的1.x.x和2.0.0+版本delete的实现有什么区别

### 分布式日志

方便起见log暂时输出到一个路径, 用特殊的标识区分实例

#### ~~How~~

- 不用docker: 每个实例的日志要么混在一起, 要么需要修改每个实例的配置文件

- 用docker需要解决的问题:

  - 所有日志输出到控制台,写入kafka

  // TODO

###Nginx配置SSL

#### 证书

- 把domain.key和domain.crt放到/etc/pki/nginx/下(路径无所谓)

- 读取证书启动错误

  ```
  BIO_new_file("/etc/pki/nginx/domain.crt") failed (SSL: error:0200100D:system library:fopen:Permission denied:
  fopen('/etc/pki/nginx/domain.crt','r') error:2006D002:BIO routines
  :BIO_new_file:system lib)
  ```

  可能是SELinux的原因 // TODO

  运行`restorecon -v -R /etc/pki/nginx`

  ref: https://serverfault.com/questions/540537/nginx-permission-denied-to-certificate-files-for-ssl-configuration

- nginx配置

```nginx
server{
    listen       80 default_server ;
    listen       443 default_server ssl;
    server_name domain;
    
    #ssl on; 共存的话就不开这个
    ssl_certificate /etc/pki/nginx/domain.crt;
    ssl_certificate_key /etc/pki/nginx/domain.key;
    ssl_session_timeout 10m;
    ssl_protocols TLSv1 TLSv1.1 TLSv1.2;
    ssl_ciphers ECDHE-RSA-AES128-GCM-SHA256:HIGH:!aNULL:!MD5:!RC4:!DHE;
    ssl_prefer_server_ciphers on;  
    #...
}
# or redirect
server {
    listen    80;	
    server_name domain.com;
    return    301 https://$server_name$request_uri;
}
```



ref:

- https://serverfault.com/questions/10854/nginx-https-serving-with-same-config-as-http
- https://www.cnblogs.com/phpper/p/6441475.html

