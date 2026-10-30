# projectREST

REST API for a user CRUD, written in Kotlin with Spring Boot and Spring Security. Authentication uses JWT, and passwords are stored with hash and salt. The database is PostgreSQL.

**Stack:** Java 25, Kotlin 2.3, Spring Boot 4, Gradle 9, PostgreSQL 16.

## Run with Docker

Requires only [Docker](https://docs.docker.com/get-docker/) with Compose.

```bash
git clone https://github.com/mateustassinari/projectREST.git
cd projectREST
docker compose up --build
```

The first run downloads the images and builds the app, which takes a few minutes. When the log shows `Started ProjectRESTApplicationKt`, the API is available at <http://localhost:8080>.

| What | Where |
|---|---|
| Swagger UI | <http://localhost:8080/swagger-ui.html> |
| OpenAPI JSON | <http://localhost:8080/v3/api-docs> |

To change the database credentials or the JWT secret, copy `.env.example` to `.env` and edit it. Without it, development defaults are used, so **set your own `JWT_SECRET` before exposing the API anywhere**.

```bash
docker compose down       # stop, keeping the data
docker compose down -v    # stop and delete the database
```

## Try it

1. Create a user. `POST /user` is public and returns a generated password:

   ```bash
   curl -X POST http://localhost:8080/user -H "Content-Type: application/json" \
     -d '{"name":"Test","login":"test","cpf":"<valid CPF>","telephone":"11999999999","mail":"test@example.com"}'
   ```

2. Log in with `POST /auth/login` using `{"login":"test","password":"<generated password>"}`. The response includes the JWT.
3. Send it on protected routes as `Authorization: Bearer <token>`. In Swagger UI, use the **Authorize** button.

## Run without Docker

Requires JDK 25 and a PostgreSQL database named `rest`. Set `JWT_SECRET`, `DB_USER` and `DB_PASSWORD` (see `.env.example`) in the environment, optionally `SPRING_DATASOURCE_URL` to change the database address, then run `./gradlew bootRun`.
