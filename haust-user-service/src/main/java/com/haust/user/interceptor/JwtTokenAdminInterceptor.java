package com.haust.user.interceptor;

import cn.hutool.jwt.JWT;
import com.haust.common.constant.UserConstant;
import com.haust.common.context.BaseContext;
import com.haust.common.exception.UserException;
import com.haust.common.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtTokenAdminInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 获取JWT令牌
        String token = request.getHeader("token");
        // 2. 校验令牌
        boolean temp = JwtUtil.validateToken(token);
        if(!temp){
            // 当前令牌不合法
           writeResponse(response,401,UserConstant.JWT_FORMAT_FAIL);
           return false;
        }
        // 3. 是否是当前管理员
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
