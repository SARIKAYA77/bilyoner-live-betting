package com.example.bilyoner;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
@EnableAutoConfiguration(exclude = {
    DataSourceAutoConfiguration.class,
    KafkaAutoConfiguration.class,
    RedisAutoConfiguration.class
})
@ComponentScan(
    basePackages = "com.example.bilyoner",
    excludeFilters = {
        @ComponentScan.Filter(type = FilterType.REGEX, pattern = "com\\.example\\.bilyoner\\.core\\.handler\\..*"),
        @ComponentScan.Filter(type = FilterType.REGEX, pattern = "com\\.example\\.bilyoner\\.core\\.chain\\..*"),
        @ComponentScan.Filter(type = FilterType.REGEX, pattern = "com\\.example\\.bilyoner\\.infrastructure\\.config\\..*")
    }
)
public class TestApplication {
    // Minimal application for testing
} 