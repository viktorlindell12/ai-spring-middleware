package se.viktor.labs.aimiddleware.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.viktor.labs.aimiddleware.model.ChatRequest;
import se.viktor.labs.aimiddleware.model.ChatResponse;
import se.viktor.labs.aimiddleware.service.ChatService;

/**
 * REST controller exposing the chat endpoint.
 */
@RestController
@RequestMapping("/api/v1")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    /**
     * Accepts a chat request, processes it through the LLM pipeline, and returns the reply.
     *
     * @param request the chat request with personality, message, and optional sessionId
     * @return the AI-generated response
     */
    @PostMapping("/chat")
    public ResponseEntity<ChatResponse> chat(@Valid @RequestBody ChatRequest request) {
        return ResponseEntity.ok(chatService.chat(request));
    }
}