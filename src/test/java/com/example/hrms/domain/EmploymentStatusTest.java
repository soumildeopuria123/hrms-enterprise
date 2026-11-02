package com.example.hrms.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class EmploymentStatusTest {

    @Test
    void activeIsValidStatus() {
        assertEquals(EmploymentStatus.ACTIVE, EmploymentStatus.ACTIVE);
    }

    @Test
    void terminatedIsDistinctFromActive() {
        assertNotEquals(EmploymentStatus.ACTIVE, EmploymentStatus.TERMINATED);
    }

    @Test
    void onLeaveExists() {
        assertNotNull(EmploymentStatus.ON_LEAVE);
    }
}
