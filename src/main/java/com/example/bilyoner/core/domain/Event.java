package com.example.bilyoner.core.domain;

import java.time.LocalDateTime;

public class Event {
    private Long id;
    private String leagueName;
    private String homeTeam;
    private String awayTeam;
    private Double homeWinOdds;
    private Double drawOdds;
    private Double awayWinOdds;
    private LocalDateTime startTime;
    private boolean isActive = true;

    public Event() {}
    public Event(Long id, String leagueName, String homeTeam, String awayTeam, Double homeWinOdds, Double drawOdds, Double awayWinOdds, LocalDateTime startTime, boolean isActive) {
        this.id = id;
        this.leagueName = leagueName;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.homeWinOdds = homeWinOdds;
        this.drawOdds = drawOdds;
        this.awayWinOdds = awayWinOdds;
        this.startTime = startTime;
        this.isActive = isActive;
    }
    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getLeagueName() { return leagueName; }
    public void setLeagueName(String leagueName) { this.leagueName = leagueName; }
    public String getHomeTeam() { return homeTeam; }
    public void setHomeTeam(String homeTeam) { this.homeTeam = homeTeam; }
    public String getAwayTeam() { return awayTeam; }
    public void setAwayTeam(String awayTeam) { this.awayTeam = awayTeam; }
    public Double getHomeWinOdds() { return homeWinOdds; }
    public void setHomeWinOdds(Double homeWinOdds) { this.homeWinOdds = homeWinOdds; }
    public Double getDrawOdds() { return drawOdds; }
    public void setDrawOdds(Double drawOdds) { this.drawOdds = drawOdds; }
    public Double getAwayWinOdds() { return awayWinOdds; }
    public void setAwayWinOdds(Double awayWinOdds) { this.awayWinOdds = awayWinOdds; }
    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
    // Business logic
    public void updateOdds(Double homeWinOdds, Double drawOdds, Double awayWinOdds) {
        this.homeWinOdds = homeWinOdds;
        this.drawOdds = drawOdds;
        this.awayWinOdds = awayWinOdds;
    }
    public void deactivate() { this.isActive = false; }
} 