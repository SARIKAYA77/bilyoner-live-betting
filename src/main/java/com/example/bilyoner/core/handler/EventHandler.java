package com.example.bilyoner.core.handler;

import com.example.bilyoner.core.domain.Event;

public interface EventHandler {
    void handle(Event event);
    void setNext(EventHandler handler);
} 