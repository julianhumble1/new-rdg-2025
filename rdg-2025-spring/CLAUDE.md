# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

```bash
# Run the application
./mvnw spring-boot:run          # Linux/Mac
mvnw.cmd spring-boot:run        # Windows

# Run all tests
mvn test

# Run a single test class
mvn test -Dtest=VenueControllerTest

# Run a single test method
mvn test -Dtest=VenueControllerTest#testMethodName

# Build (skipping tests)
mvn clean package -DskipTests
```

## Architecture

This is a **Spring Boot 3.4.1 REST API** (Java 17) for a theatrical/events database. It follows a standard layered architecture:

```
Controllers → Services → Repositories → JPA Entities
```

**Key packages** (all under `src/main/java/com/rdg/rdg_2025/rdg_2025_spring/`):
- `controllers/` — REST endpoints; CORS open to all origins; role-gated endpoints use `@PreAuthorize("hasRole('ADMIN')")`
- `services/` — business logic; owns exception handling and DTO-to-entity mapping
- `repository/` — Spring Data JPA repositories; `@EntityGraph` annotations on query methods to prevent N+1 queries
- `models/` — JPA entities; `@NamedEntityGraph` + `@BatchSize(size=50)` on collections for load optimisation
- `payload/` — request/response DTOs, split into `request/` and `response/` sub-packages per entity
- `security/` — stateless JWT auth via `AuthTokenFilter` + `JwtUtils`; `BCryptPasswordEncoder` for passwords

**Domain model at a glance**: Venue → Productions, Festivals, Performances, Events; Production → Performances, Credits (via Person); Award system; all entities carry `createdAt`/`updatedAt` and a URL slug field.

**Database**: MySQL in production; H2 in-memory for tests (`@ActiveProfiles("test")`). Secrets (`JWT_SECRET`, Cloudinary credentials) are injected via environment variables — never hardcoded.

**Image storage**: Cloudinary, wrapped by `CloudinaryController` and a dedicated service.

## Testing approach

- **Unit tests** (`controllers/`, `services/`): `@WebMvcTest` + `@MockitoBean`; test the layer in isolation via `MockMvc`
- **Integration tests** (`integration/`): `@SpringBootTest` + `@AutoConfigureMockMvc` + `@Transactional`; hit the full Spring context against H2
- Helper utilities live in `utils/` (e.g. `AuthTestUtils` for obtaining JWT tokens in integration tests)
- Use `@Nested` classes with `@DisplayName` to organise test cases — follow existing conventions when adding tests

## N+1 query pattern

When adding or modifying entity associations, define a `@NamedEntityGraph` on the entity and reference it with `@EntityGraph` on the relevant repository method. Do not rely on `FetchType.EAGER` globally — keep associations `LAZY` and load eagerly only where needed per query.
