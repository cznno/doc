# Shiro使用多个Realm时, 在校验角色和权限时遇到的问题

未完成

多个Realm, 都继承了 AuthorizingRealm.class. 

对于每一个Realm, 获取授权信息都有不同的实现, 即

```java
/**
     * 获取授权信息,在这些情况下会调用
     * 1、subject.hasRole("admin") 或 subject.isPermitted("admin")：自己去调用这个是否有什么角色或者是否有什么权限的时候；
     * 2、@RequiresRoles("admin") ：在方法上加注解的时候；
     * 3、[@shiro.hasPermission name = "admin"][/@shiro.hasPermission]：在页面上加shiro标签的时候，即进这个页面的时候扫描到有这个标签的时候。
     *
     * @param principals 身份集合
     * @return 用户的权限集合
     */
protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals)
```

但是在实际获取权限的过程中, 会发现只有第一个注入到securityManager的realm中的doGetAuthorizationInfo()方法是起作用的.

查看源码

```java
// 从这里开始
SecurityUtils.getSubject().hasRole();

//hasRole方法调用了getPrincipals()
public boolean hasRole(String roleIdentifier) {
        return hasPrincipals() && securityManager.hasRole(getPrincipals(), 	  roleIdentifier);
}

//这里只拿了第一个?
public PrincipalCollection getPrincipals() {
    List<PrincipalCollection> runAsPrincipals = getRunAsPrincipalsStack();
    return CollectionUtils.isEmpty(runAsPrincipals) ? this.principals : runAsPrincipals.get(0);
}
```

