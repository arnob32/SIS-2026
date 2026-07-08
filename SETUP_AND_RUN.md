# Setup and Run Guide

## Project Overview

This repository implements a citizen-based road condition monitoring platform using a Spring Boot microservice architecture.
It includes the following services:

- `config-server` — Spring Cloud Config Server for centralized configuration
- `eureka-server` — Spring Cloud Netflix Eureka Service Discovery
- `User_Service` — User and authentication service
- `Dispatch_Service` — Maintenance dispatch service

The repository also contains additional microservice scaffolds (which were not completed due to other members dropping out) :
- `IssueReport_Service`
- `Notification_Service`
- `FeedBack_Service`
- `Media_Service`

## Requirements

### Local requirements

- Java 17 JDK
- Maven 3.x or the included Maven wrapper (`mvnw`, `mvnw.cmd`)
- Docker Desktop with Docker Compose support
- Git client (optional)

### Recommended environment

- Windows PowerShell, macOS Terminal, or Linux shell
- IDE such as VS Code, IntelliJ IDEA, or Eclipse

## Build and Run with Docker Compose

The repository includes a root-level `docker-compose.yml` file.

From the repository root:

```powershell
docker compose up --build
```

Then open the services in your browser:

- Eureka dashboard: http://localhost:8761
- Config server: http://localhost:8888
- Dispatch service: http://localhost:8081
- User service: http://localhost:8082
- Media service: http://localhost:8085

To stop the stack:

```powershell
docker compose down
```

## Run services individually with Maven

You can also run services locally without Docker.
Use the Maven wrapper if Maven is not installed globally.

### 1. Start config-server

```powershell
cd config-server
./mvnw.cmd spring-boot:run
```

### 2. Start eureka-server

```powershell
cd eureka-server/eureka-server
./mvnw.cmd spring-boot:run
```

### 3. Start user-service

```powershell
cd User_Service
./mvnw.cmd spring-boot:run
```

### 4. Start dispatch-service

```powershell
cd Dispatch_Service
./mvnw.cmd spring-boot:run
```

## Service Ports

| Service | Port |
|---|---|
| `eureka-server` | `8761` |
| `config-server` | `8888` |
| `dispatch-service` | `8081` |
| `user-service` | `8082` |
| `user-service` | `8085` |

## Notes

- The `User_Service` and `Dispatch_Service` are configured to use the config server and Eureka discovery.
- If you run services manually, start `config-server` and `eureka-server` first.

## Troubleshooting

- If a service fails because the port is already in use, stop the process using that port or change the port in `src/main/resources/application.properties`.
- If Docker Compose fails, make sure Docker Desktop is running and your machine can build images.
- If config server is unreachable, verify `spring.config.import` values in the service `application.properties` files.

## Additional information

This setup guide is intended to help you get the core services running quickly.
For implementation details and service contracts, refer to the code and the repository `README.md`.
