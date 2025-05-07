package com.example.bilyoner.repository;

import com.example.bilyoner.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByIsActiveTrueAndStartTimeAfter(LocalDateTime now);
    Optional<Event> findByLeagueNameAndHomeTeamAndAwayTeamAndStartTime(String leagueName, String homeTeam, String awayTeam, java.time.LocalDateTime startTime);
} 