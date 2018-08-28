# multitenacy-demo
The main goal for this project is to give a simple example about the multitenacy capacity of 
Hibernate ORM. It's important to have it clear that JPA does not have any JSR that states any pattern
of multitenacy, so this implementaion will work only with Hibernate ORM + JPA.

## Main Classes
* [MultitenantHibernateConfig](https://github.com/kim-ae/multitenacy-demo/blob/master/src/main/java/br/com/kimae/multitenacydemo/config/database/MultitanantHibernateConfig.java): This one is the "manual configuration" for the JpaTransactionManager, that is used in order to create the EntityManagerFactory, used by spring data.
* [IdentifierResolver](https://github.com/kim-ae/multitenacy-demo/blob/master/src/main/java/br/com/kimae/multitenacydemo/config/database/IdentifierResolver.java): Responsable to decide the current tenant for the database.
* [DataSourceMulttenantConnectionProvider](https://github.com/kim-ae/multitenacy-demo/blob/master/src/main/java/br/com/kimae/multitenacydemo/config/database/DataSourceMulttenantConnectionProvider.java): Responsabel to provide the DataSource to the EntityManager.
* [DatabaseContext](https://github.com/kim-ae/multitenacy-demo/blob/master/src/main/java/br/com/kimae/multitenacydemo/config/database/DatabaseContext.java): The (thread) global static object that holds the current tenant for the current thread.

## Attention for details
* It's important to have the class or method annotated with `@Transaction("bean name for the transaction manager")`, in order to 
get the correct EntityManagerFactory;

* It only works with Hibernate because the multitenancy is an implementation for the SessionFactoryImpl, that is the imçlementation 
for the interface `EntityManagerFactory`;

* If you don't want to have another JpaTransactionManager it's important to exclude the Autoconfiguration for the DataSource `@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})`.

* This project uses [Lombok](https://projectlombok.org/), it's important to have it installed in your favorite IDE (intelij :x)
## Why works?
Using hibernate if `MULTI_TENANT` and `MULTI_TENANT_CONNECTION_PROVIDER` and `MULTI_TENANT_IDENTIFIER_RESOLVER` are set the SessionFactoryImpl understands that will be a name resolver and
an a provider to get the datasource used by the EntityManager, in order to create the connection with the database.

## RUN RUN RUN
In order to run this demo a mysql database is needed. If you are familiar with docker you can create a container: 
```$xslt
docker run --name mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=password -d mysql:5.6
```

Run the [script init_db.sql](https://github.com/kim-ae/multitenacy-demo/blob/master/src/main/resources/ddl/init_db.sql) to create the data for the demo. It's important to set the [application.yml](https://github.com/kim-ae/multitenacy-demo/blob/master/src/main/resources/application.yml) accordingly with your environment.
With everything setup you can run the application using your favorite IDE (intelij :x), or command line `./mvnw spring-boot:run`.