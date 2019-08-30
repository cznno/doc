# Java数据校验

## 标准

- JSR 303

  https://www.ibm.com/developerworks/cn/java/j-lo-jsr303/index.html

  Bean Validation 为 JavaBean 验证定义了相应的元数据模型和 API。缺省的元数据是 Java Annotations，通过使用 XML 可以对原有的元数据信息进行覆盖和扩展。在应用程序中，通过使用 Bean Validation 或是你自己定义的 constraint，例如 `@NotNull`, `@Max`, `@ZipCode`， 就可以确保数据模型（JavaBean）的正确性。constraint 可以附加到字段，getter 方法，类或者接口上面。对于一些特定的需求，用户可以很容易的开发定制化的 constraint。Bean Validation 是一个运行时的数据验证框架，在验证之后验证的错误信息会被马上返回。

  Hibernate Validator 是 Bean Validation 的参考实现 . Hibernate Validator 提供了 JSR 303 规范中所有内置 constraint 的实现，除此之外还有一些附加的 constraint。

- JSR 380

  http://beanvalidation.org/2.0/

  Bean Validation 2.0着重于以下主题：

  - 支持通过注释参数类型来验证容器内元素，例如```List<@Positive Integer> positiveNumbers```。还包括：
    - 容器类型的更灵活的级联验证
    - 支持 `java.util.Optional`
    - 支持由JavaFX声明的属性类型
    - 支持自定义容器类型
  - 为新的日期/时间数据类型的支持（JSR 310）`@Past`和`@Future`
  - 新的内置限制：`@Email`，`@NotEmpty`，`@NotBlank`，`@Positive`， `@PositiveOrZero`，`@Negative`，`@NegativeOrZero`，`@PastOrPresent`和`@FutureOrPresent`
  - 利用JDK 8的新功能（内置约束标记为可重复，参数名称通过反射得到）

## 通过Maven添加Bean Validation 2.0的支持

```
<dependency>
    <groupId>javax.validation</groupId>
    <artifactId>validation-api</artifactId>
    <version>2.0.0.Final</version>
</dependency>
<dependency>
    <groupId>org.hibernate</groupId>
    <artifactId>hibernate-validator</artifactId>
    <version>6.0.4.Final</version>
</dependency>
```

## 注解

### Bean Validation 1.1

| 名称          | 说明                                       |
| :---------- | ---------------------------------------- |
| AssertFalse | 验证 Boolean 对象是否为 false                   |
| AssertTrue  | 验证 Boolean 对象是否为 true                    |
| DecimalMax  | 被标注的值必须不大于约束中指定的最大值. 这个约束的参数是一个通过BigDecimal定义的最大值的字符串表示.小数存在精度 |
| DecimalMin  | 被标注的值必须不小于约束中指定的最小值. 这个约束的参数是一个通过BigDecimal定义的最小值的字符串表示.小数存在精度 |
| Digits      | 被注释的元素必须是一个数字，其值必须在可接受的范围内               |
| Future      | 验证 Date 和 Calendar 对象是否在当前时间之后           |
| Past        | 验证 Date 和 Calendar 对象是否在当前时间之前           |
| Max         | 验证 Number 和 String 对象是否小等于指定的值           |
| Min         | 验证 Number 和 String 对象是否大等于指定的值           |
| NotNull     | 验证对象是否不为null, 无法查检长度为0的字符串               |
| Null        | 验证对象是否为null                              |
| Pattern     | 验证 String 对象是否符合正则表达式的规则                 |
| Size        | (min=, max=)验证对象（Array,Collection,Map,String）长度是否在给定的范围之内 |

### Bean Validation 2

| 名称              | 说明                                       |
| --------------- | ---------------------------------------- |
| Email           | 验证是否是邮件地址，如果为null,不进行验证，算通过验证。           |
| FutureOrPresent | element must be an instant, date or time in the present or in the future, null合法 |
| Negative        | 必须是负数(0不合法), null合法                      |
| NegativeOrZero  | 数字是负数或是零, null合法                         |
| NotBlank        | 不能为null, 至少有一个不是空格的字符, 用于CharSequence    |
| NotEmpty        | 不能为null或者empty, 用于CharSequence, Collection, Map, Array |
| PastOrPresent   | element must be an instant, date or time in the past or in the present |
| Positive        | 必须是正数(0不合法), null合法                      |
| PositiveOrZero  | 数字是正数或是零, null合法                         |

### Hibernate Validator

https://docs.jboss.org/hibernate/stable/validator/api/

在org.hibernate.validator.constraints下

@NotBlank(message =)   验证字符串非null，且长度必须大于0    
@Email  被注释的元素必须是电子邮箱地址    
@Length(min=,max=)  被注释的字符串的大小必须在指定的范围内    
@NotEmpty   被注释的字符串的必须非空    
@Range(min=,max=,message=)  被注释的元素必须在合适的范围内

等等, 部分已过时



