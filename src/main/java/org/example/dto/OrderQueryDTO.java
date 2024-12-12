package org.example.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class OrderQueryDTO {
    private String commodityName;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String status;
    private Integer pageNum = 1;
    private Integer pageSize = 10;
} 