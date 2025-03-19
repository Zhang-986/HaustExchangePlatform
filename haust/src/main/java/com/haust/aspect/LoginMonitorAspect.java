package com.haust.aspect;

import com.haust.constant.MqExchangeConstant;
import com.haust.constant.MqKeyConstant;
import com.haust.constant.MqQueueConstant;
import com.haust.domain.dto.AccountDTO;
import com.haust.mq.msg.UserMsg;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Aspect
@Slf4j
@Component
@RequiredArgsConstructor
public class LoginMonitorAspect {

    private final RabbitTemplate rabbitTemplate;
    // 定义切点，拦截带有 @LoginMonitor 注解的方法
    @Pointcut("@annotation(com.haust.annotation.LoginMonitor)")
    public void loginMonitorPointcut() {}

    /**
     * 对当前登入用户进行一个日志监控记录
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    // 在切点方法执行前后添加逻辑
    @Around("loginMonitorPointcut()")
    public Object aroundLoginMonitor(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("开始记录对应信息{}",joinPoint.getArgs());
        // 1.获取方法名
//        String methodName = joinPoint.getSignature().getName();

        // 2.获取方法参数
        Object[] args =  joinPoint.getArgs();

        // 3.得到accountDTO内容
        AccountDTO accountDTO =(AccountDTO) args[0];

        // 3.获取 IP 地址（需要 HttpServletRequest）
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String ip = getClientIp(request);

        // 3.5 传入到RabbitMq进行处理
        rabbitTemplate.convertAndSend(
                MqExchangeConstant.USER_MONITOR_EXCHANGE,
                MqKeyConstant.USER_MONITOR_KEY,
                UserMsg.of(accountDTO.getAccount(),ip, 1L,LocalDateTime.now())
        );
        // 5.继续执行原方法
        return joinPoint.proceed();
    }
    public String getClientIp(HttpServletRequest request) {
        // 从 X-Forwarded-For 头中获取 IP 地址链
        String ipAddress = request.getHeader("X-Forwarded-For");
        // 如果 X-Forwarded-For 为空，则从 X-Real-IP 头中获取
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("X-Real-IP");
        }
        // 如果仍然为空，则从 request.getRemoteAddr() 获取
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }
        // 如果 X-Forwarded-For 包含多个 IP 地址（如经过多个代理），取第一个 IP
        if (ipAddress != null && ipAddress.contains(",")) {
            ipAddress = ipAddress.split(",")[0].trim();
        }

        return ipAddress;
    }



}