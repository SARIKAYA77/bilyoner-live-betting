package com.example.bilyoner.dto;

import com.example.bilyoner.model.Bet.BetType;

/**
 * Data Transfer Object for bet request
 */
public class BetRequestDTO {
    private Long eventId;
    private BetType betType;
    private Integer multipleCount;
    private Double stakeAmount;

    public BetRequestDTO() {}

    public Long getEventId() { return eventId; }
    public void setEventId(Long eventId) { this.eventId = eventId; }
    public BetType getBetType() { return betType; }
    public void setBetType(BetType betType) { this.betType = betType; }
    public Integer getMultipleCount() { return multipleCount; }
    public void setMultipleCount(Integer multipleCount) { this.multipleCount = multipleCount; }
    public Double getStakeAmount() { return stakeAmount; }
    public void setStakeAmount(Double stakeAmount) { this.stakeAmount = stakeAmount; }
} 