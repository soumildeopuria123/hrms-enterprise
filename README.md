# HRMS Enterprise

Enterprise Human Resource Management System built with **Spring Boot 3** (Java 17)
and **PostgreSQL**.

Covers four core modules for service companies:

| Module | Description |
|--------|-------------|
| **Leave Management** | Submit leave requests, status workflow (Draft → Submitted → Pending Manager → Approved/Rejected), balance validation, RBAC‑filtered views. |
| **Attendance Tracking** | Clock‑in/out, daily punch records, duplicate‑prevention, historical logs with status badges (Present/Late/Absent). |
| **Expense Claims** | Multi‑stage approval (Draft → Submitted → Pending Manager / Pending Finance → Approved/Rejected), receipt metadata, amount/category filtering. |
| **Payroll Management** | Initiate payroll runs, aggregate attendance/leave/expense data, calculate gross/deductions/net pay, finalize runs, payslip view/download. |

## Tech Stack

- **Backend**: Java 17, Spring Boot 3, Spring Data JPA, Spring Security, Lombok
- **Database**: PostgreSQL
- **Build / CI**: Maven, GitHub Actions (`.github/workflows/ci.yml`)
- **Package**: `com.example.hrms`

## Repository Layout

```
hrms-enterprise/
│
├─ src/main/java/com/example/hrms/
│  ├─ HrmsApplication.java          # Spring Boot entrypoint + config
│  ├─ domain/                       # JPA entities + enums
│  │  ├─ Employee.java
│  │  ├─ Department.java
│  │  ├─ LeaveRequest.java
│  │  ├─ Attendance.java
│  │  ├─ ExpenseClaim.java
│  │  ├─ PayrollRun.java
│  │  └─ PayrollItem.java
│  ├─ repository/                   # Spring Data JPA repositories
│  │  ├─ EmployeeRepository.java
│  │  ├─ DepartmentRepository.java
│  │  ├─ LeaveRequestRepository.java
│  │  ├─ AttendanceRepository.java
│  │  ├─ ExpenseRepository.java
│  │  └─ PayrollRepository.java
│  ├─ service/                      # use-case services
│  │  ├─ LeaveService.java
│  │  ├─ AttendanceService.java
│  │  ├─ ExpenseService.java
│  │  └─ PayrollService.java
│  └─ web/                         # REST controllers + global handler
│     ├─ LeaveController.java
│     ├─ AttendanceController.java
│     ├─ ExpenseController.java
│     ├─ PayrollController.java
│     └─ GlobalExceptionHandler.java
├─ src/main/resources/
│  ├─ application.properties
│  └─ README.md
├─ pom.xml
├─ .gitignore
└─ README.md
```

## Getting Started (local)

```bash
# 1. Start a PostgreSQL database
export DATASOURCE_URL=jdbc:postgresql://localhost:5432/hrms
export DATASOURCE_USERNAME=postgres
export DATASOURCE_PASSWORD=secret

# 2. Run
mvn spring-boot:run
```

The app starts on http://localhost:8080/api.

## API

### Leaves

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/api/leaves` | POST | Submit a new leave request |
| `/api/leaves` | GET | Fetch leaves filtered by user role/employee ID |
| `/api/leaves/{id}/status` | PATCH | Manager/HR approve or reject |

### Attendance

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/api/attendance/clock-in` | POST | Record a clock‑in punch |
| `/api/attendance/clock-out` | POST | Record a clock‑out punch and calculate hours |
| `/api/attendance` | GET | Attendance logs (filtered by employee/role) |

### Expenses

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/api/expenses` | POST | Submit an expense claim |
| `/api/expenses` | GET | List expenses filtered by employee, status, or role |
| `/api/expenses/{id}/status` | PATCH | Manager or Finance approve/reject |

### Payroll

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/api/payroll/runs` | POST | Initiate a new payroll run |
| `/api/payroll/runs` | GET | List payroll runs |
| `/api/payroll/runs/{id}/items` | GET | Line‑items / payslip data for a run |
| `/api/payroll/runs/{id}/finalize` | PATCH | Lock and finalize the payroll |

## Development notes

- Controllers currently use placeholder auth helpers (`extractEmployeeIdFromUser`,
  `determineRole`). Wire these to your Spring Security `UserDetails` / JWT / SSO
  setup before production use.
- Services use placeholder tenant/employee lookups for the demo path; replace with
  real repository calls once the security context is wired.
- Receipt storage is modelled in `ExpenseClaim` (filename/content-type fields);
  plug in your file storage (S3, local disk, etc.) in the upload path.
- Payroll aggregation is intentionally a stub: extend `PayrollService.initiateRun`
  to iterate employees, compute gross/hours, deductions and net pay, and persist
  `PayrollItem` rows.

## License

MIT — see `LICENSE`.
