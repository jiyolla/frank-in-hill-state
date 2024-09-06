package io.github.jiyolla.frankinhillstate;

import io.github.jiyolla.frankinhillstate.agent.Ta4jSmaAgent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;

@SpringBootApplication
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class MainApplication {
    private final Ta4jSmaAgent ta4jSmaAgent;

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

    @Scheduled(cron = "*/10 * * * * *")
    private void signalAgent() {
        LocalDateTime startTime = now();
        log.info("Agent wakes up at {}", startTime);
        ta4jSmaAgent.signal(startTime);
        log.info("Agent goes back to sleep at {}", now());
    }
}