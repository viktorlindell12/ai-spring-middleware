# ai-spring-middleware

Spring Boot middleware service that acts as a bridge between the user and a Large Language Model (LLM) via OpenRouter. Supports personalities, session-based conversation memory, retry logic, and structured error handling.

## Prerequisites

- Java 25
- Maven (or use the included `./mvnw` wrapper)
- An [OpenRouter](https://openrouter.ai) API key (free account works)

## Getting an API key

1. Go to [openrouter.ai](https://openrouter.ai) and sign up
2. Navigate to **Keys** in the menu
3. Create a new key and copy it

## Running locally

1. Clone the repository:
   ```bash
   git clone https://github.com/viktorlindell12/ai-spring-middleware.git
   cd ai-spring-middleware
   ```

2. Set your API key as an environment variable:

   **macOS/Linux:**
   ```bash
   export OPENROUTER_API_KEY=your-key-here
   ```

   **Windows (PowerShell):**
   ```powershell
   $env:OPENROUTER_API_KEY="your-key-here"
   ```

   **Windows (CMD):**
   ```cmd
   set OPENROUTER_API_KEY=your-key-here
   ```

3. Start the application:
   ```bash
   ./mvnw spring-boot:run
   ```

The application starts on `http://localhost:8080`.

## API

### POST /api/v1/chat

Send a message to the LLM using one of the available personalities.

**Request body:**
```json
{
  "personality": "coder",
  "message": "How do I write a for-loop in Java?",
  "sessionId": "user-123-abc"
}
```

| Field | Type | Required | Description |
|---|---|---|---|
| personality | String | Yes | `helper`, `coder`, or `pirate` |
| message | String | Yes | The user's message |
| sessionId | String | No | Reuse to continue a conversation |

**Example response:**
```json
{
  "response": "A for-loop in Java looks like this: for (int i = 0; i < 10; i++) { ... }"
}
```

## Chat UI

A simple chat interface is available at `http://localhost:8080`. Select a personality and start chatting directly in the browser.

## Swagger UI

Interactive API documentation is available at `http://localhost:8080/swagger-ui.html`.

## Running tests

```bash
./mvnw test
```

Includes WireMock integration tests that verify successful requests, retry behaviour on 429, and error handling on 500.