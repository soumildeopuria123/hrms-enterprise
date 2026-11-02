package com.example.hrms.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PayrollServiceTest {

    @Autowired
    private PayrollService payrollService;

    @Test
    void contextLoads() {
        assertNotNull(payrollService);
    }
}
