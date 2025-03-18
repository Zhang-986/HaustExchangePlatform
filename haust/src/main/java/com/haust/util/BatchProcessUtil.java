package com.haust.util;

import com.haust.configuration.ThreadConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.config.ScheduledTask;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.concurrent.*;

/**
 * 对于热数据进行合并写数据库
 */
@Component
@RequiredArgsConstructor
public class BatchProcessUtil {
    // 创建共享有界队列
    // TODO 后期对热点数据进行处理
    private BlockingQueue<Object> blockingQueue = new LinkedBlockingQueue<>();
    // 调度器
    private ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(1);
    // 线程池处理
    private final ThreadPoolExecutor executor;

    // bean实例化之后，调度器进行使用
    @PostConstruct
    private void start(){
        //开启调度器
        scheduledExecutorService.scheduleAtFixedRate(this::consume,0,10, TimeUnit.SECONDS);
    }
    // 生产者，暴露给外界服务用的
    public void process(Object object){
        blockingQueue.offer(object);
    }
    // 消费者，进行消费共享队列中的数据
    private void consume(){
        executor.submit(new Runnable() {
            @Override
            public void run() {
                ArrayList<Object> list = new ArrayList<>();
                // 从共享队列当中拿取数据
                blockingQueue.drainTo(list);
                // 进行批量写请求
                // TODO 后期对应mapper层进行处理
            }
        });
    }
}
