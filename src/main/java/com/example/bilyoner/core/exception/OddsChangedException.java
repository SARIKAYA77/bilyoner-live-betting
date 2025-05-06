package com.example.bilyoner.core.exception;

public class OddsChangedException extends RuntimeException {
    public OddsChangedException() {
        super("Oran değişti, lütfen güncel oranla tekrar deneyin!");
    }
} 