package com.example.hrms.repository;

import com.example.hrms.domain.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByWorkEmail(String workEmail);

    List<Employee> findByTenantIdOrderByLastNameAscFirstNameAsc(UUID tenantId);

    List<Employee> findByDepartmentId(Long departmentId);

    List<Employee> findByManagerId(Long managerId);

    long countByEmploymentStatus(com.example.hrms.domain.EmploymentStatus status);
}
