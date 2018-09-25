# ClassLoader

[TOC]

##[JDK10](https://docs.oracle.com/javase/10/docs/api/java/lang/ClassLoader.html)

类加载器是一个负责加载类的对象。ClassLoader是一个抽象类。给定类的二进制名称，类加载器应尝试定位或生成构成类定义的数据。典型的策略是将名称转换为文件名，然后从文件系统中读取该名称的“类文件”。

每个Class对象都包含一个ClassLoader定义该Class的引用，getClassLoader()。

数组类的对象不是由类加载器创建的，而是根据Java runtime的需要自动创建的。返回的类加载器与其元素类型的类加载器相同; 如果元素类型是基本类型，则数组类没有类加载器。

应用实现ClassLoader的子类，以便扩展Java虚拟机动态加载类的方式。

安全管理器通常可以使用类加载器来指示安全域。

除了加载类之外，类加载器还负责定位资源。资源是一些数据（例如“ `.class`文件，配置数据或图像”），用抽象的“/” - 分隔的路径名标识。资源通常与应用程序或库打包在一起，以便可以通过应用程序或库中的代码来定位它们。在某些情况下，会包含资源，以便其他库可以找到它们。

**ClassLoader使用委托模型来搜索类和资源。每个ClassLoader的实例都有一个关联的父类加载器。当请求查找类或资源时，ClassLoader实例通常会在尝试查找类或资源本身之前将对类或资源的搜索委托给其父类加载器。**

支持并发装载类的ClassLoader被称为 parallel capable class loaders，并且需要通过调用ClassLoader.registerAsParallelCapable方法在类初始化的时候注册自己。请注意，默认情况下，ClassLoader已是接受并行的。但是，如果它们接受并行，它的子类仍然需要注册自己。在委托模型不是严格分层的环境中，类加载器需要具有并行能力，否则类加载会导致死锁，因为加载器锁在类加载过程的持续时间内保持（参见[`loadClass`](https://docs.oracle.com/javase/9/docs/api/java/lang/ClassLoader.html#loadClass-java.lang.String-)方法）。

### 运行时内置类加载器

Java运行时具有以下内置类加载器：

- **Bootstrap class loader。它是虚拟机的内置类加载器，通常表示为`null`，并且没有父级。**

- **Platform class loader。所有平台类对平台类加载器都是可见的，可以用作`ClassLoader`实例的父级。平台类包括Java SE Platform API、它们的实现类以及由平台类加载器或其祖先定义的，特定于JDK的运行时类。**

  为了允许升级/覆盖定义到平台类加载器的模块，并且升级后的模块读取定义到除平台类加载器及其祖先之外的类加载器的模块，那么平台类加载器可能必须委托给其他类加载器，例如，应用程序类加载器 换句话说，平台类加载器可以看到定义到除平台类加载器及其祖先之外的类加载器的命名模块中的类。

- **System class loader。它也称为application class loader，与平台类加载器不同。系统类加载器通常用于在应用程序类路径，模块路径和JDK特定工具上定义类。**平台类加载器是系统类加载器的父级或祖先，所有平台类都是可见的。

通常，Java虚拟机以与平台相关的方式从本地文件系统加载类。但是，某些类可能不是源自文件; 它们可能来自其他来源，例如网络，或者它们可以由应用程序构建。该方法[`defineClass`](https://docs.oracle.com/javase/9/docs/api/java/lang/ClassLoader.html#defineClass-java.lang.String-byte:A-int-int-)将字节数组转换为类的实例 `Class`。可以使用创建此新定义的类的实例 [`Class.newInstance`](https://docs.oracle.com/javase/9/docs/api/java/lang/Class.html#newInstance--)。

由类加载器创建的对象的方法和构造函数可以引用其他类。要确定所引用的类，Java虚拟机将调用[`loadClass`](https://docs.oracle.com/javase/9/docs/api/java/lang/ClassLoader.html#loadClass-java.lang.String-)最初创建该类的类加载器的方法。

例如，应用程序可以创建网络类加载器以从服务器下载类文件。示例代码可能如下所示：

> ```
>    ClassLoader loader = new NetworkClassLoader（host，port）;
>    Object main = loader.loadClass（“Main”，true）.newInstance（）;
>         。。。
>  
> ```

网络类加载器子类必须定义方法[`findClass`](https://docs.oracle.com/javase/9/docs/api/java/lang/ClassLoader.html#findClass-java.lang.String-)并`loadClassData`从网络加载类。一旦下载了构成类的字节，它就应该使用该方法[`defineClass`](https://docs.oracle.com/javase/9/docs/api/java/lang/ClassLoader.html#defineClass-byte:A-int-int-)来创建一个类实例。示例实现是：

> ```
>      class NetworkClassLoader扩展ClassLoader {
>          字符串主机;
>          int port;
> 
>          public class findClass（String name）{
>              byte [] b = loadClassData（name）;
>              return defineClass（name，b，0，b.length）;
>          }
> 
>          private byte [] loadClassData（String name）{
>              //从连接加载类数据
>               。。。
>          }
>      }
>  
> ```

### 二进制名称

作为`String`方法参数 提供的任何类名`ClassLoader`必须是The Java™Language Specification定义的二进制名 。

有效类名的示例包括：

> ```
>    “java.lang.String中”
>    “javax.swing.JSpinner中的$ DefaultEditor”
>    “java.security.KeyStore中的$ $生成器$ FileBuilder 1”
>    “java.net.URLClassLoader的$ 3 $ 1”
>  
> ```

作为`String`方法参数 提供的任何包名称`ClassLoader`必须是空字符串（表示未命名的包）或由Java™语言规范定义的完全限定名称 。

## 可能是JDK9之前的实现？

- Class loader是分为：Bootstrap Class Loader，Ext Class Loader和App Class Loader（？）
- BootstrapClassLoader不是ClassLoader的子类，负责装载JRE核心类库
- ExtClassLoader和AppClassLoader是ClassLoader的子类，Ext负责JRE扩展目录ext中的类包，App负责Classpath下的类包
-  委托机制：先从父装载器寻找目标类，找不到才由自己加载

## 工作机制

1. 装载：查找和导入Class文件，由ClassLoader及其之类负责
2. 链接
   1. 校验载入Class文件的正确性
   2. 给静态变量分配存储空间
   3. 将符号引用转换为直接应用
3. 初始化：对静态变量、静态代码块执行初始化工作

## 重要方法

- Class loadClass
- Class defineClass
- Class findSystemClass
- ClassLoader getParent