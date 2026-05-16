# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

```powershell
mvnw.cmd spring-boot:run                                        # Run locally (port 8080)
mvnw.cmd test                                                   # Full test suite
mvnw.cmd test -Dtest=VenueControllerTest                        # Single test class
mvnw.cmd test "-Dtest=VenueControllerTest#testMethod"           # Single test method
mvnw.cmd clean package                                          # Build JAR
```

Required environment variables (use a `.env` file — spring-dotenv is on the classpath):
- `JWT_SECRET`
- `CLOUDINARY_API_NAME`, `CLOUDINARY_API_KEY`, `CLOUDINARY_API_SECRET`

## Architecture

Standard Spring Boot 3.4.1 layered architecture: Controller → Service → Repository → Entity. Java 17, Hibernate 6, MySQL in production, H2 in tests.

### Request flow

`AuthTokenFilter` validates the JWT on every request before it reaches any controller. Public endpoints (`/auth/**`) are whitelisted in `WebSecurityConfig`. Role checks on mutating operations are done with `@PreAuthorize` annotations on controller methods, not in the security config.

### Controllers

All controllers use `@CrossOrigin(origins = "*")` and return `ResponseEntity` with explicit status codes. Responses are always wrapped in a payload DTO (e.g. `VenueResponse`, `VenuesResponse`) — raw entities are never serialised directly.

Circular references between bidirectional relationships (e.g. `Production ↔ Venue`) are handled with `@JsonManagedReference` / `@JsonBackReference` — list endpoints rely on this to prevent infinite recursion, so do not remove these annotations.

### Services

Services catch `DataAccessException` / `DataIntegrityViolationException` at the boundary and rethrow as `DatabaseException`. Circular service dependencies (e.g. `VenueService ↔ ProductionService`) are broken with `@Lazy` on constructor parameters.

Slug generation for `Venue` and `Production` uses `SlugUtils` (wraps the Slugify library). Production names are de-duplicated automatically: if a name already exists, " (2)" is appended.

### Entities and relationships

```
Venue ──< Production ──< Performance
      ──< Festival   ──< Performance
      ──< Performance
      ──< Event

Production ──< Credit >── Person
           ──< Award  >── Person, Festival
           ──< ProductionImage
```

`Production.venue` is `@ManyToOne` EAGER. All `@OneToMany` collections on `Production` (`performances`, `credits`, `awards`) and `Venue` (`productions`, `festivals`) are lazy — see the pending performance plan (saved in memory) before adding queries that touch these collections.

### Repositories

All extend `JpaRepository`. `ProductionRepository` has two custom methods:
- `countByNameStartingWith(String)` — used for de-duplication
- `findAllProductionsWithFuturePerformances(LocalDateTime)` — JPQL with `JOIN p.performances`

### Security

JWT secret and expiry (`86400000 ms`) are configured in `application.properties`. `UserDetailsServiceImpl` loads users by username. `BCryptPasswordEncoder` is the password encoder bean.

### Tests

Controller tests use `@WebMvcTest` + `MockMvc` + `@WithMockUser`. Service tests use Mockito. Integration tests (in the `integration/` sub-package) hit a real H2 database — they are not mocked. JaCoCo coverage reports are generated to `target/site/jacoco/` after `mvnw.cmd test`.
