package se.viktor.labs.aimiddleware.model;

/**
 * Represents a single message in the LLM conversation format.
 * Used internally to build the message history sent to OpenRouter.
 *
 * @param role    the sender role: "system", "user", or "assistant"
 * @param content the message text
 */
public record Message(String role, String content) {}