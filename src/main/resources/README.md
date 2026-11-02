# HRMS Enterprise

Enterprise-grade human resource management platform for service companies.

## Features

* **Leave Management** — submit, track and approve leave requests with configurable
  balance validation and RBAC-scoped views.
* **Attendance Tracking** — clock in/out, daily punch records, duplicate-prevention,
  late/early detection with status badges.
* **Expense Claims** — multi-stage approval workflow (Manager -> Finance),
  receipt metadata, category and status filtering.
* **Payroll Management** — initiate, aggregate and finalize payroll runs;
  payslip line-item view/download.

## Tech Stack

* **Backend**: Java 17, Spring Boot 3, Spring Data JPA, PostgreSQL
* **Build**: Maven

## Build

```bash
mvn clean package
```

## Run

```bash
mvn spring-boot:run
```

Defaults to http://localhost:8080/api.

## Configuration

See `src/main/resources/application.properties` and the environment variables
`DATASOURCE_URL`, `DATASOURCE_USERNAME`, `DATASOURCE_PASSWORD`.
