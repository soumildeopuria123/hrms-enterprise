package com.example.hrms.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LeaveServiceTest {

    @Autowired
    private LeaveService leaveService;

    @Test
    void contextLoads() {
        assertNotNull(leaveService);
    }

    // TODO: wire data.sql / Testcontainers once security and repositories are
    //      available for test execution.
}
