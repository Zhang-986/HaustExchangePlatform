package com.haust.user.mq;

import cn.hutool.core.bean.BeanUtil;
import com.haust.common.constant.MqExchangeConstant;
import com.haust.common.constant.MqKeyConstant;
import com.haust.common.constant.MqQueueConstant;
import com.haust.user.mq.msg.UserMsg;
import com.haust.user.service.UserService;
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
public class UserInfoListener {
    private final UserService userService;
    /**
     * 用户登入信息侧面切入
     */
    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = MqQueueConstant.USER_MONITOR_QUEUE, durable = "true"),
                    exchange = @Exchange(value = MqExchangeConstant.USER_MONITOR_EXCHANGE , type = ExchangeTypes.TOPIC),
                    key = MqKeyConstant.USER_MONITOR_KEY
            )
    )
    public void userInfoListener(UserMsg userMsg){
        if(BeanUtil.isEmpty(userMsg)){
            return;
        }
        userService.addMonitor(userMsg);
    }
}
