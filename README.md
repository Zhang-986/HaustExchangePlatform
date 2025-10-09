# HAUST Exchange Platform 🚀

[![Java](https://img.shields.io/badge/Java-8-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.7.6-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![RabbitMQ](https://img.shields.io/badge/RabbitMQ-Enabled-orange.svg)](https://www.rabbitmq.com/)
[![Redis](https://img.shields.io/badge/Redis-6.0+-red.svg)](https://redis.io/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0+-blue.svg)](https://www.mysql.com/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

> Hi there! 👋 I'm a Java backend developer currently working as a monitoring/listener intern in Hangzhou. This is my hands-on learning project where I explore fascinating monitoring technologies and build something practical for the HAUST (Henan University of Science and Technology) community.

**About Me:** I'm deeply passionate about modern monitoring and listening technologies in Java ecosystems. This project represents my journey into event-driven architectures, asynchronous processing, and real-time monitoring systems - technologies that truly excite me! 🔥

## What Does This Platform Do? 🤔

This is an internal referral code sharing platform for HAUST students, designed to help with job hunting and experience sharing:

*   **Referral Information Hub:**
    *   Post and discover internal referral codes and job opportunities from various companies.
    *   Track your published referrals in the personal center.
    *   (Admin feature): Review and moderate submitted referrals to ensure quality.
*   **Community Discussion Forum:**
    *   A forum for posting about job hunting, asking questions, and sharing experiences.
    *   Support for comments, likes, and community engagement.
    *   Anonymous posting available.
*   **AI Intelligent Assistant (Experimental ✨):**
    *   Integrated with Dify large language model API for recruitment-related Q&A.
    *   Implemented using WebFlux for streaming responses - pretty cool!

## Technology Stack 🛠️

As a monitoring/listener enthusiast, I've focused on implementing robust event-driven and monitoring patterns:

*   **Backend Core:**
    *   **Spring Boot (2.7.6):** The foundation for building this web application.
    *   **MyBatis:** For database operations with straightforward SQL control.
    *   **MySQL:** Primary data storage.
    *   **Redis:** Caching layer for likes, user info, and frequently accessed data.
    *   **RabbitMQ:** 🎯 **Message queue for asynchronous task processing** - This is where the magic happens! Used for sensitive content monitoring notifications and user behavior tracking.
    *   **Spring Security / JWT:** Authentication and authorization.
    *   **Knife4j:** API documentation generator.
*   **Frontend (separate repository `haust_front`):**
    *   Vue 3 + TypeScript + Vite + Element Plus (This README focuses on backend)
*   **Development Environment:**
    *   JDK 8
    *   Maven

## Technical Highlights (What I Find Fascinating!) 💡

### 🎯 RabbitMQ Message Listeners
As someone passionate about monitoring technologies, implementing message listeners has been incredibly rewarding! The system uses RabbitMQ to handle asynchronous events:

```java
// User behavior monitoring listener
@RabbitListener(
    bindings = @QueueBinding(
        value = @Queue(value = MqQueueConstant.USER_MONITOR_QUEUE, durable = "true"),
        exchange = @Exchange(value = MqExchangeConstant.USER_MONITOR_EXCHANGE, type = ExchangeTypes.TOPIC),
        key = MqKeyConstant.USER_MONITOR_KEY
    )
)
public void userInfoListener(UserMsg userMsg) {
    if(BeanUtil.isEmpty(userMsg)) return;
    userService.addMonitor(userMsg);
}
```

**Why this excites me:** RabbitMQ listeners enable decoupled, scalable architectures. The ability to monitor user activities asynchronously without blocking the main application flow is fascinating!

### 🔍 AOP-Based Sensitive Content Monitoring
Using Aspect-Oriented Programming for content monitoring keeps the code clean and maintainable:

```java
// Annotation-driven sensitive content monitoring
@SensitiveMonitor(ContentType.sharing)
@Override
public void addInfo(CodingSharingDTO codingSharingDTO) {
    // ... business logic executes safely after monitoring check ...
}
```

**The monitoring aspect intercepts method calls:**
```java
@Around("sensitiveMonitorPointcut()")
public Object aroundSensitiveMonitor(ProceedingJoinPoint joinPoint) throws Throwable {
    log.info("Sensitive content monitoring engaged");
    // Extract content based on type
    // Use IK Analyzer for word segmentation
    // Check against Bloom Filter for sensitive words
    // Throw exception if sensitive content detected
    return joinPoint.proceed();
}
```

**Why this is interesting:** AOP provides a non-invasive way to add monitoring and validation logic. It's a powerful pattern for cross-cutting concerns!

### 🤖 AI Chatbot with Reactive Streams
First time integrating a large language model API (Dify) with WebFlux for streaming responses:

```java
// Using Project Reactor to handle streaming data
difyApiUtil.streamChat(text, userId, null)
    .doOnNext(chunk -> log.info("Received chunk: [{}]", chunk))
    // ... additional stream processing ...
    .block(); // Get complete result when needed
```

**Why I love this:** Reactive programming with streaming responses opens up a whole new world of possibilities. Watching AI responses flow in real-time is genuinely cool!

### ⚡ Batch Processing for Likes
To prevent database overload from rapid-fire likes (future-proofing for scale!), I implemented a simple batch processing queue:

```java
@Component
public class BatchProcessUtil {
    // Scheduled task processes queue every 5 seconds
    @PostConstruct
    private void start() {
        scheduledExecutorService.scheduleWithFixedDelay(
            this::consume, 0, 5, TimeUnit.SECONDS
        );
    }
    
    // Queue like messages for batch processing
    public void process(LikeMsg msg) {
        blockingQueue.offer(msg);
    }
}
```

**Why this matters:** This pattern demonstrates how to handle high-frequency events efficiently - a crucial skill for monitoring systems!

## Project Architecture 🏗️

Classic three-tier architecture with monitoring enhancements:

*   **Controller (Presentation Layer):** Handles HTTP requests and calls Service layer.
*   **Service (Business Layer):** Core business logic implementation.
*   **Mapper/DAO (Persistence Layer):** MyBatis interfaces for database operations.
*   **Cross-Cutting Concerns:**
    *   `aspect/` - AOP aspects for monitoring and validation
    *   `mq/` - Message queue listeners for asynchronous processing
    *   `config/` - Configuration classes
    *   `utils/` - Utility classes
    *   `entity/` - Domain entities

## Getting Started 💻

**Prerequisites:**

*   JDK 8 or higher
*   Maven
*   MySQL 8.0+
*   Redis 6.0+
*   RabbitMQ (for message queue functionality)

**Setup Instructions:**

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/Zhang-986/HaustExchangePlatform.git
    cd HaustExchangePlatform/haust
    ```

2.  **Configure:**
    *   Edit `src/main/resources/application-dev.yaml`
    *   Update MySQL, Redis, and RabbitMQ connection settings with your local configuration.

3.  **Run:**
    *   **Option A:** Run directly in your IDE (e.g., IntelliJ IDEA) by executing `HaustApplication.java`
    *   **Option B:** Build and run with Maven:
        ```bash
        mvn package
        java -jar target/haust-0.0.1-SNAPSHOT.jar
        ```

4.  **API Documentation:**
    *   After starting, access the interactive API documentation at: `http://localhost:8080/doc.html`

## Future Enhancements 🚀

There's always room for improvement and more interesting things to explore:

*   **Enhanced Monitoring:**
    *   Add more comprehensive metrics collection
    *   Implement distributed tracing
    *   Real-time performance dashboards
*   **Frontend Optimization** (in the separate frontend repo)
*   **Fine-grained Permission Control**
*   **Comprehensive Test Coverage**
*   **More Event-Driven Patterns** - Always interested in new monitoring and messaging patterns!

## Contributing 🤝

I'm always excited to learn from others and explore new ideas! If you're interested in this project:

1.  Fork the repository
2.  Create your feature branch (`git checkout -b feature/awesome-feature`)
3.  Commit your changes (`git commit -m 'Add some awesome feature'`)
4.  Push to the branch (`git push origin feature/awesome-feature`)
5.  Open a Pull Request

## License 📄

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Contact 📫

Feel free to reach out with questions or suggestions:
*   QQ: 3225483474

---

<div align="center">
  <strong>Building practical solutions while exploring fascinating monitoring technologies! 💪</strong>
  <br>
  <em>Learning, experimenting, and growing as a Java backend developer in Hangzhou</em>
</div>