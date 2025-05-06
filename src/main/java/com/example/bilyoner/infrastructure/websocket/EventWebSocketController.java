package com.example.bilyoner.infrastructure.websocket;

import com.example.bilyoner.core.domain.Event;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class EventWebSocketController {

    @MessageMapping("/events")
    @SendTo("/topic/events")
    public Event handleEventUpdate(Event event) {
        return event;
    }
} 