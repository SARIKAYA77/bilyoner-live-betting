package com.example.bilyoner.core.exception;

public class BetLimitExceededException extends RuntimeException {
    public BetLimitExceededException() {
        super("Bir maça maksimum 500 bahis yapılabilir!");
    }
} 