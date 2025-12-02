package com.deloitte.employees.infra.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("spring.datasource")
class DataSourceProperties {

    // --- DataSource (Hikari) Props ---
    private String url;
    private String username;
    private String password;
    private boolean readOnly;
    private long maxLifeTime;
    private long idleTimeout;
    private int maximumPoolSize;
    private String driverClassName;
    private long connectionTimeout;
    private int minimumIdle;
    private int leakDetectionThreshold;
    private String connectionTestQuery;

    // --- JPA Metadata ---
    private Metadata metadata;

    @Data
    public static class Metadata {
        private String databasePlatform;    // dialect
        private HibernateProps hibernate;
        private boolean showSql;
    }

    @Data
    public static class HibernateProps {
        private DDLType ddlType;     // create/update/validate/none
        private boolean formatSql;
    }

    public enum DDLType {
        NONE, VALIDATE, UPDATE, CREATE;

        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }
}
