package org.example.config;

import javax.servlet.ServletContextListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebListener
public class CustomServletContextListener implements ServletContextListener {
    private static final Logger logger = LoggerFactory.getLogger(CustomServletContextListener.class);
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.info("Web应用程序正在初始化...");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.info("Web应用程序正在关闭...");
    }
} 