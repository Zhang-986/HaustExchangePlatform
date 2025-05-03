# HAUST 内部推荐码共享平台 (一个Java后端学习项目) 🚀

嘿！这是我做的一个河南科技大学内部用的推荐码分享平台。主要是想用学到的Java后端技术搞点有用的东西，方便咱们科大学生找内推、分享面经。

## 这平台能干啥？ 🤔

*   **内推信息:**
    *   发布和查找各个公司的内推码和招聘信息。
    *   自己发布的内推都能在个人中心看到。
    *   (管理员功能): 审核大家发的内推，保证信息靠谱。
*   **交流讨论:**
    *   有个小论坛，可以发帖聊找工作、问问题、分享经验。
    *   可以评论、点赞，互相帮助嘛。
    *   想匿名发言也行。
*   **AI 智能助手 (实验性功能 ✨):**
    *   接了个 Dify 大模型 API，可以试试问它一些招聘相关的问题，看看它怎么回答。
    *   用了 WebFlux 处理，回复是流式输出的，感觉挺酷！

## 主要用了哪些技术？ 🛠️

这个项目主要是我学习和实践 Java 后端技术栈的一个尝试：

*   **后端:**
    *   **Spring Boot (2.7.6):** 核心框架，开发 Web 应用确实方便！
    *   **MyBatis:** 操作数据库，写 SQL 还是挺直接的。
    *   **MySQL:** 存数据的地方。
    *   **Redis:** 用来做缓存，提高点赞、用户信息这些常用数据的访问速度。
    *   **RabbitMQ:** 尝试用消息队列处理一些异步任务（比如敏感词检测通知）。
    *   **Spring Security / JWT:** 处理用户登录和权限控制。
    *   **Knife4j:** 生成 API 文档，方便前后端对接。
*   **前端 (另一个仓库 `haust_front`):**
    *   Vue 3 + TypeScript + Vite + Element Plus (这个 README 主要讲后端哈)
*   **其他:**
    *   JDK 8
    *   Maven

## 一些我觉得有意思的技术点💡

*   **AI 聊天机器人:**
    *   第一次尝试对接大模型 API (Dify)，用 `WebFlux` 实现流式响应，感觉打开了新世界大门！
    ```java
    // 用 Project Reactor 处理流式数据，让 AI 回复一点点显示出来
    difyApiUtil.streamChat(text, userId, null)
        .doOnNext(chunk -> log.info("收到一块回复: [{}]", chunk))
        // ... 省略部分代码 ...
        .block(); // 在某些场景下获取完整结果
    ```
*   **点赞批量处理:**
    *   为了防止大家疯狂点赞把数据库搞挂了（虽然现在用户不多哈哈），写了个简单的批处理队列，攒一波再更新数据库。
    ```java
    // 用了 BlockingQueue 和 ScheduledExecutorService 定时处理
    @Component
    public class BatchProcessUtil {
        // 定时任务，比如每 5 秒处理一次队列里的点赞消息
        @PostConstruct
        private void start(){
            scheduledExecutorService.scheduleWithFixedDelay(this::consume, 0, 5, TimeUnit.SECONDS);
        }
        // 把点赞消息放进队列
        public void process(LikeMsg msg){
            blockingQueue.offer(msg);
        }
    }
    ```
*   **用 AOP 做敏感词过滤:**
    *   学了 AOP，就想着用它来给发布内推、发帖这些操作加个敏感词检查，代码看起来整洁多了。
    ```java
    // 一个注解搞定敏感内容监控
    @SensitiveMonitor(ContentType.sharing)
    @Override
    public void addInfo(CodingSharingDTO codingSharingDTO) {
        // ... 正常的业务逻辑 ...
    }
    ```

## 项目结构 🏗️

就是经典的三层架构：

*   **Controller (表现层):** 处理前端请求，调用 Service。
*   **Service (业务层):** 写主要的业务逻辑。
*   **Mapper/DAO (持久层):** 用 MyBatis 跟数据库打交道。
*   还加了些配置 (`config`)、工具类 (`utils`)、实体类 (`entity`) 等等。

## 怎么跑起来？ 💻

**环境准备:**

*   JDK 8 或更高版本
*   Maven
*   MySQL 8.0+
*   Redis 6.0+
*   RabbitMQ (如果需要体验消息队列相关功能)

**步骤:**

1.  **克隆代码:**
    ```bash
    git clone https://github.com/your-username/HAUST_Internal_referral_code_sharing_platform.git
    cd HAUST_Internal_referral_code_sharing_platform/haust
    ```
2.  **配置:**
    *   修改 `src/main/resources/application-dev.yaml` 文件，把里面的 MySQL、Redis、RabbitMQ 连接信息改成你自己的。
3.  **运行:**
    *   可以直接在 IDE (比如 IDEA) 里运行 `HaustApplication.java`。
    *   或者用 Maven 打包运行:
        ```bash
        mvn package
        java -jar target/haust-0.0.1-SNAPSHOT.jar
        ```
4.  **API 文档:**
    *   启动后，在浏览器访问 `http://localhost:8080/doc.html` 可以看到所有后端接口。

## 一起搞？🤝

这个项目还有很多可以完善的地方，比如：

*   前端页面优化 (交给前端大佬们！)
*   更精细的权限控制
*   更完善的测试
*   ... (或者你有啥好点子？)

如果你也对这个项目感兴趣，欢迎：

1.  Fork 这个仓库
2.  创建你的分支 (`git checkout -b feature/cool-new-feature`)
3.  提交你的代码 (`git commit -m '加了个很酷的功能'`)
4.  Push 到你的分支 (`git push origin feature/cool-new-feature`)
5.  提个 Pull Request 给我

## 许可证 📄

本项目使用 MIT 许可证。

## 联系我 📫

有啥问题或者建议，可以 QQ 联系我：3225483474

---

<div align="center">
  <strong>希望能用代码给科大的同学们带来一点点方便！💪</strong>
</div>