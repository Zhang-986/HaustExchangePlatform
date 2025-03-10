package com.haust.util;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AuthUtil {
    private final StringRedisTemplate redisTemplate;

    private static final String REDIS_TOKEN_PREFIX = "user_token:";

    // 保存 JWT 到 Redis
    public void saveToken(String userId, String token) {
        redisTemplate.opsForValue().set(REDIS_TOKEN_PREFIX + userId, token, 30, TimeUnit.DAYS);
    }

    // 从 Redis 中获取 Token
    public String getToken(String userId) {
        return redisTemplate.opsForValue().get(REDIS_TOKEN_PREFIX + userId);
    }

    // 删除 Redis 中的 Token（登出）
    public void deleteToken(String userId) {
        redisTemplate.delete(REDIS_TOKEN_PREFIX + userId);
    }
}
