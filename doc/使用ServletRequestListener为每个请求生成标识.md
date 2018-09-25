# 使用ServletRequestListener为每个请求生成标识

```java
public class CustomServletRequestListener implements ServletRequestListener {
    @Override
    public void requestDestroyed(ServletRequestEvent sre) {
        MDC.clear();
    }

    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        MDC.put("RequestId", UUID.randomUUID().toString());
    }
}
```

```xml
<listener>
    <description>ServletRequestListener</description>
    <listener-class>me.cznno.CustomServletRequestListener</listener-class>
</listener>
```

```properties
log4j.appender.error.layout.ConversionPattern=[%-5p] [%d{HH:mm:ss,SSS}] %X{RequestId} %t %c - %m%n
```