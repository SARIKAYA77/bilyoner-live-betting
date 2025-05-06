package com.example.bilyoner.core.service.impl;

import com.example.bilyoner.core.domain.Bet;
import com.example.bilyoner.core.domain.Bet.BetType;
import com.example.bilyoner.core.domain.Event;
import com.example.bilyoner.core.mapper.BetMapper;
import com.example.bilyoner.core.service.BetService;
import com.example.bilyoner.core.service.EventService;
import com.example.bilyoner.repository.BetRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Implementation of BetService with Circuit Breaker pattern and timeout handling
 */
@Service
public class BetServiceImpl implements BetService {
    private final BetRepository betRepository;
    private final EventService eventService;
    
    // Circuit breaker configuration name
    private static final String BETTING_SERVICE = "bettingService";
    
    // Maximum bet constraints
    private static final int MAX_MULTIPLE_COUNT = 500;
    private static final double MAX_TOTAL_STAKE = 10000.0;
    
    // Timeout configuration
    private static final long TIMEOUT_SECONDS = 2;

    public BetServiceImpl(BetRepository betRepository, EventService eventService) {
        this.betRepository = betRepository;
        this.eventService = eventService;
    }

    @Override
    @CircuitBreaker(name = BETTING_SERVICE, fallbackMethod = "placeBetFallback")
    public CompletableFuture<Bet> placeBetAsync(Long eventId, Long customerId, BetType betType, Integer multipleCount, Double stakeAmount) {
        return CompletableFuture.supplyAsync(() -> placeBet(eventId, customerId, betType, multipleCount, stakeAmount))
                .orTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .exceptionally(ex -> {
                    // Log the exception and provide fallback
                    System.err.println("Bet placement timed out after " + TIMEOUT_SECONDS + " seconds: " + ex.getMessage());
                    throw new RuntimeException("Bet placement timed out. Please try again.");
                });
    }

    @Override
    @Transactional
    @CircuitBreaker(name = BETTING_SERVICE, fallbackMethod = "placeBetFallback")
    public Bet placeBet(Long eventId, Long customerId, BetType betType, Integer multipleCount, Double stakeAmount) {
        // Validate event exists and is active
        Event event = eventService.getEventById(eventId);
        if (!event.isActive()) {
            throw new IllegalStateException("Event is not active");
        }
        
        // Validate bet constraints
        validateBetConstraints(multipleCount, stakeAmount);
        
        // Get odds based on bet type
        double odds = getOddsForBetType(event, betType);
        
        // Create bet domain object
        Bet bet = new Bet();
        bet.setEventId(eventId);
        bet.setCustomerId(customerId);
        bet.setBetType(betType);
        bet.setMultipleCount(multipleCount);
        bet.setStakeAmount(stakeAmount);
        bet.setOddsAtBet(odds);
        bet.setPlacedAt(LocalDateTime.now());
        bet.setActive(true);
        
        // Map to entity and save
        com.example.bilyoner.model.Bet betEntity = BetMapper.toEntity(bet);
        com.example.bilyoner.model.Bet savedEntity = betRepository.save(betEntity);
        
        // Return domain object
        return BetMapper.toDomain(savedEntity);
    }

    private double getOddsForBetType(Event event, BetType betType) {
        return switch (betType) {
            case HOME_WIN -> event.getHomeWinOdds();
            case DRAW -> event.getDrawOdds();
            case AWAY_WIN -> event.getAwayWinOdds();
        };
    }

    private void validateBetConstraints(Integer multipleCount, Double stakeAmount) {
        // Validate multiple count
        if (multipleCount > MAX_MULTIPLE_COUNT) {
            throw new IllegalArgumentException("Maximum " + MAX_MULTIPLE_COUNT + " multiple bets allowed");
        }
        
        // Validate total stake
        double totalStake = multipleCount * stakeAmount;
        if (totalStake > MAX_TOTAL_STAKE) {
            throw new IllegalArgumentException("Maximum total stake is " + MAX_TOTAL_STAKE);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Bet> getCustomerBets(Long customerId) {
        List<com.example.bilyoner.model.Bet> betEntities = betRepository.findByCustomerIdAndIsActiveTrue(customerId);
        return betEntities.stream()
                .map(BetMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Bet> getEventBets(Long eventId) {
        List<com.example.bilyoner.model.Bet> betEntities = betRepository.findByEventIdAndIsActiveTrue(eventId);
        return betEntities.stream()
                .map(BetMapper::toDomain)
                .collect(Collectors.toList());
    }

    /**
     * Fallback method for Circuit Breaker
     */
    public Bet placeBetFallback(Long eventId, Long customerId, BetType betType, Integer multipleCount, Double stakeAmount, Exception ex) {
        // Log the exception
        System.err.println("Circuit breaker triggered for bet placement: " + ex.getMessage());
        
        // Provide fallback response or throw runtime exception
        throw new RuntimeException("Bet placement service is temporarily unavailable. Please try again later.");
    }
    
    /**
     * Fallback method for async Circuit Breaker
     */
    public CompletableFuture<Bet> placeBetAsyncFallback(Long eventId, Long customerId, BetType betType, Integer multipleCount, Double stakeAmount, Exception ex) {
        // Log the exception
        System.err.println("Circuit breaker triggered for async bet placement: " + ex.getMessage());
        
        // Provide fallback response or complete exceptionally
        CompletableFuture<Bet> future = new CompletableFuture<>();
        future.completeExceptionally(new RuntimeException("Bet placement service is temporarily unavailable. Please try again later."));
        return future;
    }
} 