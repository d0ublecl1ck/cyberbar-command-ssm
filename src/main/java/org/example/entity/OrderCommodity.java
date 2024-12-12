package org.example.entity;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class OrderCommodity {
    private Integer commodityId;
    private String name;
    private Integer quantity;
    private BigDecimal price;
} 