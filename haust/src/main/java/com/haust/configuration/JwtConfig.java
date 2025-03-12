package com.haust.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "haust.jwt")
@Data
public class JwtConfig {


    /**
     * 管理端员工生成jwt令牌相关配置
     */
    private static String adminSecretKey;
    private long adminTtl;
    private String adminTokenName;

    /**
     * 用户端微信用户生成jwt令牌相关配置
     */
    private String userSecretKey;
    private static long userTtl;
    private String userTokenName;


}
