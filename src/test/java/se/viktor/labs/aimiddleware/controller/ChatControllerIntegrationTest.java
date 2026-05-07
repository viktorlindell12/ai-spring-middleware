package se.viktor.labs.aimiddleware.controller;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ChatControllerIntegrationTest {

    static WireMockServer wireMock;

    static {
        wireMock = new WireMockServer(WireMockConfiguration.wireMockConfig()
                .dynamicPort()
                .http2PlainDisabled(true));
        wireMock.start();
    }

    @AfterAll
    static void stopWireMock() {
        wireMock.stop();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("llm.base-url", wireMock::baseUrl);
        registry.add("llm.api-key", () -> "test-key");
        registry.add("llm.model", () -> "test-model");
    }

    @BeforeEach
    void resetWireMock() {
        wireMock.resetAll();
    }

    @Autowired
    TestRestTemplate restTemplate;

    private static final String OPENROUTER_RESPONSE = """
            {
              "choices": [
                {
                  "message": {
                    "role": "assistant",
                    "content": "A for-loop repeats a block of code."
                  }
                }
              ]
            }
            """;

    private HttpEntity<String> chatRequest(String sessionId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String body = """
                {"personality":"coder","message":"What is a for-loop?","sessionId":"%s"}
                """.formatted(sessionId);
        return new HttpEntity<>(body, headers);
    }

    @Test
    void successfulRequest_returnsLlmResponse() {
        wireMock.stubFor(post(urlEqualTo("/chat/completions"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(OPENROUTER_RESPONSE)));

        ResponseEntity<String> response = restTemplate.exchange(
                "/api/v1/chat", HttpMethod.POST, chatRequest("session-1"), String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("for-loop");
    }

    @Test
    void tooManyRequests_retries3Times() {
        wireMock.stubFor(post(urlEqualTo("/chat/completions"))
                .willReturn(aResponse().withStatus(429)));

        ResponseEntity<String> response = restTemplate.exchange(
                "/api/v1/chat", HttpMethod.POST, chatRequest("session-2"), String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        wireMock.verify(3, postRequestedFor(urlEqualTo("/chat/completions")));
    }

    @Test
    void serverError_globalExceptionHandlerReturns500() {
        wireMock.stubFor(post(urlEqualTo("/chat/completions"))
                .willReturn(aResponse().withStatus(500)));

        ResponseEntity<String> response = restTemplate.exchange(
                "/api/v1/chat", HttpMethod.POST, chatRequest("session-3"), String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).contains("AI service is currently unavailable");
    }
}