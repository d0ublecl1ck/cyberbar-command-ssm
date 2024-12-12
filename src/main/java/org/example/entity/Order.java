package org.example.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonFormat;

@Data
public class Order {
    private Integer id;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime orderDate;
    
    private BigDecimal totalPrice;
    
    private String status;  // Pending, Completed, Cancelled
    
    private Integer machineId;
    
    private Integer userId;
    
    private List<OrderCommodity> commodities;
} 