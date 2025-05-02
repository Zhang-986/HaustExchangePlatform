# HAUST 内部推荐码共享平台 🚀

![License](https://img.shields.io/badge/license-MIT-blue.svg)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.7.6-green.svg)
![JDK](https://img.shields.io/badge/JDK-8-orange.svg)
![MyBatis](https://img.shields.io/badge/MyBatis-3.0-yellow.svg)
![Redis](https://img.shields.io/badge/Redis-6.0+-red.svg)
![RabbitMQ](https://img.shields.io/badge/RabbitMQ-3.8+-purple.svg)

> 连接人才与机会，让每一次推荐都充满价值

## 项目概览 🔍

HAUST内部推荐码共享平台是一个创新的人才推荐系统，旨在优化企业内部推荐流程，提高人才匹配效率。通过平台，企业员工可以分享和管理内推码，大幅降低招聘成本，提升人才质量。平台整合了大模型AI交互能力，实现智能化人才评估和匹配。


## 📋 核心功能

### 内推信息管理
- **内推码共享**: 用户可发布、查询、修改公司内推信息和推荐码
- **智能审核**: 管理员可高效审核内推信息，确保内容质量
- **个性化视图**: 用户个人中心展示所有已发布推荐信息

### 社区交流
- **话题讨论**: 用户可发布招聘相关帖子，分享经验和问题
- **互动机制**: 评论、回复、点赞功能促进用户交流
- **匿名保护**: 支持匿名发帖和回复，保障信息安全

### AI智能助手
- **智能问答**: 集成Dify大模型API，提供招聘问答服务
- **实时响应**: 基于WebFlux的响应式编程实现流式文本处理
- **自然语言理解**: 能够理解和分析复杂的招聘相关问题

## 🛠️ 技术亮点

### 1. 响应式编程实现
```java
// 使用Project Reactor处理流式数据
difyApiUtil.streamChat(text, String.valueOf(BaseContext.getId()), null)
    .doOnNext(chunk -> log.info("Received chunk: [{}]", chunk))
    .doOnComplete(() -> log.info("Stream completed"))
    .collect(StringBuilder::new, StringBuilder::append)
    .map(StringBuilder::toString)
    .block();
```

### 2. 高性能批处理机制
```java
// 自定义批处理队列，提高点赞等高频操作性能
@Component
@Slf4j
public class BatchProcessUtil {
    // 定时调度处理队列数据
    @PostConstruct
    private void start(){
        scheduledExecutorService.scheduleWithFixedDelay(
            this::consume, 0, 5, TimeUnit.SECONDS);
    }
    
    // 生产者-消费者模式实现批量处理
    public void process(LikeMsg msg){
        blockingQueue.offer(msg);
    }
}
```

### 3. 敏感词过滤与内容审核
```java
// AOP实现敏感内容监控
@SensitiveMonitor(ContentType.sharing)
@Override
public void addInfo(CodingSharingDTO codingSharingDTO) {
    // 业务逻辑...
}
```

## 🏗️ 系统架构



**三层架构设计**
- **表现层**: Spring MVC, Knife4j API文档
- **业务层**: 服务实现，事务管理，安全控制
- **持久层**: MyBatis, Redis缓存机制

**多模块集成**
- 用户认证与授权
- 内推信息管理
- 社区互动系统
- AI智能助手
- 批量处理引擎

## 💻 快速开始

### 环境要求
- JDK 8+
- Maven 3.6+
- MySQL 8.0+
- Redis 6.0+

### 克隆与配置
```bash
# 克隆仓库
git clone https://github.com/your-username/HAUST_Internal_referral_code_sharing_platform.git
cd HAUST_Internal_referral_code_sharing_platform/haust

# 配置数据库
# 修改 src/main/resources/application-dev.yaml 中的数据库连接信息
```


### API文档访问
启动应用后，访问以下地址查看API文档：
```
http://localhost:8080/doc.html
```

## 📊 核心业务流程

### 内推信息发布流程
1. 用户登录系统 → 填写内推信息表单
2. 系统进行敏感词过滤和格式检查
3. 信息存入数据库，等待管理员审核
4. 审核通过后，信息对外展示

### 用户互动流程

## 🔥 项目亮点

- **高并发处理**: 通过自定义线程池和批处理机制，系统能够处理高并发点赞和访问请求
- **响应式编程**: 采用WebFlux和Project Reactor实现非阻塞式IO，提升系统吞吐量
- **智能交互**: 接入大模型API，提供智能问答服务，增强用户体验
- **缓存策略**: 多级缓存设计，Redis+本地缓存结合，减轻数据库压力
- **安全防护**: 敏感词过滤、XSS防御、JWT认证等多重安全机制


## 🤝 贡献指南

我们欢迎所有形式的贡献，无论是功能建议、代码提交还是文档改进：

1. Fork本仓库
2. 创建您的特性分支 (`git checkout -b feature/amazing-feature`)
3. 提交您的变更 (`git commit -m 'Add some amazing feature'`)
4. 推送到分支 (`git push origin feature/amazing-feature`)
5. 开启一个Pull Request

## 📄 许可证

本项目采用MIT许可证 - 详见 LICENSE.md 文件

## 📞 联系我们

有任何问题或建议，请联系：[项目负责人](3225483474@qq.com)

---

<div align="center">
  <strong>用技术的力量，让每一次推荐都成为企业与人才的双赢</strong>
</div>
