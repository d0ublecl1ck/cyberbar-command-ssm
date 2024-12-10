package org.example.config;

import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;

@Component
public class ShutdownHook {
    
    @PostConstruct
    public void init() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                Thread.sleep(1000); // 给资源清理一些时间
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }));
    }
} 