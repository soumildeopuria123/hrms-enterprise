package com.example.hrms.repository;

import com.example.hrms.domain.PayrollItem;
import com.example.hrms.domain.PayrollRun;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PayrollRepository extends JpaRepository<PayrollRun, Long> {

    Optional<PayrollRun> findByPayrollRunId(UUID payrollRunId);

    List<PayrollRun> findByTenantIdOrderByCreatedAtDesc(UUID tenantId);

    List<PayrollItem> findByPayrollRunId(UUID payrollRunId);

    @Query("select sum(p.netAmount) from PayrollItem p where p.payrollRun.tenantId = :tenantId and p.payrollRun.status = 'Finalized'")
    BigDecimal totalFinalizedNetAmountForTenant(UUID tenantId);
}
