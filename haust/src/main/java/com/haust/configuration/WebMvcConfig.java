package com.haust.configuration;

import com.haust.interceptor.JwtTokenAdminInterceptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
@Slf4j
public class WebMvcConfig implements WebMvcConfigurer {
    private final JwtTokenAdminInterceptor jwtTokenAdminInterceptor;
    @Override
    public  void addInterceptors(InterceptorRegistry registry) {
        log.info("------------------开启Haust拦截器------------------");
        // 管理员口
        registry.addInterceptor(jwtTokenAdminInterceptor)
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin/login");

        // TODO 用户接口


    }
}
