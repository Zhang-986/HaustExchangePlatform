# 快速启动指南 (Quick Start)

> 🚀 5分钟快速启动 HAUST Exchange Platform

## 📝 一键检查清单

在开始之前，确保已安装：

- ✅ JDK 8+
- ✅ Maven 3.6+
- ✅ MySQL 8.0+
- ✅ Redis 6.0+
- ✅ RabbitMQ 3.8+
- ✅ Node.js 16+

## 🔥 快速启动（3步）

### 步骤 1️⃣：准备中间件

```bash
# 启动 MySQL
mysql.server start  # MacOS
# 或 sudo service mysql start  # Linux
# 或 net start mysql  # Windows

# 启动 Redis
redis-server &

# 启动 RabbitMQ
rabbitmq-server start &
```

### 步骤 2️⃣：初始化数据库

```bash
# 执行数据库脚本
mysql -u root -p < database/schema.sql

# 默认管理员账号
# 账号: admin
# 密码: admin123
```

### 步骤 3️⃣：启动项目

#### 后端启动

```bash
# 方式一：Maven 启动（推荐）
cd haust-monolith
mvn spring-boot:run

# 方式二：IDE 启动
# 运行 com.haust.HaustApplication 主类
```

后端启动成功后访问：http://localhost:8080/doc.html

#### 前端启动

```bash
cd haust-frontend
npm install
npm run dev
```

前端启动成功后访问：http://localhost:5173

## ⚙️ 配置修改（如需要）

### 后端配置

编辑：`haust-monolith/src/main/resources/application-dev.yaml`

```yaml
haust:
  datasource:
    host: localhost
    port: 3306
    database: haust_exchange_platform
    username: root
    password: your_password  # 修改这里
  redis:
    host: localhost
    port: 6379
    password: ""  # 如果设置了密码，填写这里
  rabbitmq:
    host: localhost
    port: 5672
    username: guest
    password: guest
```

### 前端配置

编辑：`haust-frontend/.env.development`

```bash
VITE_API_BASE_URL=http://localhost:8080
```

## 🎯 验证启动

### 1. 检查后端

```bash
# 测试健康检查
curl http://localhost:8080/actuator/health

# 访问 API 文档
# 浏览器打开: http://localhost:8080/doc.html
```

### 2. 检查前端

- 浏览器打开: http://localhost:5173
- 应该能看到登录页面

### 3. 测试登录

使用默认管理员账号登录：
- 账号：`admin`
- 密码：`admin123`

## 🐛 常见问题快速解决

### 问题1：端口被占用

```bash
# 后端改端口（在 application.yaml）
server:
  port: 8081

# 前端改端口（在 vite.config.ts）
server:
  port: 5174
```

### 问题2：数据库连接失败

```bash
# 检查 MySQL 是否启动
mysql -u root -p

# 检查数据库是否创建
SHOW DATABASES LIKE 'haust%';
```

### 问题3：Redis 连接失败

```bash
# 检查 Redis 是否启动
redis-cli ping
# 应该返回 PONG
```

### 问题4：前端依赖安装失败

```bash
# 使用淘宝镜像
npm config set registry https://registry.npmmirror.com
npm install
```

## 📦 微服务架构启动（可选）

如果想使用微服务架构：

```bash
# 1. 启动 Eureka (端口 8761)
cd haust-eureka && mvn spring-boot:run &

# 2. 启动 Gateway (端口 8080)
cd haust-gateway && mvn spring-boot:run &

# 3. 启动各个服务
cd haust-user-service && mvn spring-boot:run &      # 8081
cd haust-referral-service && mvn spring-boot:run &  # 8082
cd haust-forum-service && mvn spring-boot:run &     # 8083
cd haust-im-service && mvn spring-boot:run &        # 8084
```

访问 Eureka: http://localhost:8761

## 📚 完整文档

需要详细的部署说明？查看 [DEPLOYMENT.md](DEPLOYMENT.md)

## ✨ 功能模块

启动成功后，您可以体验：

- 👤 **用户系统**: 注册、登录、权限管理
- 💼 **内推信息**: 发布、浏览、管理内推信息
- 💬 **论坛系统**: 发帖、评论、点赞
- 📱 **实时聊天**: WebSocket 聊天室
- 🤖 **AI助手**: 智能问答（需配置 Dify API）

## 🎉 完成！

现在您可以开始使用 HAUST Exchange Platform 了！

遇到问题？查看 [常见问题](DEPLOYMENT.md#常见问题) 或提交 Issue。
