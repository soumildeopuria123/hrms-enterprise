package com.example.hrms.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PayrollItemTest {

    @Test
n(ext6pezb2frv1no
package com.example.hrms.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PayrollItemTest {

    @Test
    void grossEqualsNetWhenNoDeductions() {
        Employee employee = new Employee("EMP-1", "A", "B", "a@example.com", null);
        PayrollItem item = PayrollItem.builder()
            .employee(employee)
            .grossAmount(1000.0)
            .deductionsAmount(0.0)
            .netAmount(1000.0)
            .currency("USD")
            .build();
        assertEquals(1000.0, item.getGrossAmount());
        assertEquals(1000.0, item.getNetAmount());
        assertEquals(0.0, item.getDeductionsAmount());
    }

    @Test
    void builderSetsCalculationReference() {
        Employee employee = new Employee("EMP-1", "A", "B", "a@example.com", null);
        PayrollItem item = PayrollItem.builder()
            .grossAmount(500.0)
            .netAmount(450.0)
            .deductionsAmount(50.0)
            .calculationReference("manual-chk-1")
            .build();
        assertEquals("manual-chk-1", item.getCalculationReference());
    }
}
