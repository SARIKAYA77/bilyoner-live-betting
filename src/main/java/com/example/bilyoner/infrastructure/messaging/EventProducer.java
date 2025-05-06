package com.example.bilyoner.infrastructure.messaging;

import com.example.bilyoner.core.domain.Event;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class EventProducer {
    private final KafkaTemplate<String, Event> kafkaTemplate;
    private static final String TOPIC = "events";

    public EventProducer(KafkaTemplate<String, Event> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEvent(Event event) {
        kafkaTemplate.send(TOPIC, event.getId().toString(), event);
    }
} 