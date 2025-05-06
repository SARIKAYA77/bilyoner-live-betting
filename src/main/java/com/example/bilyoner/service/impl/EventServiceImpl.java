package com.example.bilyoner.service.impl;

import com.example.bilyoner.dto.EventDTO;
import com.example.bilyoner.model.Event;
import com.example.bilyoner.repository.EventRepository;
import com.example.bilyoner.service.EventService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    public EventServiceImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    @Transactional
    public Event createEvent(EventDTO eventDTO) {
        Event event = new Event();
        event.setLeagueName(eventDTO.getLeagueName());
        event.setHomeTeam(eventDTO.getHomeTeam());
        event.setAwayTeam(eventDTO.getAwayTeam());
        event.setHomeWinOdds(eventDTO.getHomeWinOdds());
        event.setDrawOdds(eventDTO.getDrawOdds());
        event.setAwayWinOdds(eventDTO.getAwayWinOdds());
        event.setStartTime(eventDTO.getStartTime());
        event.setIsActive(true);
        return eventRepository.save(event);
    }

    @Override
    public List<Event> getAllActiveEvents() {
        return eventRepository.findByIsActiveTrueAndStartTimeAfter(java.time.LocalDateTime.now());
    }

    @Override
    @Transactional
    public void updateEventOdds(Long eventId, Double homeWinOdds, Double drawOdds, Double awayWinOdds) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        event.setHomeWinOdds(homeWinOdds);
        event.setDrawOdds(drawOdds);
        event.setAwayWinOdds(awayWinOdds);
        eventRepository.save(event);
    }
} 