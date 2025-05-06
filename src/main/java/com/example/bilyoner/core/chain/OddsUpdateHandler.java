package com.example.bilyoner.core.chain;

import com.example.bilyoner.core.domain.Event;
import com.example.bilyoner.core.handler.EventHandler;

/**
 * Base handler for the Chain of Responsibility pattern
 * to process event odds updates
 */
public abstract class OddsUpdateHandler implements EventHandler {
    protected OddsUpdateHandler nextHandler;
    
    @Override
    public void setNext(EventHandler nextHandler) {
        this.nextHandler = (OddsUpdateHandler) nextHandler;
    }
    
    @Override
    public abstract void handle(Event event);
    
    protected void handleNext(Event event) {
        if (nextHandler != null) {
            nextHandler.handle(event);
        }
    }
} 