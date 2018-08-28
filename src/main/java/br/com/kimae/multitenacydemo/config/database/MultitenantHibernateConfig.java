package br.com.kimae.multitenacydemo.config.database;

import java.util.LinkedHashMap;
import java.util.Map;

import org.hibernate.MultiTenancyStrategy;
import org.hibernate.cfg.Environment;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.hibernate.dialect.MySQLDialect;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import br.com.kimae.multitenacydemo.persistence.multitanant.MultitanantEntityMarker;

@Configuration
@EnableConfigurationProperties({JpaProperties.class, DataSourceProperties.class})
@EnableTransactionManagement
@EnableJpaRepositories(
    basePackageClasses = {MultitanantEntityMarker.class} ,
    entityManagerFactoryRef = "multitenantManagerFactoryBean",
    transactionManagerRef = "multitenantTransaction"
)
public class MultitenantHibernateConfig {

    @Autowired
    private JpaProperties jpaProperties;

    @Autowired
    private DataSourceProperties dataSourceProperties;

    public MultiTenantConnectionProvider multiTenantConnectionProvider() {
        return new DataSourceMulttenantConnectionProvider(dataSourceProperties);
    }

    public CurrentTenantIdentifierResolver currentTenantIdentifierResolver() {
        return new IdentifierResolver();
    }

    @Bean(name="multitenantManagerFactoryBean")
    public LocalContainerEntityManagerFactoryBean multitenantManagerFactoryBean( ) {
        Map<String, Object> hibernateProps = new LinkedHashMap<>();
        hibernateProps.putAll(this.jpaProperties.getProperties());
        //hibernateProps.put(Environment.INTERCEPTOR,HibernateInterceptor.class.getName());
        hibernateProps.put(Environment.DIALECT, MySQLDialect.class.getName());
        hibernateProps.put(Environment.PHYSICAL_NAMING_STRATEGY, SpringPhysicalNamingStrategy.class.getName());
        hibernateProps.put(Environment.MULTI_TENANT, MultiTenancyStrategy.DATABASE);
        hibernateProps.put(Environment.MULTI_TENANT_CONNECTION_PROVIDER, multiTenantConnectionProvider() );
        hibernateProps.put(Environment.MULTI_TENANT_IDENTIFIER_RESOLVER, currentTenantIdentifierResolver());

        // No dataSource is set to resulting entityManagerFactoryBean
        LocalContainerEntityManagerFactoryBean result = new LocalContainerEntityManagerFactoryBean();

        result.setPackagesToScan(new String[] {MultitanantEntityMarker.class.getPackage().getName()});
        HibernateJpaVendorAdapter vendor = new HibernateJpaVendorAdapter();
        vendor.setShowSql(jpaProperties.isShowSql());
        result.setJpaVendorAdapter(vendor);
        result.setJpaPropertyMap(hibernateProps);

        return result;
    }

    @Bean(name="multitenantTransaction")
    public PlatformTransactionManager txManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(multitenantManagerFactoryBean().getObject());
        return transactionManager;
    }

}
