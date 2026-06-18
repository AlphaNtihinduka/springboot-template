package com.springboot_template.springboot_template.config;

import com.mongodb.ConnectionString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.List;

@Configuration
@Slf4j
public class MongoConnectionLoggingConfig {

    @Bean
    public CommandLineRunner logMongoConnection(Environment environment) {
        return args -> {
            String mongoUri = environment.getProperty("spring.mongodb.uri");

            if (mongoUri == null || mongoUri.isBlank()) {
                log.warn("Mongo config at startup: no spring.mongodb.uri resolved");
                return;
            }

            ConnectionString connectionString = new ConnectionString(mongoUri);
            List<String> hosts = connectionString.getHosts();

            log.info(
                    "Mongo config at startup: hosts={}, database={}, authDatabase={}, credentialsConfigured={}",
                    hosts,
                    connectionString.getDatabase(),
                    connectionString.getCredential() != null ? connectionString.getCredential().getSource() : null,
                    connectionString.getCredential() != null
            );
        };
    }
}