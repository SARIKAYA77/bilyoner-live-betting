package com.example.bilyoner.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bets")
public class Bet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @Column(nullable = false)
    private Long customerId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BetType betType;

    @Column(nullable = false)
    private Integer multipleCount;

    @Column(nullable = false)
    private Double stakeAmount;

    @Column(nullable = false)
    private Double oddsAtBet;

    @Column(nullable = false)
    private LocalDateTime placedAt;

    @Column(nullable = false)
    private Boolean isActive = true;

    public Bet() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Event getEvent() { return event; }
    public void setEvent(Event event) { this.event = event; }
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

    public enum BetType {
        HOME_WIN,
        DRAW,
        AWAY_WIN
    }
} 