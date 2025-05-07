package com.example.bilyoner.dto;

import java.time.LocalDateTime;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;

public class EventDTO {
    private Long id;
    @NotBlank(message = "Lig adı zorunludur")
    private String leagueName;
    @NotBlank(message = "Ev sahibi takım adı zorunludur")
    private String homeTeam;
    @NotBlank(message = "Deplasman takım adı zorunludur")
    private String awayTeam;
    @NotNull(message = "Ev sahibi kazanır oranı zorunludur")
    @DecimalMin(value = "1.01", message = "Ev sahibi oranı en az 1.01 olmalı")
    private Double homeWinOdds;
    @NotNull(message = "Beraberlik oranı zorunludur")
    @DecimalMin(value = "1.01", message = "Beraberlik oranı en az 1.01 olmalı")
    private Double drawOdds;
    @NotNull(message = "Deplasman kazanır oranı zorunludur")
    @DecimalMin(value = "1.01", message = "Deplasman oranı en az 1.01 olmalı")
    private Double awayWinOdds;
    @NotNull(message = "Maç başlama zamanı zorunludur")
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