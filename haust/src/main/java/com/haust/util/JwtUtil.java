package com.haust.util;

import cn.hutool.core.bean.BeanUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.haust.configuration.JwtConfig;
import com.haust.domain.vo.PageVO;
import com.haust.domain.vo.RoleVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.util.Date;

@RequiredArgsConstructor
public class JwtUtil {
    private final JwtConfig config;
    // 静态变量，用于存储配置
    private static String SECRET_KEY;
    private static long EXPIRATION_TIME;
    @PostConstruct
    public void init() {
        SECRET_KEY = config.getSecretKey();
        EXPIRATION_TIME = config.getExpirationTime();
    }


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