package com.epam.esm.model.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

@Configuration
@PropertySource("classpath:database.properties")
public class SpringJdbcConfig {

    @Value("${driverClass}")
    private String driveClass;
    @Value("${url}")
    private String databaseUrl;
    @Value("${userName}")
    private String userName;
    @Value("${password}")
    private String password;
    @Value("${maximumPoolSize}")
    private int maxPoolSize;

    @Bean
    public HikariConfig getHikariConfig() {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(driveClass);
        config.setJdbcUrl(databaseUrl);
        config.setUsername(userName);
        config.setPassword(password);
        config.setMaximumPoolSize(maxPoolSize);
        return config;
    }

    @Bean
    public DataSource dataSource(HikariConfig hikariConfig) {
        return new HikariDataSource(hikariConfig);
    }

    @Bean
    public JdbcTemplate getJdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate(JdbcTemplate jdbcTemplate) {
        return new NamedParameterJdbcTemplate(jdbcTemplate);
    }
}
