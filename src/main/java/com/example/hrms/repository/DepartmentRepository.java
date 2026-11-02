package com.example.hrms.repository;

import com.example.hrms.domain.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    Optional<Department> findByNameIgnoreCaseAndTenantId(String name, UUID tenantId);

    List<Department> findByTenantIdOrderByNameAsc(UUID tenantId);
}
