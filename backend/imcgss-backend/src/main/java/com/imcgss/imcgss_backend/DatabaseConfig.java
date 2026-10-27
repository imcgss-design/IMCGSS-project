package com.imcgss.imcgss_backend;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;

@Configuration
public class DatabaseConfig {

    private static final Logger log = LoggerFactory.getLogger(DatabaseConfig.class);

    @Value("${spring.datasource.url:#{null}}")
    private String configuredDatasourceUrl;

    @Value("${spring.datasource.username:#{null}}")
    private String configuredUsername;

    @Value("${spring.datasource.password:#{null}}")
    private String configuredPassword;

    @Value("${spring.datasource.driver-class-name:#{null}}")
    private String configuredDriverClassName;

    @Bean
    @Primary
    public DataSource dataSource() {
        // Render sets DATABASE_URL automatically when linking a PostgreSQL database
        String rawDbUrl = System.getenv("DATABASE_URL");
        if (rawDbUrl == null || rawDbUrl.isBlank()) {
            rawDbUrl = System.getenv("SPRING_DATASOURCE_URL");
        }

        // If Render DATABASE_URL is provided in postgres:// or postgresql:// format
        if (rawDbUrl != null && !rawDbUrl.isBlank() &&
                (rawDbUrl.startsWith("postgres://") || rawDbUrl.startsWith("postgresql://"))) {
            log.info("Configuring DataSource from Render DATABASE_URL URI");
            return createDataSourceFromUri(rawDbUrl);
        }

        // Otherwise use standard Spring configured properties
        log.info("Configuring DataSource from standard properties (URL: {})", configuredDatasourceUrl);
        HikariConfig config = new HikariConfig();
        if (configuredDatasourceUrl != null) {
            config.setJdbcUrl(configuredDatasourceUrl);
        }
        if (configuredUsername != null) {
            config.setUsername(configuredUsername);
        }
        if (configuredPassword != null) {
            config.setPassword(configuredPassword);
        }
        if (configuredDriverClassName != null && !configuredDriverClassName.isBlank()) {
            config.setDriverClassName(configuredDriverClassName);
        }

        // Hikari pool settings suitable for free-tier Render instances
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setIdleTimeout(30000);
        config.setMaxLifetime(60000);
        config.setConnectionTimeout(20000);

        return new HikariDataSource(config);
    }

    private DataSource createDataSourceFromUri(String databaseUriString) {
        try {
            URI dbUri = new URI(databaseUriString);

            String userInfo = dbUri.getUserInfo();
            String username = null;
            String password = null;

            if (userInfo != null && userInfo.contains(":")) {
                String[] parts = userInfo.split(":", 2);
                username = parts[0];
                password = parts[1];
            } else if (userInfo != null) {
                username = userInfo;
            }

            String host = dbUri.getHost();
            int port = dbUri.getPort();
            if (port == -1) {
                port = 5432;
            }

            String path = dbUri.getPath(); // e.g. /dbname
            if (path == null || path.isEmpty()) {
                path = "/imcgss_db";
            }

            // Standard PostgreSQL JDBC URL
            String jdbcUrl = "jdbc:postgresql://" + host + ":" + port + path;

            String query = dbUri.getQuery();
            if (query != null && !query.isBlank()) {
                jdbcUrl += "?" + query;
            }

            log.info("Generated PostgreSQL JDBC URL for host: {}:{}", host, port);

            HikariConfig config = new HikariConfig();
            config.setDriverClassName("org.postgresql.Driver");
            config.setJdbcUrl(jdbcUrl);

            if (username != null) {
                config.setUsername(username);
            }
            if (password != null) {
                config.setPassword(password);
            }

            // Pool optimization for Render free tier limits
            config.setMaximumPoolSize(10);
            config.setMinimumIdle(2);
            config.setIdleTimeout(30000);
            config.setMaxLifetime(60000);
            config.setConnectionTimeout(20000);

            return new HikariDataSource(config);
        } catch (URISyntaxException e) {
            log.error("Failed to parse DATABASE_URL as URI: {}", e.getMessage());
            throw new IllegalArgumentException("Invalid DATABASE_URL syntax: " + databaseUriString, e);
        }
    }
}
