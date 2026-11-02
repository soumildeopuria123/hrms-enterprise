package com.example.hrms.repository;

import com.example.hrms.domain.LeaveRequest;
import com.example.hrms.domain.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {

    Optional<LeaveRequest> findByLeaveRequestId(UUID leaveRequestId);

    List<LeaveRequest> findByEmployeeIdOrderByStartDateDesc(Long employeeId);

    List<LeaveRequest> findByTenantIdOrderBySubmittedAtDesc(UUID tenantId);

    List<LeaveRequest> findByStatus(Status status);

    List<LeaveRequest> findByEmployeeIdAndStatus(Long employeeId, Status status);
}
