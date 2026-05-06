package se.viktor.labs.aimiddleware.service;

import org.springframework.stereotype.Service;
import se.viktor.labs.aimiddleware.client.LlmClient;
import se.viktor.labs.aimiddleware.model.ChatRequest;
import se.viktor.labs.aimiddleware.model.ChatResponse;
import se.viktor.labs.aimiddleware.model.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Orchestrates the chat flow: resolves personality, builds context, calls LLM, persists history.
 */
@Service
public class ChatService {

    private final PersonalityProvider personalityProvider;
    private final ChatMemory chatMemory;
    private final LlmClient llmClient;

    public ChatService(PersonalityProvider personalityProvider, ChatMemory chatMemory, LlmClient llmClient) {
        this.personalityProvider = personalityProvider;
        this.chatMemory = chatMemory;
        this.llmClient = llmClient;
    }

    /**
     * Processes a chat request by building a full message list and sending it to the LLM.
     *
     * @param request the incoming chat request
     * @return the LLM's reply wrapped in a {@link ChatResponse}
     */
    public ChatResponse chat(ChatRequest request) {
        String systemPrompt = personalityProvider.getSystemPrompt(request.personality());

        List<Message> messages = new ArrayList<>();
        messages.add(new Message("system", systemPrompt));

        if (request.sessionId() != null && !request.sessionId().isBlank()) {
            messages.addAll(chatMemory.getHistory(request.sessionId()));
        }

        messages.add(new Message("user", request.message()));

        String reply = llmClient.sendMessages(messages);

        if (request.sessionId() != null && !request.sessionId().isBlank()) {
            chatMemory.addMessage(request.sessionId(), new Message("user", request.message()));
            chatMemory.addMessage(request.sessionId(), new Message("assistant", reply));
        }

        return new ChatResponse(reply);
    }
}