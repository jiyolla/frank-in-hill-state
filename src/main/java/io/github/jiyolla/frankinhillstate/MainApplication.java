package io.github.jiyolla.frankinhillstate;

import io.github.jiyolla.frankinhillstate.agent.Ta4jSmaAgent;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class MainApplication {
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(MainApplication.class, args);
        Ta4jSmaAgent ta4jSmaAgent = context.getBean(Ta4jSmaAgent.class);

        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

        Runnable task = () -> ta4jSmaAgent.signal(LocalDateTime.now());

        scheduledExecutorService.scheduleWithFixedDelay(task, 0, 1, TimeUnit.SECONDS);

        // Schedule a task to stop the scheduler after 100 seconds
        // scheduledExecutorService.schedule(() -> {
        //     System.out.println("Shutting down the scheduler...");
        //     scheduledExecutorService.shutdown();
        // }, 100, TimeUnit.SECONDS);
    }
}