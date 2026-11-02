package com.example.hrms.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class DepartmentTest {

    @Test
    void departmentNameIsStored() {
        Department department = new Department("Engineering", "Product and platform engineering");
        assertEquals("Engineering", department.getName());
    }

    @Test
    void departmentHasUuid() {
        Department department = new Department("HR", "People operations");
        assertNotNull(department.getTenantId());
    }

    @Test
    void equalsConsidersNameAndTenant() {
        Department d1 = new Department("Eng", "Engineering");
        Department d2 = new Department("Eng", "Engineering");
        assertEquals(d1, d2);
    }
}
