package com.example.hrms.service;

import com.example.hrms.domain.*;
import com.example.hrms.repository.LeaveRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LeaveService {

    private final LeaveRequestRepository leaveRequestRepository;

    /**
     * Submit a new leave request in Draft state so HR can validate and accept
     * before it enters the approval chain.
     */
    @Transactional
    public LeaveRequest submitLeave(LeaveRequestDTO dto, Long employeeId) {
        if (dto.getStartDate() == null || dto.getEndDate() == null) {
            throw new IllegalArgumentException("Both start and end dates are required.");
        }
        if (dto.getStartDate().isAfter(dto.getEndDate())) {
            throw new IllegalArgumentException("Start date must be before or equal to end date.");
        }
        if (dto.getUnits() <= 0) {
            throw new IllegalArgumentException("Leave units must be positive.");
        }

        Employee employee = currentEmployee(employeeId);
        LeaveRequest request = new LeaveRequest();
        request.setEmployee(employee);
        request.setLeaveType(dto.toLeaveType());
        request.setStartDate(dto.getStartDate());
        request.setEndDate(dto.getEndDate());
        request.setUnits(dto.getUnits());
        request.setReason(dto.getReason());
        request.setTenantId(employee.getTenantId());
        request.setStatus(Status.DRAFT);
        return leaveRequestRepository.save(request);
    }

    /**
     * Return pending requests visible to the caller, filtered by role.
     */
    public List<LeaveRequest> getLeaveRequestsByUser(Long employeeId, String role) {
        if ("HR".equalsIgnoreCase(role) || "ADMIN".equalsIgnoreCase(role)) {
            return leaveRequestRepository.findByTenantIdOrderBySubmittedAtDesc(
                    currentEmployee(employeeId).getTenantId());
        }
        if ("MANAGER".equalsIgnoreCase(role)) {
            return leaveRequestRepository.findByEmployeeIdOrderByStartDateDesc(employeeId);
        }
        return leaveRequestRepository.findByEmployeeIdAndStatus(employeeId, Status.SUBMITTED);
    }

    /**
     * Move a request forward or back depending on the current workflow state.
     */
    @Transactional
    public LeaveRequest approveReject(Long id, String decision, Long actorId) {
        LeaveRequest request = leaveRequestRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Leave request not found"));

        if ("APPROVE".equalsIgnoreCase(decision)) {
            request.setStatus(Status.APPROVED);
            request.setApprovedBy(actorId);
        } else if ("REJECT".equalsIgnoreCase(decision)) {
            request.setStatus(Status.REJECTED);
            request.setRejectedBy(actorId);
        } else {
            throw new IllegalArgumentException("Decision must be APPROVE or REJECT.");
        }
        return leaveRequestRepository.save(request);
    }

    public LeaveRequest getById(Long id) {
        return leaveRequestRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Leave request not found"));
    }

    private Employee currentEmployee(Long employeeId) {
        return new Employee(
                "EMP-" + employeeId,
                "Demo",
                "Employee",
                "demo@" + employeeId + ".example.com",
                UUID.fromString("00000000-0000-0000-0000-000000000001"),
                null);
    }
}
