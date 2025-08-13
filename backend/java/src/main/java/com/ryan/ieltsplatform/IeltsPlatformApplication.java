package com.ryan.ieltsplatform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main Spring Boot application class for IELTS Platform Backend
 * 
 * This application provides REST and gRPC services for the IELTS training platform.
 * It includes OpenAPI documentation, health monitoring, and structured logging.
 */
@SpringBootApplication
public class IeltsPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(IeltsPlatformApplication.class, args);
    }
}
