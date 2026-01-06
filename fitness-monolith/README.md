# 🏋️ Fitness Tracking Monolith API

A comprehensive **RESTful fitness tracking backend** built with **Spring Boot 4.0.1**, designed to manage user activities, provide personalized fitness recommendations, and implement enterprise-grade security with JWT authentication.

---

## 📋 Table of Contents

- [Overview](#overview)
- [Key Features](#key-features)
- [Tech Stack](#tech-stack)
- [Architecture](#architecture)
- [Database Schema](#database-schema)
- [API Endpoints](#api-endpoints)
- [Security Implementation](#security-implementation)
- [Getting Started](#getting-started)
- [Docker Deployment](#docker-deployment)
- [Configuration](#configuration)
- [Why This Architecture?](#why-this-architecture)
- [Technical Decisions](#technical-decisions)

---

## 🎯 Overview

This project is a **production-ready fitness tracking API** that allows users to:
- Register and authenticate securely with **JWT tokens**
- Log various fitness activities (50+ activity types supported)
- Store flexible activity metrics in **JSON format**
- Generate and retrieve personalized fitness recommendations
- Manage user profiles with role-based access control

Built as a **monolithic application** for simplicity and ease of deployment, while maintaining clean architecture principles with clear separation of concerns.

---

## ✨ Key Features

### 🔐 Authentication & Authorization
- **JWT-based stateless authentication** using JJWT library (0.13.0)
- **BCrypt password encryption** for secure credential storage
- **Role-Based Access Control (RBAC)** - USER and ADMIN roles
- Custom JWT filter for request authentication
- Token validation with cryptographic signing (HMAC-SHA256)

### 📊 Activity Tracking
- Support for **50+ activity types** (Cardio, Strength, Sports, Yoga, Martial Arts, etc.)
- **Flexible metrics storage** using PostgreSQL JSON columns
- Track duration, calories burned, start time, and custom metrics
- User-specific activity history with timestamps
- Automatic entity auditing with `@CreationTimestamp` and `@UpdateTimestamp`

### 💡 Recommendation System
- Generate fitness recommendations linked to specific activities
- Store structured suggestions (improvements, safety tips, suggestions)
- Query recommendations by user or by activity
- JSON-based storage for flexible recommendation formats

### 🏗️ Clean Architecture
- **Layered architecture**: Controller → Service → Repository
- **DTO pattern** for clean API contracts
- **ModelMapper** for automatic entity-DTO transformations
- **Global exception handling** with `@RestControllerAdvice`
- **Bean Validation** for input sanitization

---

## 🛠️ Tech Stack

### Core Framework
- **Spring Boot 4.0.1** - Enterprise application framework
- **Spring Web MVC** - RESTful web services
- **Spring Security** - Authentication and authorization
- **Spring Data JPA** - Data persistence layer

### Database
- **PostgreSQL** - Primary relational database
- **Hibernate ORM** - Object-relational mapping
- **HikariCP** - High-performance connection pooling

### Security
- **JJWT (0.13.0)** - JSON Web Token implementation
- **BCrypt** - Password hashing algorithm
- **Spring Security Filter Chain** - Request filtering

### Utilities
- **Lombok** - Boilerplate code reduction (@Data, @Builder, @RequiredArgsConstructor)
- **ModelMapper (3.2.4)** - Object mapping
- **Jackson** - JSON serialization/deserialization
- **Bean Validation** - Input validation (@Valid, @Email, @NotBlank)

### Documentation & DevOps
- **Swagger/OpenAPI 3.0.1** - Interactive API documentation
- **Maven** - Build automation and dependency management
- **Docker** - Containerization (Eclipse Temurin JRE 25)
- **Environment-based configuration** - Externalized database credentials

### Build Tools
- **Java 25** - Latest LTS version
- **Maven Compiler Plugin** - Compilation
- **Spring Boot Maven Plugin** - Packaging

---

## 🏛️ Architecture

### Layered Architecture Pattern

```
┌─────────────────────────────────────────┐
│         Controllers (REST API)          │  ← HTTP Requests
├─────────────────────────────────────────┤
│         DTOs (Data Transfer)            │  ← Request/Response Models
├─────────────────────────────────────────┤
│      Services (Business Logic)          │  ← Core Application Logic
├─────────────────────────────────────────┤
│    Repositories (Data Access Layer)     │  ← Spring Data JPA
├─────────────────────────────────────────┤
│      Entities (Domain Models)           │  ← JPA Entities
├─────────────────────────────────────────┤
│         PostgreSQL Database             │  ← Data Persistence
└─────────────────────────────────────────┘
```

### Security Flow

```
Request → JWT Filter → Validate Token → Extract User ID & Roles 
    → Set Authentication → Security Context → Controller → Service
```

### Package Structure

```
com.project.fitness/
├── config/              # Application configuration beans
├── controller/          # REST API endpoints
├── dto/                 # Data Transfer Objects
├── exception/           # Global exception handlers
├── model/               # JPA entities
├── repository/          # Spring Data repositories
├── security/            # JWT and security configuration
└── service/             # Business logic layer
```

---

## 🗄️ Database Schema

### Entity Relationships

```
User (1) ─────────── (*) Activity
  │                       │
  │                       │
  └──────(*) Recommendation (*)
```

### Table: `fitness_user`
- `id` (UUID) - Primary Key
- `email` (VARCHAR, UNIQUE)
- `password` (VARCHAR) - BCrypt hashed
- `first_name`, `last_name` (VARCHAR)
- `role` (ENUM: USER, ADMIN)
- `created_at`, `updated_at` (TIMESTAMP)

### Table: `activity`
- `id` (UUID) - Primary Key
- `user_id` (UUID) - Foreign Key → fitness_user
- `type` (ENUM) - 50+ activity types
- `additional_metrics` (JSON) - Flexible key-value storage
- `duration` (INTEGER) - Minutes
- `calories_burned` (INTEGER)
- `start_time` (TIMESTAMP)
- `created_at`, `updated_at` (TIMESTAMP)

### Table: `recommendation`
- `id` (UUID) - Primary Key
- `user_id` (UUID) - Foreign Key → fitness_user
- `activity_id` (UUID) - Foreign Key → activity
- `type` (VARCHAR)
- `recommendation` (VARCHAR 2000)
- `improvements` (JSON) - Array of strings
- `suggestions` (JSON) - Array of strings
- `safety` (JSON) - Array of strings
- `created_at`, `updated_at` (TIMESTAMP)

### Why This Design?

**1. UUID Primary Keys:**  
UUIDs prevent sequential ID enumeration attacks and allow distributed ID generation for future microservices migration.

**2. JSON Columns:**  
PostgreSQL's native JSON support allows flexible schema evolution without database migrations for activity-specific metrics (e.g., running has pace, swimming has stroke count).

**3. Bidirectional Relationships:**  
`@OneToMany` mappings with `cascade = CascadeType.ALL` and `orphanRemoval = true` ensure referential integrity and automatic cleanup.

**4. Lazy Fetching:**  
`@ManyToOne(fetch = FetchType.LAZY)` prevents N+1 query problems and reduces memory overhead.

---

## 🌐 API Endpoints

### Authentication Endpoints

#### `POST /api/users/register`
Register a new user account.

**Request Body:**
```json
{
  "email": "user@example.com",
  "password": "securePassword123",
  "firstName": "John",
  "lastName": "Doe"
}
```

**Response:** `200 OK`
```json
{
  "id": "uuid-here",
  "email": "user@example.com",
  "firstName": "John",
  "lastName": "Doe",
  "role": "USER"
}
```

---

#### `POST /api/users/signin`
Authenticate and receive JWT token.

**Request Body:**
```json
{
  "email": "user@example.com",
  "password": "securePassword123"
}
```

**Response:** `200 OK`
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "user": {
    "id": "uuid-here",
    "email": "user@example.com",
    "firstName": "John",
    "role": "USER"
  }
}
```

**Error Response:** `401 Unauthorized` (invalid credentials)

---

### Activity Endpoints (Requires Authentication)

#### `POST /api/activities/create`
Log a new fitness activity.

**Headers:**
- `Authorization: Bearer <JWT_TOKEN>`
- `userId: <USER_UUID>`

**Request Body:**
```json
{
  "type": "RUNNING",
  "duration": 45,
  "caloriesBurned": 420,
  "startTime": "2026-01-07T06:30:00",
  "additionalMetrics": {
    "distance": 7.5,
    "avgPace": "6:00",
    "elevation": 120,
    "heartRate": 145
  }
}
```

**Response:** `200 OK`
```json
{
  "id": "activity-uuid",
  "userId": "user-uuid",
  "type": "RUNNING",
  "duration": 45,
  "caloriesBurned": 420,
  "startTime": "2026-01-07T06:30:00",
  "additionalMetrics": {
    "distance": 7.5,
    "avgPace": "6:00",
    "elevation": 120,
    "heartRate": 145
  },
  "createdAt": "2026-01-07T07:15:00",
  "updatedAt": "2026-01-07T07:15:00"
}
```

---

#### `GET /api/activities/all`
Retrieve all activities for authenticated user.

**Headers:**
- `Authorization: Bearer <JWT_TOKEN>`
- `USER_ID: <USER_UUID>`

**Response:** `200 OK`
```json
[
  {
    "id": "activity-uuid-1",
    "type": "RUNNING",
    "duration": 45,
    "caloriesBurned": 420,
    ...
  },
  {
    "id": "activity-uuid-2",
    "type": "WEIGHT_TRAINING",
    "duration": 60,
    "caloriesBurned": 300,
    ...
  }
]
```

---

### Recommendation Endpoints (Requires Authentication)

#### `POST /api/recommendation/generate`
Generate fitness recommendation for an activity.

**Headers:**
- `Authorization: Bearer <JWT_TOKEN>`
- `userId: <USER_UUID>`
- `activityId: <ACTIVITY_UUID>`

**Request Body:**
```json
{
  "improvements": [
    "Increase running distance by 10% next week",
    "Focus on maintaining consistent pace"
  ],
  "suggestions": [
    "Try interval training twice a week",
    "Add strength training for leg muscles"
  ],
  "safety": [
    "Stay hydrated during runs",
    "Warm up for 5-10 minutes before starting"
  ]
}
```

**Response:** `200 OK` (Full Recommendation object)

---

#### `GET /api/recommendation/user`
Get all recommendations for a user.

**Headers:**
- `Authorization: Bearer <JWT_TOKEN>`
- `userId: <USER_UUID>`

---

#### `GET /api/recommendation/activity`
Get all recommendations for a specific activity.

**Headers:**
- `Authorization: Bearer <JWT_TOKEN>`
- `activityId: <ACTIVITY_UUID>`

---

### Admin Endpoints (Requires ADMIN Role)

#### `GET /api/admin/**`
Administrative endpoints protected by `hasRole("ADMIN")` in SecurityConfig.

---

### API Documentation

**Swagger UI:** `http://localhost:8080/swagger-ui.html`  
**OpenAPI JSON:** `http://localhost:8080/v3/api-docs`

---

## 🔒 Security Implementation

### JWT Authentication Flow

1. **User Login:**
   - Client sends credentials to `/api/users/signin`
   - Server validates credentials against BCrypt-hashed password
   - Generates JWT with user ID and role claims
   - Returns token + user details

2. **Authenticated Requests:**
   - Client includes `Authorization: Bearer <token>` header
   - `JwtAuthFilter` intercepts request before controller
   - Validates token signature using HMAC-SHA256
   - Extracts user ID and roles from token claims
   - Creates `UsernamePasswordAuthenticationToken`
   - Sets authentication in `SecurityContextHolder`
   - Request proceeds to controller

3. **Authorization:**
   - Spring Security checks role-based rules in `SecurityConfig`
   - `/api/admin/**` requires `ROLE_ADMIN`
   - `/api/users/**` permits all (public registration/login)
   - All other endpoints require authentication

### Security Configuration Highlights

```java
// JWT Secret: Base64-encoded 256-bit key
// Token Expiration: 172800000ms (48 hours)
// Password Encoding: BCrypt with default strength (10 rounds)
// CSRF: Disabled (stateless JWT authentication)
// Session Management: Stateless (no HTTP sessions)
```

### Why JWT Over Session-Based Auth?

✅ **Stateless** - No server-side session storage  
✅ **Scalable** - Works in distributed/load-balanced environments  
✅ **Mobile-friendly** - Easy to use in native apps  
✅ **Self-contained** - Token includes all necessary info  
✅ **Microservices-ready** - Can be validated by multiple services

---

## 🚀 Getting Started

### Prerequisites

- **JDK 25** or higher
- **PostgreSQL 12+** installed and running
- **Maven 3.8+** installed
- (Optional) **Docker** for containerized deployment

---

### Local Development Setup

1. **Clone the repository:**
   ```bash
   git clone <repository-url>
   cd fitness-monolith
   ```

2. **Configure database:**
   
   Create a PostgreSQL database:
   ```sql
   CREATE DATABASE fitness_demo;
   ```

3. **Set environment variables:**
   
   **Windows (PowerShell):**
   ```powershell
   $env:DB_URL="jdbc:postgresql://localhost:5432/fitness_demo"
   $env:DB_USER="postgres"
   $env:DB_PWD="yourpassword"
   ```
   
   **Linux/Mac:**
   ```bash
   export DB_URL="jdbc:postgresql://localhost:5432/fitness_demo"
   export DB_USER="postgres"
   export DB_PWD="yourpassword"
   ```

4. **Build the project:**
   ```bash
   mvn clean package
   ```

5. **Run the application:**
   ```bash
   java -jar target/fitness-monolith-0.0.1-SNAPSHOT.jar
   ```

6. **Access the API:**
   - API Base URL: `http://localhost:8080/api`
   - Swagger UI: `http://localhost:8080/swagger-ui.html`

---

## 🐳 Docker Deployment

### Build Docker Image

```bash
# Build the JAR file first
mvn clean package

# Build Docker image
docker build -t fitness-monolith:latest .
```

### Run with Docker

```bash
docker run -d \
  -p 8080:8080 \
  -e DB_URL="jdbc:postgresql://host.docker.internal:5432/fitness_demo" \
  -e DB_USER="postgres" \
  -e DB_PWD="yourpassword" \
  --name fitness-api \
  fitness-monolith:latest
```

### Docker Compose (Recommended)

Create `docker-compose.yml`:

```yaml
version: '3.8'

services:
  postgres:
    image: postgres:16-alpine
    environment:
      POSTGRES_DB: fitness_demo
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: postgres
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data

  fitness-api:
    build: .
    ports:
      - "8080:8080"
    environment:
      DB_URL: jdbc:postgresql://postgres:5432/fitness_demo
      DB_USER: postgres
      DB_PWD: postgres
    depends_on:
      - postgres

volumes:
  postgres_data:
```

Run with:
```bash
docker-compose up -d
```

---

## ⚙️ Configuration

### `application.properties`

```properties
# Database Configuration
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USER}
spring.datasource.password=${DB_PWD}

# JPA/Hibernate Settings
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# Logging
logging.level.org.hibernate.SQL=DEBUG
```

### Hibernate DDL Auto Modes

- `update` *(current)* - Updates schema automatically (development)
- `validate` - Validates schema without changes (production)
- `create` - Drops and recreates schema on startup
- `create-drop` - Creates schema, drops on shutdown

**Production recommendation:** Use `validate` and manage migrations with Flyway/Liquibase.

---

## 🤔 Why This Architecture?

### Monolithic vs Microservices

I chose a **monolithic architecture** for this project because:

✅ **Simplicity** - Single codebase, easier to develop and debug  
✅ **Single Deployment** - One Docker image, simpler CI/CD  
✅ **Lower Latency** - No network overhead between services  
✅ **Transaction Management** - Easy ACID transactions across entities  
✅ **Easier to Understand** - Clear structure for interviews/portfolio

**When to migrate to microservices:**
- Service exceeds 100k lines of code
- Need independent scaling of modules
- Team grows beyond 10 developers
- Require polyglot persistence

---

### Layered Architecture Benefits

**Controller Layer:**
- Handles HTTP concerns (status codes, headers)
- Input validation with `@Valid`
- Maps DTOs to/from JSON

**Service Layer:**
- Contains business logic
- Orchestrates repository calls
- Handles entity-DTO transformations
- Throws business exceptions

**Repository Layer:**
- Data access abstraction
- Spring Data JPA auto-implementation
- Custom query methods (e.g., `findByUserId`)

**Why this separation?**
- **Testability** - Mock layers independently
- **Maintainability** - Changes isolated to single layer
- **Reusability** - Services can call other services
- **Clean Code** - Single Responsibility Principle

---

## 💭 Technical Decisions

### 1. Why PostgreSQL over MySQL?

**Chosen:** PostgreSQL  
**Reason:** 
- Native JSON/JSONB support with indexing
- Better UUID handling
- More advanced query features
- Better for production workloads

*MySQL driver is commented in `pom.xml` for easy switching*

---

### 2. Why Lombok?

**Benefits:**
- Reduces boilerplate by 40-60%
- `@Data` generates getters/setters/toString/equals/hashCode
- `@Builder` for fluent object construction
- `@RequiredArgsConstructor` for constructor injection
- `@AllArgsConstructor`/`@NoArgsConstructor` for JPA entities

**Trade-off:** IDE plugin required, but worth the productivity gain.

---

### 3. Why ModelMapper over MapStruct?

**ModelMapper (Runtime reflection-based):**
- Zero configuration for simple mappings
- Faster development for small projects

**MapStruct (Compile-time code generation):**
- Better performance (no reflection)
- Type-safe
- Better for large enterprise apps

For this project size, ModelMapper's simplicity wins.

---

### 4. Why UUID over Auto-increment IDs?

**UUIDs:**
- Prevent ID enumeration attacks
- Allow distributed ID generation
- Globally unique (no collision risk)
- Easier microservices migration

**Trade-off:** Slightly larger index size (16 bytes vs 4/8 bytes)

---

### 5. Why JSON Columns?

**Use Case:** `additionalMetrics` in Activity entity

Different activities have different metrics:
- Running: pace, distance, elevation
- Swimming: laps, stroke type
- Weight training: sets, reps, weight

**Alternatives Considered:**
1. **EAV (Entity-Attribute-Value)** - Too complex, poor query performance
2. **Polymorphic entities** - Requires table-per-type, schema bloat
3. **JSON columns** ✅ - Flexible, queryable with PostgreSQL functions

---

### 6. Why Stateless JWT over Session-Based Auth?

**Session-Based:**
- Server stores session state
- Requires sticky sessions in load balancers
- Challenges with distributed systems

**JWT:**
- Fully stateless
- Horizontally scalable
- Works across multiple services
- Mobile-friendly

**Trade-off:** Can't revoke tokens before expiry (solved with refresh tokens in production)

---

### 7. Why Global Exception Handler?

`@RestControllerAdvice` centralizes error handling:
- Consistent error response format
- Reduces controller boilerplate
- Handles validation errors automatically
- Easier to add custom exceptions

**Before:**
```java
try { ... } catch (Exception e) { return ResponseEntity.badRequest()... }
```

**After:**
```java
throw new CustomException("message"); // Handled globally
```

---

## 📈 Future Enhancements

- [ ] Refresh token mechanism for JWT
- [ ] Password reset functionality
- [ ] Email verification
- [ ] Activity analytics dashboard
- [ ] Social features (friends, challenges)
- [ ] Integration with fitness APIs (Strava, Fitbit)
- [ ] Redis caching for frequent queries
- [ ] Pagination for activity/recommendation lists
- [ ] File upload for profile pictures
- [ ] GraphQL API as alternative to REST
- [ ] Kubernetes deployment manifests
- [ ] CI/CD pipeline (GitHub Actions)
- [ ] Integration tests with TestContainers
- [ ] Rate limiting with Spring Cloud Gateway

---

## 🧪 Testing

### Run Tests

```bash
mvn test
```

### Test Coverage

- Unit tests for services (mocked repositories)
- Security tests (MockMvc with Spring Security Test)
- Application context loads successfully

---

## 📝 Interview Talking Points

### What You Built
"I developed a secure fitness tracking REST API using Spring Boot that allows users to register, log their workouts across 50+ activity types, and receive personalized recommendations. The system uses JWT for stateless authentication and PostgreSQL with JSON columns for flexible data storage."

### Technical Highlights
"I implemented role-based access control with Spring Security, used Hibernate ORM with optimized lazy loading to prevent N+1 queries, and leveraged PostgreSQL's JSON capabilities to store activity-specific metrics without rigid schema constraints."

### Architecture Decisions
"I chose a monolithic architecture for simplicity and easier deployment, but designed it with clear layer separation—controllers handle HTTP concerns, services contain business logic, and repositories abstract data access. This makes testing easier and would allow future migration to microservices if needed."

### Security Implementation
"Authentication uses JWT tokens signed with HMAC-SHA256. Passwords are hashed with BCrypt. I created a custom filter that intercepts every request, validates the token, extracts user roles from claims, and sets the security context before the request reaches controllers."

### Database Design
"I used UUIDs for primary keys to prevent ID enumeration attacks and enable distributed ID generation. The Activity entity has a JSON column for flexible metrics—running activities store pace and distance, while swimming activities store laps and stroke type, all without schema migrations."

### DevOps
"The application is fully containerized with Docker using Eclipse Temurin JRE. Database credentials are externalized via environment variables for different deployment environments. I integrated Swagger for interactive API documentation, which helped during development and serves as live documentation."

### Challenges Solved
"One challenge was preventing N+1 query problems with bidirectional relationships. I solved this using lazy fetching with `@ManyToOne(fetch = FetchType.LAZY)` and JsonIgnore to prevent circular serialization. Another challenge was flexible activity tracking—solved with PostgreSQL JSON columns."

### Why This Tech Stack?
"Spring Boot provides production-ready features out of the box—embedded server, metrics, health checks. PostgreSQL's JSON support gave flexibility without sacrificing relational integrity. JWT enables stateless authentication, which scales horizontally. Lombok reduced boilerplate significantly."

---

## 📄 License

This project is open source and available for educational purposes.

---

## 👤 Author

**Ishon Panda**  
📧 Email: pandaishon@gmail.com  
💼 LinkedIn: [Your LinkedIn]  
🐙 GitHub: [Your GitHub]

---

## 🙏 Acknowledgments

- Spring Boot team for excellent documentation
- PostgreSQL community for robust database
- JWT.io for token debugging tools
- Swagger for API documentation standards

---

**Built with ❤️ using Spring Boot**

