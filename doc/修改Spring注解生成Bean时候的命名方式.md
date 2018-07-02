# 修改Spring注解生成Bean时候的命名方式

[demo:cznno/zero](https://github.com/cznno/zero/blob/master/src/main/java/person/cznno/zero/base/config/QualifiedBeanNameGenerator.java)

```Java
@Configuration
public class QualifiedBeanNameGenerator extends AnnotationBeanNameGenerator {
    @Override
    protected String buildDefaultBeanName(BeanDefinition definition) {
        String className;
        if (definition instanceof ScannedGenericBeanDefinition) {
            final ScannedGenericBeanDefinition scanDefinition = (ScannedGenericBeanDefinition) definition;
            className = scanDefinition.getMetadata().getClassName();
        } else {
            className = ClassUtils.getShortName(definition.getBeanClassName());
        }
        return Introspector.decapitalize(className);
    }
}
```

## Intro

在实现多个Service的时候，如果直接用@Service配置Bean默认是使用短类名（即没有包括包名）来设置Bean name

```Java
/*
可以在
org.springframework.context.annotation.AnnotationBeanNameGenerator#buildDefaultBeanName(org.springframework.beans.factory.config.BeanDefinition)中看到
*/
    protected String buildDefaultBeanName(BeanDefinition definition) {
        String shortClassName = ClassUtils.getShortName(definition.getBeanClassName());
        return Introspector.decapitalize(shortClassName);
    }
```
ref:

https://stackoverflow.com/questions/39818728/how-do-i-prefix-each-bean-id-in-a-package-inc-sub-packages-with-a-constant-st