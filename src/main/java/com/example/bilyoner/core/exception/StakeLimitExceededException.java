package com.example.bilyoner.core.exception;

public class StakeLimitExceededException extends RuntimeException {
    public StakeLimitExceededException() {
        super("Toplam yatırım limiti 10.000 TL'yi aşamaz!");
    }
} 