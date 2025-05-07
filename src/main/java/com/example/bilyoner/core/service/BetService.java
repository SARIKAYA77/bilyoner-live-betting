package com.example.bilyoner.core.service;

import com.example.bilyoner.core.domain.Bet;
import com.example.bilyoner.dto.BetSlipRequestDTO;
import com.example.bilyoner.dto.BetSlipResponseDTO;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Domain service for Bet-related operations
 */
public interface BetService {
    /**
     * Places a bet asynchronously with timeout handling
     * Uses CompletableFuture for asynchronous processing with timeout
     */
    CompletableFuture<Bet> placeBetAsync(Long eventId, Long customerId, Bet.BetType betType, Integer multipleCount, Double stakeAmount, Double oddsAtBet);
    
    /**
     * Places a bet synchronously
     * Includes validation for multiple count and stake limits
     */
    Bet placeBet(Long eventId, Long customerId, Bet.BetType betType, Integer multipleCount, Double stakeAmount, Double oddsAtBet);
    
    /**
     * Gets all active bets for a customer
     */
    List<Bet> getCustomerBets(Long customerId);
    
    /**
     * Gets all active bets for an event
     */
    List<Bet> getEventBets(Long eventId);

    BetSlipResponseDTO placeBetSlip(BetSlipRequestDTO betSlipRequestDTO, Long customerId);
} 