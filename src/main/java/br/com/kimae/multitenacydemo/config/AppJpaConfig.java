package br.com.kimae.multitenacydemo.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import com.zaxxer.hikari.HikariDataSource;

import br.com.kimae.multitenacydemo.persistence.app.AppEntityMarker;

@Configuration
@EnableConfigurationProperties({JpaProperties.class, DataSourceProperties.class})
@EnableJpaRepositories(
    basePackageClasses = {AppEntityMarker.class},
    entityManagerFactoryRef = "appEntityManager",
    transactionManagerRef = "appTransactionManager"
)
public class AppJpaConfig {

    @Autowired
    private JpaProperties jpaProperties;

    @Autowired
    private DataSourceProperties dataSourceProperties;

    private static final String DATABASE_NAME = "app";

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean appEntityManager() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(appDataSource());
        em.setPackagesToScan(new String[] { AppEntityMarker.class.getPackage().getName()});

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setShowSql(jpaProperties.isShowSql());

        em.setJpaVendorAdapter(vendorAdapter);
        Map<String, Object> properties = new HashMap<>();
        properties.put(Environment.PHYSICAL_NAMING_STRATEGY, SpringPhysicalNamingStrategy.class.getName());
        properties.putAll(jpaProperties.getProperties());
        properties.put("hibernate.hbm2ddl.auto", jpaProperties.getHibernate().getDdlAuto());
        em.setJpaPropertyMap(properties);

        return em;
    }

    @Bean
    @Primary
    public DataSource appDataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(dataSourceProperties.getUrl()+DATABASE_NAME);
        dataSource.setUsername(dataSourceProperties.getUsername());
        dataSource.setPassword(dataSourceProperties.getPassword());

        return dataSource;
    }

    @Bean
    @Primary
    public PlatformTransactionManager appTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(appEntityManager().getObject());
        return transactionManager;
    }

}
