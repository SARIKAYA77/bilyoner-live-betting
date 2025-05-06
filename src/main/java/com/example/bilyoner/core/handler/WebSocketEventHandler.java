package com.example.bilyoner.core.handler;

import com.example.bilyoner.core.domain.Event;
import org.springframework.messaging.simp.SimpMessagingTemplate;

/**
 * Handler that sends event updates to WebSocket clients
 */
public class WebSocketEventHandler implements EventHandler {
    private final SimpMessagingTemplate messagingTemplate;
    private EventHandler next;

    public WebSocketEventHandler(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void handle(Event event) {
        System.out.println("WebSocketEventHandler: Mesaj gönderiliyor: " + event);
        messagingTemplate.convertAndSend("/topic/events", event);
        
        if (next != null) {
            next.handle(event);
        }
    }

    @Override
    public void setNext(EventHandler handler) {
        this.next = handler;
    }
} 