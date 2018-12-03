# Why tk.mybatis.mapper 3.4.0 sucks

## Unsafety method tk.mybatis.mapper.entity.Example#selectProperties

Let's check out sources:

```java
public Example selectProperties(String... properties) {
    if (properties != null && properties.length > 0) {
        if (this.selectColumns == null) {
            this.selectColumns = new LinkedHashSet<String>();
        }
        for (String property : properties) {
            if (propertyMap.containsKey(property)) {
                this.selectColumns.add(propertyMap.get(property).getColumn());
            }
        }
    }
    return this;
}
```

While looping properties, the property does not in `propertyMap ` will be ignored - which will cause results users never want. The implementation in version 4.0.0 is much better.

Check this:

```java
public Example selectProperties(String... properties) {
    if (properties != null && properties.length > 0) {
        if (this.selectColumns == null) {
            this.selectColumns = new LinkedHashSet<String>();
        }
        for (String property : properties) {
            if (propertyMap.containsKey(property)) {
                this.selectColumns.add(propertyMap.get(property).getColumn());
            } else {
                throw new MapperException("类 " + entityClass.getSimpleName() + " 不包含属性 \'" + property + "\'，或该属性被@Transient注释！");
            }
        }
    }
    return this;
}
```

If there is a illegal property name, it will throw an exception. 

## We can use Example#builder in 4.0.0

```java
Example example = Example.builder(Country.class)
    .select("countryname")
    .where(Sqls.custom().andGreaterThan("id", 100))
    .orderByAsc("countrycode")
    .forUpdate()
    .build();
List<Country> countries = mapper.selectByExample(example);
```

Looks much better.

## Is SelectByExampleMapper#selectByExample safe enough?

