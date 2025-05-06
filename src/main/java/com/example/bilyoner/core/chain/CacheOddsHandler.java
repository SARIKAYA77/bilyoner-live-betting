package com.example.bilyoner.core.chain;

import com.example.bilyoner.core.domain.Event;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * Handler that updates odds in Redis cache for fast access
 */
public class CacheOddsHandler extends OddsUpdateHandler {
    private final RedisTemplate<String, Object> redisTemplate;
    
    public CacheOddsHandler(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    
    @Override
    public void handle(Event event) {
        // Cache the event in Redis with updated odds
        String cacheKey = "event:" + event.getId();
        redisTemplate.opsForValue().set(cacheKey, event);
        
        // Continue to the next handler
        handleNext(event);
    }
} 