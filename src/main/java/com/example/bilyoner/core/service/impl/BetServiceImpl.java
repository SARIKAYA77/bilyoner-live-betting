package com.example.bilyoner.core.service.impl;

import com.example.bilyoner.core.domain.Bet;
import com.example.bilyoner.core.domain.Bet.BetType;
import com.example.bilyoner.core.domain.Event;
import com.example.bilyoner.core.mapper.BetMapper;
import com.example.bilyoner.core.service.BetService;
import com.example.bilyoner.core.service.EventService;
import com.example.bilyoner.repository.BetRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.example.bilyoner.dto.BetSlipRequestDTO;
import com.example.bilyoner.dto.BetSlipResponseDTO;

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
    private final long timeoutSeconds;

    public BetServiceImpl(BetRepository betRepository, EventService eventService, @Value("${bet.timeout.seconds:2}") long timeoutSeconds) {
        this.betRepository = betRepository;
        this.eventService = eventService;
        this.timeoutSeconds = timeoutSeconds;
    }

    @Override
    @CircuitBreaker(name = BETTING_SERVICE, fallbackMethod = "placeBetFallback")
    public CompletableFuture<Bet> placeBetAsync(Long eventId, Long customerId, BetType betType, Integer multipleCount, Double stakeAmount, Double oddsAtBet) {
        return CompletableFuture.supplyAsync(() -> placeBet(eventId, customerId, betType, multipleCount, stakeAmount, oddsAtBet))
                .orTimeout(timeoutSeconds, TimeUnit.SECONDS)
                .handle((result, ex) -> {
                    if (ex == null) return result;
                    Throwable cause = ex;
                    if (ex instanceof java.util.concurrent.CompletionException && ex.getCause() != null) {
                        cause = ex.getCause();
                    }
                    if (cause instanceof java.util.concurrent.TimeoutException) {
                        throw new com.example.bilyoner.core.exception.BetTimeoutException();
                    }
                    if (cause instanceof RuntimeException) {
                        throw (RuntimeException) cause;
                    }
                    throw new RuntimeException(cause);
                });
    }

    @Override
    @Transactional
    @CircuitBreaker(name = BETTING_SERVICE, fallbackMethod = "placeBetFallback")
    public Bet placeBet(Long eventId, Long customerId, BetType betType, Integer multipleCount, Double stakeAmount, Double oddsAtBet) {
        // Event'i getir
        Event event = eventService.getEventById(eventId);

        // Sadece aktif ve future eventlere izin ver
        if (!event.isActive() || event.getStartTime().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Event is not active or already started/finished");
        }

        // Validate bet constraints
        validateBetConstraints(multipleCount, stakeAmount);
        
        // Get odds based on bet type
        double currentOdds = getOddsForBetType(event, betType);
        
        // Odds kontrolü
        if (Double.compare(currentOdds, oddsAtBet) != 0) {
            throw new com.example.bilyoner.core.exception.OddsChangedException("Oran değişti, lütfen güncel oranla tekrar deneyin!", currentOdds);
        }
        
        // Create bet domain object
        Bet bet = new Bet();
        bet.setEventId(eventId);
        bet.setCustomerId(customerId);
        bet.setBetType(betType);
        bet.setMultipleCount(multipleCount);
        bet.setStakeAmount(stakeAmount);
        bet.setOddsAtBet(currentOdds);
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
    public Bet placeBetFallback(Long eventId, Long customerId, BetType betType, Integer multipleCount, Double stakeAmount, Double oddsAtBet, Exception ex) {
        // Log the exception
        System.err.println("Circuit breaker triggered for bet placement: " + ex.getMessage());
        if (ex instanceof RuntimeException) {
            throw (RuntimeException) ex;
        }
        throw new RuntimeException(ex);
    }
    
    /**
     * Fallback method for async Circuit Breaker
     */
    public CompletableFuture<Bet> placeBetAsyncFallback(Long eventId, Long customerId, BetType betType, Integer multipleCount, Double stakeAmount, Double oddsAtBet, Exception ex) {
        System.err.println("Circuit breaker triggered for async bet placement: " + ex.getMessage());
        CompletableFuture<Bet> future = new CompletableFuture<>();
        if (ex instanceof RuntimeException) {
            future.completeExceptionally(ex);
        } else {
            future.completeExceptionally(new RuntimeException(ex));
        }
        return future;
    }

    @Override
    public BetSlipResponseDTO placeBetSlip(BetSlipRequestDTO betSlipRequestDTO, Long customerId) {
        List<BetSlipResponseDTO.BetResultDTO> results = new java.util.ArrayList<>();
        for (BetSlipRequestDTO.SingleBetDTO singleBet : betSlipRequestDTO.getBets()) {
            BetSlipResponseDTO.BetResultDTO result = new BetSlipResponseDTO.BetResultDTO();
            result.setEventId(singleBet.getEventId());
            result.setBetType(singleBet.getBetType());
            // Önce tek kupon için multipleCount limiti kontrolü
            if (betSlipRequestDTO.getMultipleCount() > MAX_MULTIPLE_COUNT) {
                result.setStatus("LIMIT_ERROR");
                result.setMessage("Bir kuponda en fazla " + MAX_MULTIPLE_COUNT + " adet oynayabilirsiniz.");
                results.add(result);
                continue;
            }
            // Sonra toplam limit kontrolü
            int totalMultipleForEvent = betRepository.sumMultipleCountByEventId(singleBet.getEventId());
            int newTotal = totalMultipleForEvent + betSlipRequestDTO.getMultipleCount();
            int remaining = MAX_MULTIPLE_COUNT - totalMultipleForEvent;
            if (newTotal > MAX_MULTIPLE_COUNT) {
                result.setStatus("LIMIT_EXCEEDED");
                if (remaining <= 0) {
                    result.setMessage("Bu maça toplamda maksimum 500 adet kupon oynanabilir. Artık yeni kupon oynanamaz.");
                } else {
                    result.setMessage("Bu maça toplamda maksimum 500 adet kupon oynanabilir. Şu ana kadar "
                        + totalMultipleForEvent + " adet oynandı, en fazla " + remaining + " adet daha oynayabilirsiniz.");
                }
                results.add(result);
                continue;
            }
            // Event ve oran kontrolleri
            Event event = eventService.getEventById(singleBet.getEventId());
            if (!event.isActive() || event.getStartTime().isBefore(java.time.LocalDateTime.now())) {
                result.setStatus("INACTIVE_EVENT");
                result.setMessage("Event is not active or already started/finished");
                results.add(result);
                continue;
            }
            double currentOdds = getOddsForBetType(event, BetType.valueOf(singleBet.getBetType()));
            if (Double.compare(currentOdds, singleBet.getOddsAtBet()) != 0) {
                result.setStatus("ODDS_CHANGED");
                result.setCurrentOdds(currentOdds);
                result.setMessage("Oran değişti, lütfen güncel oranla tekrar deneyin!");
                results.add(result);
                continue;
            }
            // Limit ve stake kontrolü
            try {
                validateBetConstraints(betSlipRequestDTO.getMultipleCount(), betSlipRequestDTO.getStakeAmount());
            } catch (Exception e) {
                result.setStatus("LIMIT_ERROR");
                result.setMessage(e.getMessage());
                results.add(result);
                continue;
            }
            // Başarılı ise bet kaydet
            Bet bet = new Bet();
            bet.setEventId(singleBet.getEventId());
            bet.setCustomerId(customerId);
            bet.setBetType(BetType.valueOf(singleBet.getBetType()));
            bet.setMultipleCount(betSlipRequestDTO.getMultipleCount());
            bet.setStakeAmount(betSlipRequestDTO.getStakeAmount());
            bet.setOddsAtBet(currentOdds);
            bet.setPlacedAt(java.time.LocalDateTime.now());
            bet.setActive(true);
            com.example.bilyoner.model.Bet betEntity = BetMapper.toEntity(bet);
            betRepository.save(betEntity);
            result.setStatus("OK");
            results.add(result);
        }
        BetSlipResponseDTO response = new BetSlipResponseDTO();
        response.setSuccess(results.stream().allMatch(r -> "OK".equals(r.getStatus())));
        response.setResults(results);
        return response;
    }
} 