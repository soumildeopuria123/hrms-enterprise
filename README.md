# Enterprise HR Management System (HRMS)

A full‑stack Human Resource Management System built with **Spring Boot** (Java) and **React + TypeScript + Tailwind Neo‑Brutalism**.  

It covers four core modules for enterprise service companies:

## Modules

| Module | Description |
|--------|-------------|
| **Leave Management** | Submit leave requests, status workflow (Draft → Submitted → Pending Manager → Approved/Rejected), balance validation, RBAC‑filtered views. |
| **Attendance Tracking** | Clock‑in/out, daily punch records, duplicate‑prevention, historical logs with status badges (Present/Late/Absent). |
| **Expense Claims** | Multi‑stage approval (Draft → Submitted → Pending Manager / Pending Finance → Approved/Rejected), receipt metadata, amount/category filtering. |
| **Payroll Management** | Initiate payroll runs, aggregate attendance/leave/expense data, calculate gross/deductions/net pay, finalize runs, payslip view/download. |

## Tech Stack

- **Backend**: Java 17, Spring Boot 3, Spring Data JPA, PostgreSQL
- **Frontend**: React, TypeScript, Tailwind CSS (Neo‑Brutalist styling: hard shadows `4px 4px 0px #000`, thick borders `2px solid #000`)
- **Build/CI**: Maven (backend), Create‑React‑App / Vite (frontend) – CI not yet configured
- **Version Control**: Git – repo hosted at <https://github.com/soumildeopuria123/hrms-enterprise>

## Repository Layout

```
hrms-enterprise/
│
├─ src/
│  ├─ main/
│  │  ├─ java/com/example/hrms/domain/        # JPA entities
│  │  ├─ java/com/example/hrms/repository/    # Spring Data JPA repos
│  │  ├─ java/com/example/hrms/service/       # Business logic
│  │  ├─ java/com/example/hrms/web/           # REST controllers
│  │  └─ resources/static/                     # React pages & assets
│  │     ├─ leave/LeaveManagementPage.tsx
│  │     ├─ attendance/AttendancePage.tsx
│  │     ├─ expenses/ExpenseClaimsPage.tsx
│  │     └─ payroll/PayrollManagementPage.tsx
│  └─ ...
│
├─ pom.xml               # Maven backend project (simplified)
├─ .gitignore
└─ README.md
```

## Getting Started (local)

```bash
# 1️⃣ Clone the repo
git clone https://github.com/soumildeopuria123/hrms-enterprise.git
cd hrms-enterprise

# 2️⃣ Backend (Maven)
cd src/main/java   # Maven project is at the root
mvn spring-boot:run   # defaults to http://localhost:8080

# 3️⃣ Frontend (npm)
cd src/main/resources/static
npm install
npm run dev   # Vite dev server, usually http://localhost:5173
```

> **Note** – The backend expects a PostgreSQL database; configure `application.properties` with your `spring.datasource.url`, `username`, `password`.  
> The frontend assumes a `useAuth` hook that reads a JWT from `localStorage`; set up your OAuth / SSO flow as described in the architecture docs.

## API Highlights

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/api/leaves` | POST | Submit a new leave request |
| `/api/leaves` | GET | Fetch leaves filtered by user role/employee ID |
| `/api/leaves/{id}/status` | PATCH | Manager/HR approve or reject |
| `/api/attendance/clock-in` | POST | Record a clock‑in punch |
| `/api/attendance/clock-out` | POST | Record a clock‑out punch and calculate hours |
| `/api/attendance` | GET | Attendance logs (filtered by employee/role) |
| `/api/expenses` | POST | Submit an expense claim |
| `/api/expenses` | GET | List expenses filtered by employee, status, or role |
| `/api/expenses/{id}/status` | PATCH | Manager or Finance approve/reject |
| `/api/payroll/runs` | POST | Initiate a new payroll run |
| `/api/payroll/runs` | GET | List payroll runs |
| `/api/payroll/runs/{id}/items` | GET | Line‑items / payslip data for a run |
| `/api/payroll/runs/{id}/finalize` | PATCH | Lock and finalize the payroll |

## License

This project is **open source** under the MIT License. See the `LICENSE` file for details.

---

