# 河南科技大学交流平台

中文 | [English](README.md)

[![Java](https://img.shields.io/badge/Java-8-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.7.6-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-2021.0.5-blue.svg)](https://spring.io/projects/spring-cloud)
[![Vue](https://img.shields.io/badge/Vue-3.5-green.svg)](https://vuejs.org/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

## 项目简介

这是一个面向河南科技大学学生的校园求职内推和交流平台。学生可以在这里分享工作机会、实习内推信息，通过论坛和即时通讯功能互相交流。

项目采用 Spring Cloud Alibaba 微服务架构，后端分为多个独立服务，前端使用 Vue 3 开发。

## 主要功能

- **用户管理**：学生注册、登录、个人信息管理，支持 AI 聊天机器人
- **职位内推**：发布和浏览内推信息，包含审核流程
- **论坛讨论**：学生交流经验、提问讨论的版块
- **即时通讯**：基于 WebSocket 的实时聊天功能
- **网关服务**：统一的请求入口，负责路由转发

## 系统架构

应用拆分为以下微服务：

- `haust-user-service` - 用户账号和认证服务
- `haust-referral-service` - 内推信息管理服务
- `haust-forum-service` - 论坛功能服务
- `haust-im-service` - 即时通讯服务
- `haust-gateway` - 网关服务，负责请求路由
- `haust-common` - 公共代码和工具类

所有服务通过 Nacos 进行服务注册与发现，使用 Spring Cloud Gateway 进行请求路由。

## 技术栈

**后端：**
- Spring Boot 2.7.6
- Spring Cloud 2021.0.5
- Spring Cloud Alibaba 2021.0.5.0
- Nacos（服务发现和配置中心）
- MySQL 8.0+
- Redis 6.0+
- RabbitMQ 3.8+
- MyBatis
- JWT

**前端：**
- Vue 3
- TypeScript
- Vite
- Element Plus
- Pinia
- Axios

## 快速开始

### 环境要求

需要安装以下软件：
- JDK 8 或更高版本
- Maven 3.6+
- MySQL 8.0+
- Redis 6.0+
- RabbitMQ 3.8+
- Nacos 2.0+
- Node.js 16+（用于前端）

### 启动 Nacos

从 https://nacos.io 下载 Nacos，或使用以下命令：

```bash
wget https://github.com/alibaba/nacos/releases/download/2.2.3/nacos-server-2.2.3.tar.gz
tar -zxvf nacos-server-2.2.3.tar.gz
cd nacos/bin

# 以单机模式启动 Nacos
sh startup.sh -m standalone  # Linux/Mac
startup.cmd -m standalone    # Windows
```

访问 Nacos 控制台：http://localhost:8848/nacos（用户名和密码都是 `nacos`）。

### 启动后端服务

在不同的终端窗口分别启动各个服务：

```bash
# 先启动网关
cd haust-gateway && mvn spring-boot:run

# 启动其他服务
cd haust-user-service && mvn spring-boot:run
cd haust-referral-service && mvn spring-boot:run
cd haust-forum-service && mvn spring-boot:run
cd haust-im-service && mvn spring-boot:run
```

在 Nacos 控制台查看所有服务是否注册成功并处于健康状态。

### 启动前端

```bash
cd haust-frontend
npm install
npm run dev
```

## 配置说明

服务通过以下配置注册到 Nacos：

```yaml
spring:
  application:
    name: haust-user-service
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848
```

网关根据服务名称路由请求：

```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: user-service
          uri: lb://haust-user-service
          predicates:
            - Path=/user/**
```

## 工作流程

1. 客户端发送请求到网关（端口 8080）
2. 网关从 Nacos 查询服务地址
3. 请求被负载均衡到可用的服务实例
4. 服务处理请求，访问 MySQL/Redis/RabbitMQ
5. 响应通过网关返回给客户端

## 相关文档

- [MIGRATION_TO_NACOS.md](MIGRATION_TO_NACOS.md) - 从 Eureka 迁移到 Nacos 的详细说明
- [MIGRATION_SUMMARY.md](MIGRATION_SUMMARY.md) - 迁移过程总结

## 开源协议

MIT License - 详见 [LICENSE](LICENSE) 文件。

## 参与贡献

欢迎提交 Pull Request。如果有重大改动，请先开 Issue 讨论。
