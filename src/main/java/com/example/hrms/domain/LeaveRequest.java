package com.example.hrms.domain;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "leave_requests")
public class LeaveRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "leave_request_id", unique = true, nullable = false)
    private UUID leaveRequestId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leave_type_id", nullable = false)
    private LeaveType leaveType;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private double units;

    @Column(length = 500)
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Status status = Status.DRAFT;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "submitted_at")
    private LocalDate submittedAt;

    @Column(name = "approved_by")
    private Long approvedBy;

    @Column(name = "rejected_by")
    private Long rejectedBy;

    @Column(name = "rejection_reason", length = 500)
    private String rejectionReason;

    public LeaveRequest() {
        this.leaveRequestId = UUID.randomUUID();
    }

    public String summary() {
        return employee.getFullName() + " — " + leaveType + " "
                + startDate + " to " + endDate + " (" + units + " days)";
    }

    // --- accessors ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public UUID getLeaveRequestId() { return leaveRequestId; }
    public void setLeaveRequestId(UUID leaveRequestId) { this.leaveRequestId = leaveRequestId; }

    public Employee getEmployee() { return employee; }
    public void setEmployee(Employee employee) { this.employee = employee; }

    public LeaveType getLeaveType() { return leaveType; }
    public void setLeaveType(LeaveType leaveType) { this.leaveType = leaveType; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public double getUnits() { return units; }
    public void setUnits(double units) { this.units = units; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public UUID getTenantId() { return tenantId; }
    public void setTenantId(UUID tenantId) { this.tenantId = tenantId; }

    public LocalDate getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(LocalDate submittedAt) { this.submittedAt = submittedAt; }

    public Long getApprovedBy() { return approvedBy; }
    public void setApprovedBy(Long approvedBy) { this.approvedBy = approvedBy; }

    public Long getRejectedBy() { return rejectedBy; }
    public void setRejectedBy(Long rejectedBy) { this.rejectedBy = rejectedBy; }

    public String getRejectionReason() { return rejectionReason; }
    public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }
}
