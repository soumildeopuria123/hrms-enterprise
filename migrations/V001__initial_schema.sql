-- initial schema for HRMS Enterprise
-- once Flyway/Liquibase is wired, this becomes the first migration.

CREATE TABLE IF NOT EXISTS departments (
  id bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
  name varchar(60) NOT NULL UNIQUE,
  description varchar(200),
  tenant_id uuid NOT NULL
);

CREATE TABLE IF NOT EXISTS employees (
  id bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
  employee_number varchar(20) NOT NULL UNIQUE,
  first_name varchar(50) NOT NULL,
  last_name varchar(50) NOT NULL,
  work_email varchar(100) NOT NULL UNIQUE,
  personal_email varchar(100),
  tenant_id uuid NOT NULL,
  department_id bigint REFERENCES departments(id),
  manager_id bigint REFERENCES employees(id),
  hire_date date,
  termination_date date,
  employment_status varchar(20) NOT NULL DEFAULT 'ACTIVE'
);

CREATE TABLE IF NOT EXISTS leave_requests (
  id bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
  leave_request_id uuid NOT NULL UNIQUE,
  employee_id bigint NOT NULL REFERENCES employees(id),
  leave_type varchar(20) NOT NULL,
  start_date date NOT NULL,
  end_date date NOT NULL,
  units double precision NOT NULL,
  reason varchar(500),
  status varchar(20) NOT NULL DEFAULT 'DRAFT',
  tenant_id uuid NOT NULL,
  submitted_at date,
  approved_by bigint,
  rejected_by bigint,
  rejection_reason varchar(500)
);

CREATE TABLE IF NOT EXISTS attendance (
  id bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
  employee_id bigint NOT NULL REFERENCES employees(id),
  punch_date date NOT NULL,
  clock_in_time time,
  clock_out_time time,
  status varchar(20) NOT NULL DEFAULT 'PRESENT',
  source varchar(20),
  notes varchar(500)
);

CREATE TABLE IF NOT EXISTS expense_claims (
  id bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
  expense_claim_id uuid NOT NULL UNIQUE,
  employee_id bigint NOT NULL REFERENCES employees(id),
  amount numeric(12,2) NOT NULL,
  currency varchar(3) NOT NULL,
  expense_date timestamp with time zone NOT NULL,
  category varchar(20) NOT NULL,
  business_purpose varchar(1000),
  status varchar(20) NOT NULL DEFAULT 'DRAFT',
  tenant_id uuid NOT NULL,
  submitted_at timestamp with time zone,
  approved_by bigint,
  rejected_by bigint,
  rejection_reason varchar(500),
  receipt_filename varchar(255),
  receipt_content_type varchar(100)
);

CREATE TABLE IF NOT EXISTS payroll_runs (
  id bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
  payroll_run_id uuid NOT NULL UNIQUE,
  tenant_id uuid NOT NULL,
  period_start date NOT NULL,
  period_end date NOT NULL,
  pay_date date NOT NULL,
  status varchar(20) NOT NULL DEFAULT 'Draft',
  created_by_user_id bigint NOT NULL,
  approved_by_user_id bigint,
  created_at date NOT NULL,
  finalized_at date,
  currency varchar(3) NOT NULL DEFAULT 'USD'
);

CREATE TABLE IF NOT EXISTS payroll_items (
  id bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
  payroll_run_id bigint NOT NULL REFERENCES payroll_runs(id),
  employee_id bigint NOT NULL REFERENCES employees(id),
  gross_amount numeric(12,2) NOT NULL,
  deductions_amount numeric(12,2) NOT NULL DEFAULT 0,
  net_amount numeric(12,2) NOT NULL,
  currency varchar(3) NOT NULL,
  calculation_reference varchar(500)
);

-- Indexes
CREATE INDEX IF NOT EXISTS idx_employees_tenant ON employees(tenant_id);
CREATE INDEX IF NOT EXISTS idx_employees_department ON employees(department_id);
CREATE INDEX IF NOT EXISTS idx_leave_requests_employee ON leave_requests(employee_id);
CREATE INDEX IF NOT EXISTS idx_leave_requests_status ON leave_requests(status);
CREATE INDEX IF NOT EXISTS idx_attendance_employee ON attendance(employee_id);
CREATE INDEX IF NOT EXISTS idx_attendance_date ON attendance(punch_date);
CREATE INDEX IF NOT EXISTS idx_expense_claims_employee ON expense_claims(employee_id);
CREATE INDEX IF NOT EXISTS idx_expense_claims_status ON expense_claims(status);
CREATE INDEX IF NOT EXISTS idx_payroll_runs_tenant ON payroll_runs(tenant_id);
CREATE INDEX IF NOT EXISTS idx_payroll_items_run ON payroll_items(payroll_run_id);
