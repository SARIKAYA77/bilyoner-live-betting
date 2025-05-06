package com.example.bilyoner.core.mapper;

import com.example.bilyoner.core.domain.Event;

public class EventMapper {
    public static com.example.bilyoner.model.Event toEntity(Event event) {
        com.example.bilyoner.model.Event entity = new com.example.bilyoner.model.Event();
        entity.setId(event.getId());
        entity.setLeagueName(event.getLeagueName());
        entity.setHomeTeam(event.getHomeTeam());
        entity.setAwayTeam(event.getAwayTeam());
        entity.setHomeWinOdds(event.getHomeWinOdds());
        entity.setDrawOdds(event.getDrawOdds());
        entity.setAwayWinOdds(event.getAwayWinOdds());
        entity.setStartTime(event.getStartTime());
        entity.setIsActive(event.isActive());
        return entity;
    }
    public static Event toDomain(com.example.bilyoner.model.Event entity) {
        return new Event(
            entity.getId(),
            entity.getLeagueName(),
            entity.getHomeTeam(),
            entity.getAwayTeam(),
            entity.getHomeWinOdds(),
            entity.getDrawOdds(),
            entity.getAwayWinOdds(),
            entity.getStartTime(),
            entity.getIsActive()
        );
    }
} 