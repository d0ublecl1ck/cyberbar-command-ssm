package org.example.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserLogQueryDTO {
    private Integer userId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer pageNum = 1;
    private Integer pageSize = 10;
} 