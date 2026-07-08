# Scenario 3: Citizen-based Road Condition Monitoring

This repository contains the DMSA SoSe26 lab project of team **backDoor**.
The selected scenario is a citizen-based platform for reporting and processing
road infrastructure problems such as potholes, broken traffic lights, damaged
signage, and similar issues.

The project was designed as a microservice architecture using Domain-Driven
Design. The implementation focuses on the currently functional backend services
and documents the remaining contexts through scaffolds, design artifacts, and
LEMMA models.

## Evaluation Branch

Please evaluate the `main` branch.

## Application Goal

Citizens can report road condition problems with descriptions, categories,
location information, and media. Authorities can review and prioritize reports,
dispatch technicians, update processing status, and close reports. The platform
is intended to improve road safety and give citizens transparency about the
status of submitted issues.

## Main Requirements

- User registration and role-based access for citizens, authorities, and technicians
- Submission of road condition reports
- Location data for each report
- Categorization of reports
- Notification of responsible authorities
- Processing, prioritization, and dispatching of reports
- Status updates for submitted reports
- Feedback after resolution
- Analysis of reported problems and bottlenecks
- Security and privacy for user/report data

## Architecture and Technologies

- Microservice architecture
- Domain-Driven Design with bounded contexts
- Spring Boot backend services
- Spring MVC and REST APIs
- Spring Data JPA with H2 for local demonstration
- Spring Security where applicable
- Spring Cloud Config for centralized configuration
- Eureka for service discovery
- Resilience4j circuit breaker for fault tolerance
- Simple web UIs for demonstration
- LEMMA models for architecture reconstruction

## Services and Current Status

| Module | Bounded Context | Port | Status |
|---|---|---:|---|
| `config-server` | Centralized Configuration | 8888 | Implemented |
| `eureka-server/eureka-server` | Service Discovery | 8761 | Implemented |
| `User_Service` | Identity and Access Management | 8082 | Implemented |
| `Dispatch_Service` | Maintenance Dispatch | 8081 | Implemented and demo-ready |
| `IssueReport_Service` | Issue Reporting | varies/local | Partly implemented/scaffolded |
| `Notification_Service` | Notification | varies/local | Scaffold |
| `FeedBack_Service` | Feedback | varies/local | Scaffold |
| `Media_Service` | Media / Photo Management | varies/local | Partly implemented/scaffolded |

Because the team composition changed during the semester, the most complete
runtime path for the final demonstration is the combination of `User_Service`
and `Dispatch_Service`. Missing cross-service behavior is represented through
mocked hand-over data and fallback behavior where necessary.

## Running the Functional Demo

Start the services in separate terminals, infrastructure first:

```powershell
cd config-server
.\mvnw.cmd spring-boot:run
```

```powershell
cd eureka-server\eureka-server
.\mvnw.cmd spring-boot:run
```

```powershell
cd User_Service
.\mvnw.cmd spring-boot:run
```

```powershell
cd Dispatch_Service
.\mvnw.cmd spring-boot:run
```

Open the Dispatch demo UI:

```text
http://localhost:8081/
```

Useful infrastructure URLs:

```text
Config Server: http://localhost:8888
Eureka Server: http://localhost:8761
User Service:  http://localhost:8082
Dispatch UI:   http://localhost:8081/
```

The services can also be started without Config Server or Eureka for local
testing, because the config import is optional. If `User_Service` is unavailable,
`Dispatch_Service` still demonstrates fault tolerance by returning fallback
technician data through its circuit breaker.

## Dispatch Service Demo Flow

The `Dispatch_Service` implements the Maintenance Dispatch bounded context. It
receives reports from Issue Reporting, reviews them, assigns priorities, assigns
work to technicians, records status history, and resolves or rejects reports.

Recommended presentation flow:

1. Open `http://localhost:8081/`.
2. Show the seeded demo reports.
3. Create a new report using the "Ingest report" form.
4. Set the report to review.
5. Assign the report to a technician.
6. Resolve or reject the report.
7. Use the API endpoints to show assignments and status history if needed.
8. Stop or omit `User_Service` to explain the circuit breaker fallback behavior.

## Dispatch REST API

Base path: `/api`

| Method | Path | Purpose |
|---|---|---|
| `GET` | `/reports` | List all reports |
| `GET` | `/reports/open` | List non-terminal reports |
| `GET` | `/reports/{id}` | Read one report |
| `GET` | `/reports/status/{status}` | Filter reports by status |
| `POST` | `/reports` | Ingest a report from Issue Reporting |
| `POST` | `/reports/{id}/priority` | Set priority |
| `POST` | `/reports/{id}/assign` | Assign work and move to `IN_PROGRESS` |
| `POST` | `/reports/{id}/status` | Change status |
| `POST` | `/reports/{id}/resolve` | Resolve a report |
| `POST` | `/reports/{id}/reject` | Reject a report |
| `GET` | `/reports/{id}/assignments` | Show work assignments |
| `GET` | `/reports/{id}/history` | Show status history |
| `GET` | `/technicians` | Load technicians from `User_Service` with fallback |
| `GET` | `/info` | Show centralized config message |

## Dispatch DDD Mapping

- Aggregate root: `Report`
- Entities: `WorkAssignment`, `StatusHistory`
- Value objects / enums: `ReportStatus`, `Priority`, `AssignmentStatus`
- Domain service: `DispatchService`
- Repositories: `ReportRepository`, `WorkAssignmentRepository`, `StatusHistoryRepository`
- Outbound adapters: `RestClient`, `UserServiceClient`

## Documentation and Design Artifacts

Documentation and design artifacts are included in the repository:

- `wiki.txt`
- `wiki_images/`
- `lemma/`
- `Labs/`

The project wiki is available here:

```text
https://github.com/arnob32/dmsa-sose26-backDoor/wiki
```

Design artifacts include requirements, domain model, bounded contexts, context
maps, event storming material, tactical design/UML material, and LEMMA models.

## LEMMA

The LEMMA models are stored in the top-level `lemma` folder, as required by the
lab submission instructions.

The most complete LEMMA reconstruction currently covers the Dispatch service:

- `lemma/dispatch-service/DispatchDomain.data`
- `lemma/dispatch-service/DispatchService.services`
- `lemma/dispatch-service/DispatchService.mapping`
- `lemma/dispatch-service/DispatchService.operation`
- `lemma/technology/javaWithSpring.technology`
- `lemma/technology/docker.technology`

## Team

Team name: **backDoor**

| Member | Matriculation Number |
|---|---:|
| Raju Naidu | 7213668 |
| Junaid Ahmed | 7222074 |
| Helmi Zaki Fadhali | 7225540 |
| Nawshad Fahim | 7216629 |

## Course

Dortmund University of Applied Sciences and Arts  
Design and Modelling of Complex Software Architecture, SoSe26
