package com.example.bilyoner.core.service.impl;

import com.example.bilyoner.core.chain.RandomOddsHandler;
import com.example.bilyoner.core.domain.Event;
import com.example.bilyoner.core.mapper.EventMapper;
import com.example.bilyoner.core.service.EventService;
import com.example.bilyoner.repository.EventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of EventService with proper mapping between domain and entities
 */
@Service("coreEventService")
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final RandomOddsHandler randomOddsHandler;  // First handler in the chain

    public EventServiceImpl(EventRepository eventRepository, RandomOddsHandler randomOddsHandler) {
        this.eventRepository = eventRepository;
        this.randomOddsHandler = randomOddsHandler;
    }

    @Override
    @Transactional
    public Event createEvent(Event event) {
        // Convert domain event to entity and save
        com.example.bilyoner.model.Event eventEntity = EventMapper.toEntity(event);
        com.example.bilyoner.model.Event savedEntity = eventRepository.save(eventEntity);
        
        // Return the persisted event as domain object
        return EventMapper.toDomain(savedEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Event> getAllActiveEvents() {
        // Get all active events after current time
        List<com.example.bilyoner.model.Event> activeEvents = 
            eventRepository.findByIsActiveTrueAndStartTimeAfter(LocalDateTime.now());
        
        // Convert to domain objects
        return activeEvents.stream()
                .map(EventMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Event updateEventOdds(Long eventId, Double homeWinOdds, Double drawOdds, Double awayWinOdds) {
        // Find event entity
        com.example.bilyoner.model.Event eventEntity = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found: " + eventId));
        
        // Update odds
        eventEntity.setHomeWinOdds(homeWinOdds);
        eventEntity.setDrawOdds(drawOdds);
        eventEntity.setAwayWinOdds(awayWinOdds);
        
        // Save and return updated event
        com.example.bilyoner.model.Event updatedEntity = eventRepository.save(eventEntity);
        return EventMapper.toDomain(updatedEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Event getEventById(Long eventId) {
        com.example.bilyoner.model.Event eventEntity = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found: " + eventId));
        
        return EventMapper.toDomain(eventEntity);
    }

    @Override
    @Transactional
    public void updateAllEventOdds() {
        // Get all active events
        List<Event> activeEvents = getAllActiveEvents();
        
        // Process each event through the chain of responsibility, starting with random odds
        for (Event event : activeEvents) {
            randomOddsHandler.handle(event);
        }
    }
} 