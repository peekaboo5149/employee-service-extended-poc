package com.deloitte.employees.infra.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Slf4j
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = ConfigConstants.REPOSITORY_PACKAGE,
        entityManagerFactoryRef = "employeeManagementEntityManagerFactoryBean"
)
@EnableConfigurationProperties(DataSourceProperties.class)
@RequiredArgsConstructor
@Primary
class DataSourceConfiguration {

    private final DataSourceProperties props;

    @Bean(name = "employeeManagementDataSource")
    public DataSource employeeManagementDataSource() {
        log.info("Initializing datasource for URL: {}", props.getUrl());

        HikariConfig hikari = new HikariConfig();
        hikari.setJdbcUrl(props.getUrl());
        hikari.setUsername(props.getUsername());
        hikari.setPassword(props.getPassword());
        hikari.setReadOnly(props.isReadOnly());
        hikari.setMaxLifetime(props.getMaxLifeTime());
        hikari.setIdleTimeout(props.getIdleTimeout());
        hikari.setMinimumIdle(props.getMinimumIdle());
        hikari.setMaximumPoolSize(props.getMaximumPoolSize());
        hikari.setDriverClassName(props.getDriverClassName());
        hikari.setConnectionTimeout(props.getConnectionTimeout());
        hikari.setLeakDetectionThreshold(props.getLeakDetectionThreshold());
        hikari.setConnectionTestQuery(props.getConnectionTestQuery());

        return new HikariDataSource(hikari);
    }

    @Bean(name = "employeeManagementEntityManagerFactoryBean")
    public LocalContainerEntityManagerFactoryBean employeeManagementEntityManagerFactory() {

        // JPA properties from YAML
        Properties jpaProps = new Properties();
        jpaProps.put("hibernate.dialect", props.getMetadata().getDatabasePlatform());
        jpaProps.put("hibernate.show_sql", props.getMetadata().isShowSql());
        jpaProps.put("hibernate.format_sql", props.getMetadata().getHibernate().isFormatSql());
        jpaProps.put("hibernate.hbm2ddl.auto", props.getMetadata().getHibernate().getDdlType().toString());

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(false);

        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(employeeManagementDataSource());
        factoryBean.setPackagesToScan(ConfigConstants.ENTITY_PACKAGE);
        factoryBean.setJpaVendorAdapter(vendorAdapter);
        factoryBean.setJpaProperties(jpaProps);

        return factoryBean;
    }

    @Bean(name = "transactionManager")
    public PlatformTransactionManager employeeManagementTransactionManager(
            @Qualifier("employeeManagementEntityManagerFactoryBean")
            EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }

    @Bean(name = "employeeManagementJdbcTemplate")
    public JdbcTemplate employeeManagementJdbcTemplate() {
        return new JdbcTemplate(employeeManagementDataSource());
    }

    @Bean(name = "employeeManagementNamedParameterJdbcTemplate")
    public NamedParameterJdbcTemplate employeeManagementNamedParameterJdbcTemplate() {
        return new NamedParameterJdbcTemplate(employeeManagementDataSource());
    }
}
