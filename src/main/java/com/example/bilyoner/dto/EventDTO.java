package com.example.bilyoner.dto;

import java.time.LocalDateTime;

public class EventDTO {
    private Long id;
    private String leagueName;
    private String homeTeam;
    private String awayTeam;
    private Double homeWinOdds;
    private Double drawOdds;
    private Double awayWinOdds;
    private LocalDateTime startTime;

    public EventDTO() {}

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
} 