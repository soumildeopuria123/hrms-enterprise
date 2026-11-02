package com.example.hrms.service;

import com.example.hrms.domain.*;
import com.example.hrms.repository.AttendanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;

    /**
     * Record a clock-in punch for today.
     * Duplicate-prevention: only one in-punch per employee per day is allowed.
     */
    @Transactional
    public Attendance clockIn(String source) {
        Employee current = currentEmployee();
        LocalDate today = LocalDate.now();

        List<Attendance> existing = attendanceRepository.findByEmployeeIdAndPunchDate(
                current.getId(), today);
        if (!existing.isEmpty()) {
            throw new IllegalStateException("Already punched in for today.");
        }

        Attendance punch = new Attendance(current, today, LocalTime.now(),
                source != null ? source : "web");
        return attendanceRepository.save(punch);
    }

    /**
     * Record a clock-out punch and leave the status as Present unless it was
     * really late (after the grace window).
     */
    @Transactional
    public Attendance clockOut(String source) {
        Employee current = currentEmployee();
        LocalDate today = LocalDate.now();

        Attendance punch = attendanceRepository.findByEmployeeIdAndPunchDate(
                current.getId(), today).stream()
                .filter(a -> a.getClockInTime() != null && a.getClockOutTime() == null)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No open punch found for today."));

        punch.setClockOutTime(LocalTime.now());
        if (LocalTime.now().isAfter(LocalTime.of(9, 30))) {
            punch.setStatus(AttendanceStatus.LATE);
        }
        if (source != null) punch.setSource(source);
        return attendanceRepository.save(punch);
    }

    /**
     * Fetch attendance logs with optional employee/date range and role filter.
     */
    public List<Attendance> getAttendanceLogs(Long employeeId, LocalDate startDate,
                                                LocalDate endDate, String role) {
        if (employeeId != null && ("EMPLOYEE".equalsIgnoreCase(role)
                || "MANAGER".equalsIgnoreCase(role))) {
            if (startDate == null) startDate = LocalDate.now().minusDays(30);
            if (endDate == null) endDate = LocalDate.now();
            return attendanceRepository.findByEmployeeIdAndPunchDateBetween(
                    employeeId, startDate, endDate);
        }
        LocalDate start = startDate != null ? startDate : LocalDate.now().minusDays(30);
        LocalDate end = endDate != null ? endDate : LocalDate.now();
        return attendanceRepository.findByPunchDateAndTenantId(start, currentEmployee().getTenantId());
    }

    private Employee currentEmployee() {
        return new Employee("EMP-1", "Demo", "Employee",
                "demo@1.example.com",
                java.util.UUID.fromString("00000000-0000-0000-0000-000000000001"),
                null);
    }
}
