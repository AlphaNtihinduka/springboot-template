# springboot-template

`springboot-template` is a Spring Boot MongoDB microservice scaffold aligned to the architecture, dependency stack, and testing style used in `course-domain`.

## What is included

- `src/main/java/com/springboot_template/springboot_template/SpringbootTemplateApplication.java`
  Starts the service and enables Mongo auditing.
- `src/main/java/com/springboot_template/springboot_template/config`
  Shared runtime configuration such as CORS and seed-data bootstrapping.
- `src/main/java/com/springboot_template/springboot_template/controller`
  REST controllers. The template ships with `PlatformAdminController`.
- `src/main/java/com/springboot_template/springboot_template/dto/request`
  Request payload contracts and validation rules.
- `src/main/java/com/springboot_template/springboot_template/dto/response`
  API response DTOs and common error payloads.
- `src/main/java/com/springboot_template/springboot_template/exception`
  Domain exceptions and the global exception handler.
- `src/main/java/com/springboot_template/springboot_template/mapper`
  MapStruct converters between DTOs and Mongo documents.
- `src/main/java/com/springboot_template/springboot_template/model`
  MongoDB document models and enums.
- `src/main/java/com/springboot_template/springboot_template/repository`
  Spring Data MongoDB repositories.
- `src/main/java/com/springboot_template/springboot_template/service`
  Service contracts and implementations.
- `src/main/resources/application.properties`
  Application name, port, MongoDB connection settings, and logging.
- `src/test/java`
  Unit tests, integration tests, and shared support utilities following the `course-domain` test layout.
- `src/test/resources/fixtures`
  JSON request/response fixtures for controller and integration tests.
- `docs/`
  Static HTML, CSS, and JS examples for quick API documentation and manual testing references.
- `Dockerfile`
  Multi-stage image build for this service.
- `docker-compose.yml`
  Local runtime for MongoDB plus the microservice on `bilimy-network`.

## Example platform access scaffold

The template includes a platform-level feature around `PlatformAdmin`.

- Collection: `platform_admin`
- Seeded user:
  - `username`: `superadmin`
  - `email`: `superadmin@bilimy.com`
  - `role`: `SUPER_ADMIN`
- Endpoints:
  - `GET /api/v1/platform-admins`
  - `POST /api/v1/platform-admins`
  - `GET /api/v1/platform-admins/{id}`
  - `PUT /api/v1/platform-admins/{id}`
  - `DELETE /api/v1/platform-admins/{id}`
  - `GET /api/v1/platform-admins/username/{username}`

## Requirements

- Java 25
- Docker Engine
- Docker Compose
- Maven 3.9+ or the included Maven wrapper

## Step-by-step setup

1. Install Docker.
   - Ubuntu:
     - `sudo apt-get update`
     - `sudo apt-get install docker.io docker-compose-plugin`
     - `sudo systemctl enable --now docker`
2. Optional: allow your user to run Docker without `sudo`.
   - `sudo usermod -aG docker $USER`
   - Log out and back in.
3. Enter the project:
   - `cd /home/alpha/Documents/bilimy/backend/springboot-template`
4. Start MongoDB and the microservice with Compose:
   - `docker compose up --build`
5. Verify the service:
   - `curl http://localhost:8080/api/v1/platform-admins`
6. Run tests locally:
   - `./mvnw test`

## Environment variables

- `SPRING_DATA_MONGODB_URI`
  Example: `mongodb://admin:templatePassword@mongodb:27017/platform_template?authSource=admin`
- `SPRING_DATA_MONGODB_DATABASE`
  Example: `platform_template`
- `MONGO_INITDB_ROOT_USERNAME`
  Default: `admin`
- `MONGO_INITDB_ROOT_PASSWORD`
  Default: `DigitalOcean2026Debesis`
- `MONGO_INITDB_DATABASE`
  Default: `platform_template`

## Running MongoDB separately

If you want MongoDB only:

1. Start only MongoDB:
   - `docker compose up mongodb`
2. Run the service locally:
   - `./mvnw spring-boot:run`

## Example seed data for MongoDB

The application seeds one super admin automatically. You can also insert a second sample document:

```javascript
db.platform_admin.insertOne({
  username: "platform.owner",
  email: "platform.owner@bilimy.com",
  displayName: "Platform Owner",
  role: "PLATFORM_ADMIN",
  status: "ACTIVE",
  permissions: [
    "PLATFORM_USERS_READ",
    "PLATFORM_USERS_WRITE",
    "SERVICES_DEPLOY"
  ],
  seeded: false,
  createdAt: ISODate("2026-06-15T08:00:00Z"),
  updatedAt: ISODate("2026-06-15T08:00:00Z")
})
```

## Common blockers

- Docker permission denied
  - Cause: your user is not in the `docker` group.
  - Fix: run Docker with `sudo` or add your user to the group with `sudo usermod -aG docker $USER`.
- Hostname resolution failure for `mongodb`
  - Cause: the service is running outside Compose and cannot resolve the Compose service name.
  - Fix: use `localhost` in `SPRING_DATA_MONGODB_URI` when running the app directly on your machine.
- Port `8080` already in use
  - Fix: change the host side mapping in `docker-compose.yml`, for example `8081:8080`.
- Port `27017` already in use
  - Fix: change the MongoDB host port mapping, for example `27019:27017`, and update `MONGODB_PORT` if needed.
- Mongo authentication failed
  - Cause: credentials in `SPRING_DATA_MONGODB_URI` do not match the Mongo container environment variables.
  - Fix: keep `MONGO_INITDB_ROOT_USERNAME`, `MONGO_INITDB_ROOT_PASSWORD`, and `SPRING_DATA_MONGODB_URI` aligned.
- Testcontainers cannot start MongoDB
  - Cause: Docker is not available to the current user.
  - Fix: ensure Docker is running and that `docker ps` works before `./mvnw test`.

## Notes for new services

- Replace the `PlatformAdmin` example feature with your target domain while keeping the same package boundaries.
- Keep repository interfaces on `MongoRepository`.
- Keep request validation at DTO level and error translation in `GlobalExceptionHandler`.
- Keep integration tests on Testcontainers MongoDB and unit tests on mocks.
