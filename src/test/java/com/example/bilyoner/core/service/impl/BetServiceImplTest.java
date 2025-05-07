package com.example.bilyoner.core.service.impl;

import com.example.bilyoner.core.domain.Bet;
import com.example.bilyoner.core.domain.Event;
import com.example.bilyoner.core.service.EventService;
import com.example.bilyoner.repository.BetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BetServiceImplTest {
    private BetRepository betRepository;
    private EventService eventService;
    private BetServiceImpl betService;

    @BeforeEach
    void setUp() {
        betRepository = mock(BetRepository.class);
        eventService = mock(EventService.class);
        betService = new BetServiceImpl(betRepository, eventService, 2L);
    }

    @Test
    void testMultipleCountLimit() {
        Event event = new Event();
        event.setId(1L);
        event.setActive(true);
        event.setStartTime(LocalDateTime.now().plusMinutes(10));
        event.setHomeWinOdds(2.0);
        event.setDrawOdds(3.0);
        event.setAwayWinOdds(4.0);

        when(eventService.getEventById(1L)).thenReturn(event);
        when(betRepository.sumMultipleCountByEventId(1L)).thenReturn(0);

        Exception ex = assertThrows(IllegalArgumentException.class, () ->
            betService.placeBet(1L, 123L, Bet.BetType.HOME_WIN, 501, 10.0, 2.0)
        );
        assertTrue(ex.getMessage().contains("Maximum 500 multiple bets allowed"));
    }

    @Test
    void testOddsChanged() {
        Event event = new Event();
        event.setId(1L);
        event.setActive(true);
        event.setStartTime(LocalDateTime.now().plusMinutes(10));
        event.setHomeWinOdds(2.5);
        event.setDrawOdds(3.0);
        event.setAwayWinOdds(4.0);

        when(eventService.getEventById(1L)).thenReturn(event);
        when(betRepository.sumMultipleCountByEventId(1L)).thenReturn(0);

        Exception ex = assertThrows(RuntimeException.class, () ->
            betService.placeBet(1L, 123L, Bet.BetType.HOME_WIN, 1, 10.0, 2.0)
        );
        assertTrue(ex.getMessage().contains("Oran değişti"));
    }

    @Test
    void testInactiveEvent() {
        Event event = new Event();
        event.setId(1L);
        event.setActive(false);
        event.setStartTime(LocalDateTime.now().plusMinutes(10));
        event.setHomeWinOdds(2.0);
        event.setDrawOdds(3.0);
        event.setAwayWinOdds(4.0);

        when(eventService.getEventById(1L)).thenReturn(event);
        when(betRepository.sumMultipleCountByEventId(1L)).thenReturn(0);

        Exception ex = assertThrows(IllegalStateException.class, () ->
            betService.placeBet(1L, 123L, Bet.BetType.HOME_WIN, 1, 10.0, 2.0)
        );
        assertTrue(ex.getMessage().contains("Event is not active"));
    }
} 