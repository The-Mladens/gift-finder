package com.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

public class PgDataSource {

    private static final HikariDataSource ds;

    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(System.getenv("DB_URL"));
        config.setUsername(System.getenv("DB_USER"));
        config.setPassword(System.getenv("DB_PASSWORD"));
        config.setMaximumPoolSize(5);
        config.setMinimumIdle(1);
        config.setPoolName("PgPool");

        ds = new HikariDataSource(config);
    }

    public static DataSource getDataSource() {
        return ds;
    }
}