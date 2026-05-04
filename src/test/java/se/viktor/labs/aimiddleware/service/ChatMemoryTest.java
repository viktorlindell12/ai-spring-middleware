package se.viktor.labs.aimiddleware.service;

import org.junit.jupiter.api.Test;
import se.viktor.labs.aimiddleware.model.Message;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ChatMemoryTest {

    private final ChatMemory memory = new ChatMemory();

    @Test
    void emptySession_returnsEmptyList() {
        assertThat(memory.getHistory("unknown")).isEmpty();
    }

    @Test
    void addMessages_storedInOrder() {
        memory.addMessage("s1", new Message("user", "Hello"));
        memory.addMessage("s1", new Message("assistant", "Hi there"));

        List<Message> history = memory.getHistory("s1");
        assertThat(history).hasSize(2);
        assertThat(history.get(0)).isEqualTo(new Message("user", "Hello"));
        assertThat(history.get(1)).isEqualTo(new Message("assistant", "Hi there"));
    }

    @Test
    void differentSessions_isolatedFromEachOther() {
        memory.addMessage("s1", new Message("user", "Session one"));
        memory.addMessage("s2", new Message("user", "Session two"));

        assertThat(memory.getHistory("s1")).hasSize(1);
        assertThat(memory.getHistory("s2")).hasSize(1);
        assertThat(memory.getHistory("s1").getFirst().content()).isEqualTo("Session one");
    }
}