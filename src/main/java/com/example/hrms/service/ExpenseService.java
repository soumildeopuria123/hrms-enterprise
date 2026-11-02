package com.example.hrms.service;

import com.example.hrms.domain.*;
import com.example.hrms.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    /**
     * Submit a new expense claim (Draft -> Submitted).
     * Validated: positive amount, 3-letter currency, known category.
     */
    @Transactional
    public ExpenseClaim submitClaim(
            Long employeeId,
            BigDecimal amount,
            String currency,
            OffsetDateTime expenseDate,
            String category,
            String businessPurpose,
            MultipartFile receipt) {

        if (amount == null || amount.signum() <= 0) {
            throw new IllegalArgumentException("Amount must be positive.");
        }
        if (currency == null || currency.length() != 3) {
            throw new IllegalArgumentException("Currency must be a 3-letter code.");
        }
        if (category == null || !ExpenseClaim.Category.lookup(category).isPresent()) {
            throw new IllegalArgumentException("Invalid category.");
        }
        if (businessPurpose == null || businessPurpose.isBlank()) {
            throw new IllegalArgumentException("Business purpose is required.");
        }

        Employee current = currentEmployee(employeeId);
        ExpenseClaim claim = new ExpenseClaim();
        claim.setEmployee(current);
        claim.setAmount(amount);
        claim.setCurrency(currency.toUpperCase());
        claim.setExpenseDate(expenseDate != null ? expenseDate : OffsetDateTime.now());
        claim.setCategory(ExpenseClaim.Category.valueOf(category.toUpperCase()));
        claim.setBusinessPurpose(businessPurpose);
        claim.setTenantId(current.getTenantId());
        claim.setStatus(Status.DRAFT);
        claim.setSubmittedAt(OffsetDateTime.now());

        if (receipt != null && !receipt.isEmpty()) {
            String filename = sanitizeFilename(receipt.getOriginalFilename());
            claim.setReceiptFilename(filename);
            claim.setReceiptContentType(receipt.getContentType());
        }
        return expenseRepository.save(claim);
    }

    /**
     * Advance status: Draft -> Submitted for approval routing.
     */
    @Transactional
    public ExpenseClaim submitForApproval(Long claimId) {
        ExpenseClaim claim = expenseRepository.findById(claimId)
                .orElseThrow(() -> new IllegalArgumentException("Expense claim not found"));
        if (claim.getStatus() != Status.DRAFT) {
            throw new IllegalStateException("Only draft claims can be submitted.");
        }
        claim.setStatus(Status.SUBMITTED);
        return expenseRepository.save(claim);
    }

    /**
     * Manager approval / rejection.
     */
    @Transactional
    public ExpenseClaim managerApproveReject(Long claimId, String decision) {
        ExpenseClaim claim = expenseRepository.findById(claimId)
                .orElseThrow(() -> new IllegalArgumentException("Expense claim not found"));
        if (claim.getStatus() != Status.SUBMITTED) {
            throw new IllegalStateException("Only submitted claims can be routed.");
        }
        if ("APPROVE".equalsIgnoreCase(decision)) {
            claim.setStatus(Status.APPROVED);
            claim.setApprovedBy(currentEmployee(1L).getId());
        } else {
            claim.setStatus(Status.REJECTED);
            claim.setRejectedBy(currentEmployee(1L).getId());
        }
        return expenseRepository.save(claim);
    }

    /**
     * Finance review / approval.
     */
    @Transactional
    public ExpenseClaim financeApproveReject(Long claimId, String decision) {
        ExpenseClaim claim = expenseRepository.findById(claimId)
                .orElseThrow(() -> new IllegalArgumentException("Expense claim not found"));
        if (!claim.getStatus().equals(Status.APPROVED)) {
            throw new IllegalStateException("Only approved claims reach finance.");
        }
        if ("APPROVE".equalsIgnoreCase(decision)) {
            claim.setStatus(Status.APPROVED);
            claim.setApprovedBy(currentEmployee(1L).getId());
        } else {
            claim.setStatus(Status.REJECTED);
            claim.setRejectedBy(currentEmployee(1L).getId());
        }
        return expenseRepository.save(claim);
    }

    public List<ExpenseClaim> getClaimsByEmployee(Long employeeId) {
        return expenseRepository.findByEmployeeIdOrderBySubmittedAtDesc(employeeId);
    }

    public List<ExpenseClaim> getClaimsByStatus(String status) {
        return expenseRepository.findByStatus(status);
    }

    public ExpenseClaim getByClaimId(UUID claimId) {
        return expenseRepository.findByExpenseClaimId(claimId)
                .orElseThrow(() -> new IllegalArgumentException("Expense claim not found"));
    }

    private Employee currentEmployee(Long employeeId) {
        return new Employee("EMP-" + employeeId, "Demo", "Employee",
                "demo@" + employeeId + ".example.com",
                UUID.fromString("00000000-0000-0000-0000-000000000001"),
                null);
    }

    private String sanitizeFilename(String filename) {
        if (filename == null) return "receipt";
        return filename.replaceAll("[^A-Za-z0-9._-]", "_");
    }
}
