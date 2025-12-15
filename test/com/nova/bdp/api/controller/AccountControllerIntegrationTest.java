package com.nova.bdp.api.controller;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.UUID;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AccountControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void getBalance_shouldReturn200_whenAccountExists() {
        UUID accountId = UUID.randomUUID();
        given()
        .when()
            .get("/api/v1/accounts/{accountId}/balance", accountId)
        .then()
            .statusCode(200);
    }
}
