package com.example.bilyoner.controller;

import com.example.bilyoner.core.domain.Event;
import com.example.bilyoner.core.service.EventService;
import com.example.bilyoner.dto.EventDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.HashMap;

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
    public ResponseEntity<?> createEvent(@RequestBody @Valid EventDTO eventDTO) {
        Optional<Event> existing = eventService.findByUniqueFields(
            eventDTO.getLeagueName(),
            eventDTO.getHomeTeam(),
            eventDTO.getAwayTeam(),
            eventDTO.getStartTime()
        );
        if (existing.isPresent()) {
            return ResponseEntity.status(409).body("Bu maç zaten bültende mevcut. Aynı maç tekrar eklenemez.");
        }
        Event createdEvent = eventService.createEvent(eventDTO);
        return ResponseEntity.ok(mapToDTO(createdEvent));
    }

    /**
     * Get all active events (bulletin)
     */
    @GetMapping("/active")
    public ResponseEntity<?> getAllActiveEvents() {
        List<Event> events = eventService.getAllActiveEvents();
        List<EventDTO> eventDTOs = events.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
        // Group by leagueName and sort by startTime
        Map<String, List<EventDTO>> grouped = eventDTOs.stream()
                .sorted(Comparator.comparing(EventDTO::getStartTime))
                .collect(Collectors.groupingBy(EventDTO::getLeagueName));
        // Build bulletin response
        List<Map<String, Object>> bulletin = new ArrayList<>();
        for (Map.Entry<String, List<EventDTO>> entry : grouped.entrySet()) {
            Map<String, Object> league = new HashMap<>();
            league.put("leagueName", entry.getKey());
            league.put("matches", entry.getValue());
            bulletin.add(league);
        }
        Map<String, Object> response = new HashMap<>();
        response.put("bulletin", bulletin);
        return ResponseEntity.ok(response);
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
     * Get event by id
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getEventById(@PathVariable Long id) {
        Event event = eventService.getEventById(id);
        if (event == null) {
            return ResponseEntity.status(404).body(Map.of("error", "Event not found"));
        }
        return ResponseEntity.ok(mapToDTO(event));
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