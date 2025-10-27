# HAUST Exchange Platform

[中文](README_CN.md) | English

[![Java](https://img.shields.io/badge/Java-8-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.7.6-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-2021.0.5-blue.svg)](https://spring.io/projects/spring-cloud)
[![Vue](https://img.shields.io/badge/Vue-3.5-green.svg)](https://vuejs.org/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

## Overview

This is a campus job referral and exchange platform for students at Henan University of Science and Technology (HAUST). The platform helps students share job opportunities, internship referrals, and connect with each other through forums and instant messaging.

The project uses a microservices architecture based on Spring Cloud Alibaba, with separate backend services and a Vue 3 frontend.

## What It Does

- **User Management**: Student registration, login, and profile management with AI chatbot support
- **Job Referrals**: Post and browse job referrals with approval workflow
- **Forum**: Discussion boards for students to share experiences and ask questions
- **Instant Messaging**: Real-time chat between students using WebSocket
- **Gateway**: Single entry point that routes requests to appropriate services

## Architecture

The application is split into these microservices:

- `haust-user-service` - Handles user accounts and authentication
- `haust-referral-service` - Manages job referral posts
- `haust-forum-service` - Runs the forum functionality
- `haust-im-service` - Provides instant messaging
- `haust-gateway` - Routes requests and handles cross-cutting concerns
- `haust-common` - Shared code and utilities

All services register with Nacos for service discovery and use Spring Cloud Gateway for routing.

## Tech Stack

**Backend:**
- Spring Boot 2.7.6
- Spring Cloud 2021.0.5
- Spring Cloud Alibaba 2021.0.5.0
- Nacos (service discovery and configuration)
- MySQL 8.0+
- Redis 6.0+
- RabbitMQ 3.8+
- MyBatis
- JWT

**Frontend:**
- Vue 3
- TypeScript
- Vite
- Element Plus
- Pinia
- Axios

## Getting Started

### Requirements

You'll need these installed:
- JDK 8 or higher
- Maven 3.6+
- MySQL 8.0+
- Redis 6.0+
- RabbitMQ 3.8+
- Nacos 2.0+
- Node.js 16+ (for the frontend)

### Running Nacos

Download Nacos from https://nacos.io or use this command:

```bash
wget https://github.com/alibaba/nacos/releases/download/2.2.3/nacos-server-2.2.3.tar.gz
tar -zxvf nacos-server-2.2.3.tar.gz
cd nacos/bin

# Start Nacos in standalone mode
sh startup.sh -m standalone  # on Linux/Mac
startup.cmd -m standalone    # on Windows
```

Access the Nacos console at http://localhost:8848/nacos (username and password are both `nacos`).

### Running the Services

Start each service in a separate terminal:

```bash
# Gateway (start this first)
cd haust-gateway && mvn spring-boot:run

# Other services
cd haust-user-service && mvn spring-boot:run
cd haust-referral-service && mvn spring-boot:run
cd haust-forum-service && mvn spring-boot:run
cd haust-im-service && mvn spring-boot:run
```

Check the Nacos console to verify all services are registered and healthy.

### Running the Frontend

```bash
cd haust-frontend
npm install
npm run dev
```

## Configuration

Services register with Nacos using this basic configuration:

```yaml
spring:
  application:
    name: haust-user-service
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848
```

The gateway routes requests based on service names:

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

## How It Works

1. Client sends a request to the gateway (port 8080)
2. Gateway looks up the service in Nacos
3. Request is load-balanced across available service instances
4. Service processes the request and accesses MySQL/Redis/RabbitMQ as needed
5. Response flows back through the gateway to the client

## Additional Documentation

- [MIGRATION_TO_NACOS.md](MIGRATION_TO_NACOS.md) - Details about migrating to Nacos from Eureka
- [MIGRATION_SUMMARY.md](MIGRATION_SUMMARY.md) - Summary of the migration

## License

MIT License - see [LICENSE](LICENSE) file for details.

## Contributing

Pull requests are welcome. For major changes, please open an issue first to discuss what you'd like to change.
