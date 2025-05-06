package com.example.bilyoner.infrastructure.config;

import com.example.bilyoner.core.handler.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import com.example.bilyoner.repository.EventRepository;

/**
 * Configuration for Event Handler Chain of Responsibility pattern
 */
@Configuration
public class EventHandlerConfig {

    /**
     * Create the CacheEventHandler
     */
    @Bean
    public CacheEventHandler cacheEventHandler(RedisTemplate<String, Object> redisTemplate) {
        return new CacheEventHandler(redisTemplate);
    }
    
    /**
     * Create the WebSocketEventHandler
     */
    @Bean
    public WebSocketEventHandler webSocketEventHandler(SimpMessagingTemplate messagingTemplate) {
        return new WebSocketEventHandler(messagingTemplate);
    }
    
    /**
     * Create the DatabaseEventHandler
     */
    @Bean
    public DatabaseEventHandler databaseEventHandler(EventRepository eventRepository) {
        return new DatabaseEventHandler(eventRepository);
    }

    /**
     * Sets up the chain of responsibility for event processing.
     * Chain order:
     * 1. CacheEventHandler - caches events in Redis
     * 2. WebSocketEventHandler - sends updates to WebSocket clients
     * 3. DatabaseEventHandler - persists to database
     */
    @Bean(name = "eventHandlerChain")
    @Primary
    public EventHandler eventHandlerChain(
            CacheEventHandler cacheEventHandler,
            WebSocketEventHandler webSocketEventHandler,
            DatabaseEventHandler databaseEventHandler) {
        
        // Set up the chain
        cacheEventHandler.setNext(webSocketEventHandler);
        webSocketEventHandler.setNext(databaseEventHandler);
        
        // Return the first handler in the chain
        return cacheEventHandler;
    }
} 