package com.nova.bdp.api.controller;

import com.nova.bdp.api.dto.CreateTransferRequest;
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
class TransferControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void createTransfer_shouldReturn201_whenRequestIsValid() {
        CreateTransferRequest request = new CreateTransferRequest(UUID.randomUUID(), UUID.randomUUID(), BigDecimal.TEN, "USD");

        given()
            .contentType(ContentType.JSON)
            .body(request)
        .when()
            .post("/api/v1/transfers")
        .then()
            .statusCode(201);
    }
}
