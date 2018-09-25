# JAVA 获取被执行方法的名称

业务流程中一个方法被多个方法调用, 需要记录不同的工作流程

## getEnclosingMethod

```java
public void givenObject_whenGetEnclosingMethod_thenFindMethod() {
    String methodName = new Object() {}
      .getClass()
      .getEnclosingMethod()
      .getName();
        
    assertEquals("givenObject_whenGetEnclosingMethod_thenFindMethod",
      methodName);
}
```

### getEnclosingMethod方法

如果这个类对象表示方法内的本地或匿名类, 则返回表示基础类的直接封闭方法的Method对象. 否则返回null

## Throwable Stack Trace

```java
public void givenThrowable_whenGetStacktrace_thenFindMethod() {
    StackTraceElement[] stackTrace = new Throwable().getStackTrace();
  
    assertEquals(
      "givenThrowable_whenGetStacktrace_thenFindMethod",
      stackTrace[0].getMethodName());
}
```

可以通过遍历stackTrace来输出调用链

## Thread Stack Trace

注意, 一些虚拟机可能会跳过一到几个栈帧

```java
public void givenCurrentThread_whenGetStackTrace_thenFindMethod() {
    StackTraceElement[] stackTrace = Thread.currentThread()
      .getStackTrace();
  
    assertEquals(
      "givenCurrentThread_whenGetStackTrace_thenFindMethod",
      stackTrace[1].getMethodName()); 
}
```

## Ref

http://www.baeldung.com/java-name-of-executing-method