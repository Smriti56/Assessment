package com.example.assessment.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class DatabaseConfiguration {
    @Value("${spring.datasource.url}")
    private String databaseUrl;

    @Value("${spring.datasource.username}")
    private String databaseUsername;

    @Value("${spring.datasource.password}")
    private String databasePassword;

    public void printDatabaseProperties() {
        System.out.println("Database URL: " + databaseUrl);
        System.out.println("Database Username: " + databaseUsername);
        System.out.println("Database Password: " + databasePassword);
    }
}
