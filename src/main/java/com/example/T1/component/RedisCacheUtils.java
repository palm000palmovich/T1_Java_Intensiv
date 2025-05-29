package com.example.T1.component;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisCacheUtils {
    private final RedisTemplate<String, Object> redisTemplate;

    public RedisCacheUtils(RedisTemplate<String, Object> redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    public void putValue(String key, Object value, long timeoutInSeconds) {
        redisTemplate.opsForValue().set(key, value, timeoutInSeconds, TimeUnit.SECONDS);
    }

    // Получение данных из кэша
    public Object getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    // Удаление данных из кэша
    public void deleteValue(String key) {
        redisTemplate.delete(key);
    }

    // Проверка наличия ключа в кэше
    public boolean hasKey(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }
}
