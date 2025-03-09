package com.haust;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HaustApplication {
    public static void main(String[] args) {
        //测试能否pull
        SpringApplication.run(HaustApplication.class, args);
    }
}
