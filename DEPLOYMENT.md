# HAUST Exchange Platform - 部署指南

> 完整的项目启动指南，包含数据库初始化、中间件配置、后端启动和前端启动的详细步骤

## 📋 目录

- [前置要求](#前置要求)
- [中间件准备](#中间件准备)
- [数据库初始化](#数据库初始化)
- [后端配置与启动](#后端配置与启动)
- [前端配置与启动](#前端配置与启动)
- [验证部署](#验证部署)
- [常见问题](#常见问题)

---

## 🔧 前置要求

在开始之前，请确保您的系统已安装以下软件：

### 必需软件

| 软件 | 版本要求 | 用途 | 下载链接 |
|------|----------|------|----------|
| **JDK** | 8 或更高 | 运行Java后端 | [Oracle JDK](https://www.oracle.com/java/technologies/downloads/) |
| **Maven** | 3.6+ | 构建后端项目 | [Maven](https://maven.apache.org/download.cgi) |
| **MySQL** | 8.0+ | 数据持久化 | [MySQL](https://dev.mysql.com/downloads/mysql/) |
| **Redis** | 6.0+ | 缓存和会话管理 | [Redis](https://redis.io/download) |
| **RabbitMQ** | 3.8+ | 消息队列 | [RabbitMQ](https://www.rabbitmq.com/download.html) |
| **Node.js** | 16+ | 运行前端开发服务器 | [Node.js](https://nodejs.org/) |
| **npm** | 8+ | 前端包管理 | 随Node.js安装 |

### 验证安装

运行以下命令验证软件安装：

```bash
# 验证 Java
java -version

# 验证 Maven
mvn -version

# 验证 MySQL
mysql --version

# 验证 Redis
redis-cli --version

# 验证 RabbitMQ
rabbitmqctl status

# 验证 Node.js 和 npm
node --version
npm --version
```

---

## 🛠️ 中间件准备

### 1. MySQL 配置

#### 启动 MySQL 服务

**Windows:**
```bash
# 启动服务
net start mysql

# 或者通过服务管理器启动
```

**Linux/MacOS:**
```bash
# 启动服务
sudo service mysql start
# 或
sudo systemctl start mysql
```

#### 创建数据库用户（可选）

建议为项目创建专用数据库用户：

```sql
-- 登录 MySQL
mysql -u root -p

-- 创建数据库用户
CREATE USER 'haust_user'@'localhost' IDENTIFIED BY 'your_password';

-- 授予权限
GRANT ALL PRIVILEGES ON haust_exchange_platform.* TO 'haust_user'@'localhost';

-- 刷新权限
FLUSH PRIVILEGES;
```

### 2. Redis 配置

#### 启动 Redis 服务

**Windows:**
```bash
# 使用下载的 redis-server.exe
redis-server.exe redis.windows.conf
```

**Linux/MacOS:**
```bash
# 启动 Redis
redis-server

# 或作为后台服务
sudo service redis-server start
# 或
sudo systemctl start redis
```

#### 测试 Redis 连接

```bash
# 连接到 Redis
redis-cli

# 测试连接
127.0.0.1:6379> ping
PONG

# 退出
127.0.0.1:6379> exit
```

#### 配置 Redis 密码（可选但推荐）

编辑 `redis.conf` 文件：

```bash
# 找到 requirepass 配置
requirepass your_redis_password
```

重启 Redis 服务使配置生效。

### 3. RabbitMQ 配置

#### 启动 RabbitMQ 服务

**Windows:**
```bash
# 启动服务
rabbitmq-server start

# 或通过服务管理器启动
net start RabbitMQ
```

**Linux/MacOS:**
```bash
# 启动服务
sudo service rabbitmq-server start
# 或
sudo systemctl start rabbitmq-server
```

#### 启用管理插件

```bash
# 启用管理界面插件
rabbitmq-plugins enable rabbitmq_management
```

#### 访问管理界面

访问 http://localhost:15672

- 默认用户名: `guest`
- 默认密码: `guest`

#### 创建专用用户（推荐）

```bash
# 添加新用户
rabbitmqctl add_user haust_user your_password

# 设置用户标签为管理员
rabbitmqctl set_user_tags haust_user administrator

# 授予权限
rabbitmqctl set_permissions -p / haust_user ".*" ".*" ".*"
```

---

## 💾 数据库初始化

### 1. 执行数据库脚本

项目提供了完整的数据库初始化脚本：`database/schema.sql`

#### 方法一：使用命令行

```bash
# 进入项目根目录
cd HaustExchangePlatform

# 执行 SQL 脚本
mysql -u root -p < database/schema.sql

# 或指定数据库
mysql -u haust_user -p < database/schema.sql
```

#### 方法二：使用 MySQL Workbench 或其他图形工具

1. 打开 MySQL Workbench
2. 连接到您的 MySQL 服务器
3. 打开 `database/schema.sql` 文件
4. 执行脚本

#### 方法三：手动执行

```bash
# 登录 MySQL
mysql -u root -p

# 执行脚本
mysql> source /path/to/HaustExchangePlatform/database/schema.sql
```

### 2. 验证数据库创建

```sql
-- 使用数据库
USE haust_exchange_platform;

-- 查看所有表
SHOW TABLES;

-- 应该看到以下表：
-- user
-- user_monitor
-- coding_sharing
-- post
-- post_reply

-- 验证管理员用户
SELECT * FROM user WHERE role = 0;
```

### 3. 数据库表说明

| 表名 | 说明 | 主要字段 |
|------|------|----------|
| `user` | 用户表 | id, account, password, role |
| `user_monitor` | 用户监控表 | id, account, login_times, ip, create_time |
| `coding_sharing` | 内推信息表 | id, user_id, company_name, detail, status |
| `post` | 帖子表 | id, title, description, user_id, liked_times |
| `post_reply` | 帖子回复表 | id, post_id, user_id, content, liked_times |

### 4. 默认账号

系统已创建默认管理员账号：

- **账号**: `admin`
- **密码**: `admin123`
- **角色**: 管理员

⚠️ **重要**: 首次登录后请立即修改密码！

---

## 🚀 后端配置与启动

### 项目架构选择

本项目支持两种架构：

1. **单体架构** (haust-monolith) - 推荐用于开发和小规模部署
2. **微服务架构** (haust-*-service) - 推荐用于生产环境和大规模部署

本指南主要介绍**单体架构**的启动方式。微服务架构请参考 [MICROSERVICES_README.md](MICROSERVICES_README.md)。

### 1. 克隆项目

```bash
# 克隆仓库
git clone https://github.com/qmhwx666/HaustExchangePlatform.git

# 进入项目目录
cd HaustExchangePlatform
```

### 2. 配置后端

编辑配置文件：`haust-monolith/src/main/resources/application-dev.yaml`

```yaml
haust:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    host: localhost                    # MySQL主机地址
    port: 3306                         # MySQL端口
    database: haust_exchange_platform  # 数据库名
    username: root                     # 数据库用户名
    password: your_mysql_password      # 数据库密码
  
  redis:
    port: 6379                        # Redis端口
    host: localhost                   # Redis主机地址
    password: your_redis_password     # Redis密码（如果设置了）
    timeout: 200ms
  
  rabbitmq:
    port: 5672                        # RabbitMQ端口
    host: localhost                   # RabbitMQ主机地址
    username: guest                   # RabbitMQ用户名
    password: guest                   # RabbitMQ密码
    virtual-host: /
```

### 3. 构建后端项目

#### 完整构建（推荐首次运行）

```bash
# 在项目根目录执行
mvn clean install -DskipTests

# 如果需要运行测试
mvn clean install
```

#### 仅构建单体应用

```bash
cd haust-monolith
mvn clean package -DskipTests
```

### 4. 启动后端

#### 方法一：使用 Maven（推荐开发环境）

```bash
cd haust-monolith
mvn spring-boot:run
```

#### 方法二：使用 IDE（IntelliJ IDEA / Eclipse）

1. 导入项目为 Maven 项目
2. 找到主类：`com.haust.HaustApplication`
3. 右键运行 `Run HaustApplication`

#### 方法三：运行 JAR 文件（推荐生产环境）

```bash
# 构建 JAR
cd haust-monolith
mvn clean package -DskipTests

# 运行 JAR
java -jar target/haust-monolith-1.0.0.jar

# 或指定配置文件
java -jar target/haust-monolith-1.0.0.jar --spring.profiles.active=dev
```

### 5. 验证后端启动

#### 查看启动日志

成功启动后，应该看到类似的日志：

```
  _   _                 _   
 | | | | __ _ _   _ ___| |_ 
 | |_| |/ _` | | | / __| __|
 |  _  | (_| | |_| \__ \ |_ 
 |_| |_|\__,_|\__,_|___/\__|
                            
:: Spring Boot ::        (v2.7.6)

INFO --- [main] com.haust.HaustApplication: Started HaustApplication in 12.345 seconds
INFO --- [main] o.s.b.w.embedded.tomcat.TomcatWebServer: Tomcat started on port(s): 8080 (http)
```

#### 访问 API 文档

打开浏览器访问：http://localhost:8080/doc.html

您应该能看到 Knife4j 生成的 API 文档界面。

#### 测试健康检查

```bash
# 使用 curl 测试
curl http://localhost:8080/actuator/health

# 应该返回
{"status":"UP"}
```

### 6. 后端启动常见问题

#### 端口被占用

```bash
# 查找占用8080端口的进程
# Windows
netstat -ano | findstr :8080

# Linux/MacOS
lsof -i :8080

# 修改端口（在 application.yaml 中）
server:
  port: 8081  # 改为其他端口
```

#### 数据库连接失败

- 检查 MySQL 服务是否启动
- 检查数据库名、用户名、密码是否正确
- 检查防火墙设置

#### Redis 连接失败

- 检查 Redis 服务是否启动
- 如果设置了密码，确保配置正确
- 检查 Redis 端口是否正确

#### RabbitMQ 连接失败

- 检查 RabbitMQ 服务是否启动
- 检查用户名和密码是否正确
- 确保 virtual-host 存在

---

## 🎨 前端配置与启动

### 1. 进入前端目录

```bash
cd haust-frontend
```

### 2. 安装依赖

```bash
# 使用 npm
npm install

# 或使用 yarn（如果已安装）
yarn install

# 或使用 pnpm（如果已安装）
pnpm install
```

如果安装速度慢，可以使用国内镜像：

```bash
# 设置淘宝镜像
npm config set registry https://registry.npmmirror.com

# 然后再安装
npm install
```

### 3. 配置环境变量

编辑 `.env.development` 文件：

```bash
# 开发环境后端 API 地址
VITE_API_BASE_URL=http://localhost:8080

# WebSocket 地址（可选，默认使用同一地址）
VITE_WS_BASE_URL=ws://localhost:8080
```

编辑 `.env.production` 文件（用于生产环境）：

```bash
# 生产环境后端 API 地址
VITE_API_BASE_URL=http://your-production-server:8080

# WebSocket 地址
VITE_WS_BASE_URL=ws://your-production-server:8080
```

### 4. 启动开发服务器

```bash
npm run dev
```

成功启动后，您应该看到：

```
  VITE v7.1.7  ready in 1234 ms

  ➜  Local:   http://localhost:5173/
  ➜  Network: http://192.168.1.100:5173/
  ➜  press h + enter to show help
```

### 5. 访问前端应用

打开浏览器访问：http://localhost:5173

您应该能看到登录页面。

### 6. 构建生产版本

```bash
# 构建生产版本
npm run build

# 构建完成后，文件在 dist/ 目录中
```

### 7. 预览生产构建

```bash
npm run preview
```

### 8. 前端启动常见问题

#### 依赖安装失败

```bash
# 清除缓存重新安装
rm -rf node_modules package-lock.json
npm cache clean --force
npm install
```

#### 端口冲突

修改 `vite.config.ts`：

```typescript
export default defineConfig({
  server: {
    port: 5174  // 改为其他端口
  }
})
```

#### API 请求失败 (CORS)

确保后端已正确配置 CORS，或使用代理配置：

在 `vite.config.ts` 中添加：

```typescript
export default defineConfig({
  server: {
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, '')
      }
    }
  }
})
```

---

## ✅ 验证部署

### 1. 完整功能测试

#### 用户注册

1. 访问前端：http://localhost:5173
2. 点击"注册"
3. 输入用户名和密码
4. 提交注册

#### 用户登录

1. 使用注册的账号登录
2. 或使用默认管理员账号：
   - 账号：`admin`
   - 密码：`admin123`

#### 发布帖子

1. 登录后进入论坛
2. 点击"发布帖子"
3. 填写标题和内容
4. 提交发布

#### 提交内推信息

1. 进入"内推信息"页面
2. 点击"提交内推"
3. 填写公司信息
4. 提交审核

#### 聊天功能

1. 进入"聊天室"
2. 输入消息
3. 查看实时消息

### 2. 检查日志

#### 后端日志

查看控制台输出，确保没有错误信息。

#### 前端日志

打开浏览器开发者工具（F12），查看 Console 标签页。

### 3. 数据库验证

```sql
-- 查看用户数据
SELECT * FROM user;

-- 查看帖子数据
SELECT * FROM post;

-- 查看内推信息
SELECT * FROM coding_sharing;
```

---

## 🎯 微服务架构启动（可选）

如果您想使用微服务架构，请按照以下顺序启动：

### 1. 启动 Eureka 服务注册中心

```bash
cd haust-eureka
mvn spring-boot:run
```

访问：http://localhost:8761

### 2. 启动 Gateway 网关

```bash
cd haust-gateway
mvn spring-boot:run
```

### 3. 启动业务服务

```bash
# 用户服务 (端口 8081)
cd haust-user-service
mvn spring-boot:run

# 内推服务 (端口 8082)
cd haust-referral-service
mvn spring-boot:run

# 论坛服务 (端口 8083)
cd haust-forum-service
mvn spring-boot:run

# IM服务 (端口 8084)
cd haust-im-service
mvn spring-boot:run
```

详细的微服务配置请参考 [MICROSERVICES_README.md](MICROSERVICES_README.md)。

---

## 🐛 常见问题

### Q1: MySQL 连接被拒绝

**问题**: `com.mysql.cj.jdbc.exceptions.CommunicationsException: Communications link failure`

**解决方案**:
1. 确认 MySQL 服务正在运行
2. 检查 `application-dev.yaml` 中的数据库配置
3. 确认 MySQL 端口 3306 没有被防火墙阻止

### Q2: Redis 连接超时

**问题**: `Unable to connect to Redis`

**解决方案**:
1. 确认 Redis 服务正在运行
2. 测试 Redis 连接：`redis-cli ping`
3. 如果设置了密码，确保配置文件中的密码正确

### Q3: RabbitMQ 认证失败

**问题**: `ACCESS_REFUSED - Login was refused`

**解决方案**:
1. 确认 RabbitMQ 服务正在运行
2. 使用管理界面检查用户权限：http://localhost:15672
3. 确认用户名和密码正确

### Q4: 前端无法连接后端

**问题**: `Network Error` 或 CORS 错误

**解决方案**:
1. 确认后端已启动并在 8080 端口运行
2. 检查 `.env.development` 中的 API 地址
3. 检查浏览器控制台的具体错误信息

### Q5: 构建失败

**问题**: `[ERROR] Failed to execute goal`

**解决方案**:
1. 清理 Maven 缓存：`mvn clean`
2. 更新依赖：`mvn clean install -U`
3. 检查 Java 版本是否为 JDK 8+
4. 检查网络连接（Maven 需要下载依赖）

### Q6: npm install 失败

**问题**: 依赖安装失败

**解决方案**:
```bash
# 清除缓存
npm cache clean --force

# 删除 node_modules
rm -rf node_modules

# 使用淘宝镜像
npm config set registry https://registry.npmmirror.com

# 重新安装
npm install
```

---

## 📚 相关文档

- [README.md](README.md) - 项目概述
- [MICROSERVICES_README.md](MICROSERVICES_README.md) - 微服务架构详解
- [FRONTEND_INTEGRATION_SUMMARY.md](FRONTEND_INTEGRATION_SUMMARY.md) - 前端集成说明
- [haust-frontend/DEVELOPMENT.md](haust-frontend/DEVELOPMENT.md) - 前端开发指南

---

## 📞 获取帮助

如果您在部署过程中遇到问题：

1. 查看本文档的常见问题部分
2. 检查项目 [Issues](https://github.com/qmhwx666/HaustExchangePlatform/issues)
3. 提交新的 Issue
4. 联系作者：QQ 3225483474

---

## 📝 更新日志

- **2025-10-23**: 初始版本，完整的部署指南

---

**祝您部署顺利！🎉**
