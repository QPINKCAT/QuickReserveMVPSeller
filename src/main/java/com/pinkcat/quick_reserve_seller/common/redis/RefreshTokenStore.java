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
    private static final String PREFIX = "RT:SELLER:";

    @Value("${jwt.refresh-expiration}")
    private long refreshExpiration;

    public void save(Long userPk, String refreshToken) {
        redisTemplate.opsForValue().set(PREFIX + userPk, refreshToken, Duration.ofDays(refreshExpiration));
    }

    public String get(Long userPk) {
        return redisTemplate.opsForValue().get(PREFIX + userPk);
    }

    public void delete(Long userPk) {
        redisTemplate.delete(PREFIX + userPk);
    }

    public boolean isValid(Long userPk, String refreshToken) {
        String stored = get(userPk);
        return refreshToken.equals(stored);
    }
}