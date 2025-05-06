package com.example.bilyoner.core.chain;

import com.example.bilyoner.core.domain.Event;
import com.example.bilyoner.infrastructure.messaging.KafkaProducer;

/**
 * Handler that publishes odds updates to Kafka for event-driven updates
 */
public class KafkaOddsHandler extends OddsUpdateHandler {
    private final KafkaProducer kafkaProducer;
    
    public KafkaOddsHandler(KafkaProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }
    
    @Override
    public void handle(Event event) {
        // Publish event with updated odds to Kafka
        kafkaProducer.publishOddsUpdate(event);
        
        // Continue to the next handler
        handleNext(event);
    }
} 