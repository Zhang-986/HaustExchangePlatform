package com.haust.interceptor;

import com.haust.constant.UserConstant;
import com.haust.context.BaseContext;
import com.haust.exception.UserException;
import com.haust.util.AuthUtil;
import com.haust.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtTokenAdminInterceptor implements HandlerInterceptor {
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
        // 3. 是否是当前管理员
        String userId = JwtUtil.getUserIdFromToken(token);
        // 3.1 得到现在UserId
        String redisToken = authUtil.getToken(userId);
        if(!userId.equals("1")){
            // 不是对应用户
            response.setStatus(401);
            return false;
        }
        // 3.2 现在对redis里边验证
        if(redisToken.equals(token)){
            response.setStatus(401);
            return false;
        }
        // 4. 存入当前用户线程
        BaseContext.setId(Long.valueOf(userId));
        log.info("{}得到登入权限",userId);
        return true;
    }
}
