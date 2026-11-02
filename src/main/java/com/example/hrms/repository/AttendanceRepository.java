package com.example.hrms.repository;

import com.example.hrms.domain.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    @Query("select a from Attendance a where a.employee.id = :employeeId and a.punchDate = :date")
    List<Attendance> findByEmployeeIdAndPunchDate(Long employeeId, LocalDate date);

    @Query("select a from Attendance a where a.employee.id = :employeeId and a.punchDate between :start and :end")
    List<Attendance> findByEmployeeIdAndPunchDateBetween(Long employeeId, LocalDate start, LocalDate end);

    @Query("select a from Attendance a where a.punchDate = :date and a.employee.tenantId = :tenantId")
    List<Attendance> findByPunchDateAndTenantId(LocalDate date, UUID tenantId);

    long countByPunchDateAndStatus(LocalDate date, com.example.hrms.domain.AttendanceStatus status);
}
