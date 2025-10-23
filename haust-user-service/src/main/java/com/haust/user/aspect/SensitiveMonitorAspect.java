package com.haust.user.aspect;

import com.haust.annotation.SensitiveMonitor;
import com.haust.common.domain.dto.CodingSharingDTO;
import com.haust.common.domain.dto.CreatePostDTO;
import com.haust.common.domain.dto.CreateReplyDTO;
import com.haust.common.domain.enumeration.ContentType;
import com.haust.common.exception.BusinessException;
import com.haust.common.util.BloomFilterUtil;
import com.haust.common.util.IKAnalyzerUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class SensitiveMonitorAspect {
    private final BloomFilterUtil bloomFilterUtil;

    /**
     * 做一个切面接口
     */
    @Pointcut("@annotation(com.haust.annotation.SensitiveMonitor)")
    public void sensitiveMonitorPointcut(){}

    /**
     * 对敏感词进行处理
     *
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("sensitiveMonitorPointcut()")
    public Object aroundSensitiveMonitor(ProceedingJoinPoint joinPoint)throws Throwable{
        log.info("敏感词切面处理");
        // 1.获得当前增强方法注解
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        SensitiveMonitor annotation = signature.getMethod().getAnnotation(SensitiveMonitor.class);
        // 2. 获得注解中的枚举类
        ContentType value = annotation.value();
        Object[] args = joinPoint.getArgs();
        // 3. 获取内容值
        if(value==ContentType.reply){
            // 4. 处理评论区内容
            CreateReplyDTO createReplyDTO = (CreateReplyDTO) args[0];
            String content = createReplyDTO.getContent();
            solve(content);
        } else if (value == ContentType.sharing) {
            //  4. 处理内推信息
            CodingSharingDTO codingSharingDTO = (CodingSharingDTO) args[0];
            String content = codingSharingDTO.getDetail();
            solve(content);
        } else if (value == ContentType.post) {
            //  4. 处理帖子内容
            CreatePostDTO createPostDTO = (CreatePostDTO) args[0];
            String content = createPostDTO.getDescription();
            solve(content);
        }
        return joinPoint.proceed();
    }

    private void solve(String content) {
        // 1. 使用 IKAnalyzerUtil 对 content 进行分词
        List<String> words = IKAnalyzerUtil.tokenize(content);
        // 2. 遍历分词结果，使用 BloomFilterUtil 检测敏感词
        for (String word : words) {
            if (bloomFilterUtil.existsItem(word)) {
                // 3. 如果检测到敏感词，进行处理（例如记录日志或替换）
                throw new BusinessException("THE ITEM IS WRONG","敏感词存在");
            }
        }
    }
}
