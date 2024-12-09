package org.example.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ManagementLog {
    private Integer id;
    private Integer adminId;
    private String operation;
    private String detail;
    private LocalDateTime operationTime;
} 