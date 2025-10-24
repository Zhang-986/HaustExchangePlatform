# HasutExchangeSharingPlatform 🎓

<div align="center">

[![Java](https://img.shields.io/badge/Java-8-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.7.6-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-2021.0.5-blue.svg)](https://spring.io/projects/spring-cloud)
[![Vue](https://img.shields.io/badge/Vue-3.5-green.svg)](https://vuejs.org/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

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
