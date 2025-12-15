package com.nova.bdp.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nova.bdp.api.dto.CreateTransferRequest;
import com.nova.bdp.api.dto.TransactionResponse;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class TransferControllerTest {

    private MockMvc mockMvc;

    @Mock
    private MeterRegistry meterRegistry;

    @InjectMocks
    private TransferController transferController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        meterRegistry = new SimpleMeterRegistry();
        transferController = new TransferController(meterRegistry);
        mockMvc = MockMvcBuilders.standaloneSetup(transferController).build();
    }

    @Test
    void createTransfer_shouldReturn201_whenRequestIsValid() throws Exception {
        CreateTransferRequest request = new CreateTransferRequest(UUID.randomUUID(), UUID.randomUUID(), BigDecimal.TEN, "USD");
        
        mockMvc.perform(post("/api/v1/transfers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }
}
