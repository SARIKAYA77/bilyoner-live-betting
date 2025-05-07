package com.example.bilyoner.dto;

import java.util.List;

public class BetSlipRequestDTO {
    private List<SingleBetDTO> bets;
    private Double stakeAmount;
    private Integer multipleCount;

    public List<SingleBetDTO> getBets() { return bets; }
    public void setBets(List<SingleBetDTO> bets) { this.bets = bets; }
    public Double getStakeAmount() { return stakeAmount; }
    public void setStakeAmount(Double stakeAmount) { this.stakeAmount = stakeAmount; }
    public Integer getMultipleCount() { return multipleCount; }
    public void setMultipleCount(Integer multipleCount) { this.multipleCount = multipleCount; }

    public static class SingleBetDTO {
        private Long eventId;
        private String betType;
        private Double oddsAtBet;

        public Long getEventId() { return eventId; }
        public void setEventId(Long eventId) { this.eventId = eventId; }
        public String getBetType() { return betType; }
        public void setBetType(String betType) { this.betType = betType; }
        public Double getOddsAtBet() { return oddsAtBet; }
        public void setOddsAtBet(Double oddsAtBet) { this.oddsAtBet = oddsAtBet; }
    }
} 