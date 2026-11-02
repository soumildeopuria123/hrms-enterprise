package com.example.hrms.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class EmployeeTest {

    @Test
    void employeeDefaultsStatusToActive() {
        Employee employee = new Employee("EMP-999", "Mina", "Reed", "mina@example.com", null);
        assertNotNull(employee.getHireDate());
        assertNull(employee.getTerminationDate());
    }

    @Test
    void fullNameCombinesFirstNameAndLastName() {
        Employee employee = new Employee("EMP-100", "Mina", "Reed", "mina@example.com", "00000000-0000-0000-0000-000000000001");
        assertEquals("Mina Reed", employee.getFullName());
    }

    @Test
    void activeEmployeesAreNotTerminated() {
        Employee active = new Employee("EMP-100", "Mina", "Reed", "mina@example.com", "00000000-0000-0000-0000-000000000001");
        active.setTerminationDate(null);
        assertFalse(active.isTerminated());
    }
}
