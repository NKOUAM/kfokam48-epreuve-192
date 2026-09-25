package com.kfokam48.evaluation.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class SessionControllerIT {

    @Value("${local.server.port}")
    int port;

    private HttpResponse<String> post(String path, String json) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + port + path))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    @Test
    void ouvre_une_session_valide() throws Exception {
        HttpResponse<String> r = post("/api/sessions",
                "{\"titre\":\"Cours test\",\"promotionId\":1}");

        assertThat(r.statusCode()).isEqualTo(201);
        assertThat(r.body()).contains("\"code\"").contains("\"expirationAt\"");
    }

    @Test
    void refuse_titre_manquant() throws Exception {
        HttpResponse<String> r = post("/api/sessions",
                "{\"titre\":\"\",\"promotionId\":1}");

        assertThat(r.statusCode()).isEqualTo(400);
        assertThat(r.body()).contains("VALIDATION");
    }
}
