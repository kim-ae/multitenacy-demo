package br.com.kimae.multitenacydemo.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;

import com.zaxxer.hikari.HikariDataSource;

public class DataSourceMulttenantConnectionProvider extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl {

    private Map<String, DataSource> datasourcesCache = new HashMap<>();

    private final String password;
    private final String user;
    private final String urlBase;

    public DataSourceMulttenantConnectionProvider( DataSourceProperties dataSourceProperties) {
        this.password = dataSourceProperties.getPassword();
        this.user = dataSourceProperties.getUsername();
        this.urlBase = dataSourceProperties.getUrl();
    }

    @Override
    protected DataSource selectAnyDataSource() {
        return datasourcesCache.values().iterator().next();
    }

    @Override
    protected DataSource selectDataSource(final String tenantIdentifier) {
        if(!datasourcesCache.containsKey(tenantIdentifier)) {
            datasourcesCache.put(tenantIdentifier, create(tenantIdentifier));
        }
        return datasourcesCache.get(tenantIdentifier);
    }

    private DataSource create(String identifier) {
        HikariDataSource db = new HikariDataSource();
        db.setJdbcUrl(urlBase+identifier);
        db.setUsername(user);
        db.setPassword(password);
        return db;
    }
}
