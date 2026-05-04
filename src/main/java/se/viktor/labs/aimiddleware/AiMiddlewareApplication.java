package se.viktor.labs.aimiddleware;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableRetry
public class AiMiddlewareApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiMiddlewareApplication.class, args);
    }
}