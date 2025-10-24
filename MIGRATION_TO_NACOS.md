# Nacos 服务注册中心迁移指南

## 概述

本项目已从 Netflix Eureka 迁移到 Spring Cloud Alibaba Nacos 作为服务注册与发现中心。Nacos 是阿里巴巴开源的服务注册中心，提供了更丰富的功能和更好的性能。

## 主要变更

### 1. 依赖变更

#### 父 POM (pom.xml)
- ✅ 新增 Spring Cloud Alibaba 依赖管理
- ✅ 移除 haust-eureka 模块
- ✅ 添加 Spring Cloud Alibaba 版本：2021.0.5.0

```xml
<spring-cloud-alibaba.version>2021.0.5.0</spring-cloud-alibaba.version>

<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-alibaba-dependencies</artifactId>
    <version>${spring-cloud-alibaba.version}</version>
    <type>pom</type>
    <scope>import</scope>
</dependency>
```

#### 各个微服务模块
所有服务的依赖从：
```xml
<!-- 旧的 Eureka Client -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```

更改为：
```xml
<!-- 新的 Nacos Discovery -->
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
</dependency>

<!-- LoadBalancer (使用 OpenFeign 时必需) -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-loadbalancer</artifactId>
</dependency>
```

### 2. 配置文件变更

所有服务的 `application.yml/yaml` 配置从：

```yaml
# 旧的 Eureka 配置
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
  instance:
    prefer-ip-address: true
```

更改为：

```yaml
# 新的 Nacos 配置
spring:
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848
```

### 3. 受影响的服务

以下服务已完成迁移：
- ✅ haust-gateway (网关服务)
- ✅ haust-user-service (用户服务)
- ✅ haust-referral-service (内推服务)
- ✅ haust-forum-service (论坛服务)
- ✅ haust-im-service (即时通讯服务)

**注意**: haust-eureka 模块已从父 pom.xml 的 `<modules>` 列表中移除，不再参与构建。

### 4. 已移除的模块引用

- ❌ haust-eureka 模块已从父 pom.xml 的 modules 列表中移除（目录仍保留但不再构建）

## 如何使用

### 1. 下载并启动 Nacos

#### 下载 Nacos
访问 [Nacos 官网](https://nacos.io/zh-cn/) 或 [GitHub Release](https://github.com/alibaba/nacos/releases) 下载最新版本的 Nacos Server。

推荐版本：Nacos 2.0.3 或更高版本

#### 启动 Nacos (单机模式)

**Linux/Mac:**
```bash
cd nacos/bin
sh startup.sh -m standalone
```

**Windows:**
```bash
cd nacos\bin
startup.cmd -m standalone
```

#### 访问 Nacos 控制台
启动成功后，访问：http://localhost:8848/nacos

默认用户名和密码都是：`nacos`

### 2. 启动微服务

确保 Nacos 已经启动后，按以下顺序启动服务：

1. **启动网关服务**
   ```bash
   cd haust-gateway
   mvn spring-boot:run
   ```

2. **启动业务服务**（顺序不限）
   ```bash
   # 用户服务
   cd haust-user-service
   mvn spring-boot:run
   
   # 内推服务
   cd haust-referral-service
   mvn spring-boot:run
   
   # 论坛服务
   cd haust-forum-service
   mvn spring-boot:run
   
   # 即时通讯服务
   cd haust-im-service
   mvn spring-boot:run
   ```

### 3. 验证服务注册

在 Nacos 控制台中，进入"服务管理" -> "服务列表"，应该能看到以下服务：
- haust-gateway
- haust-user-service
- haust-referral-service
- haust-forum-service
- haust-im-service

每个服务应该显示为健康状态（绿色）。

## 配置说明

### 基本配置

```yaml
spring:
  application:
    name: 服务名称  # 必需，用于在 Nacos 中标识服务
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848  # Nacos 服务器地址
```

### 高级配置（可选）

如果需要更多配置，可以添加：

```yaml
spring:
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848
        namespace: dev  # 命名空间，用于环境隔离
        group: DEFAULT_GROUP  # 分组
        cluster-name: DEFAULT  # 集群名称
        weight: 1  # 权重，用于负载均衡
        metadata:  # 自定义元数据
          version: 1.0.0
```

## 与 Eureka 的主要区别

| 特性 | Eureka | Nacos |
|------|--------|-------|
| **AP/CP 模式** | AP（可用性优先） | 支持 AP 和 CP 模式切换 |
| **健康检查** | 客户端心跳 | 支持 TCP、HTTP、MySQL 等多种方式 |
| **负载均衡** | Ribbon | Spring Cloud LoadBalancer |
| **管理界面** | 简单 | 功能丰富，支持配置管理 |
| **多数据中心** | 支持 | 原生支持 |
| **配置中心** | 不支持 | 原生支持 |

## 优势

1. **功能更丰富**：Nacos 不仅提供服务注册与发现，还支持配置管理
2. **更好的性能**：更低的内存占用和更快的响应速度
3. **国内生态**：阿里巴巴开源，在国内有更好的社区支持
4. **更灵活**：支持 AP 和 CP 模式切换，适应不同场景
5. **更好的可视化**：提供功能强大的管理控制台

## 常见问题

### Q: 服务无法注册到 Nacos？
A: 
1. 确认 Nacos 是否正常启动（访问 http://localhost:8848/nacos）
2. 检查配置文件中的 `spring.cloud.nacos.discovery.server-addr` 是否正确
3. 检查网络是否可以访问 Nacos 服务器

### Q: 服务注册成功但无法相互调用？
A:
1. 检查 LoadBalancer 依赖是否已添加
2. 确认服务名称配置正确
3. 在 Nacos 控制台检查服务健康状态

### Q: 如何在生产环境部署 Nacos？
A: 建议使用集群模式部署 Nacos：
1. 至少部署 3 个 Nacos 节点
2. 使用 MySQL 作为持久化存储
3. 配置 Nginx 做负载均衡

参考官方文档：https://nacos.io/zh-cn/docs/cluster-mode-quick-start.html

### Q: 开发环境如何快速启动？
A: 使用 Docker 快速启动单机版 Nacos：
```bash
docker run -d \
  --name nacos \
  -e MODE=standalone \
  -p 8848:8848 \
  nacos/nacos-server:latest
```

## 进一步学习

- [Nacos 官方文档](https://nacos.io/zh-cn/docs/what-is-nacos.html)
- [Spring Cloud Alibaba 文档](https://spring-cloud-alibaba-group.github.io/github-pages/2021/zh-cn/index.html)
- [服务注册与发现最佳实践](https://nacos.io/zh-cn/docs/best-practice.html)

## 更新说明

本文档记录了从 Eureka 到 Nacos 的迁移过程和使用说明。

**注意**: haust-eureka 模块目录仍然存在于代码库中，但已从构建配置中移除，不再参与编译和部署。如需彻底删除该模块，可以手动删除 haust-eureka 目录。
