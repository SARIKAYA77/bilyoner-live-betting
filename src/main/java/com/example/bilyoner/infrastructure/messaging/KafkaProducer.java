package com.example.bilyoner.infrastructure.messaging;

import com.example.bilyoner.core.domain.Event;
import com.example.bilyoner.core.mapper.EventMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Kafka producer for sending events to Kafka topics
 */
@Component
public class KafkaProducer {
    private static final String ODDS_UPDATE_TOPIC = "betting.odds.updates";
    private final KafkaTemplate<String, com.example.bilyoner.model.Event> kafkaTemplate;
    
    public KafkaProducer(KafkaTemplate<String, com.example.bilyoner.model.Event> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
    
    /**
     * Publishes an odds update event to Kafka
     */
    public void publishOddsUpdate(Event event) {
        // Convert domain event to entity for serialization
        com.example.bilyoner.model.Event eventEntity = EventMapper.toEntity(event);
        kafkaTemplate.send(ODDS_UPDATE_TOPIC, event.getId().toString(), eventEntity);
    }
} 