package com.example.bilyoner.controller;

import com.example.bilyoner.core.domain.Event;
import com.example.bilyoner.core.service.EventService;
import com.example.bilyoner.dto.EventDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    /**
     * Create a new event
     */
    @PostMapping
    public ResponseEntity<EventDTO> createEvent(@RequestBody EventDTO eventDTO) {
        // Convert DTO to domain
        Event event = new Event();
        event.setLeagueName(eventDTO.getLeagueName());
        event.setHomeTeam(eventDTO.getHomeTeam());
        event.setAwayTeam(eventDTO.getAwayTeam());
        event.setHomeWinOdds(eventDTO.getHomeWinOdds());
        event.setDrawOdds(eventDTO.getDrawOdds());
        event.setAwayWinOdds(eventDTO.getAwayWinOdds());
        event.setStartTime(eventDTO.getStartTime());
        
        // Create event and return as DTO
        Event createdEvent = eventService.createEvent(event);
        return ResponseEntity.ok(mapToDTO(createdEvent));
    }

    /**
     * Get all active events (bulletin)
     */
    @GetMapping
    public ResponseEntity<List<EventDTO>> getAllActiveEvents() {
        List<Event> events = eventService.getAllActiveEvents();
        List<EventDTO> eventDTOs = events.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(eventDTOs);
    }

    /**
     * Update event odds
     */
    @PutMapping("/{eventId}/odds")
    public ResponseEntity<EventDTO> updateEventOdds(
            @PathVariable Long eventId,
            @RequestParam Double homeWinOdds,
            @RequestParam Double drawOdds,
            @RequestParam Double awayWinOdds) {
        
        Event updatedEvent = eventService.updateEventOdds(eventId, homeWinOdds, drawOdds, awayWinOdds);
        return ResponseEntity.ok(mapToDTO(updatedEvent));
    }

    /**
     * Helper method to map domain to DTO
     */
    private EventDTO mapToDTO(Event event) {
        EventDTO dto = new EventDTO();
        dto.setId(event.getId());
        dto.setLeagueName(event.getLeagueName());
        dto.setHomeTeam(event.getHomeTeam());
        dto.setAwayTeam(event.getAwayTeam());
        dto.setHomeWinOdds(event.getHomeWinOdds());
        dto.setDrawOdds(event.getDrawOdds());
        dto.setAwayWinOdds(event.getAwayWinOdds());
        dto.setStartTime(event.getStartTime());
        return dto;
    }
} 