package com.example.hrms.web;

import com.example.hrms.domain.*;
import com.example.hrms.service.PayrollService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/payroll")
@RequiredArgsConstructor
public class PayrollController {

    private final PayrollService payrollService;

    /** POST /api/payroll/runs – Initiate a new payroll run */
    @PostMapping("/runs")
    public ResponseEntity<PayrollRun> initiateRun(
            @RequestBody PayrollRunRequest request,
            @AuthenticationPrincipal UserDetails user) {

        PayrollRun run = payrollService.initiateRun(
                request.getTenantId(),
                request.getPeriodStart(),
                request.getPeriodEnd(),
                request.getPayDate(),
                extractUserId(user));
        return ResponseEntity.status(201).body(run);
    }

    /** GET /api/payroll/runs – List all payroll runs (optionally filtered by tenant) */
    @GetMapping("/runs")
    public ResponseEntity<List<PayrollRunSummary>> listRuns(
            @RequestParam(required = false) UUID tenantId,
            @AuthenticationPrincipal UserDetails user) {

        Long userId = extractUserId(user);
        if (tenantId != null) {
            List<PayrollRun> runs = payrollService.getByTenant(tenantId);
            return ResponseEntity.ok(runs.stream()
                    .map(r -> PayrollRunSummary.builder()
                            .id(r.getId())
                            .payrollRunId(r.getPayrollRunId())
                            .periodStart(r.getPeriodStart())
                            .periodEnd(r.getPeriodEnd())
                            .payDate(r.getPayDate())
                            .status(r.getStatus())
                            .build())
                    .toList());
        }
        // default: return runs for the authenticated user's tenant/organization
        List<PayrollRun> runs = payrollRepo.findAll(); // replace with actual tenant filtering
        return ResponseEntity.ok(runs.stream()
                .map(r -> PayrollRunSummary.builder()
                        .id(r.getId())
                        .payrollRunId(r.getPayrollRunId())
                        .periodStart(r.getPeriodStart())
                        .periodEnd(r.getPeriodEnd())
                        .payDate(r.getPayDate())
                        .status(r.getStatus())
                        .build())
                .toList());
    }

    /** GET /api/payroll/runs/{id}/items – Fetch line items / payslip data */
    @GetMapping("/runs/{payrollRunId}/items")
    public ResponseEntity<List<PayrollService.PayrollItemDTO>> getItems(
            @PathVariable UUID payrollRunId) {

        List<PayrollService.PayrollItemDTO> items = payrollService.getItems(payrollRunId);
        return ResponseEntity.ok(items);
    }

    /** PATCH /api/payroll/runs/{id}/finalize – Lock and finalize payroll */
    @PatchMapping("/runs/{payrollRunId}/finalize")
    public ResponseEntity<PayrollRun> finalize(
            @PathVariable UUID payrollRunId,
            @AuthenticationPrincipal UserDetails user) {

        Long finalizerId = extractUserId(user);
        PayrollRun run = payrollService.finalizeRun(payrollRunId, finalizerId);
        return ResponseEntity.ok(run);
    }

    /** ---- helpers ---- */

    private Long extractUserId(UserDetails user) {
        // Adapt to your UserDetails implementation (e.g., Long.parseLong(user.getUsername()))
        return 1L; // placeholder
    }

    @lombok.Data @lombok.Builder @lombok.AllArgsConstructor @lombok.NoArgsConstructor
    public static class PayrollRunRequest {
        private UUID tenantId;
        private LocalDate periodStart;
        private LocalDate periodEnd;
        private LocalDate payDate;
    }

    @lombok.Data @lombok.Builder @lombok.AllArgsConstructor @lombok.NoArgsConstructor
    public static class PayrollRunSummary {
        private Long id;
        private UUID payrollRunId;
        private LocalDate periodStart;
        private LocalDate periodEnd;
        private LocalDate payDate;
        private PayrollStatus status;
    }
}