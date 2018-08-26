package br.com.kimae.multitenacydemo.config;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;

import org.hibernate.MultiTenancyStrategy;
import org.hibernate.cfg.Environment;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.hibernate.dialect.MySQLDialect;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
public class MultitanantHibernateConfig {

    @Autowired
    private JpaProperties jpaProperties;

    @Autowired
    private DataSourceProperties dataSourceProperties;

    @Bean
    public MultiTenantConnectionProvider multiTenantConnectionProvider() {
        return new DataSourceMulttenantConnectionProvider(dataSourceProperties);
    }

    @Bean
    public CurrentTenantIdentifierResolver currentTenantIdentifierResolver() {
        return new IdentifierResolver();
    }

    @Bean(name="multitenantManagerFactoryBean")
    public LocalContainerEntityManagerFactoryBean multitenantManagerFactoryBean(MultiTenantConnectionProvider multiTenantConnectionProvider,
        CurrentTenantIdentifierResolver currentTenantIdentifierResolver) {
        Map<String, Object> hibernateProps = new LinkedHashMap<>();
        hibernateProps.putAll(this.jpaProperties.getProperties());
        //hibernateProps.put(Environment.INTERCEPTOR,HibernateInterceptor.class.getName());
        hibernateProps.put(Environment.DIALECT, MySQLDialect.class.getName());
        hibernateProps.put(Environment.PHYSICAL_NAMING_STRATEGY, SpringPhysicalNamingStrategy.class.getName());
        hibernateProps.put(Environment.MULTI_TENANT, MultiTenancyStrategy.DATABASE);
        hibernateProps.put(Environment.MULTI_TENANT_CONNECTION_PROVIDER, multiTenantConnectionProvider);
        hibernateProps.put(Environment.MULTI_TENANT_IDENTIFIER_RESOLVER, currentTenantIdentifierResolver);

        // No dataSource is set to resulting entityManagerFactoryBean
        LocalContainerEntityManagerFactoryBean result = new LocalContainerEntityManagerFactoryBean();

        result.setPackagesToScan(new String[] {MultitanantEntityMarker.class.getPackage().getName()});
        HibernateJpaVendorAdapter vendor = new HibernateJpaVendorAdapter();
        vendor.setShowSql(jpaProperties.isShowSql());
        result.setJpaVendorAdapter(vendor);
        result.setJpaPropertyMap(hibernateProps);

        return result;
    }

    @Bean(name="multitenantManagerFactory")
    public EntityManagerFactory entityManagerFactory(@Qualifier("multitenantManagerFactoryBean") LocalContainerEntityManagerFactoryBean multitenantManagerFactoryBean) {
        return multitenantManagerFactoryBean.getObject();
    }

    @Bean(name="multitenantTransaction")
    public PlatformTransactionManager txManager(@Qualifier("multitenantManagerFactory") EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }

}
