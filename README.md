# 河科大校招内推分享平台 🎓

<div align="center">

[![Java](https://img.shields.io/badge/Java-8-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.7.6-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-2021.0.5-blue.svg)](https://spring.io/projects/spring-cloud)
[![Vue](https://img.shields.io/badge/Vue-3.5-green.svg)](https://vuejs.org/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

**一个为河科大学生打造的校招内推和求职交流平台**

</div>

---

## 写在前面

这是一个面向河南科技大学（HAUST）同学们的校招内推信息分享平台。作为一名在杭州实习的 Java 后端开发者，我在求职过程中深刻体会到内推信息的重要性。很多时候，一个靠谱的内推码能让简历直达 HR，大大提高面试机会。

但是内推信息往往散落在各个 QQ 群、微信群里，很容易错过。所以我想做这样一个平台，把大家的内推信息集中起来，方便同学们查找和使用。同时也加入了论坛功能，让大家可以分享求职经验、讨论面试题，互相帮助。

这个项目也是我学习微服务架构、消息队列、缓存等技术的实践场所。代码可能不够完美，但我会持续优化和改进。欢迎大家提 issue 和 PR！

## 平台功能

### 📋 内推信息发布与管理
- **发布内推**：同学们可以发布自己手上的内推码或内推机会，填写公司名称、岗位要求、联系方式等信息
- **浏览内推**：支持分页浏览、搜索筛选，快速找到心仪公司的内推机会
- **个人中心**：管理自己发布的内推信息，可以修改或删除
- **管理员审核**：为保证信息质量，管理员会对提交的内推进行审核，通过后才会展示

### 💬 求职论坛
- **发帖交流**：分享求职经验、讨论面试题、提问求助，支持匿名发帖
- **互动功能**：点赞、评论、回复，和大家一起讨论
- **个人主页**：查看和管理自己发布的帖子

### 💭 即时通讯
- **实时聊天**：基于 WebSocket 的聊天室，可以和其他在线用户实时交流
- **在线状态**：显示当前在线用户列表

### 🤖 AI 求职助手（实验性功能）
- **智能问答**：集成大语言模型 API，可以咨询求职相关问题
- **流式响应**：使用 WebFlux 实现流式返回，体验更流畅

## 技术架构

### 后端技术栈
这个项目采用微服务架构，后端主要使用 Spring Cloud Alibaba 技术栈：

**核心框架**
- **Spring Boot 2.7.6**：项目基础框架
- **Spring Cloud 2021.0.5**：微服务全家桶
- **Spring Cloud Alibaba 2021.0.5.0**：阿里巴巴微服务组件套件
- **Nacos**：服务注册与发现中心、配置中心（替代 Eureka 和 Spring Cloud Config）
- **Gateway**：API 网关，统一请求入口，实现路由转发、负载均衡和跨域处理

**Spring Cloud Alibaba 核心组件**
- **Nacos Discovery**：服务注册与发现，实现微服务间的自动注册和调用
- **LoadBalancer**：客户端负载均衡，替代 Ribbon，提供更好的性能和可维护性

**微服务模块**
- **haust-user-service**：用户服务，负责用户注册、登录、认证，以及 AI 问答功能
- **haust-referral-service**：内推服务，负责内推信息的发布、审核、查询
- **haust-forum-service**：论坛服务，负责帖子、评论、点赞等功能
- **haust-im-service**：即时通讯服务，基于 WebSocket 的聊天功能
- **haust-common**：公共模块，封装通用工具类和实体

**数据存储**
- **MySQL 8.0+**：主数据库，存储用户、内推、帖子等核心数据
- **Redis 6.0+**：缓存数据库，缓存点赞信息、用户信息等高频访问数据
- **MyBatis**：ORM 框架，方便数据库操作

**中间件**
- **RabbitMQ**：消息队列，用于异步处理敏感词监控、用户行为追踪等任务
- **WebSocket**：实时通信协议，用于聊天室功能

**其他组件**
- **JWT**：Token 认证方案
- **IK Analyzer**：中文分词器，用于敏感词过滤
- **Knife4j**：API 文档工具，方便接口调试
- **WebFlux**：响应式编程框架，用于 AI 流式响应
- **AOP**：面向切面编程，用于敏感词监控

### 前端技术栈（haust-frontend）
现代化的 Vue 3 前端应用：

- **Vue 3**：渐进式 JavaScript 框架，使用 Composition API
- **TypeScript**：类型安全的开发体验
- **Vite**：新一代前端构建工具，开发体验极佳
- **Vue Router 4**：官方路由库
- **Pinia**：Vue 3 状态管理
- **Element Plus**：Vue 3 组件库，UI 美观易用
- **Axios**：HTTP 客户端
- **SockJS + STOMP**：WebSocket 通信库

### 开发环境要求
- **JDK 8+**
- **Maven 3.6+**
- **Node.js 16+**
- **MySQL 8.0+**
- **Redis 6.0+**
- **RabbitMQ 3.8+**
- **Nacos 2.0+**

## 技术亮点

### Spring Cloud Alibaba 微服务架构
采用 Spring Cloud Alibaba 微服务架构，相比传统的 Netflix OSS 方案具有更多优势：

**Nacos 服务注册中心**
- **双模式支持**：同时支持 AP（可用性优先）和 CP（一致性优先）模式，可根据场景灵活切换
- **服务发现**：自动注册和发现微服务，实现服务间的透明调用
- **健康检查**：支持 TCP、HTTP、MySQL 等多种健康检查方式，及时发现和剔除故障实例
- **负载均衡**：配合 Spring Cloud LoadBalancer 实现客户端负载均衡
- **管理界面**：提供功能强大的可视化管理控制台，方便运维管理

**为什么选择 Spring Cloud Alibaba？**
1. **更好的性能**：Nacos 的内存占用和响应速度优于 Eureka
2. **功能更丰富**：Nacos 不仅支持服务发现，还内置配置中心功能，一个组件解决两个问题
3. **国内生态**：阿里巴巴开源，在国内有完善的中文文档和活跃的社区支持
4. **持续维护**：Netflix 已停止维护大部分组件，而 Spring Cloud Alibaba 持续更新迭代
5. **生产验证**：经过阿里巴巴双十一等大规模场景的验证，稳定性和可靠性有保障

### 微服务架构
采用 Spring Cloud 微服务架构，服务之间通过 Nacos 进行注册发现，Gateway 作为统一入口进行路由转发。各个服务职责清晰，便于扩展和维护。

### 敏感词过滤
为了营造良好的社区氛围，系统使用 AOP + IK 分词器实现了敏感词过滤功能。在发布内推信息和帖子时，会自动检测内容是否包含敏感词，如果包含则拒绝发布。

```java
@SensitiveMonitor(ContentType.sharing)
@Override
public void addInfo(CodingSharingDTO codingSharingDTO) {
    // 业务逻辑，会先经过敏感词检测
}
```

AOP 切面会拦截带有 `@SensitiveMonitor` 注解的方法，提取内容进行分词和敏感词匹配。这样做的好处是业务代码无需关心敏感词过滤逻辑，保持了代码的简洁性。

### 消息队列异步处理
使用 RabbitMQ 实现异步处理，比如：
- 用户行为监控（发帖、评论等操作记录）
- 敏感词检测结果通知

```java
@RabbitListener(
    bindings = @QueueBinding(
        value = @Queue(value = "user.monitor.queue", durable = "true"),
        exchange = @Exchange(value = "user.monitor.exchange", type = ExchangeTypes.TOPIC),
        key = "user.monitor.key"
    )
)
public void userInfoListener(UserMsg userMsg) {
    // 异步处理用户行为监控
    userService.addMonitor(userMsg);
}
```

这种异步处理方式不会阻塞主流程，提高了系统响应速度。

### 点赞批量处理
为了避免高频点赞操作对数据库造成压力，实现了一个简单的批量处理队列。点赞操作先放入队列，然后定时任务每 5 秒批量写入数据库。

### WebFlux 流式响应
AI 问答功能使用 WebFlux 实现流式响应，用户可以看到 AI 逐字输出，体验更好。

```java
difyApiUtil.streamChat(text, userId, null)
    .doOnNext(chunk -> log.info("收到数据块: [{}]", chunk))
    .block();
```

### Redis 缓存优化
对于点赞信息、用户信息等高频访问的数据，使用 Redis 进行缓存，减轻数据库压力。同时使用 Redisson 实现分布式锁，保证缓存一致性。

## 项目结构

```
HaustExchangePlatform/
├── haust-gateway/             # API 网关
├── haust-common/              # 公共模块（工具类、常量、实体等）
├── haust-user-service/        # 用户服务
│   ├── controller/            # 控制器层
│   ├── service/               # 业务逻辑层
│   ├── mapper/                # 数据访问层
│   ├── aspect/                # AOP 切面
│   ├── mq/                    # 消息队列监听器
│   └── configuration/         # 配置类
├── haust-referral-service/    # 内推服务
├── haust-forum-service/       # 论坛服务
├── haust-im-service/          # 即时通讯服务
└── haust-frontend/            # Vue 3 前端项目
    ├── src/
    │   ├── api/               # API 接口封装
    │   ├── views/             # 页面组件
    │   ├── components/        # 通用组件
    │   ├── router/            # 路由配置
    │   ├── store/             # 状态管理
    │   └── types/             # TypeScript 类型定义
    └── public/                # 静态资源
```

每个微服务采用经典的三层架构：
- **Controller 层**：处理 HTTP 请求，参数校验
- **Service 层**：业务逻辑实现
- **Mapper 层**：数据库操作（MyBatis）

## 快速开始

### 环境准备

在开始之前，请确保你的电脑上已经安装了以下软件：

- **JDK 8+**：Java 开发环境
- **Maven 3.6+**：项目构建工具
- **MySQL 8.0+**：数据库
- **Redis 6.0+**：缓存数据库
- **RabbitMQ 3.8+**：消息队列
- **Nacos 2.0+**：服务注册中心（需单独下载安装）
- **Node.js 16+**：前端开发环境

### 后端启动

#### 1. 克隆项目

```bash
git clone https://github.com/Zhang-986/HaustExchangePlatform.git
cd HaustExchangePlatform
```

#### 2. 启动 Nacos

下载并启动 Nacos 服务注册中心：

1. 访问 [Nacos 官网](https://nacos.io/zh-cn/) 下载 Nacos Server
2. 解压后进入 bin 目录
3. Linux/Mac 执行：`sh startup.sh -m standalone`
4. Windows 执行：`startup.cmd -m standalone`
5. 访问 `http://localhost:8848/nacos`，默认用户名密码都是 `nacos`

#### 3. 配置数据库

在 MySQL 中创建数据库，然后执行 SQL 脚本（如果有的话）初始化表结构。

#### 4. 修改配置文件

各个微服务的配置文件位于 `src/main/resources/` 目录下，需要修改的配置包括：

- **application.yml** 或 **application-dev.yml**
  - MySQL 连接信息（地址、端口、用户名、密码）
  - Redis 连接信息
  - RabbitMQ 连接信息
  - Nacos 服务地址（默认 localhost:8848）

**配置示例：**

```yaml
spring:
  application:
    name: haust-user-service
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848  # Nacos 服务地址
  datasource:
    url: jdbc:mysql://localhost:3306/haust_platform?useUnicode=true&characterEncoding=utf8
    username: root
    password: your_password
  
  redis:
    host: localhost
    port: 6379
    password: # 如果有密码就填，没有就留空
  
  rabbitmq:
    host: localhost
    port: 5672
    username: guest
    password: guest
```

#### 5. 启动服务

微服务的启动顺序建议如下：

1. **确保 Nacos 已启动**（访问 http://localhost:8848/nacos 确认）
2. **启动 Gateway 网关**（haust-gateway）
3. **启动各个业务服务**（user-service、referral-service、forum-service、im-service）

可以在 IDEA 中直接运行各个服务的 `Application.java` 主类，也可以用 Maven 命令：

```bash
# 在各个服务目录下执行
mvn spring-boot:run
```

或者打包后运行：

```bash
mvn clean package
java -jar target/服务名.jar
```

#### 6. 查看 API 文档

启动完成后，可以访问 Knife4j 提供的 API 文档进行接口测试：

- 用户服务：`http://localhost:端口/doc.html`
- 内推服务：`http://localhost:端口/doc.html`
- 论坛服务：`http://localhost:端口/doc.html`

具体端口号请查看各服务的配置文件。

同时可以访问 Nacos 控制台查看服务注册情况：`http://localhost:8848/nacos`

### 前端启动

#### 1. 进入前端目录

```bash
cd haust-frontend
```

#### 2. 安装依赖

```bash
npm install
```

如果安装速度慢，可以使用淘宝镜像：

```bash
npm install --registry=https://registry.npmmirror.com
```

#### 3. 修改配置

编辑 `.env.development` 文件，配置后端 API 地址：

```
VITE_API_BASE_URL=http://localhost:网关端口
```

#### 4. 启动开发服务器

```bash
npm run dev
```

启动成功后，浏览器访问 `http://localhost:5173` 即可看到前端页面。

#### 5. 构建生产版本

```bash
npm run build
```

构建完成后，dist 目录下就是打包好的静态文件，可以部署到 Nginx 或其他 Web 服务器。

### 常见问题

**Q: 启动服务报错连接不上 MySQL？**  
A: 检查 MySQL 是否启动，配置文件中的用户名密码是否正确。

**Q: 服务启动失败，提示无法连接到 Nacos？**  
A: 确保 Nacos 已经启动，并且配置文件中的 Nacos 地址正确（默认 localhost:8848）。

**Q: 前端访问接口报跨域错误？**  
A: 后端 Gateway 已经配置了跨域，如果还有问题检查配置文件中的跨域设置。

**Q: RabbitMQ 连接失败？**  
A: 确保 RabbitMQ 服务已启动，端口号和用户名密码配置正确。

**Q: Redis 连接失败？**  
A: 确保 Redis 服务已启动，检查端口号和密码配置。

更多问题欢迎在 issue 中提问！

## 功能详情

### 用户相关
- **注册登录**：支持普通用户和管理员登录，使用 JWT Token 进行身份认证
- **个人中心**：查看和管理自己发布的内推信息和帖子

### 内推模块
- **浏览内推**：分页展示所有审核通过的内推信息
- **发布内推**：填写公司名称、岗位、要求、内推码等信息
- **编辑删除**：在个人中心管理自己发布的内推
- **管理员审核**：管理员可以在后台审核待审核的内推信息

### 论坛模块
- **浏览帖子**：分页浏览所有帖子，支持按热度、时间排序
- **发布帖子**：支持匿名发帖，编写标题和内容
- **查看详情**：查看帖子详情，可以评论和回复
- **点赞功能**：对帖子和评论进行点赞
- **我的帖子**：查看和管理自己发布的帖子

### 聊天室
- **实时聊天**：基于 WebSocket 实现的聊天室，支持实时收发消息
- **在线用户**：显示当前在线用户列表
- **消息历史**：查看聊天记录

### AI 助手
- **智能问答**：输入求职相关问题，AI 会给出建议和回答
- **流式响应**：AI 回复逐字显示，体验更自然

## 待优化功能

项目还在持续完善中，以下是一些计划中的功能：

### 功能增强
- [ ] 富文本编辑器：让帖子和内推信息支持更丰富的格式
- [ ] 图片上传：支持上传图片到帖子或内推信息
- [ ] 私信功能：用户之间可以私聊
- [ ] 消息通知：有人评论、点赞或审核通过时及时通知用户
- [ ] 搜索功能：快速搜索内推信息和帖子
- [ ] 标签分类：给帖子打标签，方便分类查找

### 技术优化
- [ ] 单元测试：增加测试用例，提高代码质量
- [ ] 性能监控：集成 APM 工具，监控系统性能
- [ ] 分布式追踪：使用 Sleuth + Zipkin 追踪请求链路
- [ ] 权限细化：更细粒度的权限控制
- [ ] Docker 部署：提供 Docker Compose 一键部署方案
- [ ] CI/CD：自动化构建和部署流程

欢迎大家提出建议和需求！

## 如何贡献

非常欢迎大家参与项目开发！无论是提 bug、提需求，还是贡献代码，都欢迎。

### 参与方式

1. **Fork 本仓库**到你的 GitHub 账号
2. **Clone 到本地**进行开发
   ```bash
   git clone https://github.com/你的用户名/HaustExchangePlatform.git
   ```
3. **创建新分支**进行功能开发或 bug 修复
   ```bash
   git checkout -b feature/新功能名称
   # 或
   git checkout -b fix/bug描述
   ```
4. **提交代码**
   ```bash
   git add .
   git commit -m "描述你的改动"
   git push origin feature/新功能名称
   ```
5. **发起 Pull Request**，描述清楚你的改动内容

### 代码规范

- 遵循现有的代码风格
- 添加必要的注释
- 提交信息清晰明了

### 提 Issue

如果你发现了 bug 或者有新的功能建议，欢迎在 [Issues](https://github.com/Zhang-986/HaustExchangePlatform/issues) 页面提出。

提 issue 时请描述清楚：
- 问题的详细现象
- 复现步骤
- 你的运行环境（操作系统、JDK 版本、数据库版本等）

## 开源协议

本项目采用 MIT 协议开源，详见 [LICENSE](LICENSE) 文件。

简单来说就是：你可以自由使用、修改、分发本项目代码，但需要保留原作者的版权声明。

## 联系方式

如果你有任何问题或建议，可以通过以下方式联系我：

- **QQ**：3225483474
- **GitHub Issues**：[提交 Issue](https://github.com/Zhang-986/HaustExchangePlatform/issues)

## 致谢

感谢所有为这个项目做出贡献的同学！

特别感谢河南科技大学的同学们对本项目的支持和反馈。

---

<div align="center">
  <strong>希望这个平台能帮助到正在找工作的同学们！</strong>
  <br>
  <em>如果觉得项目有帮助，欢迎 ⭐️ Star 支持一下~</em>
  <br><br>
  <sub>在求职路上，我们一起加油！💪</sub>
</div>

---

# English Introduction / 英文简介

## HAUST Exchange Platform - Spring Cloud Alibaba Microservices Architecture

A campus recruitment referral and job-seeking exchange platform built with **Spring Cloud Alibaba** microservices architecture for Henan University of Science and Technology (HAUST) students.

### 🏗️ Architecture Overview

This project demonstrates a production-ready microservices architecture using **Spring Cloud Alibaba** ecosystem, which is the leading microservices solution in China and an excellent alternative to Netflix OSS stack.

### 🎯 Key Technologies

#### Spring Cloud Alibaba Components

**Nacos - Service Discovery & Configuration Center**
- **Dual Mode Support**: Supports both AP (Availability Priority) and CP (Consistency Priority) modes
- **Service Registration & Discovery**: Automatic service registration and discovery for seamless inter-service communication
- **Health Checking**: Multiple health check protocols including TCP, HTTP, and MySQL
- **Load Balancing**: Integrates with Spring Cloud LoadBalancer for client-side load balancing
- **Management Console**: Rich web-based UI for service management and monitoring

**Spring Cloud Gateway**
- API Gateway serving as the single entry point for all microservices
- Dynamic routing and request forwarding
- Built-in CORS support and authentication integration
- Filter chain for cross-cutting concerns

#### Microservices Modules

1. **haust-user-service** - User management service (authentication, authorization, AI chatbot)
2. **haust-referral-service** - Referral information service (job referrals, approval workflow)
3. **haust-forum-service** - Forum service (posts, comments, likes)
4. **haust-im-service** - Instant messaging service (WebSocket-based real-time chat)
5. **haust-gateway** - API Gateway (routing, load balancing, CORS)
6. **haust-common** - Common utilities and shared entities

#### Technology Stack

**Backend**
- **Spring Boot 2.7.6** - Application framework
- **Spring Cloud 2021.0.5** - Microservices framework
- **Spring Cloud Alibaba 2021.0.5.0** - Alibaba microservices components
- **Nacos 2.0+** - Service registry and configuration center
- **MySQL 8.0+** - Relational database
- **Redis 6.0+** - Caching layer
- **RabbitMQ 3.8+** - Message queue for async processing
- **MyBatis** - ORM framework
- **JWT** - Token-based authentication
- **WebSocket** - Real-time bidirectional communication

**Frontend**
- **Vue 3** - Progressive JavaScript framework with Composition API
- **TypeScript** - Type-safe development
- **Vite** - Next-generation frontend build tool
- **Element Plus** - Vue 3 UI component library
- **Pinia** - State management
- **Axios** - HTTP client

### ⭐ Why Spring Cloud Alibaba?

**Superior to Netflix OSS:**

1. **Better Performance**: Nacos has lower memory footprint and faster response times compared to Eureka
2. **Richer Features**: Nacos combines service discovery and configuration management in one component
3. **Active Maintenance**: Unlike Netflix OSS (most components are in maintenance mode), Spring Cloud Alibaba is actively developed
4. **Battle-Tested**: Proven at scale during Alibaba's Double 11 shopping festival
5. **Better Documentation**: Comprehensive Chinese and English documentation with active community support

**Nacos vs Eureka Comparison:**

| Feature | Eureka | Nacos |
|---------|--------|-------|
| **CAP Model** | AP only | AP & CP modes |
| **Health Check** | Client heartbeat | TCP/HTTP/MySQL/gRPC |
| **Load Balancing** | Ribbon (maintenance mode) | Spring Cloud LoadBalancer |
| **Management UI** | Basic | Feature-rich with metrics |
| **Configuration Management** | Requires Config Server | Built-in |
| **Active Development** | ❌ No | ✅ Yes |

### 🚀 Quick Start

#### Prerequisites

- **JDK 8+**
- **Maven 3.6+**
- **MySQL 8.0+**
- **Redis 6.0+**
- **RabbitMQ 3.8+**
- **Nacos 2.0+**
- **Node.js 16+** (for frontend)

#### Start Nacos Server

**Download and run Nacos:**

```bash
# Download Nacos from https://nacos.io
wget https://github.com/alibaba/nacos/releases/download/2.2.3/nacos-server-2.2.3.tar.gz
tar -zxvf nacos-server-2.2.3.tar.gz
cd nacos/bin

# Start in standalone mode
sh startup.sh -m standalone  # Linux/Mac
startup.cmd -m standalone    # Windows
```

**Access Nacos Console:**
- URL: http://localhost:8848/nacos
- Username: `nacos`
- Password: `nacos`

#### Start Microservices

```bash
# Start Gateway
cd haust-gateway && mvn spring-boot:run

# Start business services (in different terminals)
cd haust-user-service && mvn spring-boot:run
cd haust-referral-service && mvn spring-boot:run
cd haust-forum-service && mvn spring-boot:run
cd haust-im-service && mvn spring-boot:run
```

#### Verify Service Registration

Check the Nacos Console → Service Management → Service List to see all registered services with healthy status (green indicator).

### 📚 Architecture Highlights

**Service Discovery with Nacos:**
- All microservices automatically register with Nacos on startup
- Services discover each other using service names instead of hardcoded URLs
- Health checks ensure only healthy instances receive traffic
- LoadBalancer automatically distributes requests across instances

**API Gateway Pattern:**
- Single entry point for all client requests
- Centralized authentication and authorization
- Request routing based on service names
- CORS handling and rate limiting

**Async Processing with RabbitMQ:**
- Sensitive word detection for posts and referrals
- User behavior tracking and analytics
- Decouples long-running operations from request-response cycle

**Real-time Communication:**
- WebSocket-based chat room for instant messaging
- Real-time notifications and updates
- Online user presence tracking

### 📖 Documentation

- **[MIGRATION_TO_NACOS.md](MIGRATION_TO_NACOS.md)** - Detailed guide on Nacos migration from Eureka
- **[MIGRATION_SUMMARY.md](MIGRATION_SUMMARY.md)** - Summary of the migration process

### 🔧 Configuration Example

**Service Registration with Nacos:**

```yaml
spring:
  application:
    name: haust-user-service  # Service name for discovery
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848  # Nacos server address
```

**Gateway Routing Configuration:**

```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: user-service
          uri: lb://haust-user-service  # Load balanced routing
          predicates:
            - Path=/user/**
```

### 📊 Microservices Communication Flow

```
Client Request
    ↓
Gateway (Port 8080)
    ↓
Nacos Service Discovery
    ↓
LoadBalancer (Round Robin)
    ↓
Target Microservice
    ↓
MySQL / Redis / RabbitMQ
```

### 🌐 Learn More

- **[Spring Cloud Alibaba Official Docs](https://spring-cloud-alibaba-group.github.io/github-pages/2021/en-us/index.html)**
- **[Nacos Official Website](https://nacos.io/en-us/)**
- **[Spring Cloud Gateway Docs](https://spring.io/projects/spring-cloud-gateway)**

### 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

### 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

---

**Built with ❤️ using Spring Cloud Alibaba**