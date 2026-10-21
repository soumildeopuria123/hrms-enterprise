package com.example.hrms.domain;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "expense_claims")
public class ExpenseClaim {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "expense_claim_id", nullable = false, unique = true, columnDefinition = "BINARY(16)")
    private UUID expenseClaimId;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(name = "amount", precision = 19, scale = 2, nullable = false)
    private BigDecimal amount;

    @Column(name = "currency", length = 3, nullable = false)
    private String currency; // USD, EUR

    @Column(name = "expense_date", nullable = false)
    private OffsetDateTime expenseDate;

    @Column(name = "category", length = 50, nullable = false)
    private String category; // Travel, Meals, Software, Equipment

    @Column(name = "business_purpose", columnDefinition = "text")
    private String businessPurpose;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    private Status status; // Draft, Submitted, PendingManager, PendingFinance, Approved, Rejected

    @Column(name = "submitted_at", nullable = false, updatable = false)
    private OffsetDateTime submittedAt;

    // Getters, setters, constructors omitted for brevity
}