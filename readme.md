# Portfolio Management System

## Overview
This system is a modular monolith for portfolio management, built with Spring Boot and PostgreSQL. It communicates with an external AI service (Python) via RabbitMQ. Quartz Scheduler periodically triggers portfolio analysis and sends stock data to the AI service for prediction.

All module facades and security layers are centralized in the Gateway module.

## Architecture
#### Modular Monolith

- Modules: Identity, Portfolio, Scheduler, Common, Gateway

- Database: Each module has its own tables

- Integration with AI: Asynchronous messaging via RabbitMQ

- Scheduler: Quartz-based jobs

- Security: JWT and Spring Security (configured in Gateway)


###  Module Responsibilities
#### Identity

- User authentication, roles, and permissions
- Suggested tables: users, roles, user_roles
- endpoints: /api/v1/auth/login, /api/v1/auth/register, /api/users/me

#### Portfolio

- Manages portfolios, positions, and AI predictions
- Suggested tables: portfolios, positions, predictions, prediction_actions
- endpoints: /api/v1/portfolios, /api/v1/portfolios/{id}/positions

#### Scheduler

- Quartz jobs for triggering portfolio checks
- Works with PortfolioFacade to fetch data and send messages
- endpoints: /api/scheduler/trigger, /api/scheduler/status

#### Common

- Audit logs, shared DTOs, error handling, configs, health checks

#### Gateway

- Central entry point
- Hosts controllers, facades, and security configurations
- Handles JWT authentication and routing to module facades


### Workflow
1. Quartz job is triggered at a configured interval.

2. Scheduler fetches portfolio data through PortfolioFacade.

3. A prediction_request message is published to RabbitMQ.

4. The Python AI service consumes the message, performs predictions, and publishes a prediction_response.

5. Java consumer processes the response and updates database tables (predictions, prediction_actions).

### Quick Start

```bash
 mvn clean install
 docker compose up
```

#### Scheduler Job (Quartz)
```java
import org.quartz.Job;
import org.quartz.JobExecutionContext;

@Component
public class MyJob implements Job {
    
    @Override
    public void execute(JobExecutionContext context) {
        // task will run periodically
    }
}
```

#### Configs
| environment | value|
|---|---|
| DB_USER | postgres user |
| DB_PASSWORD | postgres pass |
| DB_NAME | portfolio |
|JWT_SECRET_KEY| secret-key|
|SERVER_PORT| 4569|
|RABBIT_PASS| rabbit password|

### Notes

- Used Flyway for DB migrations.
- Used Testcontainers for integration tests.

## API Endpoints

##### can use swagger
`/api/v1/swagger-ui/index.html`

### Authentication

| Endpoint               | Method | Description                         | Auth Required |
|------------------------|--------|-------------------------------------|---------------|
| `/api/v1/auth/signup`   | POST   | Register new user                   | No            |
| `/api/v1/auth/login`      | POST   | Authenticate and get JWT token      | No            |

### User

| Endpoint               | Method | Description                         | Auth Required |
|------------------------|--------|-------------------------------------|---------------|
| `/api/v1/users`         | GET    | List all users       | Yes           |
| `/api/v1/users/{id}`    | GET    | Get user details         | Yes           |
| `/api/v1/users`         | POST   | Create new user                   | Yes         |
| `/api/v1/users/{id}`    | PUT    | Update user                       | Yes         |
| `/api/v1/users/{id}`    | DELETE | Delete user                       | Yes         |

### Portfolio

| Endpoint               | Method | Description                         | Auth Required |
|------------------------|--------|-------------------------------------|---------------|
| `/api/v1/portfolios`         | GET    | List all portfolios                    | Yes           |
| `/api/v1/portfolios/{id}`    | GET    | Get portfolio details with bids        | Yes           |
| `/api/v1/portfolios`         | POST   | Create new portfolio                   | Yes           |

### Scheduler

| Endpoint               | Method | Description                         | Auth Required |
|------------------------|--------|-------------------------------------|---------------|
| `/api/v1/scheduler`            | POST   | Submit new scheduler                      | Yes        |
| `/api/v1/scheduler/jobs`| GET    | get all scheduler                   | Admin         |
| `/api/v1/scheduler/jobs/{id}`| GET    | get scheduler         job          | Admin         |
| `/api/v1/scheduler/jobs/{id}`| PUT    | Update scheduler   job                | Admin         |
| `/api/v1/scheduler/jobs/{id}`| DELETE    | delete scheduler   job                | Admin         |
| `/api/v1/scheduler/jobs/type`| GET    | get  jobs  enum               | Admin         |
