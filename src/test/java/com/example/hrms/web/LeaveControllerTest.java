package com.example.hrms.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(LeaveController.class)
class LeaveControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser
    void listLeaveRequestsReturnsOk() throws Exception {
        mockMvc.perform(get("/api/leave"))
            .andExpect(status().isOk());
    }

    @Test
    void listLeaveRequestsRequiresAuthentication() throws Exception {
        mockMvc.perform(get("/api/leave"))
            .andExpect(status().isUnauthorized());
    }
}
