package com.example.hrms.repository;

import com.example.hrms.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PayrollRepository extends JpaRepository<PayrollRun, Long> {

    List<PayrollRun> findByTenantId(UUID tenantId);

    List<PayrollRun> findByStatus(PayrollStatus status);

    Optional<PayrollRun> findByPayrollRunId(UUID payrollRunId);

    List<PayrollItem> findByPayrollRunId(UUID payrollRunId);
}