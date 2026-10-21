package com.example.hrms.repository;

import com.example.hrms.domain.LeaveRequest;
import com.example.hrms.domain.Employee;
import org.springframework.data.jpa.repository.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LeaveRepository extends JpaRepository<LeaveRequest, Long> {

    @Query("SELECT lr FROM LeaveRequest lr WHERE lr.employee.id = :empId")
    List<LeaveRequest> findByEmployeeId(@Param("empId") Long empId);

    @Query("SELECT lr FROM LeaveRequest lr WHERE lr.leaveType.name = :type AND lr.status IN ('PENDING_MANAGER', 'PENDING_HR')")
    List<LeaveRequest> findPendingByLeaveType(@Param("type") String type);

    @Query("SELECT lr FROM LeaveRequest lr WHERE lr.status = :status")
    List<LeaveRequest> findByStatus(@Param("status") Status status);
}