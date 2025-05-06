package com.example.bilyoner.core.handler;

import com.example.bilyoner.core.domain.Event;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * Handler that caches events in Redis
 */
public class CacheEventHandler implements EventHandler {
    private final RedisTemplate<String, Object> redisTemplate;
    private EventHandler next;

    public CacheEventHandler(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void handle(Event event) {
        // Cache the event in Redis
        redisTemplate.opsForValue().set("event:" + event.getId().toString(), event);
        
        if (next != null) {
            next.handle(event);
        }
    }

    @Override
    public void setNext(EventHandler handler) {
        this.next = handler;
    }
} 