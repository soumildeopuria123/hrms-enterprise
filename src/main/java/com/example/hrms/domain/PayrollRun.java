package com.example.hrms.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "payroll_runs")
public class PayrollRun {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "payroll_run_id", unique = true, nullable = false)
    private UUID payrollRunId;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "period_start", nullable = false)
    private LocalDate periodStart;

    @Column(name = "period_end", nullable = false)
    private LocalDate periodEnd;

    @Column(name = "pay_date", nullable = false)
    private LocalDate payDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PayrollStatus status = PayrollStatus.Draft;

    @Column(name = "created_by_user_id", nullable = false)
    private Long createdByUserId;

    @Column(name = "approved_by_user_id")
    private Long approvedByUserId;

    @Column(name = "created_at", nullable = false)
    private LocalDate createdAt;

    @Column(name = "finalized_at")
    private LocalDate finalizedAt;

    @Column(name = "currency", length = 3, nullable = false)
    private String currency = "USD";

    public PayrollRun() {
        this.payrollRunId = UUID.randomUUID();
        this.createdAt = LocalDate.now();
    }

    public String displayLabel() {
        return "Payroll run for " + periodStart + "–" + periodEnd + " (" + currency + ")";
    }

    // --- accessors ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public UUID getPayrollRunId() { return payrollRunId; }
    public void setPayrollRunId(UUID payrollRunId) { this.payrollRunId = payrollRunId; }

    public UUID getTenantId() { return tenantId; }
    public void setTenantId(UUID tenantId) { this.tenantId = tenantId; }

    public LocalDate getPeriodStart() { return periodStart; }
    public void setPeriodStart(LocalDate periodStart) { this.periodStart = periodStart; }

    public LocalDate getPeriodEnd() { return periodEnd; }
    public void setPeriodEnd(LocalDate periodEnd) { this.periodEnd = periodEnd; }

    public LocalDate getPayDate() { return payDate; }
    public void setPayDate(LocalDate payDate) { this.payDate = payDate; }

    public PayrollStatus getStatus() { return status; }
    public void setStatus(PayrollStatus status) { this.status = status; }

    public Long getCreatedByUserId() { return createdByUserId; }
    public void setCreatedByUserId(Long createdByUserId) { this.createdByUserId = createdByUserId; }

    public Long getApprovedByUserId() { return approvedByUserId; }
    public void setApprovedByUserId(Long approvedByUserId) { this.approvedByUserId = approvedByUserId; }

    public LocalDate getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDate createdAt) { this.createdAt = createdAt; }

    public LocalDate getFinalizedAt() { return finalizedAt; }
    public void setFinalizedAt(LocalDate finalizedAt) { this.finalizedAt = finalizedAt; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
}

enum PayrollStatus {
    Draft,
    Processing,
    Approved,
    Rejected,
    Finalized
}
