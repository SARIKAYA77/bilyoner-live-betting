package com.example.bilyoner.core.chain;

import com.example.bilyoner.core.domain.Event;
import com.example.bilyoner.core.mapper.EventMapper;
import com.example.bilyoner.repository.EventRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Final handler in the chain that persists odds updates to the database
 */
public class DatabaseOddsHandler extends OddsUpdateHandler {
    private final EventRepository eventRepository;
    
    public DatabaseOddsHandler(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }
    
    @Override
    @Transactional
    public void handle(Event event) {
        // Convert domain event to entity and save to database
        com.example.bilyoner.model.Event eventEntity = EventMapper.toEntity(event);
        eventRepository.save(eventEntity);
        
        // This is the last handler in the chain, so we don't call handleNext
    }
} 