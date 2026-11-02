package com.example.hrms.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class LeaveTypeTest {

    @Test
    void annualIsValid() {
        assertEquals(LeaveType.ANNUAL, LeaveType.ANNUAL);
    }

    @Test
    void sickIsDistinctFromAnnual() {
        assertNotEquals(LeaveType.ANNUAL, LeaveType.SICK);
    }

    @Test
    void unpaidExists() {
        assertNotNull(LeaveType.UNPAID);
    }
}
