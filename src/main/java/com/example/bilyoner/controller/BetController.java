package com.example.bilyoner.controller;

import com.example.bilyoner.core.domain.Bet;
import com.example.bilyoner.core.domain.Bet.BetType;
import com.example.bilyoner.core.service.BetService;
import com.example.bilyoner.dto.BetDTO;
import com.example.bilyoner.dto.BetRequestDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/bets")
public class BetController {

    private final BetService betService;

    public BetController(BetService betService) {
        this.betService = betService;
    }

    /**
     * Place a bet asynchronously
     * Includes timeout handling
     */
    @PostMapping
    public CompletableFuture<ResponseEntity<BetDTO>> placeBet(
            @RequestBody BetRequestDTO betRequestDTO,
            @RequestHeader("Customer-ID") Long customerId) {
        
        return betService.placeBetAsync(
                betRequestDTO.getEventId(),
                customerId,
                BetType.valueOf(betRequestDTO.getBetType().name()),
                betRequestDTO.getMultipleCount(),
                betRequestDTO.getStakeAmount()
            )
            .thenApply(bet -> ResponseEntity.ok(mapToDTO(bet)))
            .exceptionally(ex -> ResponseEntity.badRequest().body(null));
    }

    /**
     * Get all active bets for a customer
     */
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<BetDTO>> getCustomerBets(@PathVariable Long customerId) {
        List<Bet> bets = betService.getCustomerBets(customerId);
        List<BetDTO> betDTOs = bets.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(betDTOs);
    }

    /**
     * Get all active bets for an event
     */
    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<BetDTO>> getEventBets(@PathVariable Long eventId) {
        List<Bet> bets = betService.getEventBets(eventId);
        List<BetDTO> betDTOs = bets.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(betDTOs);
    }

    /**
     * Helper method to map domain to DTO
     */
    private BetDTO mapToDTO(Bet bet) {
        BetDTO dto = new BetDTO();
        dto.setId(bet.getId());
        dto.setEventId(bet.getEventId());
        dto.setCustomerId(bet.getCustomerId());
        dto.setBetType(com.example.bilyoner.model.Bet.BetType.valueOf(bet.getBetType().name()));
        dto.setMultipleCount(bet.getMultipleCount());
        dto.setStakeAmount(bet.getStakeAmount());
        dto.setOddsAtBet(bet.getOddsAtBet());
        dto.setPlacedAt(bet.getPlacedAt());
        dto.setIsActive(bet.isActive());
        return dto;
    }
} 