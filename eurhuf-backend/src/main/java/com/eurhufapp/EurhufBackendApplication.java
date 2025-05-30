package com.eurhufapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main application class for the EURHUF Chart App backend.
 * This Spring Boot application fetches EURHUF exchange rate data from Yahoo Finance API
 * and provides endpoints for the frontend to consume.
 */
@SpringBootApplication
@EnableCaching
@EnableScheduling
public class EurhufBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurhufBackendApplication.class, args);
    }
}