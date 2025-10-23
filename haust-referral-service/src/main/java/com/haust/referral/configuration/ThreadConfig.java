package com.haust.referral.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class ThreadConfig {
    /**
     * IO密集型线程池
     */
    @Bean
    public ThreadPoolExecutor threadPoolExecutor(){
        // 获取 CPU 核心数
        int corePoolSize = Runtime.getRuntime().availableProcessors() * 2;
        int maxPoolSize = corePoolSize * 2; // 最大线程数为核心线程数的 2 倍
        long keepAliveTime = 60L; // 线程空闲时间（秒）
        int queueCapacity = 100; // 有界队列容量
        return new ThreadPoolExecutor(
                corePoolSize, // 核心线程数
                maxPoolSize, // 最大线程数
                keepAliveTime, // 线程空闲时间
                TimeUnit.SECONDS, // 时间单位
                new LinkedBlockingQueue<>(queueCapacity), // 有界队列
                Executors.defaultThreadFactory(), // 默认线程工厂
                new ThreadPoolExecutor.CallerRunsPolicy() // 拒绝策略：由调用线程执行任务
        );
    }
}
