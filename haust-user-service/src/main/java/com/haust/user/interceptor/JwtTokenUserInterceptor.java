package com.haust.user.interceptor;

import com.haust.common.constant.RedisConstant;
import com.haust.common.constant.UserConstant;
import com.haust.common.context.BaseContext;
import com.haust.common.exception.BusinessException;
import com.haust.common.result.ResultResponse;
import com.haust.common.util.JwtUtil;
import io.netty.util.internal.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@RequiredArgsConstructor
@Component
@Slf4j
public class JwtTokenUserInterceptor implements HandlerInterceptor {
    public final StringRedisTemplate template;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 获取JWT令牌
        String token = request.getHeader("token");
        // 2. 校验令牌
        boolean temp = JwtUtil.validateToken(token);

        if(!temp){
            // 当前令牌不合法
            writeResponse(response, 401, UserConstant.JWT_FORMAT_FAIL);
            return false;
        }
        // 3. 用户这边得到信息
        String userId = JwtUtil.getUserIdFromToken(token);

        // 4. 存入当前用户线程
        BaseContext.setId(Long.valueOf(userId));
        return true;
    }
    private void writeResponse(HttpServletResponse response, int code, String message) throws IOException {
        // 设置响应内容类型和状态码
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(code);

        // 构建 JSON 字符串
        String jsonResponse = String.format("{\"code\": %d, \"message\": \"%s\", \"data\": null}", code, message);

        // 将 JSON 写入响应
        response.getWriter().write(jsonResponse);
    }
}
