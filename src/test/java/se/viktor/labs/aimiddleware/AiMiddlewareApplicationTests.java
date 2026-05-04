package se.viktor.labs.aimiddleware;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = "llm.api-key=test-key")
class AiMiddlewareApplicationTests {

    @Test
    void contextLoads() {
    }
}