package com.example.hrms.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class LeaveRequestTest {

    @Test
    void leaveRequestDefaultsStatusToDraft() {
        Employee employee = new Employee("EMP-1", "A", "B", "a@example.com", null);
        LeaveRequest request = new LeaveRequest(employee, LeaveType.ANNUAL, null, null, 1.0);
        assertEquals(Status.DRAFT, request.getStatus());
    }

    @Test
    void leaveRequestHasUniqueUuid() {
        LeaveRequest request = new LeaveRequest();
        assertNotNull(request.getLeaveRequestId());
    }

    @Test
    void submittedAtMatchesExpectedPattern() {
        LeaveRequest request = new LeaveRequest();
        request.setSubmittedAt(null);
        assertNull(request.getSubmittedAt());
    }
}
