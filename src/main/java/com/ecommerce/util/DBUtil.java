package com.ecommerce.util;
import com.ecommerce.config.AppConfig;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.Connection;
import java.sql.SQLException;
public final class DBUtil {
    private static final Logger log = LoggerFactory.getLogger(DBUtil.class);
    private static volatile HikariDataSource ds;
    private DBUtil() {}
    public static Connection getConnection() throws SQLException {
        return dataSource().getConnection();
    }
    public static void shutdown() {
        HikariDataSource d = ds;
        if (d != null && !d.isClosed()) { d.close(); ds = null; }
    }
    private static HikariDataSource dataSource() {
        HikariDataSource local = ds;
        if (local != null && !local.isClosed()) return local;
        synchronized (DBUtil.class) {
            if (ds != null && !ds.isClosed()) return ds;
            HikariConfig cfg = new HikariConfig();
            cfg.setJdbcUrl(AppConfig.get("db.url"));
            cfg.setUsername(AppConfig.get("db.user"));
            cfg.setPassword(AppConfig.get("db.password"));
            cfg.setDriverClassName("com.mysql.cj.jdbc.Driver");
            cfg.setMaximumPoolSize(AppConfig.getInt("db.pool.size", 10));
            cfg.setConnectionTimeout(5_000);
            cfg.setInitializationFailTimeout(-1);
            cfg.setPoolName("ecommerce-pool");
            log.info("Initializing JDBC pool to {}", cfg.getJdbcUrl());
            ds = new HikariDataSource(cfg);
            return ds;
        }
    }
}
