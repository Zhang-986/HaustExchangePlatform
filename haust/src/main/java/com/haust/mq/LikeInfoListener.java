package com.haust.mq;

import cn.hutool.core.bean.BeanUtil;
import com.haust.constant.MqExchangeConstant;
import com.haust.constant.MqKeyConstant;
import com.haust.constant.MqQueueConstant;
import com.haust.mq.msg.LikeMsg;
import com.haust.service.RepliesService;
import com.haust.service.impl.RepliesServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LikeInfoListener {
    private final RepliesService repliesService;
    /**
     * 点赞批量处理数据库
     */
    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = MqQueueConstant.BEHAVIOR_MONITOR_QUEUE, durable = "true"),
                    exchange = @Exchange(value = MqExchangeConstant.BEHAVIOR_MONITOR_EXCHANGE , type = ExchangeTypes.TOPIC),
                    key = MqKeyConstant.BEHAVIOR_MONITOR_KEY
            )
    )
    public void likeInfoListener(LikeMsg likeMsg){
        if(BeanUtil.isEmpty(likeMsg)){
            return;
        }
        repliesService.addLike(likeMsg);
    }
}
