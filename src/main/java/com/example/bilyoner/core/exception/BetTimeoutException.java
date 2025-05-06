package com.example.bilyoner.core.exception;

public class BetTimeoutException extends RuntimeException {
    public BetTimeoutException() {
        super("Bet placement timed out after 2 seconds");
    }
} 