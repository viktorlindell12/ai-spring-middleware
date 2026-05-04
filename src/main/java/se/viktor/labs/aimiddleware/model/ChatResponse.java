package se.viktor.labs.aimiddleware.model;

/**
 * Response returned to the client after the LLM has processed the request.
 *
 * @param response the AI-generated reply
 */
public record ChatResponse(String response) {}