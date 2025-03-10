package com.haust.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;

public class JwtUtil {

    // 密钥
    private static final String SECRET_KEY = "$2a$10$KusLM9P0RxNB4a4ZcZ1Ocev/SbMLr8PMIZzYkNEZ1KPBMJuuc2.0W";
    // 过期时间（单位：毫秒）
    private static final long EXPIRATION_TIME = 3600 * 1000 * 8; // 8 小时

    /**
     * 生成 JWT
     *
     * @param userId 用户 ID
     * @return JWT 令牌
     */
    public static String generateToken(String userId) {
        return JWT.create()
                .withSubject(userId) // 主题（通常是用户 ID）
                .withIssuedAt(new Date()) // 签发时间
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // 过期时间
                .sign(Algorithm.HMAC256(SECRET_KEY)); // 使用 HMAC256 算法签名
    }

    /**
     * 解析 JWT
     *
     * @param token JWT 令牌
     * @return 解析后的 DecodedJWT 对象
     */
    public static DecodedJWT parseToken(String token) {
        return JWT.require(Algorithm.HMAC256(SECRET_KEY))
                .build()
                .verify(token);
    }

    /**
     * 验证 JWT 是否有效
     *
     * @param token JWT 令牌
     * @return 是否有效
     */
    public static boolean validateToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    /**
     * 从 JWT 中获取用户 ID
     *
     * @param token JWT 令牌
     * @return 用户 ID
     */
    public static String getUserIdFromToken(String token) {
        DecodedJWT decodedJWT = parseToken(token);
        return decodedJWT.getSubject();
    }

    public static void main(String[] args) {
        // 生成 JWT
        String userId = "123456";
        String token = generateToken(userId);
        System.out.println("Generated Token: " + token);

        // 验证 JWT
        boolean isValid = validateToken(token);
        System.out.println("Is Token Valid? " + isValid);

        // 从 JWT 中获取用户 ID
        String extractedUserId = getUserIdFromToken(token);
        System.out.println("Extracted User ID: " + extractedUserId);
    }
}