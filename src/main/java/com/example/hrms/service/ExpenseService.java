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

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    /** Submit a new expense claim (Draft → Submitted) */
    @Transactional
    public ExpenseClaim submitClaim(
            Long employeeId,
            BigDecimal amount,
            String currency,
            OffsetDateTime expenseDate,
            String category,
            String businessPurpose,
            MultipartFile receipt) {

        // Basic validation
        if (amount == null || amount.signum() <= 0) {
            throw new IllegalArgumentException("Amount must be positive.");
        }
        if (currency == null || currency.length() != 3) {
            throw new IllegalArgumentException("Currency must be a 3‑letter code.");
        }
        if (category == null || !Arrays.asList("Travel", "Meals", "Software", "Equipment").contains(category)) {
            throw new IllegalArgumentException("Invalid category.");
        }

        ExpenseClaim claim = new ExpenseClaim();
        claim.setExpenseClaimId(java.util.UUID.randomUUID());
        claim.setAmount(amount);
        claim.setCurrency(currency);
        claim.setExpenseDate(expenseDate);
        claim.setCategory(category);
        claim.setBusinessPurpose(businessPurpose);
        claim.setStatus(Status.DRAFT);
        claim.setSubmittedAt(OffsetDateTime.now());

        return expenseRepository.save(claim);
    }

    /** Advance status: Draft → Submitted */
    @Transactional
    public ExpenseClaim submitForApproval(Long claimId) {
        ExpenseClaim claim = expenseRepository.findById(claimId)
                .orElseThrow(() -> new IllegalArgumentException("Expense claim not found"));
        claim.setStatus(Status.SUBMITTED);
        return expenseRepository.save(claim);
    }

    /** Manager approval / rejection */
    @Transactional
    public ExpenseClaim managerApproveReject(Long claimId, String decision) {
        ExpenseClaim claim = expenseRepository.findById(claimId)
                .orElseThrow(() -> new IllegalArgumentException("Expense claim not found"));
        if ("APPROVE".equalsIgnoreCase(decision)) {
            claim.setStatus(Status.APPROVED);
        } else {
            claim.setStatus(Status.REJECTED);
        }
        return expenseRepository.save(claim);
    }

    /** Finance review / approval */
    @Transactional
    public ExpenseClaim financeApproveReject(Long claimId, String decision) {
        ExpenseClaim claim = expenseRepository.findById(claimId)
                .orElseThrow(() -> new IllegalArgumentException("Expense claim not found"));
        if ("APPROVE".equalsIgnoreCase(decision)) {
            claim.setStatus(Status.APPROVED);
        } else {
            claim.setStatus(Status.REJECTED);
        }
        return expenseRepository.save(claim);
    }

    /** Fetch claims by employee */
    public List<ExpenseClaim> getClaimsByEmployee(Long employeeId) {
        return expenseRepository.findByEmployeeId(employeeId);
    }

    /** Fetch claims by status */
    public List<ExpenseClaim> getClaimsByStatus(String status) {
        return expenseRepository.findByStatus(status);
    }
}