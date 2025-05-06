package com.example.bilyoner.service;

import com.example.bilyoner.core.domain.Event;
import com.example.bilyoner.core.mapper.EventMapper;
import com.example.bilyoner.repository.EventRepository;
import com.example.bilyoner.core.handler.EventHandler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class OddsUpdateScheduler {
    private final EventRepository eventRepository;
    private final EventHandler eventHandlerChain;

    public OddsUpdateScheduler(EventRepository eventRepository, @Qualifier("eventHandlerChain") EventHandler eventHandlerChain) {
        this.eventRepository = eventRepository;
        this.eventHandlerChain = eventHandlerChain;
    }

    @Scheduled(fixedRate = 5000)
    public void updateOdds() {
        System.out.println("OddsUpdateScheduler: Çalıştı");
        List<com.example.bilyoner.model.Event> activeEvents = eventRepository.findByIsActiveTrueAndStartTimeAfter(LocalDateTime.now());
        if (activeEvents.isEmpty()) {
            System.out.println("OddsUpdateScheduler: Aktif event yok");
            return;
        }
        com.example.bilyoner.model.Event eventEntity = activeEvents.get(0);
        Event event = EventMapper.toDomain(eventEntity);
        // Oranları random değiştir
        double newHome = randomize(event.getHomeWinOdds());
        double newDraw = randomize(event.getDrawOdds());
        double newAway = randomize(event.getAwayWinOdds());
        event.updateOdds(newHome, newDraw, newAway);
        System.out.println("OddsUpdateScheduler: Zincir başlatılıyor, eventId=" + event.getId());
        // Zinciri başlat
        eventHandlerChain.handle(event);
    }

    private double randomize(double odds) {
        double min = -0.05, max = 0.05;
        double variation = min + Math.random() * (max - min);
        double newOdds = odds * (1 + variation);
        return Math.max(1.01, Math.round(newOdds * 100.0) / 100.0);
    }
} 