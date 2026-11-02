package com.example.hrms.service;

import com.example.hrms.domain.*;
import com.example.hrms.repository.PayrollRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PayrollService {

    private final PayrollRepository payrollRepo;
    private final AttendanceService attendanceService;
    private final LeaveService leaveService;
    private final ExpenseService expenseService;

    /**
     * Initiate a new payroll run for a specific period.
     * Aggregates attendance, leave and expense data, computes gross/deductions/net
     * pay, and creates PayrollItem entities.
     */
    @Transactional
    public PayrollRun initiateRun(
            UUID tenantId,
            LocalDate periodStart,
            LocalDate periodEnd,
            LocalDate payDate,
            Long createdByUserId) {

        if (periodEnd.isBefore(periodStart)) {
            throw new IllegalArgumentException("Period end must be after period start.");
        }
        if (payDate.isBefore(periodEnd)) {
            throw new IllegalArgumentException("Pay date must be after period end.");
        }

        PayrollRun run = new PayrollRun();
        run.setPayrollRunId(UUID.randomUUID());
        run.setTenantId(tenantId);
        run.setPeriodStart(periodStart);
        run.setPeriodEnd(periodEnd);
        run.setPayDate(payDate);
        run.setStatus(PayrollStatus.Processing);
        run.setCreatedByUserId(createdByUserId);
        run = payrollRepo.save(run);

        // Placeholder: iterate over employees, compute gross/hours, deductions, net pay
        // and create PayrollItem entities.
        run.setStatus(PayrollStatus.Processing);
        return payrollRepo.save(run);
    }

    /**
     * Finalize / lock the payroll run.
     * Only runs in PROCESSING state may be finalized.
     */
    @Transactional
    public PayrollRun finalizeRun(UUID payrollRunId, Long finalizerUserId) {
        PayrollRun run = payrollRepo.findByPayrollRunId(payrollRunId)
                .orElseThrow(() -> new IllegalArgumentException("Payroll run not found"));
        if (run.getStatus() != PayrollStatus.Processing) {
            throw new IllegalStateException("Only runs in PROCESSING state may be finalized.");
        }
        run.setStatus(PayrollStatus.Finalized);
        run.setApprovedByUserId(finalizerUserId);
        run.setFinalizedAt(LocalDate.now());
        return payrollRepo.save(run);
    }

    /**
     * Fetch line-items for a specific run.
     */
    public List<PayrollService.PayrollItemDTO> getItems(UUID payrollRunId) {
        return payrollRepo.findByPayrollRunId(payrollRunId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private PayrollItemDTO toDto(PayrollItem item) {
        return PayrollItemDTO.builder()
                .employeeName(item.getEmployee().getFullName())
                .grossAmount(item.getGrossAmount())
                .deductionsAmount(item.getDeductionsAmount())
                .netAmount(item.getNetAmount())
                .currency(item.getCurrency())
                .calculationReference(item.getCalculationReference())
                .build();
    }

    @lombok.Getter @lombok.Setter @lombok.Builder @lombok.AllArgsConstructor @lombok.NoArgsConstructor
    public static class PayrollItemDTO {
        private String employeeName;
        private BigDecimal grossAmount;
        private BigDecimal deductionsAmount;
        private BigDecimal netAmount;
        private String currency;
        private String calculationReference;
    }

    /**
     * Get payroll runs by tenant.
     */
    public List<PayrollRun> getByTenant(UUID tenantId) {
        return payrollRepo.findByTenantIdOrderByCreatedAtDesc(tenantId);
    }

    /**
     * Compute gross pay from attendance hours with hourly rate.
     */
    public BigDecimal computeGrossPay(BigDecimal hourlyRate, BigDecimal hoursWorked) {
        return hourlyRate.multiply(hoursWorked).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Compute deductions from gross pay with deduction rate.
     */
    public BigDecimal computeDeductions(BigDecimal grossPay, BigDecimal deductionRate) {
        return grossPay.multiply(deductionRate).divide(BigDecimal.ONE, 2, RoundingMode.HALF_UP);
    }

    /**
     * Compute net pay from gross and deductions.
     */
    public BigDecimal computeNetPay(BigDecimal grossPay, BigDecimal deductions) {
        return grossPay.subtract(deductions).setScale(2, RoundingMode.HALF_UP);
    }
}
