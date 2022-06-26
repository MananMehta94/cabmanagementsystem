package com.phonepe.cabs.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class DataSources {

    @Bean
    public DataSource cabManagementDataSource(){
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl("jdbc:postgresql://localhost:5432/cabmanagement");
        hikariConfig.setSchema("cabmanagement");
        hikariConfig.setUsername("mananmehta");
        return new HikariDataSource(hikariConfig);
    }

    @Bean
    public NamedParameterJdbcTemplate cabManagementNamedParameterJdbcTemplate(DataSource cabManagementDataSource){
        NamedParameterJdbcTemplate namedParameterJdbcTemplate =  new NamedParameterJdbcTemplate(cabManagementDataSource);
        return  namedParameterJdbcTemplate;
    }
}
