# SB Fitness (Spring Boot Monolith)

A Spring Boot monolith for a simple fitness tracking workflow:
- User registration + sign-in (JWT)
- Track activities
- Generate and fetch recommendations

## Run Locally 

```bash
git clone https://github.com/ishonpanda12161/sb-fitness.git
cd sb-fitness/fitness-monolith
```

The app expects DB settings via environment variables (see `src/main/resources/application.properties`):

- `DB_URL`
- `DB_USER`
- `DB_PWD`

Example:
```bash
export DB_URL="jdbc:postgresql://localhost:5432/fitness-demo"
export DB_USER="postgres"
export DB_PWD="postgres"
```

## Docker

```bash
docker pull ishonpanda/fitness-monolith

```

## API Documentation (Swagger / OpenAPI)

This project includes Springdoc OpenAPI UI.

Once the app is running:
- **local**: `http://localhost:8080/swagger-ui/index.html`
- **deployed** `https://fitness-monolith-oszz.onrender.com/swagger-ui/index.html#/`
---

## API Endpoints

### Auth / Users (`/api/users`)

#### Register
- **POST** `/api/users/register`
- Body: `RegisteredRequest`
- Response: `UserResponse`

Example:
```bash
curl -X POST "http://localhost:8080/api/users/register" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Alice",
    "email": "alice@example.com",
    "password": "secret"
  }'
```

#### Sign in
- **POST** `/api/users/signin`
- Body: `LoginRequest`
- Response: `LoginResponse` (includes JWT token)

Example:
```bash
curl -X POST "http://localhost:8080/api/users/signin" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "alice@example.com",
    "password": "secret"
  }'
```

---

### Activities (`/api/activities`)

#### Create activity
- **POST** `/api/activities/create`
- Headers:
  - `userId: <USER_ID>`  *(note: header name is `userId` in controller)*
- Body: `ActivityRequest`
- Response: `ActivityResponse`

Example:
```bash
curl -X POST "http://localhost:8080/api/activities/create" \
  -H "Content-Type: application/json" \
  -H "userId: 123" \
  -d '{
    "type": "RUN",
    "duration": 30
  }'
```

#### Get all activities for a user
- **GET** `/api/activities/all`
- Headers:
  - `USER_ID: <USER_ID>` *(note: controller expects `USER_ID` here)*

Example:
```bash
curl -X GET "http://localhost:8080/api/activities/all" \
  -H "USER_ID: 123"
```

---

### Recommendations (`/api/recommendation`)

#### Generate recommendation
- **POST** `/api/recommendation/generate`
- Headers:
  - `userId: <USER_ID>`
  - `activityId: <ACTIVITY_ID>`
- Body: `RecommendationRequest`
- Response: `Recommendation`

Example:
```bash
curl -X POST "http://localhost:8080/api/recommendation/generate" \
  -H "Content-Type: application/json" \
  -H "userId: 123" \
  -H "activityId: 456" \
  -d '{
    "goal": "WEIGHT_LOSS"
  }'
```

#### Get recommendations for a user
- **GET** `/api/recommendation/user`
- Headers:
  - `userId: <USER_ID>`

Example:
```bash
curl -X GET "http://localhost:8080/api/recommendation/user" \
  -H "userId: 123"
```

#### Get recommendations for an activity
- **GET** `/api/recommendation/activity`
- Headers:
  - `activityId: <ACTIVITY_ID>`

Example:
```bash
curl -X GET "http://localhost:8080/api/recommendation/activity" \
  -H "activityId: 456"
```

---

## Configuration

Main config file:
- `fitness-monolith/src/main/resources/application.properties`

DB properties are environment-driven:
- `spring.datasource.url=${DB_URL}`
- `spring.datasource.username=${DB_USER}`
- `spring.datasource.password=${DB_PWD}`

JPA:
- `spring.jpa.hibernate.ddl-auto=update`

---
