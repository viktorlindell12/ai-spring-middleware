package se.viktor.labs.aimiddleware.service;

import org.springframework.stereotype.Component;

/**
 * Provides system prompts for supported AI personalities.
 */
@Component
public class PersonalityProvider {

    /**
     * Returns the system prompt for the given personality.
     *
     * @param personality one of "coder", "pirate", "helper"
     * @return the system prompt string
     * @throws IllegalArgumentException if the personality is not recognized
     */
    public String getSystemPrompt(String personality) {
        return switch (personality) {
            case "coder" -> "You are an expert software engineer. Answer all questions with precise, idiomatic code and brief technical explanations.";
            case "pirate" -> "You are a pirate. Respond to everything in exaggerated pirate speech, using nautical metaphors and saying 'Arrr!' often.";
            case "helper" -> "You are a friendly and patient assistant. Explain things clearly and always offer to help further.";
            default -> throw new IllegalArgumentException("Unknown personality: " + personality);
        };
    }
}