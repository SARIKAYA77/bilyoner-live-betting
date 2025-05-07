package com.example.bilyoner.core.service;

import com.example.bilyoner.core.domain.Event;
import java.util.List;
import java.util.Optional;

/**
 * Domain service for Event-related operations
 */
public interface EventService {
    /**
     * Creates a new event
     */
    Event createEvent(Event event);
    
    /**
     * Returns all active events (bulletin)
     */
    List<Event> getAllActiveEvents();
    
    /**
     * Updates the odds for an event
     */
    Event updateEventOdds(Long eventId, Double homeWinOdds, Double drawOdds, Double awayWinOdds);
    
    /**
     * Gets an event by its ID
     */
    Event getEventById(Long eventId);
    
    /**
     * Initiates an odds update for all active events
     * This will be scheduled to run every second
     */
    void updateAllEventOdds();

    Optional<Event> findByUniqueFields(String leagueName, String homeTeam, String awayTeam, java.time.LocalDateTime startTime);

    Event createEvent(com.example.bilyoner.dto.EventDTO eventDTO);
} 