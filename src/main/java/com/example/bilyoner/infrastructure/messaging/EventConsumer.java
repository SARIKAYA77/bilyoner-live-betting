package com.example.bilyoner.infrastructure.messaging;

import com.example.bilyoner.core.domain.Event;
import com.example.bilyoner.core.handler.EventHandler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class EventConsumer {
    private final EventHandler eventHandler;

    public EventConsumer(@Qualifier("eventHandlerChain") EventHandler eventHandler) {
        this.eventHandler = eventHandler;
    }

    @KafkaListener(topics = "events", groupId = "betting-group")
    public void consume(Event event) {
        eventHandler.handle(event);
    }
} 