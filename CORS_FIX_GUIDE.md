# CORS Fix Guide

## 问题描述 (Problem Description)

前端（运行在 `http://localhost:5173`）尝试访问后端API（`http://localhost:8080`）时，遇到了CORS（跨域资源共享）错误：

```
Access to XMLHttpRequest at 'http://localhost:8080/user/login' from origin 'http://localhost:5173' 
has been blocked by CORS policy: Response to preflight request doesn't pass access control check: 
No 'Access-Control-Allow-Origin' header is present on the requested resource.
```

## 根本原因 (Root Cause)

在微服务架构中：
- **前端** 运行在 `http://localhost:5173`
- **Gateway** 运行在端口 `8080`，作为所有微服务的入口
- **User Service** 运行在端口 `8081`，包含用户登录等功能

虽然 User Service 有 CORS 配置（见 `haust-user-service/src/main/java/com/haust/user/configuration/CorsConfig.java`），但是：
- 前端请求首先到达 **Gateway**（端口 8080）
- Gateway 没有 CORS 配置，因此在转发请求到 User Service 之前就会被浏览器的预检请求（preflight request）拦截
- 即使 User Service 有 CORS 配置，请求也无法到达那里

## 解决方案 (Solution)

在 Gateway 中添加 CORS 配置，允许跨域请求：

### 新增文件

**文件路径**: `haust-gateway/src/main/java/com/haust/gateway/config/CorsConfig.java`

该配置文件：
1. 允许所有域名访问（`addAllowedOriginPattern("*")`）
2. 允许跨域发送 Cookie（`setAllowCredentials(true)`）
3. 允许所有 HTTP 方法（GET, POST, PUT, DELETE 等）
4. 允许所有请求头
5. 暴露所有响应头

**注意**: 使用 `org.springframework.web.cors.reactive.CorsWebFilter` 而不是普通的 `CorsFilter`，因为 Spring Cloud Gateway 是基于 WebFlux 的响应式框架。

## 测试说明 (Testing Instructions)

### 前置条件

确保以下服务正在运行：
1. **Eureka Server** (端口 8761)
2. **Gateway** (端口 8080)
3. **User Service** (端口 8081)
4. **Frontend** (端口 5173)

### 启动步骤

#### 1. 启动 Eureka Server
```bash
cd haust-eureka
mvn spring-boot:run
```

访问 http://localhost:8761 确认 Eureka 正常运行

#### 2. 启动 Gateway
```bash
cd haust-gateway
mvn spring-boot:run
```

#### 3. 启动 User Service
```bash
cd haust-user-service
mvn spring-boot:run
```

等待所有服务在 Eureka 中注册完成（查看 http://localhost:8761）

#### 4. 启动前端
```bash
cd haust-frontend
npm install  # 如果还没安装依赖
npm run dev
```

### 验证修复

1. 打开浏览器，访问 http://localhost:5173
2. 打开浏览器开发者工具（F12）的 Console 和 Network 标签
3. 尝试登录（使用测试账号）
4. 观察 Network 标签：
   - 应该看到 `OPTIONS /user/login` 预检请求成功（状态码 200）
   - 应该看到 `POST /user/login` 请求成功，没有 CORS 错误
5. Console 标签不应该再有 CORS 相关的错误信息

### 预期结果

**修复前**:
- ❌ Console 显示 CORS 错误
- ❌ Network 中看到 `net::ERR_FAILED`
- ❌ 登录失败

**修复后**:
- ✅ 无 CORS 错误
- ✅ OPTIONS 预检请求返回正确的 CORS 头
- ✅ POST 请求成功
- ✅ 登录成功

### 检查 CORS 头

使用 curl 命令测试 CORS 预检请求：

```bash
curl -X OPTIONS http://localhost:8080/user/login \
  -H "Origin: http://localhost:5173" \
  -H "Access-Control-Request-Method: POST" \
  -H "Access-Control-Request-Headers: Content-Type" \
  -v
```

应该看到响应头中包含：
```
Access-Control-Allow-Origin: http://localhost:5173
Access-Control-Allow-Methods: *
Access-Control-Allow-Headers: *
Access-Control-Allow-Credentials: true
```

## 生产环境注意事项 (Production Considerations)

⚠️ **重要**: 当前配置允许所有域名访问（`*`），这适合开发环境，但在生产环境中应该：

1. 将 `addAllowedOriginPattern("*")` 改为指定的前端域名：
```java
config.addAllowedOrigin("https://your-frontend-domain.com");
```

2. 如果有多个允许的域名：
```java
config.addAllowedOrigin("https://domain1.com");
config.addAllowedOrigin("https://domain2.com");
```

3. 考虑限制允许的 HTTP 方法：
```java
config.addAllowedMethod("GET");
config.addAllowedMethod("POST");
config.addAllowedMethod("PUT");
config.addAllowedMethod("DELETE");
```

## 相关文件 (Related Files)

- Gateway CORS 配置: `haust-gateway/src/main/java/com/haust/gateway/config/CorsConfig.java`
- User Service CORS 配置: `haust-user-service/src/main/java/com/haust/user/configuration/CorsConfig.java`
- Gateway 应用配置: `haust-gateway/src/main/resources/application.yml`
- Frontend API 配置: `haust-frontend/src/api/request.ts`

## 故障排除 (Troubleshooting)

### 问题：仍然出现 CORS 错误

**检查项**:
1. 确认 Gateway 已重新启动并加载了新的 CORS 配置
2. 清除浏览器缓存并刷新页面
3. 检查 Gateway 是否正确注册到 Eureka
4. 确认前端确实在访问 `http://localhost:8080` 而不是直接访问 User Service 的 `8081` 端口

### 问题：OPTIONS 请求失败

**检查项**:
1. 确认 Gateway 正在运行
2. 使用 `curl` 命令直接测试 OPTIONS 请求
3. 查看 Gateway 日志是否有错误信息

### 问题：POST 请求失败但 OPTIONS 成功

这表示 CORS 配置正确，但可能是其他问题：
1. 检查 User Service 是否正在运行
2. 检查 Eureka 中 User Service 的状态
3. 查看 Gateway 和 User Service 的日志

## 技术细节 (Technical Details)

### 为什么使用 Reactive CORS Filter?

Spring Cloud Gateway 基于 Spring WebFlux 构建，使用响应式编程模型。因此需要使用：
- `org.springframework.web.cors.reactive.CorsWebFilter` (✅ 正确)
- 而不是 `org.springframework.web.filter.CorsFilter` (❌ 不适用)

### CORS 预检请求 (Preflight Request)

浏览器在发送实际请求之前，会先发送一个 OPTIONS 请求来检查是否允许跨域：
1. **OPTIONS 请求**: 浏览器询问服务器是否允许跨域
2. **服务器响应**: 返回 CORS 相关的响应头
3. **实际请求**: 如果允许，浏览器发送实际的 POST/GET 等请求

我们的配置确保 Gateway 能够正确响应这些预检请求。
