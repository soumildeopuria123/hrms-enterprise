package com.example.hrms.web;

import com.example.hrms.domain.*;
import com.example.hrms.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    /** POST /api/attendance/clock-in – Punch in for the current day */
    @PostMapping("/clock-in")
    public ResponseEntity<Attendance> clockIn(@RequestBody Map<String, String> payload,
                                              @AuthenticationPrincipal UserDetails user) {
        String source = payload.getOrDefault("source", "web");
        Attendance recorded = attendanceService.clockIn(source);
        return ResponseEntity.ok(recorded);
    }

    /** POST /api/attendance/clock-out – Punch out and calculate hours */
    @PostMapping("/clock-out")
    public ResponseEntity<Attendance> clockOut(@RequestBody Map<String, String> payload,
                                               @AuthenticationPrincipal UserDetails user) {
        String source = payload.getOrDefault("source", "web");
        Attendance recorded = attendanceService.clockOut(source);
        return ResponseEntity.ok(recorded);
    }

    /** GET /api/attendance – Fetch attendance logs filtered by role/employee */
    @GetMapping
    public ResponseEntity<List<Attendance>> getLogs(
            @RequestParam(required = false) Long employeeId,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @AuthenticationPrincipal UserDetails user) {

        String role = determineRole(user);
        List<Attendance> logs = attendanceService.getAttendanceLogs(
                employeeId, startDate, endDate, role);
        return ResponseEntity.ok(logs);
    }

    /** Helper – determine role from principal (placeholder) */
    private String determineRole(UserDetails user) {
        return "EMPLOYEE"; // placeholder – adapt to your security model
    }
}