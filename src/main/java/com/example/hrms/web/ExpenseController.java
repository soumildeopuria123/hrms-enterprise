package com.example.hrms.web;

import com.example.hrms.domain.*;
import com.example.hrms.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;

    /** POST /api/expenses – Submit a new expense claim */
    @PostMapping
    public ResponseEntity<ExpenseClaim> submit(
            @RequestParam BigDecimal amount,
            @RequestParam String currency,
            @RequestParam OffsetDateTime expenseDate,
            @RequestParam String category,
            @RequestParam String businessPurpose,
            @RequestParam(required = false) MultipartFile receipt,
            @AuthenticationPrincipal UserDetails user) {

        Long employeeId = extractEmployeeIdFromUser(user);
        ExpenseClaim created = expenseService.submitClaim(
                employeeId, amount, currency, expenseDate, category, businessPurpose, receipt);
        return ResponseEntity.status(201).body(created);
    }

    /** GET /api/expenses – Fetch expense claims filtered by employee, status, or role */
    @GetMapping
    public ResponseEntity<List<ExpenseClaimDTO>> list(
            @RequestParam(required = false) Long employeeId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String role,
            @AuthenticationPrincipal UserDetails user) {

        List<ExpenseClaim> claims;

        if ("EMPLOYEE".equalsIgnoreCase(role) && employeeId != null) {
            claims = expenseService.getClaimsByEmployee(employeeId);
        } else if (status != null) {
            claims = expenseService.getClaimsByStatus(status);
        } else {
            // Admin / default: return all claims (apply pagination in production)
            claims = expenseRepository.findAll(); // assume repo exposure
        }

        List<ExpenseClaimDTO> dtos = claims.stream()
                .map(this::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    /** PATCH /api/expenses/{id}/status – Manager / Finance approval/rejection */
    @PatchMapping("/{id}/status")
    public ResponseEntity<ExpenseClaim> updateStatus(
            @PathVariable Long id,
            @RequestParam String decision,
            @AuthenticationPrincipal UserDetails user) {

        String role = determineRole(user);
        ExpenseClaim updated;
        if ("MANAGER".equalsIgnoreCase(role)) {
            updated = expenseService.managerApproveReject(id, decision);
        } else if ("FINANCE".equalsIgnoreCase(role)) {
            updated = expenseService.financeApproveReject(id, decision);
        } else {
            throw new SecurityException("Insufficient role to approve/reject expenses.");
        }
        return ResponseEntity.ok(updated);
    }

    /** DTO for API responses */
    private ExpenseClaimDTO toDto(ExpenseClaim claim) {
        return ExpenseClaimDTO.builder()
                .id(claim.getId())
                .expenseClaimId(claim.getExpenseClaimId())
                .employeeName(claim.getEmployee().getFullName())
                .amount(claim.getAmount())
                .currency(claim.getCurrency())
                .expenseDate(claim.getExpenseDate())
                .category(claim.getCategory())
                .businessPurpose(claim.getBusinessPurpose())
                .status(claim.getStatus())
                .submittedAt(claim.getSubmittedAt())
                .build();
    }

    /** Helper – map principal → employee ID (adapt to your security model) */
    private Long extractEmployeeIdFromUser(UserDetails user) {
        // Example: return Long.parseLong(user.getUsername());
        return 1L; // placeholder
    }

    /** Helper – determine role from principal authorities */
    private String determineRole(UserDetails user) {
        // Inspect user.getAuthorities() and return "EMPLOYEE", "MANAGER", "FINANCE", etc.
        return "EMPLOYEE"; // placeholder
    }

    @lombok.Data @lombok.Builder @lombok.AllArgsConstructor @lombok.NoArgsConstructor
    public static class ExpenseClaimDTO {
        private Long id;
        private UUID expenseClaimId;
        private String employeeName;
        private BigDecimal amount;
        private String currency;
        private OffsetDateTime expenseDate;
        private String category;
        private String businessPurpose;
        private String status;
        private OffsetDateTime submittedAt;
    }
}