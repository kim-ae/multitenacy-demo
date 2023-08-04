package br.com.kimae.multitenacydemo.config.database;

import static org.hibernate.cfg.AvailableSettings.PHYSICAL_NAMING_STRATEGY;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import br.com.kimae.multitenacydemo.config.security.DataSourceFactory;
import br.com.kimae.multitenacydemo.persistence.app.AppEntityMarker;

@Configuration
@EnableConfigurationProperties({ JpaProperties.class, DataSourceProperties.class })
@EnableJpaRepositories(
		basePackageClasses = { AppEntityMarker.class },
		entityManagerFactoryRef = "appEntityManager",
		transactionManagerRef = "appTransactionManager"
)
public class AppJpaConfig {

	private final JpaProperties jpaProperties;

	private final DataSourceProperties dataSourceProperties;

	private static final String DATABASE_NAME = "app";

	public AppJpaConfig(JpaProperties jpaProperties, DataSourceProperties dataSourceProperties) {
		this.jpaProperties = jpaProperties;
		this.dataSourceProperties = dataSourceProperties;
	}

	@Bean
	@Primary
	public LocalContainerEntityManagerFactoryBean appEntityManager() {
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(appDataSource());
		em.setPackagesToScan(new String[] { AppEntityMarker.class.getPackage().getName() });

		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		vendorAdapter.setShowSql(jpaProperties.isShowSql());

		em.setJpaVendorAdapter(vendorAdapter);
		Map<String, Object> properties = new HashMap<>();
		properties.put(PHYSICAL_NAMING_STRATEGY, CamelCaseToUnderscoresNamingStrategy.class.getName());
		properties.putAll(jpaProperties.getProperties());
		properties.put("hibernate.hbm2ddl.auto", jpaProperties.isGenerateDdl());
		em.setJpaPropertyMap(properties);

		return em;
	}

	public DataSource appDataSource() {
		return DataSourceFactory.fromProperties(dataSourceProperties, DATABASE_NAME);
	}

	@Bean
	@Primary
	public PlatformTransactionManager appTransactionManager() {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(appEntityManager().getObject());
		return transactionManager;
	}

}
