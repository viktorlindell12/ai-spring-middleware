package se.viktor.labs.aimiddleware.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.web.client.RestClient;

/**
 * Configures the {@link RestClient} bean used to communicate with the LLM API.
 * Base URL and authorization header are set once at startup from application.yml.
 */
@EnableRetry
@Configuration
public class RestClientConfig {

    /**
     * Creates a pre-configured {@link RestClient} with base URL and Bearer token.
     *
     * @param baseUrl the LLM API base URL (llm.base-url in application.yml)
     * @param apiKey  the API key injected from the OPENROUTER_API_KEY environment variable
     * @return a configured {@link RestClient} instance
     */
    @Bean
    public RestClient restClient(
            @Value("${llm.base-url}") String baseUrl,
            @Value("${llm.api-key}") String apiKey) {

        return RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }
}