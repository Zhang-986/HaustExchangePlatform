# HAUST Exchange Platform - Spring Cloud Microservices Architecture

[![Java](https://img.shields.io/badge/Java-8-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.7.6-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-2021.0.5-blue.svg)](https://spring.io/projects/spring-cloud)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

## 🎯 Architecture Overview

This platform has been successfully migrated from a monolithic architecture to a Spring Cloud microservices architecture, providing better scalability, maintainability, and independent deployment capabilities.

### Service Architecture Diagram

```
                                    ┌─────────────────┐
                                    │  Eureka Server  │
                                    │   Port: 8761    │
                                    └────────┬────────┘
                                             │
                     ┌───────────────────────┼───────────────────────┐
                     │                       │                       │
              ┌──────▼──────┐         ┌─────▼─────┐          ┌─────▼─────┐
              │   Gateway   │         │   User    │          │ Referral  │
              │ Port: 8080  │         │ Service   │          │  Service  │
              └─────────────┘         │Port: 8081 │          │Port: 8082 │
                     │                └───────────┘          └───────────┘
                     │
        ┌────────────┼────────────┐
        │            │            │
   ┌────▼────┐  ┌───▼────┐  ┌───▼────┐
   │  User   │  │ Forum  │  │   IM   │
   │ Service │  │Service │  │Service │
   └─────────┘  │8083    │  │ 8084   │
                └────────┘  └────────┘
```

## 🚀 Microservices Overview

### Infrastructure Services

#### 1. Eureka Service Registry (haust-eureka)
- **Port**: 8761
- **Purpose**: Service discovery and registration
- **Technology**: Spring Cloud Netflix Eureka
- **Access**: http://localhost:8761

**Features:**
- Central service registry
- Health monitoring
- Load balancing support
- Service instance management

#### 2. API Gateway (haust-gateway)
- **Port**: 8080
- **Purpose**: API routing, load balancing, unified entry point
- **Technology**: Spring Cloud Gateway
- **Access**: http://localhost:8080

**Route Configuration:**
```yaml
# User Service Routes
- /user/**
- /admin/login

# Referral Service Routes
- /user/addInfo, /user/modify, /user/page
- /admin/page, /admin/permit

# Forum Service Routes
- /user/posts/**, /user/replies/**
- /admin/replies/**

# IM Service Routes
- /im/**
```

#### 3. Common Module (haust-common)
- **Purpose**: Shared components across all services
- **Contents**:
  - Domain entities (User, Post, CodingSharing, etc.)
  - DTOs and VOs
  - Common utilities (JWT, Password, Dify AI)
  - Exception definitions
  - Constants

### Business Services

#### 4. User Service (haust-user-service)
- **Port**: 8081
- **Database**: User tables
- **Dependencies**: Redis, RabbitMQ, MySQL

**Responsibilities:**
- User registration and authentication
- JWT token generation and validation
- User login monitoring (via RabbitMQ)
- AI-powered Q&A assistant (Dify integration)
- Admin login and user log queries

**Key Endpoints:**
```
POST   /user/register       - User registration
POST   /user/login          - User login
GET    /user/info           - AI assistant Q&A
POST   /admin/login         - Admin login
GET    /admin/monitor       - Query user login logs
```

**Technologies:**
- Spring Boot Web
- MyBatis
- Redis for caching
- RabbitMQ for async messaging
- WebFlux for AI streaming responses
- Dashscope SDK for AI integration

#### 5. Referral Service (haust-referral-service)
- **Port**: 8082
- **Database**: Referral/CodingSharing tables
- **Dependencies**: Redis, MySQL

**Responsibilities:**
- Internal referral information management
- Sensitive content detection and filtering
- Admin approval workflow
- CRUD operations for job referrals

**Key Endpoints:**
```
POST   /user/addInfo        - Submit referral info
PUT    /user/modify         - Update referral info
GET    /user/page           - Query referrals (paginated)
DELETE /user/delete/{id}    - Delete referral
GET    /user/myInfo         - Query user's referrals
GET    /admin/page          - Admin query (pending approval)
PUT    /admin/permit        - Approve/reject referral
```

**Technologies:**
- Spring Boot Web
- MyBatis
- Redis for caching
- IK Analyzer for Chinese text segmentation
- Bloom Filter for sensitive word detection
- AOP for sensitive content monitoring

#### 6. Forum Service (haust-forum-service)
- **Port**: 8083
- **Database**: Post and PostReply tables
- **Dependencies**: Redis, RabbitMQ, MySQL

**Responsibilities:**
- Forum post management
- Comment and reply system
- Like/unlike functionality
- Batch processing for high-frequency operations
- Admin comment moderation

**Key Endpoints:**
```
POST   /user/posts          - Create post
PUT    /user/posts/{id}     - Update post
GET    /user/posts          - Query posts (paginated)
DELETE /user/posts/{id}     - Delete post
POST   /user/posts/{id}/like - Like/unlike post

POST   /user/replies        - Add comment
GET    /user/replies        - Query comments
POST   /user/replies/{id}/like - Like/unlike comment
GET    /user/replies/hot/{id}  - Get hot comments

DELETE /admin/replies/{id}  - Delete comment (admin)
```

**Technologies:**
- Spring Boot Web
- MyBatis
- Redis for caching
- RabbitMQ for like batch processing
- PageHelper for pagination

#### 7. IM Service (haust-im-service)
- **Port**: 8084
- **Database**: Message history (optional)
- **Dependencies**: Redis, RabbitMQ (optional)

**Responsibilities:**
- Real-time instant messaging
- WebSocket connection management
- Public chatroom functionality
- Private messaging (P2P)
- User presence management

**Key Features:**
```
WebSocket Endpoint: ws://localhost:8080/im/ws

Message Types:
- JOIN:    User joins chatroom
- CHAT:    Public message
- PRIVATE: Private message
- LEAVE:   User disconnects
```

**Technologies:**
- Spring Boot WebSocket
- STOMP protocol
- SockJS for fallback
- Redis for session management (optional)

## 📁 Project Structure

```
haust-exchange-platform/
├── pom.xml                          # Parent POM
├── haust-common/                    # Shared module
│   ├── domain/                      # Entities, DTOs, VOs
│   ├── exception/                   # Exception definitions
│   ├── constant/                    # Constants
│   ├── util/                        # Utilities
│   └── properties/                  # Configuration properties
├── haust-eureka/                    # Service registry
├── haust-gateway/                   # API Gateway
├── haust-user-service/              # User service
│   ├── controller/                  # REST controllers
│   ├── service/                     # Business logic
│   ├── mapper/                      # MyBatis mappers
│   ├── mq/                          # RabbitMQ listeners
│   └── interceptor/                 # JWT interceptors
├── haust-referral-service/          # Referral service
│   ├── controller/
│   ├── service/
│   ├── mapper/
│   ├── aspect/                      # AOP aspects
│   └── util/                        # Service-specific utils
├── haust-forum-service/             # Forum service
│   ├── controller/
│   ├── service/
│   ├── mapper/
│   ├── mq/                          # Like batch processor
│   └── util/
└── haust-im-service/                # IM service
    ├── controller/
    ├── handler/                     # WebSocket handlers
    ├── config/                      # WebSocket config
    └── domain/                      # Message entities
```

## 🛠️ Technology Stack

### Core Technologies
- **Java**: 8
- **Spring Boot**: 2.7.6
- **Spring Cloud**: 2021.0.5
- **Maven**: Multi-module project

### Spring Cloud Components
- **Eureka**: Service discovery
- **Gateway**: API gateway and routing
- **OpenFeign**: Inter-service communication (ready)
- **LoadBalancer**: Client-side load balancing

### Data & Caching
- **MySQL**: 8.0.24
- **MyBatis**: 2.3.0
- **Redis**: 6.0+
- **Redisson**: 3.17.7
- **PageHelper**: 1.4.1

### Messaging & WebSocket
- **RabbitMQ**: 2.4.12
- **Spring WebSocket**: 2.7.6
- **STOMP**: Protocol for WebSocket

### Utilities
- **JWT**: 4.4.0 - Token-based authentication
- **Lombok**: 1.18.20 - Code generation
- **Hutool**: 5.7.17 - Java utility library
- **FastJSON**: 2.0.31 - JSON processing
- **IK Analyzer**: 8.5.0 - Chinese text segmentation
- **Knife4j**: 3.0.3 - API documentation

### AI Integration
- **Dashscope SDK**: 2.18.3 - Alibaba AI services
- **Spring WebFlux**: Reactive programming for streaming

## 🚀 Getting Started

### Prerequisites
```bash
# Required
- JDK 8 or higher
- Maven 3.6+
- MySQL 8.0+
- Redis 6.0+
- RabbitMQ 3.8+

# Optional (for development)
- IntelliJ IDEA or Eclipse
- Postman or similar API testing tool
```

### Database Setup
```sql
-- Create databases for each service
CREATE DATABASE haust_user DEFAULT CHARACTER SET utf8mb4;
CREATE DATABASE haust_referral DEFAULT CHARACTER SET utf8mb4;
CREATE DATABASE haust_forum DEFAULT CHARACTER SET utf8mb4;

-- Import schema from haust-monolith/sql (if available)
```

### Configuration
Update configuration files for each service in `src/main/resources/application-dev.yaml`:

```yaml
haust:
  datasource:
    host: your_mysql_host
    port: 3306
    database: your_database_name
    username: your_username
    password: your_password
  redis:
    host: your_redis_host
    port: 6379
    password: your_redis_password
  rabbitmq:
    host: your_rabbitmq_host
    port: 5672
    username: guest
    password: guest
```

### Build All Services
```bash
# Build all modules
cd HaustExchangePlatform
mvn clean package -DskipTests

# Or build specific service
cd haust-user-service
mvn clean package -DskipTests
```

### Start Services

**Recommended startup order:**

1. **Start Infrastructure Services**
```bash
# 1. Start Eureka (wait for it to fully start)
cd haust-eureka
mvn spring-boot:run

# 2. Start Gateway
cd haust-gateway
mvn spring-boot:run
```

2. **Start Business Services** (in any order)
```bash
# User Service
cd haust-user-service
mvn spring-boot:run

# Referral Service
cd haust-referral-service
mvn spring-boot:run

# Forum Service
cd haust-forum-service
mvn spring-boot:run

# IM Service
cd haust-im-service
mvn spring-boot:run
```

### Verify Services
1. **Check Eureka Dashboard**: http://localhost:8761
   - All services should show as UP
   
2. **Test Gateway**: http://localhost:8080
   - All requests should route through gateway

3. **API Documentation**: 
   - User Service: http://localhost:8081/doc.html
   - Referral Service: http://localhost:8082/doc.html
   - Forum Service: http://localhost:8083/doc.html
   - IM Service: http://localhost:8084/doc.html

## 📡 API Examples

### User Registration
```bash
POST http://localhost:8080/user/register
Content-Type: application/json

{
  "username": "testuser",
  "password": "password123",
  "email": "test@example.com"
}
```

### Submit Referral
```bash
POST http://localhost:8080/user/addInfo
Authorization: Bearer <your-jwt-token>
Content-Type: application/json

{
  "company": "Tech Company",
  "position": "Software Engineer",
  "description": "Looking for backend developers",
  "contactInfo": "hr@company.com"
}
```

### Create Forum Post
```bash
POST http://localhost:8080/user/posts
Authorization: Bearer <your-jwt-token>
Content-Type: application/json

{
  "title": "Interview Experience",
  "content": "Sharing my interview experience at...",
  "anonymous": false
}
```

### WebSocket IM Example (JavaScript)
```javascript
const socket = new SockJS('http://localhost:8080/im/ws');
const stompClient = Stomp.over(socket);

stompClient.connect({}, function(frame) {
    console.log('Connected: ' + frame);
    
    // Subscribe to public messages
    stompClient.subscribe('/topic/public', function(message) {
        console.log('Received:', JSON.parse(message.body));
    });
    
    // Send message
    stompClient.send("/app/chat.sendMessage", {}, JSON.stringify({
        sender: 'username',
        content: 'Hello!',
        type: 'CHAT'
    }));
});
```

## 🔧 Development

### Adding a New Service
1. Create module directory under project root
2. Create `pom.xml` with parent reference
3. Add module to parent `pom.xml`
4. Create application class with `@EnableDiscoveryClient`
5. Configure `application.yml` with unique port
6. Register routes in Gateway configuration

### Inter-Service Communication (Future)
```java
// Example Feign client
@FeignClient(name = "haust-user-service")
public interface UserServiceClient {
    @GetMapping("/user/{id}")
    User getUserById(@PathVariable Long id);
}
```

## 📊 Monitoring & Health

Each service exposes actuator endpoints:
```
GET http://localhost:808X/actuator/health
GET http://localhost:808X/actuator/info
```

## 🔒 Security Considerations

- JWT tokens validated at User Service
- Sensitive content filtering in Referral Service
- Gateway-level authentication (to be implemented)
- Service-to-service authentication (to be implemented)

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch
3. Commit your changes
4. Push to the branch
5. Open a Pull Request

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👥 Contact

- QQ: 3225483474
- Project Link: https://github.com/qmhwx666/HaustExchangePlatform

---

**Built with ❤️ using Spring Cloud Microservices Architecture**
