package com.example.hrms.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class AttendanceTest {

    @Test
    void attendanceDefaultsToPresent() {
        Employee employee = new Employee("EMP-1", "A", "B", "a@example.com", null);
        Attendance attendance = new Attendance(employee, null);
        assertEquals(AttendanceStatus.PRESENT, attendance.getStatus());
    }

    @Test
    void failedPunchExists() {
        assertNotNull(AttendanceStatus.FAILED);
    }

    @Test
    void appliedPunchStatusIsValid() {
        assertNotNull(AttendanceStatus.APPLIED);
    }
}
