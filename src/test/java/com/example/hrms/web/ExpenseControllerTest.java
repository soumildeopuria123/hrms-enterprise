package com.example.hrms.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ExpenseController.class)
class ExpenseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser
    void getExpenseClaimsReturnsOk() throws Exception {
        mockMvc.perform(get("/api/expenses"))
            .andExpect(status().isOk());
    }

    @Test
    void getExpenseClaimsRequiresAuthentication() throws Exception {
        mockMvc.perform(get("/api/expenses"))
            .andExpect(status().isUnauthorized());
    }
}
