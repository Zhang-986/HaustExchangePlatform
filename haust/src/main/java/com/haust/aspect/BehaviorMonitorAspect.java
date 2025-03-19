package com.haust.aspect;

import com.alibaba.fastjson.JSON;
import com.haust.constant.MqExchangeConstant;
import com.haust.constant.MqKeyConstant;
import com.haust.context.BaseContext;
import com.haust.domain.dto.AccountDTO;
import com.haust.domain.po.CodingSharing;
import com.haust.mq.msg.BehaviorMsg;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 这边做一个用户行为的监控记录，rabbitMq进行合并写请求异步处理
 */
@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class BehaviorMonitorAspect {
    private final RabbitTemplate rabbitTemplate;

    @Pointcut("@annotation(com.haust.annotation.BehaviorMonitor)")
    public void BehaviorMonitorPointCut(){}

    @Around("BehaviorMonitorPointCut()")
    public Object aroundBehaviorMonitor(ProceedingJoinPoint joinPoint) throws Throwable {
        // 1. 截取当前方法相关信息
        Object[] args = joinPoint.getArgs();
        // 2. 获取参数信息，拿到相关信息值
        Long userId = BaseContext.getId();
        Long targetId = (Long) args[0];
        // 3. rabbitMq进行异步解耦批量处理信息
        rabbitTemplate.convertAndSend(
                MqExchangeConstant.BEHAVIOR_MONITOR_EXCHANGE,
                MqKeyConstant.BEHAVIOR_MONITOR_KEY,
                BehaviorMsg.of(userId,"用户查看信息",String.valueOf(targetId), LocalDateTime.now())
        );
        return joinPoint.proceed();
    }
}
