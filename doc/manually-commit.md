尝试通过配置 dataSource 和 transactionManager 对事务进行控制

```java
class Foo{
    private final TransactionTemplate transactionTemplate;
    
    // use constructor-injection to supply the PlatformTransactionManager
    @Autowired
    public Foo(PlatformTransactionManager transactionManager) {
        this.transactionTemplate = new TransactionTemplate(transactionManager);
    }
    
    public void doQuery(){
        WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        DruidDataSource dataSource = (DruidDataSource) webApplicationContext.getBean("dataSource");
        SqlSessionFactoryBean sqlSessionFactory = (SqlSessionFactoryBean) webApplicationContext.getBean("sqlSessionFactory");
        DataSourceTransactionManager manager = (DataSourceTransactionManager) webApplicationContext.getBean("transactionManager");
        Connection connection = null;
        
        try {
            connection = manager.getDataSource().getConnection();
        
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        //do some query
        
        try {
            connection.commit();
            DataSourceUtils.doReleaseConnection(connection, dataSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
    
```
