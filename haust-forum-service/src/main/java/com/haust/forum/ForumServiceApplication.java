package com.haust.forum.forum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {"com.haust.forum", "com.haust.common"})
@EnableDiscoveryClient
@EnableFeignClients
public class ForumServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ForumServiceApplication.class, args);
    }
}
