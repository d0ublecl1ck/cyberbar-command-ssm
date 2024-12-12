package org.example.dto;

import lombok.Data;
import java.math.BigDecimal;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.DecimalMin;

@Data
public class OrderCommodityDTO {
    @NotNull(message = "商品ID不能为空")
    private Integer commodityId;
    
    @NotNull(message = "商品名称不能为空")
    private String name;
    
    @NotNull(message = "数量不能为空")
    private Integer quantity;
    
    @NotNull(message = "价格不能为空")
    @DecimalMin(value = "0.01", message = "价格必须大于0")
    private BigDecimal price;
} 