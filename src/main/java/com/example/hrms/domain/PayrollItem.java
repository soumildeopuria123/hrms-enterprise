package com.example.hrms.domain;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "payroll_items")
public class PayrollItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "payroll_item_id", nullable = false, unique = true, columnDefinition = "BINARY(16)")
    private UUID payrollItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payroll_run_id", nullable = false)
    private PayrollRun payrollRun;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(name = "gross_amount", precision = 19, scale = 2, nullable = false)
    private BigDecimal grossAmount;

    @Column(name = "deductions_amount", precision = 19, scale = 2, nullable = false)
    private BigDecimal deductionsAmount;

    @Column(name = "net_amount", precision = 19, scale = 2, nullable = false)
    private BigDecimal netAmount;

    @Column(name = "currency", length = 3, nullable = false)
    private String currency;

    @Column(name = "calculation_reference", length = 100)
    private String calculationReference;

    // Getters, setters, constructors omitted for brevity
}