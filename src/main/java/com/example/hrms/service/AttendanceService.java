package com.example.hrms.service;

import com.example.hrms.domain.*;
import com.example.hrms.repository.AttendanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;

    /** Record a clock‑in punch for the current day */
    @Transactional
    public Attendance clockIn(String source) {
        // Placeholder: obtain employee from security context
        Attendance attendance = new Attendance();
        attendance.setStatus("Present");
        attendance.setSource(source);
        attendance.setClockInAt(java.time.LocalTime.now());
        return attendanceRepository.save(attendance);
    }

    /** Record a clock‑out punch, calculate work hours */
    @Transactional
    public Attendance clockOut(String source) {
        // Placeholder: find existing clock‑in for today
        Attendance attendance = attendanceRepository.findTopByEmployeeAndDate(null, null).orElse(new Attendance());
        attendance.setClockOutAt(java.time.LocalTime.now());
        long workMinutes = java.time.temporal.ChronoUnit.MINUTES.between(attendance.getClockInAt(), attendance.getClockOutAt());
        attendance.setStatus(workMinutes >= 240 ? "Present" : "Half-Day");
        return attendanceRepository.save(attendance);
    }

    /** Fetch attendance logs filtered by employee or date range */
    public List<Attendance> getAttendanceLogs(Long employeeId, LocalDate startDate, LocalDate endDate, String role) {
        // Placeholder filtering logic
        return attendanceRepository.findAll();
    }

    /** Calculate total work hours for a record */
    public long calculateWorkHours(Attendance a) {
        if (a.getClockInAt() == null || a.getClockOutAt() == null) return 0L;
        return java.time.temporal.ChronoUnit.MINUTES.between(a.getClockInAt(), a.getClockOutAt());
    }
}