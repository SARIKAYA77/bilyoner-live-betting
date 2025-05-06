package com.example.bilyoner.core.handler;

import com.example.bilyoner.core.domain.Event;
import com.example.bilyoner.core.mapper.EventMapper;
import com.example.bilyoner.repository.EventRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Handler that saves events to the database
 */
public class DatabaseEventHandler implements EventHandler {
    private final EventRepository eventRepository;
    private EventHandler next;

    public DatabaseEventHandler(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    @Transactional
    public void handle(Event event) {
        // Save event to database asynchronously
        eventRepository.save(EventMapper.toEntity(event));
        
        if (next != null) {
            next.handle(event);
        }
    }

    @Override
    public void setNext(EventHandler handler) {
        this.next = handler;
    }
} 