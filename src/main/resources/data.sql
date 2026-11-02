-- HRMS Enterprise: seed data for a demo tenant.
-- Run against the target database after schema creation.

-- Departments
INSERT INTO departments (name, description, tenant_id)
VALUES
  ('Engineering', 'Product and platform engineering', '00000000-0000-0000-0000-000000000001'),
  ('People Operations', 'HR and employee lifecycle', '00000000-0000-0000-0000-000000000001'),
  ('Finance', 'Payroll and expenses', '00000000-0000-0000-0000-000000000001')
ON CONFLICT (name, tenant_id) DO NOTHING;

-- Employees
WITH dep AS (
  SELECT id FROM departments WHERE name = 'Engineering' AND tenant_id = '00000000-0000-0000-0000-000000000001' LIMIT 1
),
  hr AS (
  SELECT id FROM departments WHERE name = 'People Operations' AND tenant_id = '00000000-0000-0000-0000-000000000001' LIMIT 1
)
INSERT INTO employees (employee_number, first_name, last_name, work_email, personal_email, tenant_id, department_id, hire_date, employment_status)
VALUES
  ('EMP-1001', 'Ada', 'Lovelace', 'ada@example.com', 'ada.personal@example.com', '00000000-0000-0000-0000-000000000001', (SELECT id FROM dep), CURRENT_DATE - INTERVAL '900 days', 'ACTIVE'),
  ('EMP-1002', 'Grace', 'Hopper', 'grace@example.com', 'grace.personal@example.com', '00000000-0000-0000-0000-000000000001', (SELECT id FROM dep), CURRENT_DATE - INTERVAL '600 days', 'ACTIVE'),
  ('EMP-2001', 'Mary', 'Williams', 'mary.williams@example.com', 'mary.personal@example.com', '00000000-0000-0000-0000-000000000001', (SELECT id FROM hr), CURRENT_DATE - INTERVAL '400 days', 'ACTIVE')
ON CONFLICT (work_email) DO NOTHING;

-- Leave requests (demo)
INSERT INTO leave_requests (leave_request_id, employee_id, leave_type, start_date, end_date, units, reason, status, tenant_id, submitted_at)
VALUES
  (gen_random_uuid(), (SELECT id FROM employees WHERE employee_number = 'EMP-1001' LIMIT 1), 'ANNUAL', CURRENT_DATE + INTERVAL '7 days', CURRENT_DATE + INTERVAL '9 days', 2.0, 'Planned vacation week', 'SUBMITTED', '00000000-0000-0000-0000-000000000001', CURRENT_DATE),
  (gen_random_uuid(), (SELECT id FROM employees WHERE employee_number = 'EMP-1002' LIMIT 1), 'SICK', CURRENT_DATE - INTERVAL '3 days', CURRENT_DATE - INTERVAL '1 days', 2.0, 'Medical leave', 'APPROVED', '00000000-0000-0000-0000-000000000001', CURRENT_DATE - INTERVAL '5 days')
ON CONFLICT DO NOTHING;

-- Attendance punches (demo)
INSERT INTO attendance (employee_id, punch_date, clock_in_time, clock_out_time, status, source)
SELECT id, CURRENT_DATE - INTERVAL '1 days', '09:00:00', '18:00:00', 'PRESENT', 'web'
FROM employees
WHERE employee_number IN ('EMP-1001', 'EMP-1002')
ON CONFLICT DO NOTHING;

-- Expense claims (demo)
INSERT INTO expense_claims (expense_claim_id, employee_id, amount, currency, expense_date, category, business_purpose, status, tenant_id, submitted_at)
SELECT gen_random_uuid(), id, 42.50, 'USD', CURRENT_DATE - INTERVAL '2 days', 'MEALS', 'Team lunch', 'SUBMITTED', tenant_id, CURRENT_DATE - INTERVAL '2 days'
FROM employees
WHERE employee_number = 'EMP-1001'
ON CONFLICT DO NOTHING;

-- Payroll runs (demo)
INSERT INTO payroll_runs (payroll_run_id, tenant_id, period_start, period_end, pay_date, status, created_by_user_id, created_at, currency)
SELECT gen_random_uuid(), tenant_id, CURRENT_DATE - INTERVAL '30 days', CURRENT_DATE - INTERVAL '1 days', CURRENT_DATE + INTERVAL '5 days', 'FINALIZED', 1, CURRENT_DATE, 'USD'
FROM employees
LIMIT 1
ON CONFLICT DO NOTHING;

-- Payroll items for the demo run
INSERT INTO payroll_items (payroll_run_id, employee_id, gross_amount, deductions_amount, net_amount, currency, calculation_reference)
SELECT pr.id, e.id, 2500.00, 250.00, 2250.00, pr.currency, 'demo-chk-1'
FROM payroll_runs pr
JOIN employees e ON e.tenant_id = pr.tenant_id
WHERE pr.status = 'FINALIZED'
LIMIT 2
ON CONFLICT DO NOTHING;
