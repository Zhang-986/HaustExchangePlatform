package com.haust.referral;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {"com.haust.referral", "com.haust.common"})
@EnableDiscoveryClient
@EnableFeignClients
public class  ReferralServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReferralServiceApplication.class, args);
    }
}
