package com.example.bilyoner.infrastructure.config;

import com.example.bilyoner.core.service.EventService;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * Configuration for scheduled tasks
 */
@Configuration
@EnableScheduling
public class SchedulerConfig {

    private final EventService eventService;

    public SchedulerConfig(EventService eventService) {
        this.eventService = eventService;
    }

    @Scheduled(fixedRate = 1000)
    public void scheduleOddsUpdate() {
        eventService.updateAllEventOdds();
    }
} 