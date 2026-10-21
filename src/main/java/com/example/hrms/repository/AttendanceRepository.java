package com.example.hrms.repository;

import com.example.hrms.domain.Attendance;
import com.example.hrms.domain.Employee;
import org.springframework.data.jpa.repository.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    @Query("SELECT a FROM Attendance a WHERE a.employee = :emp AND a.workDate BETWEEN :start AND :end")
    List<Attendance> findByEmployeeAndDateRange(@Param("emp") Employee emp, @Param("start") LocalDate start, @Param("end") LocalDate end);

    @Query("SELECT a FROM Attendance a WHERE a.employee = :emp AND a.workDate = :date ORDER BY a.id DESC")
    Optional<Attendance> findTopByEmployeeAndDate(@Param("emp") Employee emp, @Param("date") LocalDate date);

    @Query("SELECT COUNT(a) > 0 FROM Attendance a WHERE a.employee = :emp AND a.workDate = :date AND a.clockInAt IS NOT NULL")
    boolean existsClockInToday(@Param("emp") Employee emp, @Param("date") LocalDate date);
}