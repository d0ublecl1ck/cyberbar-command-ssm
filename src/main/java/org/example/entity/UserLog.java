package org.example.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserLog {
    private Integer id;
    private Integer userId;
    private String operation;
    private String detail;
    private LocalDateTime operationTime;
} 