package com.example.hrms.service;

import com.example.hrms.domain.*;
import com.example.hrms.repository.PayrollRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PayrollService {

    private final PayrollRepository payrollRepo;
    private final EmployeeService employeeService;   // assume exists
    private final AttendanceService attendanceService;
    private final LeaveService leaveService;
    private final ExpenseService expenseService;

    /** Initiate a new payroll run for a specific period */
    @Transactional
    public PayrollRun initiateRun(
            UUID tenantId,
            LocalDate periodStart,
            LocalDate periodEnd,
            LocalDate payDate,
            Long createdByUserId) {

        PayrollRun run = new PayrollRun();
        run.setPayrollRunId(UUID.randomUUID());
        run.setTenantId(tenantId);
        run.setPeriodStart(periodStart);
        run.setPeriodEnd(periodEnd);
        run.setPayDate(payDate);
        run.setStatus(PayrollStatus.Draft);
        run.setCreatedByUserId(createdByUserId);
        run = payrollRepo.save(run);

        // Placeholder: aggregate attendance/leave/expense data, compute items
        // In a real implementation, iterate over employees, compute gross/hours, deductions, net pay
        // and create PayrollItem entities.

        run.setStatus(PayrollStatus.Processing);
        return payrollRepo.save(run);
    }

    /** Finalize / lock the payroll run */
    @Transactional
    public PayrollRun finalizeRun(UUID payrollRunId, Long finalizerUserId) {
        PayrollRun run = payrollRepo.findByPayrollRunId(payrollRunId)
                .orElseThrow(() -> new IllegalArgumentException("Payroll run not found"));

        if (run.getStatus() != PayrollStatus.PROCESSING) {
            throw new IllegalStateException("Only runs in PROCESSING state may be finalized.");
        }

        run.setStatus(PayrollStatus.FINALIZED);
        run.setApprovedByUserId(finalizerUserId);
        return payrollRepo.save(run);
    }

    /** Fetch line‑items for a specific run */
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
}