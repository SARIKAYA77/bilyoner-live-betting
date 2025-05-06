package com.example.bilyoner.infrastructure.config;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.SlidingWindowType;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.common.circuitbreaker.configuration.CircuitBreakerConfigCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * Configuration for Circuit Breaker pattern
 */
@Configuration
public class CircuitBreakerConfig {
    
    /**
     * Customizes the Circuit Breaker configuration for the betting service
     */
    @Bean
    public CircuitBreakerConfigCustomizer bettingServiceCircuitBreakerConfig() {
        return CircuitBreakerConfigCustomizer.of("bettingService",
                builder -> builder
                        .slidingWindowSize(10)
                        .slidingWindowType(SlidingWindowType.COUNT_BASED)
                        .failureRateThreshold(50.0f)
                        .waitDurationInOpenState(Duration.ofSeconds(5))
                        .permittedNumberOfCallsInHalfOpenState(3)
                        .minimumNumberOfCalls(5)
                        .automaticTransitionFromOpenToHalfOpenEnabled(true)
        );
    }
    
    /**
     * Creates a Circuit Breaker registry
     */
    @Bean
    public CircuitBreakerRegistry circuitBreakerRegistry() {
        return CircuitBreakerRegistry.ofDefaults();
    }
} 