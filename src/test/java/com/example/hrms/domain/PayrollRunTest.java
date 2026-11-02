package com.example.hrms.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PayrollRunTest {

    @Test
    void payrollRunDefaultsStatusToDraft() {
        PayrollRun run = new PayrollRun(null, null, null, null);
        assertEquals(PayrollStatus.DRAFT, run.getStatus());
    }

    @Test
    void finalizedExists() {
        assertEquals(PayrollStatus.FINALIZED, PayrollStatus.FINALIZED);
    }

    @Test
    void executedExists() {
        assertEquals(PayrollStatus.EXECUTED, PayrollStatus.EXECUTED);
    }
}
