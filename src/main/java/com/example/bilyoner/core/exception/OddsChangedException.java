package com.example.bilyoner.core.exception;

public class OddsChangedException extends RuntimeException {
    private final double currentOdds;

    public OddsChangedException() {
        super("Oran değişti, lütfen güncel oranla tekrar deneyin!");
        this.currentOdds = 0.0;
    }

    public OddsChangedException(String message) {
        super(message);
        this.currentOdds = 0.0;
    }

    public OddsChangedException(String message, double currentOdds) {
        super(message);
        this.currentOdds = currentOdds;
    }

    public double getCurrentOdds() {
        return currentOdds;
    }
} 