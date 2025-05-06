package com.example.bilyoner.dto;

import com.example.bilyoner.model.Bet.BetType;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for Bet
 */
public class BetDTO {
    private Long id;
    private Long eventId;
    private Long customerId;
    private BetType betType;
    private Integer multipleCount;
    private Double stakeAmount;
    private Double oddsAtBet;
    private LocalDateTime placedAt;
    private Boolean isActive;
    
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
    
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
} 