package com.example.bilyoner.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String leagueName;

    @Column(nullable = false)
    private String homeTeam;

    @Column(nullable = false)
    private String awayTeam;

    @Column(nullable = false)
    private Double homeWinOdds;

    @Column(nullable = false)
    private Double drawOdds;

    @Column(nullable = false)
    private Double awayWinOdds;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private Boolean isActive = true;

    public Event() {}

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
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
} 