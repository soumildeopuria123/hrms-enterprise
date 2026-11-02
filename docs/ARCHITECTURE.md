# Architecture

## Layers

```
web → service → repository → database
```

- **web** — REST controllers expose the HTTP API and own request/response DTOs.
- **service** — use-case services coordinate multiple aggregates and own transaction
  boundaries (`@Transactional`).
- **repository** — Spring Data JPA interfaces; derived methods preferred, explicit
  `@Query` only when necessary.
- **domain** — JPA entities carry state and identity; enums carry the workflow
  vocabulary (`Status`, `LeaveType`, `PayrollStatus`, `AttendanceStatus`).

## Tenancy

Most aggregates carry a `tenantId` so a single installation can host multiple
organisations with isolated data. The frontend security model is responsible for
resolving the current tenant and passing it through to the API.

## Workflow vocabulary

- **Leave**: Draft → Submitted → Pending Manager → Approved/Rejected.
- **Expense**: Draft → Submitted → Pending Manager / Pending Finance → Approved/Rejected.
- **Payroll**: Draft → Processing → Approved/Rejected → Finalized.
- **Attendance**: Present / Late / Absent / Half day / On leave.

## Auth

Spring Security is on the classpath. Endpoint controllers use placeholder helpers
(`extractEmployeeIdFromUser`, `determineRole`) so the API compiles and can be
smoke-tested before the security context is wired.

## Storage

PostgreSQL is the persistence layer. `ddl-auto=update` is used for development;
migration tooling (Flyway/Liquibase) should be introduced before production
rollouts.
