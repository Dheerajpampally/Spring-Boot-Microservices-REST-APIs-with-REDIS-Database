//package com.redis.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.jdbc.core.JdbcTemplate;
//import javax.sql.DataSource;
//import com.sybase.jdbc3.jdbc.SybDataSource;
//
//@Configuration
//public class DatabaseConfig {
//
//    @Bean
//    public DataSource dataSource() {
//        SybDataSource dataSource = new SybDataSource();
//        dataSource.setUrl("jdbc:sybase:Tds:150.1.25.230:3050?HOSTNAME=JB_XPLPUB");
//        dataSource.setUser("developers");
//        dataSource.setPassword("29Feb2020@2020");
//        return dataSource;
//    }
//
//    @Bean
//    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
//        return new JdbcTemplate(dataSource);
//    }
//}
