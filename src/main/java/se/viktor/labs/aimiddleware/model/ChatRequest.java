package se.viktor.labs.aimiddleware.model;

import jakarta.validation.constraints.NotBlank;

/**
 * Incoming request from the client.
 *
 * @param personality the personality the AI should adopt (e.g. "coder", "pirate", "helper")
 * @param message     the user's message
 * @param sessionId   unique identifier for the conversation session
 */
public record ChatRequest(
        @NotBlank(message = "Personality must not be blank") String personality,
        @NotBlank(message = "Message must not be blank") String message,
        @NotBlank(message = "Session ID must not be blank") String sessionId
) {}