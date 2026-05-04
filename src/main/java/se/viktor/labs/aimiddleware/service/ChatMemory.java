package se.viktor.labs.aimiddleware.service;

import org.springframework.stereotype.Component;
import se.viktor.labs.aimiddleware.model.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Stores conversation history per session in memory.
 */
@Component
public class ChatMemory {

    private final ConcurrentHashMap<String, List<Message>> history = new ConcurrentHashMap<>();

    public List<Message> getHistory(String sessionId) {
        return history.getOrDefault(sessionId, List.of());
    }

    public void addMessage(String sessionId, Message message) {
        history.computeIfAbsent(sessionId, id -> new ArrayList<>()).add(message);
    }
}