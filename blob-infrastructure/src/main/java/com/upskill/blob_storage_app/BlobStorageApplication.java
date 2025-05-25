package com.upskill.blob_storage_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "com.upskill.blob_storage_app.entity")
@EnableJpaRepositories(basePackages = "com.upskill.blob_storage_app.repository")
public class BlobStorageApplication {
    public static void main(String[] args) {
        SpringApplication.run(BlobStorageApplication.class, args);
    }
} 