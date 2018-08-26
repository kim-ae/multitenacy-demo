package br.com.kimae.multitenacydemo.config.security;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;

import com.zaxxer.hikari.HikariDataSource;

public class DataSourceFactory {

    private static final String CONNECTION_PROPERTIES = "?useSSL=false";

    public static DataSource fromProperties(DataSourceProperties dataSourceProperties, String identifier){
        HikariDataSource db = new HikariDataSource();
        db.setJdbcUrl(dataSourceProperties.getUrl() + identifier + CONNECTION_PROPERTIES);
        db.setUsername(dataSourceProperties.getUsername());
        db.setPassword(dataSourceProperties.getPassword());
        return db;
    }
}
