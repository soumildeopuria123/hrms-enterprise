package com.example.hrms.repository;

import com.example.hrms.domain.ExpenseClaim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ExpenseRepository extends JpaRepository<ExpenseClaim, Long> {

    Optional<ExpenseClaim> findByExpenseClaimId(UUID expenseClaimId);

    List<ExpenseClaim> findByEmployeeIdOrderBySubmittedAtDesc(Long employeeId);

    List<ExpenseClaim> findByTenantIdOrderBySubmittedAtDesc(UUID tenantId);

    List<ExpenseClaim> findByStatus(String status);

    List<ExpenseClaim> findByCategoryAndStatus(com.example.hrms.domain.ExpenseClaim.Category category, String status);

    long countByEmployeeIdAndStatus(Long employeeId, String status);
}
