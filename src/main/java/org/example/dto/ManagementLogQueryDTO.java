package org.example.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ManagementLogQueryDTO {
    private Integer adminId;
    /** 操作类型 */
    private String operation;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer pageNum = 1;
    private Integer pageSize = 10;
} 