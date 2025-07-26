package com.pinkcat.quick_reserve_seller.common.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RefreshTokenStore {

    private final RedisTemplate<String, String> redisTemplate;
    private static final String PREFIX = "RT:ADMIN:";

    @Value("${jwt.refresh-expiration}")
    private long refreshExpiration;

    public void save(String userId, String refreshToken) {
        redisTemplate.opsForValue().set(PREFIX + userId, refreshToken, Duration.ofDays(refreshExpiration));
    }

    public String get(String userId) {
        return redisTemplate.opsForValue().get(PREFIX + userId);
    }

    public void delete(String userId) {
        redisTemplate.delete(PREFIX + userId);
    }

    public boolean isValid(String userId, String refreshToken) {
        String stored = get(userId);
        return refreshToken.equals(stored);
    }
}