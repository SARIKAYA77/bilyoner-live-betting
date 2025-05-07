package com.example.bilyoner.dto;

import java.util.List;

public class BetSlipResponseDTO {
    private boolean success;
    private List<BetResultDTO> results;

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    public List<BetResultDTO> getResults() { return results; }
    public void setResults(List<BetResultDTO> results) { this.results = results; }

    public static class BetResultDTO {
        private Long eventId;
        private String betType;
        private String status; // "OK", "ODDS_CHANGED", "LIMIT_EXCEEDED", "ERROR"
        private Double currentOdds; // null if not odds changed
        private String message; // optional

        public Long getEventId() { return eventId; }
        public void setEventId(Long eventId) { this.eventId = eventId; }
        public String getBetType() { return betType; }
        public void setBetType(String betType) { this.betType = betType; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public Double getCurrentOdds() { return currentOdds; }
        public void setCurrentOdds(Double currentOdds) { this.currentOdds = currentOdds; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }
} 