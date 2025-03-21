package com.haust.util;
import cn.hutool.core.bean.BeanUtil;
import com.haust.mapper.PostReplyMapper;
import com.haust.mq.msg.LikeMsg;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.concurrent.*;
/**
 * 对于热数据进行合并写数据库
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class BatchProcessUtil {
    private final PostReplyMapper postReplyMapper;
    // 创建共享有界队列
    private BlockingQueue<LikeMsg> blockingQueue = new LinkedBlockingQueue<>(10000);
    // 调度器
    private ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(1);
    // 线程池处理
    private final ThreadPoolExecutor executor;

    // bean实例化之后，调度器进行使用
    @PostConstruct
    private void start(){
        //开启调度器
        scheduledExecutorService.scheduleWithFixedDelay(this::consume,10    ,60, TimeUnit.SECONDS);
    }
    // 生产者，暴露给外界服务用的
    public void process(LikeMsg msg){
        blockingQueue.offer(msg);
    }
    // 消费者，进行消费共享队列中的数据
    private void consume(){
        log.info("开始消费队列，当前队列大小: {}", blockingQueue.size());
        executor.submit(new Runnable() {
            @Override
            public void run() {
                ArrayList<LikeMsg> list = new ArrayList<>();
                // 从共享队列当中拿取数据
                blockingQueue.drainTo(list);
                // 进行批量写请求
                if(BeanUtil.isEmpty(list)){
                    return;
                }
                postReplyMapper.addLike(list);
            }
        });
    }
    @PreDestroy
    public void destroy() {
        scheduledExecutorService.shutdown();
        try {
            if (!scheduledExecutorService.awaitTermination(10, TimeUnit.SECONDS)) {
                scheduledExecutorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        // 处理队列中剩余的消息
        consume();
    }
}
