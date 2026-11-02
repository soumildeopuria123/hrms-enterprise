package com.example.hrms.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ExpenseClaimTest {

    @Test
    void expenseClaimDefaultsStatusToDraft() {
        Employee employee = new Employee("EMP-1", "A", "B", "a@example.com", null);
        ExpenseClaim claim = new ExpenseClaim(employee, 10.0, "USD", null);
        assertEquals(ExpenseStatus.DRAFT, claim.getStatus());
    }

    @Test
    void approvedExpenseStatusExists() {
        assertEquals(ExpenseStatus.APPROVED, ExpenseStatus.APPROVED);
    }

    @Test
    void rejectedExpenseStatusExists() {
        assertEquals(ExpenseStatus.REJECTED, ExpenseStatus.REJECTED);
    }
}
