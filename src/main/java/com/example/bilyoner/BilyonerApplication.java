package com.example.bilyoner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main application class for Bilyoner Live Betting Application
 * Features:
 * - Spring Boot application with H2 database
 * - Real-time odds updates with WebSocket
 * - Event-driven architecture with Kafka
 * - Circuit Breaker pattern for resilience
 * - Chain of Responsibility pattern for event processing
 * - Asynchronous bet processing with timeout handling
 */
@SpringBootApplication
@EnableKafka
@EnableAsync
@EnableScheduling
public class BilyonerApplication {

    public static void main(String[] args) {
        SpringApplication.run(BilyonerApplication.class, args);
    }

}
