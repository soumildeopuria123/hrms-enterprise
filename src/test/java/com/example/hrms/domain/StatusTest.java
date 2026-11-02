package com.example.hrms.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class StatusTest {

    @Test
    void pendingIsInitialValue() {
        assertEquals(Status.PENDING, Status.PENDING);
    }

    @Test
    void resolvedAndRejectedAreDistinct() {
        assertNotEquals(Status.RESOLVED, Status.REJECTED);
    }

    @Test
    void approvedIsValid() {
        assertNotNull(Status.APPROVED);
    }
}
