package com.haust.mq;

import com.haust.constant.MqExchangeConstant;
import com.haust.constant.MqKeyConstant;
import com.haust.constant.MqQueueConstant;
import com.haust.mq.msg.BehaviorMsg;
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
public class BehaviorInfoListener {
    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = MqQueueConstant.BEHAVIOR_MONITOR_QUEUE, durable = "true"),
                    exchange = @Exchange(value = MqExchangeConstant.BEHAVIOR_MONITOR_EXCHANGE , type = ExchangeTypes.TOPIC),
                    key = MqKeyConstant.BEHAVIOR_MONITOR_KEY
            )
    )
    public void solveBehaviorInfo(BehaviorMsg msg){
        log.info("{}",msg);
    }
}
