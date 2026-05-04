package se.viktor.labs.aimiddleware.service;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PersonalityProviderTest {

    private final PersonalityProvider provider = new PersonalityProvider();

    @Test
    void coder_returnsPrompt() {
        assertThat(provider.getSystemPrompt("coder")).isNotBlank();
    }

    @Test
    void pirate_returnsPrompt() {
        assertThat(provider.getSystemPrompt("pirate")).isNotBlank();
    }

    @Test
    void helper_returnsPrompt() {
        assertThat(provider.getSystemPrompt("helper")).isNotBlank();
    }

    @Test
    void unknownPersonality_throwsIllegalArgumentException() {
        assertThatThrownBy(() -> provider.getSystemPrompt("wizard"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("wizard");
    }
}