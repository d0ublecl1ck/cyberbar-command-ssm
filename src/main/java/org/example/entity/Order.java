package org.example.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Order {
    private Integer id;
    private String commodityName;
    private BigDecimal commodityPrice;
    private Integer quantity;
    private LocalDateTime orderDate;
    private BigDecimal totalPrice;
    private String status;
} 