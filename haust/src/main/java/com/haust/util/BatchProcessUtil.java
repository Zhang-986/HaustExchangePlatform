package com.haust.util;

import cn.hutool.core.bean.BeanUtil;
import lombok.RequiredArgsConstructor;
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
    public void process(Object msg){
        blockingQueue.offer(msg);
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
                if(BeanUtil.isEmpty(list)){
                    return;
                }
                // TODO redis合并处理
            }
        });
    }
}
