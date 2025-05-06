package com.example.bilyoner.core.domain;

import java.time.LocalDateTime;

public class Bet {
    private Long id;
    private Long eventId;
    private Long customerId;
    private BetType betType;
    private Integer multipleCount;
    private Double stakeAmount;
    private Double oddsAtBet;
    private LocalDateTime placedAt;
    private boolean isActive = true;

    public enum BetType { HOME_WIN, DRAW, AWAY_WIN }

    public Bet() {}
    public Bet(Long id, Long eventId, Long customerId, BetType betType, Integer multipleCount, Double stakeAmount, Double oddsAtBet, LocalDateTime placedAt, boolean isActive) {
        this.id = id;
        this.eventId = eventId;
        this.customerId = customerId;
        this.betType = betType;
        this.multipleCount = multipleCount;
        this.stakeAmount = stakeAmount;
        this.oddsAtBet = oddsAtBet;
        this.placedAt = placedAt;
        this.isActive = isActive;
    }
    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getEventId() { return eventId; }
    public void setEventId(Long eventId) { this.eventId = eventId; }
    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
    public BetType getBetType() { return betType; }
    public void setBetType(BetType betType) { this.betType = betType; }
    public Integer getMultipleCount() { return multipleCount; }
    public void setMultipleCount(Integer multipleCount) { this.multipleCount = multipleCount; }
    public Double getStakeAmount() { return stakeAmount; }
    public void setStakeAmount(Double stakeAmount) { this.stakeAmount = stakeAmount; }
    public Double getOddsAtBet() { return oddsAtBet; }
    public void setOddsAtBet(Double oddsAtBet) { this.oddsAtBet = oddsAtBet; }
    public LocalDateTime getPlacedAt() { return placedAt; }
    public void setPlacedAt(LocalDateTime placedAt) { this.placedAt = placedAt; }
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
    // Business logic
    public void deactivate() { this.isActive = false; }
} 