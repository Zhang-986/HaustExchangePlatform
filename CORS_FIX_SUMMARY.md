# CORS Fix Summary / CORS修复摘要

## 问题 (Problem)
前端（localhost:5173）访问后端API（localhost:8080）时出现CORS错误。

## 解决方案 (Solution)
在 Spring Cloud Gateway 中添加 CORS 配置。

## 修改的文件 (Modified Files)

### 新增文件 (New File)
- `haust-gateway/src/main/java/com/haust/gateway/config/CorsConfig.java`

## 关键代码 (Key Code)

```java
@Configuration
public class CorsConfig {
    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOriginPattern("*");
        config.setAllowCredentials(true);
        config.addAllowedMethod("*");
        config.addAllowedHeader("*");
        config.addExposedHeader("*");
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        
        return new CorsWebFilter(source);
    }
}
```

## 为什么这样修复 (Why This Works)

1. **Gateway 是入口**: 前端请求先到达 Gateway（8080端口），而不是直接访问各个微服务
2. **CORS 在 Gateway 处理**: 浏览器的 CORS 预检请求必须在 Gateway 层就得到正确响应
3. **Reactive Filter**: 使用 `CorsWebFilter` 因为 Gateway 基于 Spring WebFlux 响应式框架

## 测试方法 (How to Test)

### 快速测试
1. 启动 Eureka (8761)
2. 启动 Gateway (8080) - **必须重启以加载新配置**
3. 启动 User Service (8081)
4. 启动前端 (5173)
5. 在浏览器中打开 http://localhost:5173 并尝试登录

### 使用 curl 测试
```bash
curl -X OPTIONS http://localhost:8080/user/login \
  -H "Origin: http://localhost:5173" \
  -H "Access-Control-Request-Method: POST" \
  -H "Access-Control-Request-Headers: Content-Type" \
  -v
```

应该看到响应头包含:
```
Access-Control-Allow-Origin: http://localhost:5173
Access-Control-Allow-Credentials: true
```

## 预期结果 (Expected Result)

✅ 浏览器 Console 中不再有 CORS 错误  
✅ Network 标签中看到 OPTIONS 请求成功 (200)  
✅ POST /user/login 请求成功  
✅ 用户可以成功登录

## 注意事项 (Notes)

⚠️ **生产环境**: 将 `addAllowedOriginPattern("*")` 改为具体的前端域名
⚠️ **重启服务**: Gateway 必须重启才能加载新的 CORS 配置
⚠️ **清除缓存**: 测试时建议清除浏览器缓存

## 架构说明 (Architecture)

```
浏览器 (localhost:5173)
    ↓ HTTP请求
Gateway (localhost:8080) ← [CORS配置在这里]
    ↓ 转发请求
User Service (localhost:8081)
```

Gateway 的 CORS 配置保证了浏览器的预检请求能够成功，从而允许前端应用与后端API通信。

## 完整文档 (Full Documentation)
详细信息请参见: `CORS_FIX_GUIDE.md`
