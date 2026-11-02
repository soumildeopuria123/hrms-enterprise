package com.example.hrms.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AttendanceController.class)
class AttendanceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser
    void getTodayAttendanceReturnsOk() throws Exception {
        mockMvc.perform(get("/api/attendance/today"))
            .andExpect(status().isOk());
    }

    @Test
    void getTodayAttendanceRequiresAuthentication() throws Exception {
        mockMvc.perform(get("/api/attendance/today"))
            .andExpect(status().isUnauthorized());
    }
}
