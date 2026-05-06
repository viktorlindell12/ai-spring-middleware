package se.viktor.labs.aimiddleware.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Chat", description = "Send messages to the LLM via configured personalities")
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
    @Operation(
            summary = "Send a chat message",
            description = "Sends a message to the LLM using the specified personality and optional session ID for conversation history.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful response from LLM"),
                    @ApiResponse(responseCode = "400", description = "Invalid request"),
                    @ApiResponse(responseCode = "500", description = "AI service is currently unavailable")
            }
    )
    @PostMapping("/chat")
    public ResponseEntity<ChatResponse> chat(@Valid @RequestBody ChatRequest request) {
        return ResponseEntity.ok(chatService.chat(request));
    }
}