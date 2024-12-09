package org.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 全局异常处理器
 * 统一处理应用中抛出的异常
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    
    /**
     * 处理所有未捕获的异常
     * @param e 异常对象
     * @return 包含错误信息的响应实体
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("{\"message\":\"" + e.getMessage() + "\"}");
    }
} 