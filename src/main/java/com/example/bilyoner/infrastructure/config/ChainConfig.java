package com.example.bilyoner.infrastructure.config;

import com.example.bilyoner.core.chain.*;
import com.example.bilyoner.core.handler.EventHandler;
import com.example.bilyoner.repository.EventRepository;
import com.example.bilyoner.infrastructure.messaging.KafkaProducer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.apache.kafka.common.serialization.StringSerializer;

/**
 * Configuration for Chain of Responsibility pattern
 */
@Configuration
public class ChainConfig {

    /**
     * Create the RandomOddsHandler
     */
    @Bean
    public RandomOddsHandler randomOddsHandler() {
        return new RandomOddsHandler();
    }
    
    /**
     * Create the KafkaOddsHandler
     */
    @Bean
    public KafkaOddsHandler kafkaOddsHandler(KafkaProducer kafkaProducer) {
        return new KafkaOddsHandler(kafkaProducer);
    }
    
    /**
     * Create the CacheOddsHandler
     */
    @Bean
    public CacheOddsHandler cacheOddsHandler(RedisTemplate<String, Object> redisTemplate) {
        return new CacheOddsHandler(redisTemplate);
    }
    
    /**
     * Create the WebSocketOddsHandler
     */
    @Bean
    public WebSocketOddsHandler webSocketOddsHandler(SimpMessagingTemplate messagingTemplate) {
        return new WebSocketOddsHandler(messagingTemplate);
    }
    
    /**
     * Create the DatabaseOddsHandler
     */
    @Bean
    public DatabaseOddsHandler databaseOddsHandler(EventRepository eventRepository) {
        return new DatabaseOddsHandler(eventRepository);
    }

    /**
     * Sets up the chain of responsibility for odds updates.
     * Chain order:
     * 1. RandomOddsHandler - generates random odds
     * 2. KafkaOddsHandler - publishes to Kafka
     * 3. CacheOddsHandler - updates Redis cache
     * 4. WebSocketOddsHandler - pushes to clients
     * 5. DatabaseOddsHandler - persists to database
     */
    @Bean(name = "oddsUpdateHandlerChain")
    @Primary
    public EventHandler oddsUpdateHandler(
            RandomOddsHandler randomOddsHandler,
            KafkaOddsHandler kafkaOddsHandler,
            CacheOddsHandler cacheOddsHandler,
            WebSocketOddsHandler webSocketOddsHandler,
            DatabaseOddsHandler databaseOddsHandler) {
        
        // Set up the chain
        randomOddsHandler.setNext(kafkaOddsHandler);
        kafkaOddsHandler.setNext(cacheOddsHandler);
        cacheOddsHandler.setNext(webSocketOddsHandler);
        webSocketOddsHandler.setNext(databaseOddsHandler);
        
        // Return the first handler in the chain
        return randomOddsHandler;
    }
} 