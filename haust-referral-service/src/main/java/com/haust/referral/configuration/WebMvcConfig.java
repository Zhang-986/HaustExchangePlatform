package com.haust.referral.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    // Configuration for referral service
    // No interceptors needed as authentication is handled by gateway/user service
}
