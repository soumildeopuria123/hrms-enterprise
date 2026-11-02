# Database migrations

This project uses the schema auto-creation path (`ddl-auto=update`) for local
development but the plan for production is to use explicit migrations.

## Planned path

1. Introduce Flyway (or Liquibase) as a build-time dependency.
2. Move `application.properties` ddl-auto to `validate` once migrations exist.
3. Keep one migration per logical change, named `V<nn>__<short description>.sql`.

## Existing initial schema (documented here for reference)

- `employees` — central employee aggregate
- `departments` — organisational units
- `leave_requests` — leave workflow aggregate
- `attendance` — daily punch records
- `expense_claims` — expense claims with receipt metadata
- `payroll_runs` — payroll run aggregate
- `payroll_items` — payroll line items

## First migration stub

See `migrations/V001__initial_schema.sql` for the intended initial DDL once
Flyway is wired in.
