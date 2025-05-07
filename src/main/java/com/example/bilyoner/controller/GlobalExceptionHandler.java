package com.example.bilyoner.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import com.example.bilyoner.core.exception.EventNotFoundException;
import com.example.bilyoner.core.exception.BetLimitExceededException;
import com.example.bilyoner.core.exception.StakeLimitExceededException;
import com.example.bilyoner.core.exception.OddsChangedException;
import com.example.bilyoner.core.exception.BetTimeoutException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return ResponseEntity.badRequest().body(error);
    }
    
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        
        // Check if this is a timeout exception
        if (ex.getMessage() != null && ex.getMessage().contains("timed out")) {
            return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body(error);
        }
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
    
    @ExceptionHandler(TimeoutException.class)
    public ResponseEntity<Map<String, String>> handleTimeoutException(TimeoutException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "İşlem zaman aşımına uğradı. Lütfen daha sonra tekrar deneyin.");
        return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body(error);
    }
    
    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    public ResponseEntity<Map<String, String>> handleValidationExceptions(Exception ex) {
        Map<String, String> error = new HashMap<>();
        if (ex instanceof MethodArgumentNotValidException) {
            ((MethodArgumentNotValidException) ex).getBindingResult().getFieldErrors().forEach(fieldError -> {
                error.put(fieldError.getField(), fieldError.getDefaultMessage());
            });
        } else if (ex instanceof BindException) {
            ((BindException) ex).getBindingResult().getFieldErrors().forEach(fieldError -> {
                error.put(fieldError.getField(), fieldError.getDefaultMessage());
            });
        }
        return ResponseEntity.badRequest().body(error);
    }
    
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, String>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Parametre '" + ex.getName() + "' geçersiz bir değer içeriyor: " + ex.getValue());
        return ResponseEntity.badRequest().body(error);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenericException(Exception ex) {
        Map<String, String> error = new HashMap<>();
        // Eğer static resource hatasıysa, 404 dön
        if (ex instanceof NoResourceFoundException) {
            error.put("error", "Kaynak bulunamadı: " + ((NoResourceFoundException) ex).getResourcePath());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
        error.put("error", "Beklenmeyen bir hata oluştu: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(EventNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleEventNotFound(EventNotFoundException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Event not found");
        error.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(BetLimitExceededException.class)
    public ResponseEntity<Map<String, String>> handleBetLimitExceeded(BetLimitExceededException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Bet limit exceeded");
        error.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(error);
    }

    @ExceptionHandler(StakeLimitExceededException.class)
    public ResponseEntity<Map<String, String>> handleStakeLimitExceeded(StakeLimitExceededException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Stake limit exceeded");
        error.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(error);
    }

    @ExceptionHandler(OddsChangedException.class)
    public ResponseEntity<Map<String, Object>> handleOddsChanged(OddsChangedException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("error", "Odds changed");
        error.put("message", ex.getMessage());
        error.put("currentOdds", ex.getCurrentOdds());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(BetTimeoutException.class)
    public ResponseEntity<Map<String, String>> handleBetTimeout(BetTimeoutException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Timeout");
        error.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body(error);
    }
} 