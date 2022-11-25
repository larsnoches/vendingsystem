//package org.cyrilselyanin.vendingsystem.auth.config;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.jdbc.DataSourceBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.jdbc.datasource.DriverManagerDataSource;
//
//import javax.sql.DataSource;
//
//@Configuration
//public class DataBaseConfig {
//
////	@Value("${auth.database.username}")
////	private String username;
////
////	@Value("${auth.database.password}")
////	private String password;
//
////	@Bean
////	public DataSource getDataSource() {
//////		DriverManagerDataSource dataSource =  new DriverManagerDataSource();
//////		dataSource.setDriverClassName("org.postgresql.Driver");
//////		dataSource.setUrl("jdbc:mysql://localhost:3306/mydb");
//////		dataSource.setUsername(username);
//////		dataSource.setPassword(password);
//////		return dataSource;
////		DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
////		dataSourceBuilder.driverClassName("org.postgresql.Driver");
////		return dataSourceBuilder.build();
////	}
//
//}
