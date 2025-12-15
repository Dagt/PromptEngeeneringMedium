package com.nova.bdp.api.controller;

import com.nova.bdp.api.dto.LoanPaymentRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.math.BigDecimal;
import java.util.UUID;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LoanControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void registerLoanPayment_shouldReturn200_whenRequestIsValid() {
        UUID loanId = UUID.randomUUID();
        LoanPaymentRequest request = new LoanPaymentRequest(BigDecimal.TEN);

        given()
            .contentType(ContentType.JSON)
            .body(request)
        .when()
            .patch("/api/v1/loans/{loanId}/payment", loanId)
        .then()
            .statusCode(200);
    }
}
