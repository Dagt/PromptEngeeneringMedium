package com.nova.bdp.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nova.bdp.api.dto.LoanPaymentRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class LoanControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private LoanController loanController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(loanController).build();
    }

    @Test
    void registerLoanPayment_shouldReturn200_whenRequestIsValid() throws Exception {
        UUID loanId = UUID.randomUUID();
        LoanPaymentRequest request = new LoanPaymentRequest(BigDecimal.TEN);

        mockMvc.perform(patch("/api/v1/loans/{loanId}/payment", loanId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }
}
