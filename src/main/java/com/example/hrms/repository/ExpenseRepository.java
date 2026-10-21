package com.example.hrms.repository;

import com.example.hrms.domain.ExpenseClaim;
import com.example.hrms.domain.Employee;
import org.springframework.data.jpa.repository.*;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public interface ExpenseRepository extends JpaRepository<ExpenseClaim, Long> {

    @Query("SELECT e FROM ExpenseClaim e WHERE e.employee = :emp")
    List<ExpenseClaim> findByEmployee(@Param("emp") Employee emp);

    @Query("SELECT e FROM ExpenseClaim e WHERE e.status = :status")
    List<ExpenseClaim> findByStatus(@Param("status") Status status);

    @Query("SELECT e FROM ExpenseClaim e WHERE e.tenantId = :tenant")
    List<ExpenseClaim> findByTenant(@Param("tenant") UUID tenant);

    @Query("SELECT e FROM ExpenseClaim e WHERE e.status = 'PENDING_MANAGER'")
    List<ExpenseClaim> findPendingManager();

    @Query("SELECT e FROM ExpenseClaim e WHERE e.status = 'PENDING_FINANCE'")
    List<ExpenseClaim> findPendingFinance();

    @Query(
        "SELECT COUNT(e) > 0 FROM ExpenseClaim e " +
        "WHERE e.employee = :emp " +
        "AND e.expenseDate = :date " +
        "AND e.category = :cat " +
        "AND e.status IN ('DRAFT', 'SUBMITTED')")
    boolean existsSimilarDraft(@Param("emp") Employee emp, @Param("date") OffsetDateTime date, @Param("cat") String category);
}