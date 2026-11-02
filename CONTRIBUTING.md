# Contributing to HRMS Enterprise

## Project conventions

- Java 17, Spring Boot 3, Spring Data JPA.
- Package structure: `com.example.hrms.{domain,repository,service,web}`.
- Entities use Jakarta Persistence annotations; avoid business logic inside entities.
- Services own use-case transactions.
- Controllers are thin; they delegate to services and map to/from DTOs.

## Code style

- Use Lombok where it genuinely reduces noise (`@RequiredArgsConstructor`, DTOs only).
- Keep repository queries naming-based where possible; add `@Query` only when needed.
- Add a `package-info.java` for any new top-level package.

## Review checklist

- Every public endpoint has a path, method and (where relevant) status code documented
  in `README.md`.
- New domain fields are covered by a repository method or a derived query.
- Error responses from `GlobalExceptionHandler` are intentional, not accidental 500s.

## Submitting changes

1. Branch off `main`.
2. Open a PR against `main` and self-review the diff.
3. Ensure CI passes (`mvn package`).
