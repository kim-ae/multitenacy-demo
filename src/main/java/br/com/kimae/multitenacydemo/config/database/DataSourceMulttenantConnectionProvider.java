package br.com.kimae.multitenacydemo.config.database;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;

import com.zaxxer.hikari.HikariDataSource;

import br.com.kimae.multitenacydemo.config.security.DataSourceFactory;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DataSourceMulttenantConnectionProvider extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl {

    private Map<String, DataSource> datasourcesCache = new HashMap<>();

    private final DataSourceProperties dataSourceProperties;

    public DataSourceMulttenantConnectionProvider(DataSourceProperties dataSourceProperties) {
        this.dataSourceProperties = dataSourceProperties;
    }

    @Override
    protected DataSource selectAnyDataSource() {
        return datasourcesCache.values().iterator().next();
    }

    @Override
    protected DataSource selectDataSource(final String tenantIdentifier) {
        if (!datasourcesCache.containsKey(tenantIdentifier)) {
            datasourcesCache.put(tenantIdentifier, create(tenantIdentifier));
        }
        log.debug("Selecting {}", tenantIdentifier);
        return datasourcesCache.get(tenantIdentifier);
    }

    private DataSource create(String identifier) {
        return DataSourceFactory.fromProperties(dataSourceProperties, identifier);
    }
}
