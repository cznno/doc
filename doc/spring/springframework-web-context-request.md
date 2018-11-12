# springframework.web.context.request

## RequestContextHolder

以线程绑定[RequestAttributes](https://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/web/context/request/RequestAttributes.html)的形式暴露web request的容器类。对象的形式公开Web请求 。如果inheritable设置为true，则当前线程生成的任何子线程将继承该request。