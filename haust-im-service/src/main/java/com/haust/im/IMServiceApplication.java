package com.haust.im;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {"com.haust.im", "com.haust.common"})
@EnableDiscoveryClient
@EnableFeignClients
public class IMServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(IMServiceApplication.class, args);
    }
}
