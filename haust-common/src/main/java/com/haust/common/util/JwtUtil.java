package com.haust.common.util;


import com.auth0.jwt.JWT;

import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import lombok.RequiredArgsConstructor;

import java.util.Date;

/**
 * JWT令牌生成与解析
 */
@RequiredArgsConstructor
public class JwtUtil {
    // 静态变量，用于存储配置
    private static String SECRET_KEY = "123123123891734324";
    private static long EXPIRATION_TIME = 288000000;


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


}