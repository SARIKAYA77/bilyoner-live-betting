package com.example.bilyoner.dto;

import com.example.bilyoner.model.Bet.BetType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.DecimalMin;

/**
 * Data Transfer Object for bet request
 */
public class BetRequestDTO {
    @NotNull(message = "Event ID zorunludur")
    private Long eventId;
    @NotNull(message = "Bahis türü zorunludur")
    private BetType betType;
    @NotNull(message = "Çoklu kupon adedi zorunludur")
    @Min(value = 1, message = "Çoklu kupon adedi en az 1 olmalı")
    private Integer multipleCount;
    @NotNull(message = "Bahis miktarı zorunludur")
    @DecimalMin(value = "0.01", message = "Bahis miktarı en az 0.01 olmalı")
    private Double stakeAmount;
    @NotNull(message = "Bahis oranı zorunludur")
    private Double oddsAtBet;

    public BetRequestDTO() {}

    public Long getEventId() { return eventId; }
    public void setEventId(Long eventId) { this.eventId = eventId; }
    public BetType getBetType() { return betType; }
    public void setBetType(BetType betType) { this.betType = betType; }
    public Integer getMultipleCount() { return multipleCount; }
    public void setMultipleCount(Integer multipleCount) { this.multipleCount = multipleCount; }
    public Double getStakeAmount() { return stakeAmount; }
    public void setStakeAmount(Double stakeAmount) { this.stakeAmount = stakeAmount; }
    public Double getOddsAtBet() { return oddsAtBet; }
    public void setOddsAtBet(Double oddsAtBet) { this.oddsAtBet = oddsAtBet; }
} 