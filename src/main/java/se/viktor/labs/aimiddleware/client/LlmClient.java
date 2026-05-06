package se.viktor.labs.aimiddleware.client;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import se.viktor.labs.aimiddleware.model.Message;

import java.util.List;

/**
 * Client responsible for sending requests to the OpenRouter LLM API.
 * Knows nothing about personalities or sessions – receives a ready-built message list.
 */
@Component
public class LlmClient {

    private static final String MODEL = "tencent/hy3-preview:free";
    private static final String CHAT_COMPLETIONS_PATH = "/chat/completions";

    private final RestClient restClient;

    public LlmClient(RestClient restClient) {
        this.restClient = restClient;
    }

    /**
     * Sends a list of messages to the LLM and returns the assistant's reply.
     *
     * @param messages the full conversation history including system prompt and user messages
     * @return the text content of the LLM's response
     */
    @Retryable(retryFor = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 1000, multiplier = 2))
    public String sendMessages(List<Message> messages) {
        LlmRequest request = new LlmRequest(MODEL, messages);

        LlmResponse response = restClient.post()
                .uri(CHAT_COMPLETIONS_PATH)
                .body(request)
                .retrieve()
                .body(LlmResponse.class);

        return response.choices().getFirst().message().content();
    }

    // Internal records – represent the OpenRouter API request/response format

    private record LlmRequest(String model, List<Message> messages) {}

    private record LlmResponse(List<Choice> choices) {
        private record Choice(Message message) {}
    }
}