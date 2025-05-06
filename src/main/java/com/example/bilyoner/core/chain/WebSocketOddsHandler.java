package com.example.bilyoner.core.chain;

import com.example.bilyoner.core.domain.Event;
import org.springframework.messaging.simp.SimpMessagingTemplate;

/**
 * Handler that sends real-time odds updates to clients via WebSocket
 */
public class WebSocketOddsHandler extends OddsUpdateHandler {
    private final SimpMessagingTemplate messagingTemplate;
    private static final String ODDS_TOPIC = "/topic/odds";
    
    public WebSocketOddsHandler(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }
    
    @Override
    public void handle(Event event) {
        System.out.println("WebSocketOddsHandler: Mesaj gönderiliyor: " + event);
        messagingTemplate.convertAndSend(ODDS_TOPIC, event);
        handleNext(event);
    }
} 