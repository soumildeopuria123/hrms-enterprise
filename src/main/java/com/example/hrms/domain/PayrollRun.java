package com.example.hrms.domain;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "payroll_runs")
public class PayrollRun {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "payroll_run_id", nullable = false, unique = true, columnDefinition = "BINARY(16)")
    private UUID payrollRunId;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "period_start", nullable = false)
    private LocalDate periodStart;

    @Column(name = "period_end", nullable = false)
    private LocalDate periodEnd;

    @Column(name = "pay_date")
    private LocalDate payDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private PayrollStatus status; // Draft, Processing, Finalized, Approved

    @Column(name = "created_by_user_id", nullable = false)
    private Long createdByUserId;

    @Column(name = "approved_by_user_id")
    private Long approvedByUserId;

    // Getters, setters, constructors omitted for brevity
}