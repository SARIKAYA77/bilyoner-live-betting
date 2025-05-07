package com.example.bilyoner.repository;

import com.example.bilyoner.model.Bet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BetRepository extends JpaRepository<Bet, Long> {
    List<Bet> findByEventIdAndIsActiveTrue(Long eventId);
    List<Bet> findByCustomerIdAndIsActiveTrue(Long customerId);

    @Query("SELECT COALESCE(SUM(b.multipleCount), 0) FROM Bet b WHERE b.event.id = :eventId")
    int sumMultipleCountByEventId(@Param("eventId") Long eventId);
} 