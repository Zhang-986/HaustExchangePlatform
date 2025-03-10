package com.haust.interceptor;

import com.haust.context.BaseContext;
import com.haust.util.AuthUtil;
import com.haust.util.JwtUtil;
import io.netty.util.internal.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@RequiredArgsConstructor
@Component
@Slf4j
public class JwtTokenUserInterceptor implements HandlerInterceptor {
    private final AuthUtil authUtil;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 获取JWT令牌
        String token = request.getHeader("token");
        // 2. 校验令牌
        boolean temp = JwtUtil.validateToken(token);
        if(!temp){
            // 当前令牌不合法
            response.setStatus(401);
            return false;
        }
        // 3. 用户这边得到信息，从redis进行比对
        String userId = JwtUtil.getUserIdFromToken(token);
        String redisToken = authUtil.getToken(userId);
        // 3.5 当前redis拿出来的token与拿到的进行比较
        if (StringUtil.isNullOrEmpty(redisToken)) {
            // TODO 封装类进行请求的处理
            response.setStatus(401);
            return false;
        }
        // 4. 存入当前用户线程
        BaseContext.setId(Long.valueOf(userId));
        log.info("{}",userId);
        return true;
    }
}
