package com.example.bilyoner.infrastructure.messaging;

import com.example.bilyoner.core.domain.Event;
import com.example.bilyoner.core.mapper.EventMapper;
import com.example.bilyoner.core.handler.EventHandler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Kafka consumer for processing odds update events
 */
@Component
public class KafkaConsumer {
    // The first handler in the chain of responsibility
    private final EventHandler eventHandler;
    
    public KafkaConsumer(@Qualifier("eventHandlerChain") EventHandler eventHandler) {
        this.eventHandler = eventHandler;
    }
    
    /**
     * Listens for odds update events on the Kafka topic
     */
    @KafkaListener(topics = "betting.odds.updates", groupId = "betting-group")
    public void consumeOddsUpdate(com.example.bilyoner.model.Event eventEntity) {
        // Convert entity to domain model and process through chain
        Event domainEvent = EventMapper.toDomain(eventEntity);
        
        // Process the event through the chain of responsibility
        eventHandler.handle(domainEvent);
    }
} 