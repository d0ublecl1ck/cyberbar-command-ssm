package org.example.config;

import org.springframework.stereotype.Component;
import javax.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class ShutdownHook {
    
    private static final Logger logger = LoggerFactory.getLogger(ShutdownHook.class);
    
    @PreDestroy
    public void cleanup() {
        logger.info("应用程序正在关闭，执行清理工作...");
        try {
            // 给资源清理一些时间
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("清理工作被中断", e);
        }
        logger.info("清理工作完成");
    }
} 