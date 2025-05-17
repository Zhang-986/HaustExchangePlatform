package com.haust;

import com.alibaba.dashscope.utils.ApiKey;
import com.haust.properties.ApiProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * 这是GYH的PR
 */
@SpringBootApplication
public class HaustApplication {
    public static void main(String[] args) {
        SpringApplication.run(HaustApplication.class, args);
    }
}
