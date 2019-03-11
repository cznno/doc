- field注入不能创建不可变对象   
- 使用field注入的类与di容器紧耦合  
- 只有使用反射才能实例化这些类，对单元测试不友好  
- 依赖被隐藏了，不能通过构造方法暴露出来  
- 当类违反了单一职责原则的时候，用field注入会变得难以发现这个问题  

ref
- https://stackoverflow.com/questions/39890849/what-exactly-is-field-injection-and-how-to-avoid-it
- https://www.vojtechruzicka.com/field-dependency-injection-considered-harmful/
