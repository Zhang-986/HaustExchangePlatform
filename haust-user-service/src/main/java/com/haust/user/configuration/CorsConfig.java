package com.haust.user.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {
    
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        
        // 允许所有域名访问，生产环境建议指定具体域名
        config.addAllowedOriginPattern("*");
        
        // 允许跨域发送cookie
        config.setAllowCredentials(true);
        
        // 允许所有请求方法
        config.addAllowedMethod("*");
        
        // 允许所有头信息
        config.addAllowedHeader("*");
        
        // 添加暴露的头信息
        config.addExposedHeader("*");
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        
        return new CorsFilter(source);
    }
}