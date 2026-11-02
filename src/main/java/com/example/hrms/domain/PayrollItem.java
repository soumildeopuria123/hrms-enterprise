package com.example.hrms.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "payroll_items")
public class PayrollItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payroll_run_id", nullable = false)
    private PayrollRun payrollRun;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal grossAmount;

    @Column(name = "deductions_amount", precision = 12, scale = 2)
    private BigDecimal deductionsAmount = BigDecimal.ZERO;

    @Column(name = "net_amount", precision = 12, scale = 2, nullable = false)
    private BigDecimal netAmount;

    @Column(nullable = false, length = 3)
    private String currency;

    @Column(name = "calculation_reference", length = 500)
    private String calculationReference;

    public PayrollItem() {}

    public PayrollItem(PayrollRun payrollRun, Employee employee,
                       BigDecimal grossAmount, BigDecimal deductionsAmount,
                       String currency, String calculationReference) {
        this.payrollRun = payrollRun;
        this.employee = employee;
        this.grossAmount = grossAmount;
        this.deductionsAmount = deductionsAmount;
        this.netAmount = grossAmount.subtract(deductionsAmount);
        this.currency = currency;
        this.calculationReference = calculationReference;
    }

    public String displayLabel() {
        return employee.getFullName() + " — net " + currency + " " + netAmount;
    }

    // --- accessors ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public PayrollRun getPayrollRun() { return payrollRun; }
    public void setPayrollRun(PayrollRun payrollRun) { this.payrollRun = payrollRun; }

    public Employee getEmployee() { return employee; }
    public void setEmployee(Employee employee) { this.employee = employee; }

    public BigDecimal getGrossAmount() { return grossAmount; }
    public void setGrossAmount(BigDecimal grossAmount) { this.grossAmount = grossAmount; }

    public BigDecimal getDeductionsAmount() { return deductionsAmount; }
    public void setDeductionsAmount(BigDecimal deductionsAmount) { this.deductionsAmount = deductionsAmount; }

    public BigDecimal getNetAmount() { return netAmount; }
    public void setNetAmount(BigDecimal netAmount) { this.netAmount = netAmount; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public String getCalculationReference() { return calculationReference; }
    public void setCalculationReference(String calculationReference) { this.calculationReference = calculationReference; }
}
