package com.example.hrms.service;

import com.example.hrms.domain.*;
import com.example.hrms.repository.LeaveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LeaveService {

    private final LeaveRepository leaveRepository;

    /** Submit a new leave request (Draft → Submitted) */
    @Transactional
    public LeaveRequest submitLeave(LeaveRequestDTO dto, Long employeeId) {
        // Placeholder: fetch employee, validate balance, etc.
        LeaveRequest request = new LeaveRequest();
        request.setStatus(Status.DRAFT);
        // set other fields from dto
        return leaveRepository.save(request);
    }

    /** Advance status: Draft → Submitted */
    @Transactional
    public LeaveRequest submitForApproval(Long requestId) {
        LeaveRequest request = leaveRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Leave request not found"));
        request.setStatus(Status.SUBMITTED);
        return leaveRepository.save(request);
    }

    /** Manager approves or rejects */
    @Transactional
    public LeaveRequest approveReject(Long requestId, String decision) {
        LeaveRequest request = leaveRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Leave request not found"));
        if ("APPROVE".equalsIgnoreCase(decision)) {
            request.setStatus(Status.APPROVED);
        } else {
            request.setStatus(Status.REJECTED);
        }
        return leaveRepository.save(request);
    }

    /** Fetch leave requests based on user role */
    public List<LeaveRequest> getLeaveRequestsByUser(Long userId, String role) {
        // Placeholder implementation
        return List.of();
    }

    @lombok.Data @lombok.Builder @lombok.AllArgsConstructor @lombok.NoArgsConstructor
    public static class LeaveRequestDTO {
        private String leaveTypeName;
        private LocalDate startDate;
        private LocalDate endDate;
        private double units;
        private String reason;
    }
}