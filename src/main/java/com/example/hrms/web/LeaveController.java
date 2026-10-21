package com.example.hrms.web;

import com.example.hrms.domain.*;
import com.example.hrms.service.LeaveService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/leaves")
@RequiredArgsConstructor
public class LeaveController {

    private final LeaveService leaveService;

    /** POST /api/leaves – Submit a new leave request */
    @PostMapping
    public ResponseEntity<LeaveRequest> submit(@RequestBody LeaveRequestDTO dto,
                                               @AuthenticationPrincipal UserDetails user) {
        Long employeeId = extractEmployeeIdFromUser(user);
        LeaveRequest created = leaveService.submitLeave(dto, employeeId);
        return ResponseEntity.status(201).body(created);
    }

    /** GET /api/leaves – Fetch leave requests based on user role/employee ID */
    @GetMapping
    public ResponseEntity<List<LeaveRequest>> getAll(@AuthenticationPrincipal UserDetails user) {
        Long userId = extractUserId(user);
        String role = determineRole(user);
        List<LeaveRequest> requests = leaveService.getLeaveRequestsByUser(userId, role);
        return ResponseEntity.ok(requests);
    }

    /** PATCH /api/leaves/{id}/status – Approve/Reject step endpoint */
    @PatchMapping("/{id}/status")
    public ResponseEntity<LeaveRequest> updateStatus(@PathVariable Long id,
                                                    @RequestParam String decision,
                                                    @AuthenticationPrincipal UserDetails user) {
        LeaveRequest updated = leaveService.approveReject(id, decision, extractUserId(user));
        return ResponseEntity.ok(updated);
    }

    /** Helper – map principal → employee ID (adapt to your security model) */
    private Long extractEmployeeIdFromUser(UserDetails user) {
        // Example: return Long.parseLong(user.getUsername());
        return 1L; // placeholder
    }

    private Long extractUserId(UserDetails user) {
        return Long.parseLong(user.getUsername());
    }

    private String determineRole(UserDetails user) {
        // Inspect user.getAuthorities() and return "EMPLOYEE", "MANAGER", "HR", etc.
        return "EMPLOYEE"; // placeholder
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