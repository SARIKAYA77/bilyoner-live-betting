package com.example.bilyoner.core.mapper;

import com.example.bilyoner.core.domain.Bet;
import com.example.bilyoner.core.domain.Bet.BetType;

public class BetMapper {
    public static com.example.bilyoner.model.Bet toEntity(Bet bet) {
        com.example.bilyoner.model.Bet entity = new com.example.bilyoner.model.Bet();
        entity.setId(bet.getId());
        entity.setCustomerId(bet.getCustomerId());
        entity.setBetType(com.example.bilyoner.model.Bet.BetType.valueOf(bet.getBetType().name()));
        entity.setMultipleCount(bet.getMultipleCount());
        entity.setStakeAmount(bet.getStakeAmount());
        entity.setOddsAtBet(bet.getOddsAtBet());
        entity.setPlacedAt(bet.getPlacedAt());
        entity.setIsActive(bet.isActive());
        
        // Set event reference (if needed)
        if (bet.getEventId() != null) {
            com.example.bilyoner.model.Event eventEntity = new com.example.bilyoner.model.Event();
            eventEntity.setId(bet.getEventId());
            entity.setEvent(eventEntity);
        }
        
        return entity;
    }

    public static Bet toDomain(com.example.bilyoner.model.Bet entity) {
        return new Bet(
            entity.getId(),
            entity.getEvent() != null ? entity.getEvent().getId() : null,
            entity.getCustomerId(),
            BetType.valueOf(entity.getBetType().name()),
            entity.getMultipleCount(),
            entity.getStakeAmount(),
            entity.getOddsAtBet(),
            entity.getPlacedAt(),
            entity.getIsActive()
        );
    }
} 