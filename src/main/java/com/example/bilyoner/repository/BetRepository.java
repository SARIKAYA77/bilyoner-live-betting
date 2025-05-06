package com.example.bilyoner.repository;

import com.example.bilyoner.model.Bet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BetRepository extends JpaRepository<Bet, Long> {
    List<Bet> findByEventIdAndIsActiveTrue(Long eventId);
    List<Bet> findByCustomerIdAndIsActiveTrue(Long customerId);
} 