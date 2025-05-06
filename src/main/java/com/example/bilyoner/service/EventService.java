package com.example.bilyoner.service;

import com.example.bilyoner.dto.EventDTO;
import com.example.bilyoner.model.Event;

import java.util.List;

public interface EventService {
    Event createEvent(EventDTO eventDTO);
    List<Event> getAllActiveEvents();
    void updateEventOdds(Long eventId, Double homeWinOdds, Double drawOdds, Double awayWinOdds);
} 